package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.orders.bean.OrderType;
import com.yesmywine.orders.dao.*;
import com.yesmywine.orders.entity.*;
import com.yesmywine.orders.service.FreightService;
import com.yesmywine.orders.service.ReceiveOMSService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.util.number.DoubleUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by wangdiandian on 2017/6/7.
 */
@Service
@Transactional
public class ReceiveOMSImpl implements ReceiveOMSService {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveOMSImpl.class);


    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderDeliverDao orderDeliverDao;
    @Autowired
    private OrderReturnExchangeDao orderReturnExchangeDao;
    @Autowired
    private OrderDispatchDao orderDispatchDao;
    @Autowired
    private OrderPayinfoDao orderPayinfoDao;
    @Autowired
    private OrderImpl orderImpl;
    @Autowired
    private FreightService freightService;
    @Autowired
    private OrderDetailSkuDao orderDetailSkuDao;
    @Autowired
    private OrderDetailDao orderDetailDao;

    public String status(Long orderNo, String waybillNumber, String shipperCode, String deliverdTime) throws YesmywineException {

        Orders orders = ordersDao.findByOrderNo(orderNo);
        if (!orders.getStatus().equals(3)) {
            ValueUtil.isError("该订单为非待发货状态！");
        }
        orders.setStatus(5);//已发货（状态：5待收货）
        orders.setDelieverTime(DateUtil.toDate(deliverdTime, "yyyy-MM-dd HH:mm:ss"));
        ordersDao.save(orders);
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(orders.getOrderNo());
        orderDispatch.setOperator(1);//0客户/1也买酒
        orderDispatch.setStatus(5);//5待收货
        orderDispatch.setLabel(0);
        orderDispatchDao.save(orderDispatch);
        OrderDeliver orderDeliver = orderDeliverDao.findByOrderNo(Long.valueOf(orderNo));
        if (waybillNumber != null) {//保存物流单号
            orderDeliver.setWaybillNumber(waybillNumber);
            orderDeliver.setShipperCode(shipperCode);
            orderDeliver.setDeliverDate(DateUtil.toDate(deliverdTime, "yyyy-MM-dd HH:mm:ss"));
        }

        Map<Integer, Integer> skuMap = getGoodsSkuList(orders);
        Iterator it = skuMap.entrySet().iterator();
        JSONArray skuArray = new JSONArray();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Integer skuId = (Integer) entry.getKey();
            Integer count = (Integer) entry.getValue();
            JSONObject subChanneInventory = new JSONObject();
            subChanneInventory.put("skuId", skuId);
            subChanneInventory.put("count", count);
            skuArray.add(subChanneInventory);
        }

        List<OrderDetail> orderDetailList=orderDetailDao.findByOrderNo(Long.valueOf(orderNo));
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("goodsId",orderDetailList.get(0).getGoodsId());
        paramsMap.put("goodsName",orderDetailList.get(0).getGoodsName());
        String image=orderDetailList.get(0).getReasonImg();
        JSONArray arr = JSON.parseArray(image);
        String s= arr.get(0).toString();
        String id = ValueUtil.getFromJson(s, "id");
        String name = ValueUtil.getFromJson(s, "name");

        JSONObject object = new JSONObject();
        object.put("id",id);
        object.put("name",name);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(object);
        paramsMap.put("goodsImageUrl", image);
        paramsMap.put("LogisticsNumber", waybillNumber);
        Map<String, Object> map=orderImpl.viewLogistics(Long.valueOf(orderNo),orders.getUserId());
        String shippers =map.get("shippers").toString();
        String logisticsName = ValueUtil.getFromJson(shippers, "shipperName");
        paramsMap.put("LogisticsName", logisticsName);
        paramsMap.put("userId", orders.getUserId());
        paramsMap.put("orderId", orders.getId());
        paramsMap.put("orderNumber", orders.getOrderNo());

        String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/userservice/message/itf",RequestMethod.post,paramsMap,null);
        if (!"201".equals(resultCode)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError("调用用失败，请联系维护人员");
        }

        String result = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/inventory/channelInventory/syn", ValueUtil.toJson(HttpStatus.SC_CREATED, "sub", skuArray.toJSONString()), RequestMethod.post);
        if (result == null || !result.equals("201")) {
            //发送站内信
            orders.setSynStatus(3);//同步商城库存失败
            ordersDao.save(orders);
        }
        if (orders.getOrderType() == OrderType.Ordinary||orders.getOrderType() == OrderType.LuckyBag||orders.getOrderType() == OrderType.PreSale) {
            givePoint(orders);
        }
        Map<String, Object> smsParams = new HashMap<>();
        JSONObject objects = new JSONObject();
        objects.put("username", orders.getReceiver());
        objects.put("orderNo", orders.getOrderNo());
        smsParams.put("phones", orderDeliver.getPhone());
        smsParams.put("json", objects);
        smsParams.put("code", "fnEsKlAadv");
        String smsResult = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
//        String code = ValueUtil.getFromJson(result, "code");
        return "sucess";
    }

    public String givePoint(Orders orders) throws YesmywineException {

        //发货送积分
        Map<String, Object> params = new HashMap<>();
        OrderPayinfo orderPayinfo = orderPayinfoDao.findByOrderNo(orders.getOrderNo());
        params.put("money", orderPayinfo.getAmount());
        params.put("status", "下单");
        String pointResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/sso/point/itf", RequestMethod.get, params, null);
        String code = ValueUtil.getFromJson(pointResult, "code");
        if ("200".equals(code)) {
            String point = ValueUtil.getFromJson(pointResult, "data");
            orderPayinfo.setIntegral(Integer.valueOf(point));
            orderPayinfoDao.save(orderPayinfo);//保存送的积分
            Map<String, Object> onlineParams = new HashMap<>();
            onlineParams.put("userId", orders.getUserId());
            onlineParams.put("orderNumber", orders.getOrderNo());
            onlineParams.put("point", point);
            onlineParams.put("channelCode", "GW");
            String onlineResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/beans/online/itf", RequestMethod.post, onlineParams, null);
            String onlineCode = ValueUtil.getFromJson(onlineResult, "code");
            if (!"201".equals(onlineCode)) {
                ValueUtil.isError(ValueUtil.getFromJson(onlineResult, "msg"));
            }
        } else {
            ValueUtil.isError(ValueUtil.getFromJson(pointResult, "msg"));
        }
        return "success";
    }

    private Map<Integer, Integer> getGoodsSkuList(Orders orders) {
        Map<Integer, Integer> skuMap = new HashMap<>();
        List<OrderDetail> detailList = orders.getOrderDetails();
        for (OrderDetail detail : detailList) {
            List<OrderDetailSku> skuList = detail.getOrderDetailSkuList();
            for (OrderDetailSku sku : skuList) {
                Integer skuId = sku.getSkuId();
                Integer skuCount = sku.getCounts() * detail.getCount();
                if (!skuMap.containsKey(skuId)) {
                    skuMap.put(skuId, skuCount);
                } else {
                    Integer count = skuMap.get(skuId);
                    skuMap.put(skuId, skuCount + count);
                }
            }
        }
        return skuMap;
    }

    public String returnOrders(String returnNo, String dealTime, HttpServletRequest request) throws YesmywineException {

        OrderReturnExchange orderReturnExchange = orderReturnExchangeDao.findByReturnNo(returnNo);
//        orderReturnExchange.getStatus()
        List<OrderReturnExchange> returnList = orderReturnExchangeDao.findByOrderNo(orderReturnExchange.getOrderNo());
//        orderReturnExchange.setDealTime(DateUtil.toDate(dealTime, "yyyy-MM-dd HH:mm:ss"));
        orderReturnExchange.setDealTime(new Date());
        Orders orders = ordersDao.findByOrderNo(Long.valueOf(orderReturnExchange.getOrderNo()));
        orders.setStatus(0);//状态完成 0

        ordersDao.save(orders);
        //保存退货单单操作信息
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(Long.valueOf(returnNo));
        orderDispatch.setOperator(1);//0客户/1也买酒
        orderDispatch.setStatus(8);//8退换货完成
        orderDispatch.setLabel(1);//0订单，1退换货
        orderDispatchDao.save(orderDispatch);
        orderDispatchDao.save(orderDispatch);

        if (orderReturnExchange.getType() == 1) {//1退货
            Double returnGoodsCostPrice = 0.0;//退货商品的总成本价
            Double allGoodsCostPrice = 0.0;//所有商品总成本价
            List<OrderDetail> detailList = orders.getOrderDetails();
            for (OrderDetail detail : detailList) {
                Integer goodsId = detail.getGoodsId();
                Double goodsCostPrice = getGoosCostPrice(detail, request);
                allGoodsCostPrice = DoubleUtils.add(allGoodsCostPrice, goodsCostPrice);
                //判断是否为退货商品
                if (goodsId.equals(orderReturnExchange.getGoodId())) {
                    //退货商品成本价
                    returnGoodsCostPrice = goodsCostPrice;
                }
            }

            OrderPayinfo orderPayinfo = orderPayinfoDao.findByOrderNo(orderReturnExchange.getOrderNo());
            Double preferentialFreight = orderPayinfo.getPreferentialFreight();//不为空则免运费
            //运费计算
            Double freight = 0.0;
            if (returnList.size() > 1) {
                if (!(orderReturnExchange.getQualityProblem() == null ? false : orderReturnExchange.getQualityProblem())) {
                    Double orderResiduePrice = DoubleUtils.sub(DoubleUtils.sub(orderPayinfo.getAmount(),orderPayinfo.getCouponAmount()), returnGoodsCostPrice);



                    Map<String, Object> smsParams = new HashMap<>();
                        smsParams.put("id", orderReturnExchange.getAreaId().toString());

                        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/receivingAddress/itf", RequestMethod.get, smsParams, null);
                        String areaIdCode = ValueUtil.getFromJson(result, "code");
                        if (!"200".equals(areaIdCode)) {
                            String msg = ValueUtil.getFromJson(result, "msg");
                            ValueUtil.isError("获取收货地址失败，请联系管理员"+msg);
                        }
                    String areaId = ValueUtil.getFromJson(result, "data","provinceId");//得到省id去找运费
                    JSONObject freightJson = freightService.calculate(areaId, orderResiduePrice);
                    Double courierfees = Double.valueOf(freightJson.getString("courierfees")==null?"0":freightJson.getString("courierfees"));
                    Double transfersAmount = Double.valueOf(freightJson.getString("transfersAmount")==null?"0":freightJson.getString("transfersAmount"));
                    returnGoodsCostPrice = DoubleUtils.sub(returnGoodsCostPrice, courierfees);
                    returnGoodsCostPrice = DoubleUtils.sub(returnGoodsCostPrice, transfersAmount);
                }
            }

            //退酒豆
            Double returnBeans = DoubleUtils.mul(DoubleUtils.div(returnGoodsCostPrice, allGoodsCostPrice), orderPayinfo.getBeanAmount() == null ? 0.0 : orderPayinfo.getBeanAmount());
            orderReturnExchange.setReturnBeanAmount(returnBeans);
            //退余额
            Double returnPayAmount = DoubleUtils.mul(DoubleUtils.div(returnGoodsCostPrice, allGoodsCostPrice), orderPayinfo.getBalance() == null ? 0.0 : orderPayinfo.getBalance());
            orderReturnExchange.setReturnPayAmount(returnPayAmount);
            //退积分
            Double returnIntegral = DoubleUtils.div(returnGoodsCostPrice, allGoodsCostPrice) * (orderPayinfo.getIntegral() == null ? 0 : orderPayinfo.getIntegral());
            orderReturnExchange.setReturnIntegral(returnIntegral.intValue());
            Map<String, Object> userServiceParams = new HashMap<>();
            userServiceParams.put("userId", orders.getUserId());
            userServiceParams.put("orderNumber", orders.getOrderNo());
            userServiceParams.put("returnBean", returnBeans);
            userServiceParams.put("returnMoney", returnPayAmount);
            userServiceParams.put("returnPoint", returnIntegral.intValue());

            String userServiceResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/userservice/payment/returns", RequestMethod.post, userServiceParams, request);
            if (userServiceResult != null) {
                JSONObject jsonObj = JSON.parseObject(userServiceResult);
                String userServiceCode = jsonObj.getString("code");
                if (!userServiceCode.equals("201")) {
                    logger.info("订单取消,退换货单号：" + returnNo + " 调用用户服务失败，请联系维护人员");
                }
            } else {
                logger.info("订单取消,退换货单号：" + returnNo + " 调用用户服务失败，请联系维护人员");
            }

            //退礼品卡金额
            Double returnGiftCard = DoubleUtils.mul(DoubleUtils.div(returnGoodsCostPrice, allGoodsCostPrice), orderPayinfo.getGiftCardAmount() == null ? 0.0 : orderPayinfo.getGiftCardAmount());
            orderReturnExchange.setReturnGiftCardAmount(returnGiftCard);

            if (orderPayinfo.getCardNumber() != null) {
                Map<String, Object> giftCardParmas = new HashMap<>();
                JSONObject requestData = new JSONObject();
                requestData.put("cardNumber", orderPayinfo.getCardNumber());
                requestData.put("orderNo", orderPayinfo.getOrderNo());
                requestData.put("usedAmount", returnGiftCard);
                requestData.put("channel", 0);
                giftCardParmas.put("jsonData", requestData.toJSONString());
                String giftCardService = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/giftCard/return/itf", RequestMethod.post, giftCardParmas, request);
                if (giftCardService != null) {
                    JSONObject jsonObj = JSON.parseObject(giftCardService);
                    String giftCardServiceCode = jsonObj.getString("code");
                    if (!giftCardServiceCode.equals("201")) {
                        logger.info("订单取消,退换货单号：" + returnNo + " 调用礼品卡服务失败，请联系维护人员");
                    }
                } else {
                    logger.info("订单取消,退换货单号：" + returnNo + " 调用礼品卡服务失败，请联系维护人员");
                }
            }

            //退支付金额
            Double returnPrice = DoubleUtils.mul(DoubleUtils.div(returnGoodsCostPrice, allGoodsCostPrice), orders.getCopeWith());

            if (returnPrice > 0.0) {
                orderReturnExchange.setReturnPayWay(returnPrice);
                Map<String, Object> payParams = new HashMap<>();
                payParams.put("orderNumber", orders.getOrderNo());
                payParams.put("payAmount", orders.getCopeWith());
                payParams.put("refundAmount", returnPrice);
                payParams.put("refundNumber", orderReturnExchange.getReturnNo());
//                payParams.put("payment",orders.getPaymentType());

                String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/pay/refund/itf", RequestMethod.post, payParams, null);
                if (result != null) {
                    JSONObject jsonObj = JSON.parseObject(result);
                    String code1 = jsonObj.getString("code");
                    if (!code1.equals("201")) {
                        logger.info("订单取消,退换货单号：" + returnNo + " 调用退款失败，请联系客服人员");
                    }
                } else {
                    logger.info("订单取消,退换货单号：" + returnNo + " 调用退款失败，请联系客服人员");
                }
            }

            orderReturnExchange.setStatus(1);
            orderReturnExchangeDao.save(orderReturnExchange);
//            Map<Integer, Integer> skuMap = getGoodsSkuList(orders);
//
//            Iterator it = skuMap.entrySet().iterator();
//            JSONArray skuArray = new JSONArray();
//            while (it.hasNext()) {
//                Map.Entry entry = (Map.Entry) it.next();
//                String skuId = (String) entry.getKey();
//                Integer count = (Integer) entry.getValue();
//                JSONObject subChanneInventory = new JSONObject();
//                subChanneInventory.put("skuId", skuId);
//                subChanneInventory.put("count", count);
//                skuArray.add(subChanneInventory);
//
//            }
        } else {//换货
            orderReturnExchange.setStatus(1);
            orderReturnExchangeDao.save(orderReturnExchange);
        }
        return "SUCCESS";
    }

    private Double getGoosCostPrice(OrderDetail detail, HttpServletRequest request) throws YesmywineException {
        Double goodsCostPrice = 0.0;
        //获取退货商品成本价
        List<OrderDetailSku> goodsSkuList = detail.getOrderDetailSkuList();

        Double returnTotalPrice = 0.0;
        for (OrderDetailSku skuInfo : goodsSkuList) {
            Map<String, Object> params = new HashMap<>();
            params.put("skuCode", skuInfo.getSkuCode());
            String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/sku/itf/getCostPrice", RequestMethod.get, params, request);
            if (result != null) {
                JSONObject jsonObj = JSON.parseObject(result);
                String code1 = jsonObj.getString("code");
                if (code1.equals("200")) {
                    Double skuCostPrice = Double.valueOf(ValueUtil.getFromJson(result, "data"));
                    Double goodsSkuTotal = skuCostPrice * skuInfo.getCounts();
                    goodsCostPrice = DoubleUtils.add(goodsCostPrice, goodsSkuTotal);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("成本价无法获取，暂时无法退货");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("成本价无法获取，暂时无法退货");
            }
        }
        return goodsCostPrice;
    }

    public String cancelOrders(Long orderNo, Integer status) throws YesmywineException {
        Orders orders = ordersDao.findByOrderNo(orderNo);
        if (!orders.getStatus().equals(4)) {
            ValueUtil.isError("非处理中状态！无法取消！");
        }
        if (status == 1) {//取消失败
            orders.setStatus(9);
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(orders.getOrderNo());
            orderDispatch.setOperator(1);//0客户/1也买酒
            orderDispatch.setStatus(9);//9取消失败
            orderDispatch.setLabel(0);//0订单
            orderDispatchDao.save(orderDispatch);
        } else if (status == 0) {//取消
            orders.setStatus(Integer.valueOf(status));
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(orders.getOrderNo());
            orderDispatch.setOperator(1);//0客户/1也买酒
            orderDispatch.setStatus(2);//2取消
            orderDispatch.setLabel(0);//0订单
            orderDispatchDao.save(orderDispatch);
            OrderPayinfo orderPayinfo = orderPayinfoDao.findByOrderNo(orders.getOrderNo());
            retract(orderPayinfo, orders, orders.getUserId());
            if (orders.getOrderType() == OrderType.LuckyBag) {
                orderImpl.cancelluckyBag(orders.getOrderNo());
            }
            if (orders.getOrderType() == OrderType.MentionWine) {
                orderImpl.cancelMentionWine(orders.getOrderNo(), orders.getUserId());
            }
            if (orders.getOrderType() == OrderType.PreSale) {
                List<OrderDetail> orderDetails = orderDetailDao.findByOrderNo(orders.getOrderNo());
                for (int i = 0; i < orderDetails.size(); i++) {
                    orderImpl.cancelPreSaleOrders(orderDetails.get(i).getCount(), orderDetails.get(i).getGoodsId());
                }
            }

            //释放冻结库存
            Map<Integer, Integer> skuMap = getGoodsSkuList(orders);
            Iterator it = skuMap.entrySet().iterator();
            JSONArray skuArray = new JSONArray();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String skuId =  entry.getKey().toString();
                Integer count = (Integer) entry.getValue();
                JSONObject releaseJson = new JSONObject();
                releaseJson.put("skuId", skuId);
                releaseJson.put("count", count);
                skuArray.add(releaseJson);

            }
            String result = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/inventory/channelInventory/syn", ValueUtil.toJson(HttpStatus.SC_CREATED, "releaseFreeze", skuArray), RequestMethod.post);
            if (result == null || !result.equals("201")) {
                //发送站内信
                orders.setSynStatus(3);//同步商城库存失败
                ordersDao.save(orders);
            }
//                List<OrderDetailSku> orderDetailSkuList = orderDetailSkuDao.findByOrderNo(orders.getOrderNo());
//                releaseFreeze(orderDetailSkuList,orders);

            //退款
            //退酒豆
            Double returnBeans = orderPayinfo.getBeanAmount() == null ? 0.0 : orderPayinfo.getBeanAmount();
            //退余额
            Double returnPayAmount = orderPayinfo.getBalance() == null ? 0.0 : orderPayinfo.getBalance();
            //退积分
            Integer returnIntegral = orderPayinfo.getIntegral() == null ? 0 : orderPayinfo.getIntegral();
            Map<String, Object> userServiceParams = new HashMap<>();
            userServiceParams.put("userId", orders.getUserId());
            userServiceParams.put("orderNumber", orders.getOrderNo());
            userServiceParams.put("returnBean", returnBeans);
            userServiceParams.put("returnMoney", returnPayAmount);
            userServiceParams.put("returnPoint", returnIntegral.intValue());
            String userServiceResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/userservice/payment/returns", RequestMethod.post, userServiceParams, null);
            if (userServiceResult != null) {
                JSONObject jsonObj = JSON.parseObject(userServiceResult);
                String userServiceCode = jsonObj.getString("code");
                if (!userServiceCode.equals("201")) {
                    logger.info("订单取消,订单号：" + orderNo + " 调用用户服务失败，请联系维护人员");
                }
            } else {
                logger.info("订单取消,订单号：" + orderNo + " 调用用户服务失败，请联系维护人员");
            }


            //退礼品卡金额
            Double returnGiftCard = orderPayinfo.getGiftCardAmount() == null ? 0.0 : orderPayinfo.getGiftCardAmount();
            if (orderPayinfo.getCardNumber() != null) {
                Map<String, Object> giftCardParmas = new HashMap<>();
                JSONObject requestData = new JSONObject();
                requestData.put("cardNumber", orderPayinfo.getCardNumber());
                requestData.put("orderNo", orderPayinfo.getOrderNo());
                requestData.put("usedAmount", returnGiftCard);
                requestData.put("channel", 0);
                giftCardParmas.put("jsonData", requestData.toJSONString());
                String giftCardService = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/giftCard/return/itf", RequestMethod.post, giftCardParmas, null);
                if (giftCardService != null) {
                    JSONObject jsonObj = JSON.parseObject(giftCardService);
                    String giftCardServiceCode = jsonObj.getString("code");
                    if (!giftCardServiceCode.equals("201")) {
                        logger.info("订单取消,订单号：" + orderNo + " 调用礼品卡服务失败，请联系维护人员");
                    }
                } else {
                    logger.info("订单取消,订单号：" + orderNo + " 调用礼品卡服务失败，请联系维护人员");
                }
            }

            //退支付金额
            Double returnPrice = orders.getCopeWith() == null ? 0.0 : orders.getCopeWith();

            if (returnPrice > 0.0) {
                Map<String, Object> payParams = new HashMap<>();
                payParams.put("orderNumber", orders.getOrderNo());
                payParams.put("payAmount", orders.getCopeWith());
                payParams.put("refundAmount", returnPrice);
                String payResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/pay/refund", RequestMethod.post, payParams, null);
                if (payResult != null) {
                    JSONObject jsonObj = JSON.parseObject(payResult);
                    String code1 = jsonObj.getString("code");
                    if (!code1.equals("201")) {
                        logger.info("订单取消,订单号：" + orderNo + " 调用退款失败，请联系维护人员");
                    }
                } else {
                    logger.info("订单取消,订单号：" + orderNo + " 调用退款失败，请联系客服人员");
                }
            }

        }
        return "success";
    }

    public void retract(OrderPayinfo orderPayinfo, Orders orders, Integer userId) throws YesmywineException {
        if (orderPayinfo.getCouponId() != null) {
            HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/userservice/itf/userCoupon/return", RequestMethod.post);//退优惠券
            httpBean.addParameter("userId", userId);
            httpBean.addParameter("userCouponId", orderPayinfo.getCouponId());
            httpBean.run();
            String temp = httpBean.getResponseContent();
        }
        if (orderPayinfo.getCardNumber() != null) {
            HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/goods/giftCard/return/itf", RequestMethod.post);//退礼品卡金额
            com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("userId", userId);
            jsonObject.put("orderNo", orders.getOrderNo());
            jsonObject.put("cardNumber", orderPayinfo.getCardNumber());
            jsonObject.put("channel", orders.getChannel());
            jsonObject.put("usedAmount", orderPayinfo.getGiftCardAmount());
            jsonArray.add(jsonObject);
            String json = ValueUtil.toJson(jsonArray);
            httpBean.addParameter("jsonData", json);
            httpBean.run();
            String temp = httpBean.getResponseContent();
        }

        if (orderPayinfo.getBeanAmount() != null || orderPayinfo.getCardNumber() != null || orderPayinfo.getIntegral() != null) {
            Map<String, Object> payParams = new HashMap<>();
            payParams.put("userId", userId);
            payParams.put("orderNumber", orders.getOrderNo());
            if (orderPayinfo.getBeanAmount() != null) {
                payParams.put("returnBean", orderPayinfo.getBeanAmount());//退还就都有
            } else {
                payParams.put("returnBean", 0);//退还就都有
            }
            if (orderPayinfo.getBalance() != null) {
                payParams.put("returnMoney", orderPayinfo.getBalance());//退还余额
            } else {
                payParams.put("returnMoney", 0);
            }
            if (orderPayinfo.getIntegral() != null) {
                payParams.put("returnPoint", orderPayinfo.getIntegral());//退还积分
            } else {
                payParams.put("returnPoint", 0);

            }

            String userServiceResult = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/userservice/payment/returns", RequestMethod.post, payParams, null);
            if (userServiceResult != null) {
                JSONObject jsonObj = JSON.parseObject(userServiceResult);
                String userServiceCode = jsonObj.getString("code");
                if (!userServiceCode.equals("201")) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("调用用户服务失败，请联系维护人员");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("调用用户服务失败，请联系维护人员");
            }
        }
    }
}
