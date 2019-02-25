package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.orders.bean.OrderType;
import com.yesmywine.orders.bean.Payment;
import com.yesmywine.orders.bean.WhetherEnum;
import com.yesmywine.orders.dao.*;
import com.yesmywine.orders.entity.*;
import com.yesmywine.orders.service.FinishOrdersService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * Created by wangdiandian on 2017/2/13.
 */
@Service
public class FinishOrdersImpl extends BaseServiceImpl<Orders, Long> implements FinishOrdersService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderPayinfoDao orderPayinfoDao;
    @Autowired
    private OrderDeliverDao orderDeliverDao;
    @Autowired
    private OrderInvoiceDao orderInvoiceDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderDetailSkuDao orderDetailSkuDao;
    @Autowired
    private OrderDispatchDao orderDispatchDao;
    @Autowired
    private OrderImpl orderImpl;
    @Autowired
    private ReceiveOMSImpl receiveOMSImpl;

    public String paymentSuccess(String paymentType, Long orderNo) throws YesmywineException {
        //订单操作表
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(orderNo);
        orderDispatch.setOperator(1);//0客户/1也买酒
        orderDispatch.setStatus(3);//3待发货
        orderDispatch.setLabel(0);//0订单，1退换货
        orderDispatchDao.save(orderDispatch);
        //付款成功后改订单状态
        ValueUtil.verify(paymentType, "paymentTypeNull");
        Orders orders = ordersDao.findByOrderNo(orderNo);
//        orders.setStatus(3);//3待发货
        switch (paymentType) {
            case "Alipay":
                orders.setPaymentType(Payment.Alipay);
                break;
            case "WeChat":
                orders.setPaymentType(Payment.WeChat);
                break;
            case "UnionPay":
                orders.setPaymentType(Payment.UnionPay);
                break;
            case "CashOnDelivery":
                orders.setPaymentType(Payment.CashOnDelivery);
                break;
        }
        orders.setPayTime(new Date());

        ordersDao.save(orders);
        String remainingSum = null;
        String phoneNumber = null;

        updateOrderPayinfo(paymentType, orderNo, orders, remainingSum, phoneNumber);//付款成功后改改付款信息
        Map<String, Object> map = new HashMap<>();
        map.put("userId", orders.getUserId());
        String results = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/userInfomation/show/itf", RequestMethod.get, map, null);
        String codes = ValueUtil.getFromJson(results, "code");
        if ("200".equals(codes)) {
            String email = ValueUtil.getFromJson(results, "data", "email");
            String userName = ValueUtil.getFromJson(results, "data", "userName");
            phoneNumber = ValueUtil.getFromJson(results, "data", "phoneNumber");

            if (ValueUtil.notEmpity(email)) {
                JSONObject object = new JSONObject();
                object.put("orderNumber", orders.getOrderNo());
                JSONObject object1 = new JSONObject();
                object1.put("orderNumber", orders.getOrderNo());
                object1.put("userName", userName);

                if (orders.getPaymentType() == Payment.Alipay) {
                    object1.put("payment", "支付宝");
                } else if (orders.getPaymentType() == Payment.WeChat) {
                    object1.put("payment", "微信");
                } else if (orders.getPaymentType() == Payment.UnionPay) {
                    object1.put("payment", "银联");
                } else if (orders.getPaymentType() == Payment.CashOnDelivery) {
                    object1.put("payment", "货到付款");
                }
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("code", "FgQwIa5sa5");
                paramsMap.put("title", object);
                paramsMap.put("theme", object1);
                paramsMap.put("receiveMailAccount", email);

                String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/web/email/emailSend", RequestMethod.post, paramsMap, null);
                String code = ValueUtil.getFromJson(result, "code");
            }

            if (orders.getOrderType() == OrderType.WineStore) {//存酒
                orders.setStatus(0);//0完成
                WineStore(orders, orders.getUserId(), userName, phoneNumber);
                receiveOMSImpl.givePoint(orders);//送积分
            }else if (orders.getOrderType() == OrderType.VirtualGoods){
                orders.setStatus(0);//0完成
                receiveOMSImpl.givePoint(orders);//送积分
                orderImpl.randomGiftCart(orders.getOrderNo());
            }
            else {
                if (orders.getOrderType()==OrderType.MentionWine) {
                    mentionWine(orders.getUserId(), orders.getOrderNo());
                }
                orders.setStatus(3);//3待发货
                //发送短信 订单支付（尽快为您发货）
                Map<String, Object> smsParams = new HashMap<>();
                JSONObject objects = new JSONObject();
                OrderDeliver orderDeliver = orderDeliverDao.findByOrderNo(orders.getOrderNo());
                objects.put("username", orders.getReceiver());
                objects.put("orderNo", orders.getOrderNo());
                smsParams.put("phones", orderDeliver.getPhone());
                smsParams.put("json", objects);
                smsParams.put("code", "ZXP4kLQn6h");
                String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
                String smscode = ValueUtil.getFromJson(result, "code");
            }
            orderImpl.salesVolume(orderNo);
        }
        pushOMS( orderNo,ValueUtil.getFromJson(results,"data"));
        return "success";
    }
    public void mentionWine(Integer userId,Long orderNo)throws YesmywineException{
        List<OrderDetail> orderDetailDaos = orderDetailDao.findByOrderNo(orderNo);
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for(int i = 0; i<orderDetailDaos.size();i++) {
            com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
            jsonObject1.put("id", orderDetailDaos.get(i).getKeepwineId());
            jsonObject1.put("count", orderDetailDaos.get(i).getCount());
            jsonObject1.put("fee", orderDetailDaos.get(i).getWineStoreMoney());
            jsonArray.add(jsonObject1);
        }

        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/confirm/itf", RequestMethod.post);
        httpRequest.addParameter("json", jsonArray);
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("extractorderNumber",orderNo);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");

        if(!"201".equals(code) ||code ==null) {
        String msg = ValueUtil.getFromJson(temp, "msg");
            ValueUtil.isError("调用确认存酒库失败"+msg);
    }
}
    public void updateOrderPayinfo(String paymentType, Long orderNo,Orders orders,String remainingSum, String phoneNumber) throws YesmywineException {//付款成功后改改付款信息
        OrderPayinfo orderPayinfo = orderPayinfoDao.findByOrderNo(orderNo);
        orderPayinfo.setOrderNo(orderNo);
        switch (paymentType) {
            case "Alipay":
                orderPayinfo.setPayType(Payment.Alipay);
                break;
            case "WeChat":
                orderPayinfo.setPayType(Payment.WeChat);
                break;
            case "UnionPay":
                orderPayinfo.setPayType(Payment.UnionPay);
                break;
            case "CashOnDelivery":
                orderPayinfo.setPayType(Payment.CashOnDelivery);
                break;
        }
        orderPayinfo.setPaymentTime(new Date());
        Map<String, Object> payParams = new HashMap<>();
        payParams.put("money", orders.getCopeWith());
        payParams.put("status", "下单");

        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/sso/point", RequestMethod.get, payParams, null);
        if (result != null) {
            JSONObject jsonObj = JSON.parseObject(result);
            String code1 = jsonObj.getString("code");
            if (code1.equals("200")) {
                String data = jsonObj.getString("data");
                orderPayinfo.setIntegral(Integer.valueOf(data));
            }
        }
        orderPayinfoDao.save(orderPayinfo);
//        if (orderPayinfo.getCouponId() != null) {//使用优惠券
//            updateCoupon(orders.getUserId(), orderPayinfo.getCouponId());
//        }
//        if (orderPayinfo.getCardNumber() != null) {//使用礼品卡
//            updateGiftCard(orderPayinfo.getCardNumber(), orderNo, orderPayinfo.getCouponAmount());
//        }
//        if (orderPayinfo.getBeanAmount() != null) {//使用酒豆
//            updateBeanWine(orderNo, orders.getUserId(), orderPayinfo.getBeanAmount());
//        }
//        if (orderPayinfo.getBalance() != null) {//使用余额
//            updateBalance(orders.getUserId(), orderPayinfo.getBalance(), orderNo,remainingSum,phoneNumber);
//        }

    }

    public String pushOMS(Long orderNo,String userInfo) throws YesmywineException {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();

        Orders orders = ordersDao.findByOrderNo(orderNo);
        OrderDeliver orderDeliver = orderDeliverDao.findByOrderNo(orderNo);
        OrderPayinfo payinfo = orderPayinfoDao.findByOrderNo(orderNo);
        jsonObject.put("customerCode", "GW");//渠道号
        jsonObject.put("orgCode", "YMJ");//默认YMJ
        jsonObject.put("relatCode", orderNo.toString());//
        switch (orders.getOrderType()){//订单类型(Ordinary普通订单/ WineStore存酒库订单/ MentionWine提酒订单/GiftPurchase礼品购订单/LuckyBag福袋订单)|Y
            case LuckyBag:
                jsonObject.put("orderType", "福袋订单");
                break;
            case MentionWine:
                jsonObject.put("orderType", "提酒订单");
                break;
            case PreSale:
                jsonObject.put("orderType", "预售订单");
                break;
            default:
                jsonObject.put("orderType", "普通订单");
                break;
        }

        String vipName = ValueUtil.getFromJson(userInfo, "vipRule","vipName");
        jsonObject.put("userVip", vipName);//会员等级（普通、青铜、白银、黄金、铂金、钻石、黑钻）

        jsonObject.put("orderTime", orders.getCreateTime().toString());//下单时间
        jsonObject.put("payTime", orders.getPayTime()==null?"":orders.getPayTime().toString());//付款时间
        Integer type = orderDeliver.getDeliverType();
        if (type == 1) {
            jsonObject.put("beStoreMention", "Y");//是否门店自提
        } else {
            jsonObject.put("beStoreMention", "N");//是否门店自提
        }
        jsonObject.put("contactPerson", orderDeliver.getReceiver());//收货人

        Map<String ,Object> map = new HashMap<>();
        map.put("userId", orders.getUserId());
        map.put("id",orderDeliver.getAreaId());
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/userservice/receivingAddress/itf",RequestMethod.get,map,null);
        String code1 = ValueUtil.getFromJson(result, "code");

        if ("200".equals(code1)) {
            String province = ValueUtil.getFromJson(result, "data", "province");
            jsonObject.put("province", province);//省
            String city = ValueUtil.getFromJson(result, "data", "city");
            jsonObject.put("city", city);//市
            String area = ValueUtil.getFromJson(result, "data", "area");
            jsonObject.put("district", area);//区
        }
        jsonObject.put("address", orderDeliver.getAddress());//收货地址
        jsonObject.put("mobile", orderDeliver.getPhone());//收货人手机号
        String invoice = orders.getInvoiceNeedFlag().toString();
        if (invoice.equals("YES")) {
            OrderInvoice orderInvoice = orderInvoiceDao.findByOrderNo(orderNo);
            Integer voice = orderInvoice.getInvoiceType();
            if (voice == 0) {
                jsonObject.put("invoiceType", "普通");
            } else if (voice == 1) {
                jsonObject.put("invoiceType", "电子");
            } else if(voice == 2){
                jsonObject.put("invoiceType", "增值税");
            } else{
                jsonObject.put("invoiceType", "无");
            }
            jsonObject.put("invoiceHeader", orderInvoice.getReceiptHeader());//发票抬头
            jsonObject.put("invoiceAmount", orders.getCopeWith());//发票金额
            jsonObject.put("invoiceContent", orderInvoice.getReceiptContent());//发票内容
        }
//        jsonObject.put("salesAmount", orders.getTotalGoodsAmount()==null?0.0:orders.getTotalGoodsAmount().toString());//销售金额
        jsonObject.put("salesAmount", payinfo.getTotalGoodsAmount());//销售金额
        switch (orderDeliver.getCashOnDelivery()){
            case YES:
                jsonObject.put("paidAmount", 0.0);//已支付金额
//                jsonObject.put("bePaidAmount", orders.getCopeWith().toString());//待支付金额
                jsonObject.put("bePaidAmount", orders.getTotalGoodsAmount()==null?0.0:orders.getTotalGoodsAmount().toString());//待支付金额
                break;
            case NO:
                jsonObject.put("paidAmount", orders.getCopeWith().toString());//已支付金额
                jsonObject.put("bePaidAmount", 0.0);//待支付金额
                break;
        }

        switch (orders.getPaymentType()){
            case Alipay:
                jsonObject.put("paymentMethod1", "支付宝");//支付方式
                break;
            case WeChat:
                jsonObject.put("paymentMethod1", "微信");//支付方式
                break;
            case UnionPay:
                jsonObject.put("paymentMethod1", "银联");//支付方式
                break;
            case CashOnDelivery:
                jsonObject.put("paymentMethod1", "货到付款");//支付方式
                break;
            case Internal:
                jsonObject.put("paymentMethod1", "内部支付");//支付方式
                break;
        }
        jsonObject.put("description", orders.getComment());

        List<OrderDetail> orderDetail = orders.getOrderDetails();
        com.alibaba.fastjson.JSONArray jsonArray2 = new com.alibaba.fastjson.JSONArray();
        switch (orders.getOrderType()){
            case LuckyBag:
                for (int i = 0; i < orderDetail.size(); i++) {
                    OrderDetail detail = orderDetail.get(i);
                    List<OrderDetailSku> skuList = detail.getOrderDetailSkuList();
                    for(OrderDetailSku sku:skuList) {
                        String skuCode = sku.getSkuCode();
                        Integer skuCount = sku.getCounts()*detail.getCount();
                        com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
                        jsonObject1.put("skuCode", skuCode);
                        jsonObject1.put("price", detail.getGoodsPrice());
                        jsonObject1.put("quantity", skuCount);
                        jsonObject1.put("beGifts", "N");
                        jsonArray2.add(jsonObject1);
                    }
                }
                jsonObject.put("orderDetailSKU", jsonArray2);
                jsonObject.put("orderDetailGoods", null);
                break;
            default:
                for (int i = 0; i < orderDetail.size(); i++) {
                    com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
                    jsonObject1.put("goodsCode", orderDetail.get(i).getGoodsCode());
                    jsonObject1.put("price", orderDetail.get(i).getGoodsPrice());
                    jsonObject1.put("quantity", orderDetail.get(i).getCount());
                    jsonObject1.put("beGifts", orderDetail.get(i).getGift()==null?"N":orderDetail.get(i).getGift());
                    jsonArray2.add(jsonObject1);
                }
                jsonObject.put("orderDetailGoods", jsonArray2);
        }

        pushOMS(jsonObject.toJSONString(),orders);

        return "success";
    }

    private Map<String,Integer> getGoodsSkuList(Orders orders) {
        Map<String,Integer> skuMap = new HashMap<>();
        List<OrderDetail> detailList = orders.getOrderDetails();
        for(OrderDetail detail:detailList){
            List<OrderDetailSku> skuList = detail.getOrderDetailSkuList();
            for(OrderDetailSku sku:skuList) {
                String skuCode = sku.getSkuCode();
                Integer skuCount = sku.getCounts()*detail.getCount();
                if(!skuMap.containsKey(skuCode)){
                    skuMap.put(skuCode,skuCount);
                }else{
                    Integer count = skuMap.get(skuCode);
                    skuMap.put(skuCode,skuCount+count);
                }
            }
        }
        return skuMap;
    }

    private int count = 0;

    private String pushOMS(String jsonData,Orders orders) throws YesmywineException {
        String result = SynchronizeUtils.getOmsResult(Dictionary.OMS_HOST,"/pullOmsOrder",RequestMethod.post,null,jsonData);
        if(result!=null){
            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            if(code==null||!code.equals("success")){
                orders.setSynStatus(0);
                orders.setSynFailMassage(message);
                ordersDao.save(orders);
//                ValueUtil.isError("推送OMS失败");
            }
        }else{
            orders.setSynStatus(0);
            ordersDao.save(orders);
//            ValueUtil.isError("推送OMS失败");
        }
        return "success";
    }

//    public String updateGiftCard(Long cardNumber,Long orderNo,Double usedAmount)throws YesmywineException {//使用礼品卡
//        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
//        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//        jsonObject.put("cardNumber",cardNumber);
//        jsonObject.put("orderNo",orderNo);
//        jsonObject.put("usedAmount",usedAmount);
//        jsonObject.put("usedTime1",new Date());
//        jsonObject.put("channel",0);
//        jsonArray.add(jsonObject);
//        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/goods/giftCard/spend/itf", RequestMethod.post);
//        httpBean.addParameter("jsonData", jsonArray);
//        httpBean.run();
//        String temp = httpBean.getResponseContent();
//        String code = ValueUtil.getFromJson(temp, "code");
//        if(!code.equals("201")){
//            ValueUtil.isError("系统错误，暂时不能使用礼品卡！");
//        }
//        return "success";
//    }

//    public String updateBeanWine(Long orderNo,Integer userId ,Double beans) throws YesmywineException {//使用酒豆
//        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/web/userservice/payment/beanConsume", RequestMethod.post);
//        httpBean.addParameter("userId",userId);
//        httpBean.addParameter("orderNumber",orderNo);
//        String a[] = beans.toString().split("\\.");
//        httpBean.addParameter("bean",a[0]);
//        httpBean.addParameter("channelCode","GW");
//        // TODO: 2017/7/4
//        httpBean.run();
//        String temp = httpBean.getResponseContent();
//        String code = ValueUtil.getFromJson(temp, "code");
//        if(!code.equals("201")){
//            ValueUtil.isError("系统错误，暂时不能使用酒豆！");
//        }
//        return "success";
//    }

//    public String updateCoupon(Integer userId ,Integer couponId)throws YesmywineException {//使用优惠券
//        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/userCoupon/use/itf", RequestMethod.post);
//        httpRequest.addParameter("userId", userId);
//        httpRequest.addParameter("userCouponId",couponId);
//        httpRequest.run();
//        String temp = httpRequest.getResponseContent();
//        String code = ValueUtil.getFromJson(temp, "code");
//        if(!code.equals("201")){
//            ValueUtil.isError("系统错误，暂时不能使用优惠券！");
//        }
//        return "success";
//    }
//    public String updateBalance(Integer userId ,Double balance,Long orderNo,String remainingSum, String phoneNumber) throws YesmywineException {//使用余额
//        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/userservice/payment/remainConsume", RequestMethod.post);
//        httpRequest.addParameter("userId", userId);
//        httpRequest.addParameter("payMoney",balance );
//        httpRequest.addParameter("orderNumber",orderNo);
//        httpRequest.run();
//        String temp = httpRequest.getResponseContent();
//        String code = ValueUtil.getFromJson(temp, "code");
//        if(!code.equals("201")){
//            ValueUtil.isError("系统错误，暂时不能使用余额！");
//        }
//        //发送短信余额变更
//
//        Double smsBalance=Double.valueOf(remainingSum)-balance;
//            Map<String, Object> smsParams = new HashMap<>();
//            JSONObject objects = new JSONObject();
//            if (null != phoneNumber) {
//                objects.put("time", new Date());
//                objects.put("type", "消费");
//                objects.put("rechargeAmount",balance);
//                objects.put("balance", smsBalance);
//                smsParams.put("json", objects);
//                smsParams.put("phones", phoneNumber);
//                smsParams.put("code", "zgMWvgUbks");
//
//                String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
//                String smscode = ValueUtil.getFromJson(result, "code");
//                if (!"201".equals(smscode)) {
//                    String msg = ValueUtil.getFromJson(result, "msg");
//                    ValueUtil.isError("发送短信失败"+msg);
//                }
//            }
//        return "success";
//    }
//
//
    public void WineStore(Orders orders,Integer userId,String userName,String phoneNumber) throws YesmywineException {
        //存酒库加存酒库存
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
        List<OrderDetail> details = orderDetailDao.findByOrderNo(orders.getOrderNo());
        for (int i = 0; i < details.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("goodsId", details.get(i).getGoodsId());
            jsonObject.put("count", details.get(i).getCount());
            jsonObject.put("goodsName", details.get(i).getGoodsName());
            jsonObject.put("perPrize", details.get(i).getGoodsPrice());
            jsonObject.put("goodsImageUrl", details.get(i).getReasonImg());

            jsonArray.add(jsonObject);
        }
        json.put("orderNumber", orders.getOrderNo());
        json.put("goods", jsonArray);
        HttpBean httpBean1 = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/itf", RequestMethod.post);
        httpBean1.addParameter("userId", userId);
        httpBean1.addParameter("json", json);
        httpBean1.run();
        String temp = httpBean1.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");

        if (!"201".equals(code)) {
                String msg = ValueUtil.getFromJson(temp, "msg");
                ValueUtil.isError("发送短信失败"+msg);
        } else {
            //发送短信 存酒订单
            Map<String, Object> smsParams = new HashMap<>();
            JSONObject objects = new JSONObject();
            if (null != phoneNumber) {
                objects.put("username", userName);
                smsParams.put("phones", phoneNumber);
                objects.put("orderNo", orders.getOrderNo());

                smsParams.put("json", objects);
                smsParams.put("code", "l7gxSXnVKa");

                String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
                String smsCode = ValueUtil.getFromJson(result, "code");
//                if (!"201".equals(smsCode)) {
//                    String msg = ValueUtil.getFromJson(result, "msg");
//                    ValueUtil.isError("发送短信失败"+msg);
//                }
            }
        }
    }

    public String ordersEvaluate(Long orderNo)throws YesmywineException{
        ValueUtil.verify(orderNo);
        Orders orders=ordersDao.findByOrderNo(orderNo);
        if(orders.getComment()==WhetherEnum.YES){
            ValueUtil.isError("订单已评论！");
        }
        orders.setComment(WhetherEnum.YES);
        ordersDao.save(orders);

        return "success";
    }
    public String goodsEvaluate(Long orderNo)throws YesmywineException{
        ValueUtil.verify(orderNo);
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderNo(orderNo);
        for(int i=0;i<orderDetailList.size();i++){
            if(orderDetailList.get(i).getComment()==WhetherEnum.YES){
                ValueUtil.isError("商品已评论！");
            }
        }
        orderDetailList.forEach(r -> {
            r.setComment(WhetherEnum.YES);
        });
        orderDetailDao.save(orderDetailList);
        return "success";
    }

}