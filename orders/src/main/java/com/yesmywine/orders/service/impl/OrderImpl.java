package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.orders.IdUtil;
import com.yesmywine.orders.bean.OrderType;
import com.yesmywine.orders.bean.Payment;
import com.yesmywine.orders.bean.WhetherEnum;
import com.yesmywine.orders.dao.*;
import com.yesmywine.orders.entity.*;
import com.yesmywine.orders.service.OrderReturnExchangeService;
import com.yesmywine.orders.service.OrderService;
import com.yesmywine.orders.service.SecKillService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.JSONUtil;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangdiandian on 2017/2/10.
 */
@Service
@Transactional
public class OrderImpl extends BaseServiceImpl<Orders, Long> implements OrderService {
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrderPayinfoDao orderPayinfoDao;
    @Autowired
    private OrderDeliverDao orderDeliverDao;
    @Autowired
    private SecKillService secKillService;
    @Autowired
    private OrderInvoiceDao orderInvoiceDao;
    @Autowired
    private OrderDispatchDao orderDispatchDao;
    @Autowired
    private TaskConfigureDao taskConfigureDao;
    @Autowired
    private OrderReturnExchangeService orderReturnExchangeService;
    @Autowired
    private FreightImpl freightImpl;
    @Autowired
    private OrderDetailSkuDao orderDetailSkuDao;
    @Autowired
    private FinishOrdersImpl finishOrders;
    @Autowired
    private OrderRefreshImpl rderRefreshImpl;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OtherUsePayImpl otherUsePay;
    @Autowired
    private ReceiveOMSImpl receiveOMSImpl;
    @Autowired
    private OrderGiftCardDao orderGiftCardDao;


    public Map<String, Object> creatOrder(@RequestParam Map<String, String> param, String userInfo, HttpServletRequest request) throws YesmywineException {//订单生成插入各类表

        Orders order = new Orders();
        ordersDao.save(order);//保存订单表
        Long orderNo = generateOrderNumber(order.getId());//根据刚生成的订单id生成订单号
        order.setOrderNo(orderNo);
        String invoiceNeedFlag = param.get("invoiceNeedFlag");
        switch (invoiceNeedFlag) {
            case "YES":
                order.setInvoiceNeedFlag(WhetherEnum.YES);
                OrderInvoice orderInvoice = createOrderInvoice(param, orderNo);
                orderInvoiceDao.save(orderInvoice);
                break;
            default:
                order.setInvoiceNeedFlag(WhetherEnum.NO);
                break;
        }
        String userId = ValueUtil.getFromJson(userInfo, "id");
        String username = ValueUtil.getFromJson(userInfo, "userName");
        String phoneNumber = ValueUtil.getFromJson(userInfo, "phoneNumber");
        order.setUserId(Integer.valueOf(userId));//用户
        order.setNote(param.get("note"));//备注
        String type = param.get("orderType");

        OrderDeliver orderDeliver = createOrdersType(orderNo, type, order, param, userInfo);

        if (order.getOrderType() == OrderType.MentionWine) {//提酒单
            //冻结提酒库
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/extract/itf", RequestMethod.post);
            httpRequest.addParameter("json", param.get("json"));
            httpRequest.addParameter("userId", userId);
            httpRequest.addParameter("extractorderNumber", orderNo);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            if (!code.equals("201")) {
                ValueUtil.isError("调用冻结存酒库失败，请联系维护人员");
            }
        }

        JSONObject jsStr = JSONObject.parseObject(userInfo); //将字符串{“id”：1}
        String email = jsStr.getString("email");//获取id的值
        if (email!=null) {
            //发送邮件
            JSONObject object = new JSONObject();
            object.put("orderNumber", order.getOrderNo());
            JSONObject object1 = new JSONObject();
            object1.put("orderNumber", order.getOrderNo());
            object1.put("userName", username);

            if (order.getPaymentType() == Payment.Alipay) {
                object1.put("payment", "支付宝");
            } else if (order.getPaymentType() == Payment.WeChat) {
                object1.put("payment", "微信");
            } else if (order.getPaymentType() == Payment.UnionPay) {
                object1.put("payment", "银联");
            } else if (order.getPaymentType() == Payment.CashOnDelivery) {
                object1.put("payment", "货到付款");
            } else if (order.getPaymentType() == Payment.Internal) {
                object1.put("payment", "内部支付");
            }
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("code", "GaVU3EGvFK");
            paramsMap.put("title", object);
            paramsMap.put("theme", object1);
            paramsMap.put("receiveMailAccount", email);
            String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/web/email/emailSend", RequestMethod.post, paramsMap, null);
            String code = ValueUtil.getFromJson(result, "code");
        }

        Map<String, Object> map = new HashMap<>();
        String payType = param.get("payType");
        if (payType.equals("CashOnDelivery")) {
            //货到付款
            //订单操作表
            order.setStatus(3);
            ordersDao.save(order);//保存订单表
            createOrderDispatch(orderNo, order.getStatus());//保存订单操作信息
            pushToOms(order, userInfo);//推送给OMS
            //发送短信  货到付款订单
            Map<String, Object> smsParams = new HashMap<>();
            JSONObject objects = new JSONObject();

            objects.put("username", order.getReceiver());
            objects.put("orderNo", order.getOrderNo());
            objects.put("orderAmount", order.getCopeWith());
            smsParams.put("json", objects);
            smsParams.put("phones", orderDeliver.getPhone());
            smsParams.put("code", "9xl6itAuTq");

            String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
            String code = ValueUtil.getFromJson(result, "code");
            return null;
        } else {
            if (order.getCopeWith() == 0.00) {
                if (order.getOrderType() == OrderType.WineStore) {//存酒
                    finishOrders.WineStore(order, Integer.valueOf(userId), username, phoneNumber);
                    order.setStatus(0);//0完成
                    order.setPayTime(new Date());
                    ordersDao.save(order);//保存订单表
                    createOrderDispatch(orderNo, order.getStatus());//保存订单操作信息
                    receiveOMSImpl.givePoint(order);//送积分
                    //调用销量接口
                    salesVolume(orderNo);

                } else if (order.getOrderType() == OrderType.MentionWine) {//提酒
                    order.setStatus(0);//0完成
                    order.setPayTime(new Date());
                    order.setComment(WhetherEnum.NO);//完成未评价
                    ordersDao.save(order);//保存订单表
                    finishOrders.mentionWine(Integer.valueOf(userId), orderNo);
                    createOrderDispatch(orderNo, order.getStatus());//保存订单操作信息
                    pushToOms(order, userInfo);//推动给OMS

                } else if (order.getOrderType() == OrderType.VirtualGoods) {//虚拟商品-礼品卡
                    order.setStatus(0);//0完成
                    order.setPayTime(new Date());
                    order.setComment(WhetherEnum.NO);//完成未评价
                    ordersDao.save(order);//保存订单表
                    createOrderDispatch(orderNo, order.getStatus());//保存订单操作信息
                    //随机在未被购买的礼品卡中抽取礼品卡
                    randomGiftCart(orderNo);
                    receiveOMSImpl.givePoint(order);//送积分
                } else {
                    order.setStatus(3);//3待发货

                    ordersDao.save(order);//保存订单表
                    createOrderDispatch(orderNo, order.getStatus());//保存订单操作信息
                    pushToOms(order, userInfo);//推动给OMS
                    //调用销量接口
                    salesVolume(orderNo);
                }
                return null;
            } else {
                if (order.getOrderType() == OrderType.WineStore) {
                    order.setStatus(0);
                } else{
                    order.setStatus(1);
                }
                ordersDao.save(order);//保存订单表
                createOrderDispatch(orderNo, order.getStatus());//保存订单操作信息
                map.put("orderNo", order.getOrderNo());
                map.put("copeWith", order.getCopeWith());
                map.put("payment", order.getPaymentType());
                if (order.getOrderType() != OrderType.WineStore) {
                    //发送短信  订单提交
                    Map<String, Object> smsParams = new HashMap<>();
                    JSONObject objects = new JSONObject();
                    if (orderDeliver != null) {
                        objects.put("username", order.getReceiver());
                        smsParams.put("phones", orderDeliver.getPhone());
                    } else {
                        if (null != phoneNumber) {
                            objects.put("username", username);
                            smsParams.put("phones", phoneNumber);
                        }
                    }
                    objects.put("orderNo", order.getOrderNo());
                    objects.put("orderAmount", order.getCopeWith());
                    smsParams.put("json", objects);
                    smsParams.put("code", "tQZVRpo8kr");
                    String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
                    String code = ValueUtil.getFromJson(result, "code");
                }
            }
            return map;

        }
    }

    private void pushToOms(Orders orders, String userInfo) {
        try {
            finishOrders.pushOMS(orders.getOrderNo(), userInfo);
        } catch (YesmywineException e) {
            orders.setSynStatus(0);
            ordersDao.save(orders);
        }
    }

    public static Long generateOrderNumber(Long id) {//生成订单编号
        Long orderNo = IdUtil.genId("yyMMdd1{s}{s}{s}{r}{r}{s}{s}{r}", id, 5);
        return orderNo;
    }

    public OrderInvoice createOrderInvoice(Map<String, String> param, Long orderNo) {//保存发票
        OrderInvoice orderInvoice = new OrderInvoice();
        orderInvoice.setOrderNo(orderNo);//订单编码
        Integer invoiceType = Integer.valueOf(param.get("invoiceType"));
        orderInvoice.setInvoiceType(invoiceType);
        String receiptHeader = param.get("receiptHeader");
        orderInvoice.setReceiptHeader(receiptHeader);
        String receiptContent = param.get("receiptContent");
        orderInvoice.setReceiptContent(receiptContent);
        String alueAddedInfo = param.get("alueAddedInfo");
        orderInvoice.setAlueAddedInfo(alueAddedInfo);
        orderInvoiceDao.save(orderInvoice);
        return orderInvoice;
    }

    private OrderDeliver createOrdersType(Long orderNo, String type, Orders order, @RequestParam Map<String, String> param, String userInfo) throws YesmywineException {//订单明细
        // 将json字符串转成json对象
        //空的话根据商品去判断是福袋，预售，普通订单
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDeliver orderDeliver = null;
        OrderPayinfo orderPayinfo;

        String userId = ValueUtil.getFromJson(userInfo, "id");
        String username = ValueUtil.getFromJson(userInfo, "userName");

        if (ValueUtil.notEmpity(type) && type.equals("MentionWine")) {
            order.setOrderType(OrderType.MentionWine);//提酒订单
            String json = param.get("json");
            orderDetails = createMentionWineOrderDetailS(json, Integer.valueOf(userId), orderNo);//提酒单保存订单明细
            Double price = 0.00;
            for (int i = 0; i < orderDetails.size(); i++) {
                Double goodsPrice = orderDetails.get(i).getGoodsPrice();
                price = price + goodsPrice;
            }
            order.setOrderDetails(orderDetails);//保存订单明细
            Double WineStoreMoney = 0.00;
            Integer totalNum = 0;

            for (int i = 0; i < orderDetails.size(); i++) {
                Integer count = orderDetails.get(i).getCount();
                totalNum = totalNum + count;
                WineStoreMoney = WineStoreMoney + orderDetails.get(i).getWineStoreMoney();
                BigDecimal bg = new BigDecimal(WineStoreMoney);
                WineStoreMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

            order.setTotalNum(totalNum);//商品总数量
            String payType = param.get("payType");

            orderDeliver = createOrderDeliver(param, orderNo, payType);
            orderDeliverDao.save(orderDeliver);
            if (payType.equals("CashOnDelivery")) {//货到付款
                order.setStatus(3);//状态（3待发货）
                orderPayinfo = createOrderPayinfo(param, orderNo, WineStoreMoney, userInfo, type, null, price, null);//订单付款信息表
                orderPayinfoDao.save(orderPayinfo);//保存订单支付详情表
                order.setPaymentType(orderPayinfo.getPayType());
                order.setCopeWith(orderPayinfo.getCopeWith());
                order.setAmount(orderPayinfo.getAmount());
                order.setReceiver(orderDeliver.getReceiver());
            } else {
                orderPayinfo = createOrderPayinfo(param, orderNo, WineStoreMoney, userInfo, type, null, price, null);//订单付款信息表
                orderPayinfoDao.save(orderPayinfo);//保存订单支付详情表
                order.setPaymentType(orderPayinfo.getPayType());
                order.setCopeWith(orderPayinfo.getCopeWith());
                order.setAmount(orderPayinfo.getAmount());
                order.setStatus(1);
                order.setChannel(0);
                ordersDao.save(order);
                Double totalGoodsAmount = WineStoreMoney + orderPayinfo.getFreight();
                order.setTotalGoodsAmount(totalGoodsAmount);
                order.setReceiver(orderDeliver.getReceiver());
            }
        } else {
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/cart/order/itf", RequestMethod.get);
            httpRequest.addParameter("userId", userId);
            httpRequest.addParameter("username", username);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            Double nowTotalPrice = 0.00;//调用接口得到实付的金额
            String everyGoodsInfo = null;
            Double memberRivilege = 0.00;//会员优惠金额
            Double totalGoodsAmount = 0.0;
            Double activityBalance = 0.0;//活动优惠多少金额

            if (code.equals("200")) {
                everyGoodsInfo = ValueUtil.getFromJson(temp, "data", "everyGoodsInfo");
                //删除购物车选中的商品
                String deleteCartTemp = deleteCart(order.getUserId());
                String cartCode = ValueUtil.getFromJson(deleteCartTemp, "code");
//                if (!cartCode.equals("201")) {
//                    ValueUtil.isError("调用购物车服务失败，请联系维护人员");
//
//                }
                if(everyGoodsInfo.equals("[]")){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("您多次提交失败，请稍后再试");
                }
                totalGoodsAmount = Double.valueOf(ValueUtil.getFromJson(temp, "data", "originTotalPrice"));//商品总金额
                order.setTotalGoodsAmount(totalGoodsAmount);
                nowTotalPrice = Double.valueOf(ValueUtil.getFromJson(temp, "data", "nowTotalPrice"));
                ;//调用接口得到实付的金额
                memberRivilege = Double.valueOf(ValueUtil.getFromJson(temp, "data", "memberRivilege"));//会员优惠金额
                activityBalance = Double.valueOf(ValueUtil.getFromJson(temp, "data", "balance"));//活动差额（活动优惠）

            }
            JSONArray arr = JSON.parseArray(everyGoodsInfo);
            String goodsType = null;


            Map<String,Integer> skuMap = new HashMap<>();
            Map<String,String> goodsMap = new HashMap<>();
            String saleModel =null;

            for (int i = 0; i < arr.size(); i++) {
                JSONObject goodsInfo = (JSONObject) arr.get(i);
                JSONArray skuInfoArr = goodsInfo.getJSONArray("skuInfo");
                Integer goodsCount = goodsInfo.getInteger("goodsCount");
                String goodsNames = goodsInfo.getString("goodsName");
                Integer goStatus =Integer.valueOf(goodsInfo.getString("goStatus"));
                if(goStatus==2){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("商品"+goodsNames+"已下架,不能提交订单");
                }
                if(skuInfoArr!=null&&skuInfoArr.size()>0){
                    for(int m=0;m<skuInfoArr.size();m++){
                        JSONObject skuInfo = (JSONObject) skuInfoArr.get(m);
                        String skuId = skuInfo.getString("skuId");
                        Integer skuCount = Integer.valueOf(skuInfo.getString("count"))*goodsCount;
                        if(!skuMap.containsKey(skuId)){
                            skuMap.put(skuId,skuCount);
                            goodsMap.put(skuId,goodsNames);
                        }else{
                            Integer beforeCount = skuMap.get(skuId);
                            skuMap.put(skuId,beforeCount+skuCount);
                        }
                    }
                }
                //判断商品库存是否足够
                Set<String> noEnoughGoodsSet = getNoEnoughGoods(skuMap,goodsMap);//库存不足的商品
                if(noEnoughGoodsSet.size()>0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("商品："+noEnoughGoodsSet.toString()+"库存不足");
                }

                OrderDetail orderDetail = new OrderDetail();
                com.alibaba.fastjson.JSONObject adjustCommand1 = (com.alibaba.fastjson.JSONObject) arr.get(i);
                goodsType = adjustCommand1.getString("goodsType");
                //赠品
                String activity = adjustCommand1.getString("activity");
                if (activity != null) {
                    String regulationInfo = adjustCommand1.getString("regulationInfo");//判断是否是赠品
                    Integer regulationId = adjustCommand1.getInteger("regulationId");
                    String isShare = ValueUtil.getFromJson(activity, "isShare");
                    String actionCode = ValueUtil.getFromJson(activity, "actionCode");
//                    if (actionCode.equals(actionCode.indexOf("giftA"))) {
                        if (isShare.equals("true")) {
                            JSONArray regulationArr = JSON.parseArray(regulationInfo);
                            for (int j = 0; j < regulationArr.size(); j++) {
                                com.alibaba.fastjson.JSONObject regulationObject = (com.alibaba.fastjson.JSONObject) regulationArr.get(j);
                                String regulationType = regulationObject.getString("regulationType");
                                String isMeet = regulationObject.getString("isMeet");
                                Integer otherRegulationId = Integer.valueOf(regulationObject.getString("regulationId"));
                                if (isMeet.equals("true") && regulationType.equals("giftA")) {//赠品
                                    String action = regulationObject.getString("action");
                                    JSONArray actionArray = JSONArray.parseArray(action);
                                    for (int f = 0; f < actionArray.size(); f++) {
                                        com.alibaba.fastjson.JSONObject actionArrayObject = (com.alibaba.fastjson.JSONObject) actionArray.get(f);
                                        Integer goodsId = actionArrayObject.getInteger("goodsId");
                                        String goodsName = actionArrayObject.getString("goodsName");
                                        String goodsCode = actionArrayObject.getString("goodsCode");
                                        String goodsImageUrl = actionArrayObject.getString("goodsImageUrl");
//                                        String saleModels = actionArrayObject.getString("saleModel");

                                        OrderDetail giftorderDetail = new OrderDetail();
                                        giftorderDetail.setOrderNo(orderNo);
                                        giftorderDetail.setGoodsId(goodsId);
                                        giftorderDetail.setChannelCode("1");//默认
                                        giftorderDetail.setGoodsName(goodsName);
                                        giftorderDetail.setGoodsCode(goodsCode);
                                        giftorderDetail.setCount(1);
                                        giftorderDetail.setGoodsPrice(0.0);
                                        giftorderDetail.setGift(WhetherEnum.YES);
                                        giftorderDetail.setTradeIn(WhetherEnum.NO);
                                        giftorderDetail.setReasonImg(goodsImageUrl);
//                                        giftorderDetail.setSaleModel(saleModels);

                                        String skuInfo = actionArrayObject.getString("skuInfo");
                                        JSONArray skuArr = JSON.parseArray(skuInfo);
                                        List<OrderDetailSku> orderDetailSkuList = new ArrayList<>();
                                        for (int sk = 0; sk < skuArr.size(); sk++) {
                                            com.alibaba.fastjson.JSONObject skuObject = (com.alibaba.fastjson.JSONObject) skuArr.get(sk);
                                            OrderDetailSku orderDetailSku = new OrderDetailSku();
                                            String skuId = skuObject.getString("skuId");
                                            String skuCode = skuObject.getString("skuCode");
                                            Integer counts = skuObject.getInteger("count");
                                            orderDetailSku.setGoodsId(goodsId);
                                            orderDetailSku.setSkuId(Integer.valueOf(skuId));
                                            orderDetailSku.setSkuCode(skuCode);
                                            orderDetailSku.setOrderNo(orderNo);
                                            if (counts < 0) {
                                                orderDetailSku.setCounts(1);
                                            } else {
                                                orderDetailSku.setCounts(counts);
                                            }
                                            orderDetailSkuList.add(orderDetailSku);
                                        }
                                        giftorderDetail.setOrderDetailSkuList(orderDetailSkuList);
                                        orderDetails.add(giftorderDetail);
                                    }
                                }
                                    if (isMeet.equals("true") && regulationType.equals("tradeInA")) {//换购
                                                String chlidGoodsInfo = adjustCommand1.getString("chlidGoodsInfo");
                                                String  goodsId= ValueUtil.getFromJson(chlidGoodsInfo, "goodsId");
                                                String  goodsName= ValueUtil.getFromJson(chlidGoodsInfo, "goodsName");
                                                String  goodsCode= ValueUtil.getFromJson(chlidGoodsInfo, "goodsCode");
                                                String  goodsImageUrl= ValueUtil.getFromJson(chlidGoodsInfo, "goodsImg");
//                                    String  saleModels= ValueUtil.getFromJson(chlidGoodsInfo, "saleModel");
                                        String  nowPrice= ValueUtil.getFromJson(chlidGoodsInfo, "nowPrice");


                                        OrderDetail tradeorderDetail = new OrderDetail();
                                        tradeorderDetail.setOrderNo(orderNo);
                                        tradeorderDetail.setGoodsId(Integer.valueOf(goodsId));
                                        tradeorderDetail.setChannelCode("1");//默认
                                        tradeorderDetail.setGoodsName(goodsName);
                                        tradeorderDetail.setGoodsCode(goodsCode);
                                        tradeorderDetail.setCount(1);
                                        tradeorderDetail.setGift(WhetherEnum.NO);
                                        tradeorderDetail.setTradeIn(WhetherEnum.YES);
                                        tradeorderDetail.setReasonImg(goodsImageUrl);
                                        tradeorderDetail.setGoodsPrice(Double.valueOf(nowPrice));

//                                    giftorderDetail.setSaleModel(saleModels);

                                                String  skuInfo= ValueUtil.getFromJson(chlidGoodsInfo, "skuInfo");
                                                JSONArray skuArr = JSON.parseArray(skuInfo);
                                                List<OrderDetailSku> orderDetailSkuList = new ArrayList<>();
                                                for (int sk = 0; sk < skuArr.size(); sk++) {
                                                    com.alibaba.fastjson.JSONObject skuObject = (com.alibaba.fastjson.JSONObject) skuArr.get(sk);
                                                    OrderDetailSku orderDetailSku = new OrderDetailSku();
                                                    String skuId = skuObject.getString("skuId");
                                                    String skuCode = skuObject.getString("skuCode");
                                                    Integer counts = skuObject.getInteger("count");
                                                    orderDetailSku.setGoodsId(Integer.valueOf(goodsId));
                                                    orderDetailSku.setSkuId(Integer.valueOf(skuId));
                                                    orderDetailSku.setSkuCode(skuCode);
                                                    orderDetailSku.setOrderNo(orderNo);
                                                    if (counts < 0) {
                                                        orderDetailSku.setCounts(1);
                                                    } else {
                                                        orderDetailSku.setCounts(counts);
                                                    }
                                                    orderDetailSkuList.add(orderDetailSku);
                                                }
                                        tradeorderDetail.setOrderDetailSkuList(orderDetailSkuList);
                                        orderDetails.add(tradeorderDetail);
                                            }
                                        }
                            }
                        else {
                            String regulationType = ValueUtil.getFromJson(regulationInfo, "regulationType");
                            String isMeet = ValueUtil.getFromJson(regulationInfo, "isMeet");
                            Integer otherRegulationId = Integer.parseInt(ValueUtil.getFromJson(regulationInfo, "regulationId"));
                            if (isMeet.equals("true")&& regulationType.equals("giftA")&&otherRegulationId.compareTo(regulationId)==0){
                                String action = ValueUtil.getFromJson(regulationInfo, "action");
                                JSONArray actionArray = JSONArray.parseArray(action);
                                for (int f = 0; f < actionArray.size(); f++) {
                                    com.alibaba.fastjson.JSONObject actionArrayObject = (com.alibaba.fastjson.JSONObject) actionArray.get(f);
                                    Integer goodsId = actionArrayObject.getInteger("goodsId");
                                    String goodsName = actionArrayObject.getString("goodsName");
                                    String goodsCode = actionArrayObject.getString("goodsCode");
                                    String goodsImageUrl = actionArrayObject.getString("goodsImageUrl");
//                                    String saleModels = actionArrayObject.getString("saleModel");

                                    OrderDetail giftorderDetail = new OrderDetail();
                                    giftorderDetail.setOrderNo(orderNo);
                                    giftorderDetail.setGoodsId(goodsId);
                                    giftorderDetail.setChannelCode("1");//默认
                                    giftorderDetail.setGoodsName(goodsName);
                                    giftorderDetail.setGoodsCode(goodsCode);
                                    giftorderDetail.setCount(1);
                                    giftorderDetail.setGoodsPrice(0.0);
                                    giftorderDetail.setGift(WhetherEnum.YES);
                                    giftorderDetail.setReasonImg(goodsImageUrl);
//                                    giftorderDetail.setSaleModel(saleModels);

                                    String skuInfo = actionArrayObject.getString("skuInfo");
                                    JSONArray skuArr = JSON.parseArray(skuInfo);
                                    List<OrderDetailSku> orderDetailSkuList = new ArrayList<>();
                                    for (int sk = 0; sk < skuArr.size(); sk++) {
                                        com.alibaba.fastjson.JSONObject skuObject = (com.alibaba.fastjson.JSONObject) skuArr.get(sk);
                                        OrderDetailSku orderDetailSku = new OrderDetailSku();
                                        String skuId = skuObject.getString("skuId");
                                        String skuCode = skuObject.getString("skuCode");
                                        Integer counts = skuObject.getInteger("count");
                                        orderDetailSku.setGoodsId(goodsId);
                                        orderDetailSku.setSkuId(Integer.valueOf(skuId));
                                        orderDetailSku.setSkuCode(skuCode);
                                        orderDetailSku.setOrderNo(orderNo);
                                        if (counts < 0) {
                                            orderDetailSku.setCounts(1);
                                        } else {
                                            orderDetailSku.setCounts(counts);
                                        }
                                        orderDetailSkuList.add(orderDetailSku);
                                    }
                                    giftorderDetail.setOrderDetailSkuList(orderDetailSkuList);
                                    orderDetails.add(giftorderDetail);
                                }
                            }
                            if (isMeet.equals("true")&& regulationType.equals("tradeInA")&&otherRegulationId.compareTo(regulationId)==0){//是否是换购
                                String chlidGoodsInfo = adjustCommand1.getString("chlidGoodsInfo");
                                String  goodsId= ValueUtil.getFromJson(chlidGoodsInfo, "goodsId");
                                String  goodsName= ValueUtil.getFromJson(chlidGoodsInfo, "goodsName");
                                String  goodsCode= ValueUtil.getFromJson(chlidGoodsInfo, "goodsCode");
                                String  goodsImageUrl= ValueUtil.getFromJson(chlidGoodsInfo, "goodsImg");
                                String  nowPrice= ValueUtil.getFromJson(chlidGoodsInfo, "nowPrice");

//                                String  saleModels= ValueUtil.getFromJson(chlidGoodsInfo, "saleModels");

                                OrderDetail tradeorderDetail = new OrderDetail();
                                tradeorderDetail.setOrderNo(orderNo);
                                tradeorderDetail.setGoodsId(Integer.valueOf(goodsId));
                                tradeorderDetail.setChannelCode("1");//默认
                                tradeorderDetail.setGoodsName(goodsName);
                                tradeorderDetail.setGoodsCode(goodsCode);
                                tradeorderDetail.setCount(1);
                                tradeorderDetail.setGift(WhetherEnum.NO);
                                tradeorderDetail.setTradeIn(WhetherEnum.YES);
                                tradeorderDetail.setGoodsPrice(Double.valueOf(nowPrice));
                                tradeorderDetail.setReasonImg(goodsImageUrl);
//                                giftorderDetail.setSaleModel(saleModels);

                                String  skuInfo= ValueUtil.getFromJson(chlidGoodsInfo, "skuInfo");
                                    JSONArray skuArr = JSON.parseArray(skuInfo);
                                    List<OrderDetailSku> orderDetailSkuList = new ArrayList<>();
                                    for (int sk = 0; sk < skuArr.size(); sk++) {
                                        com.alibaba.fastjson.JSONObject skuObject = (com.alibaba.fastjson.JSONObject) skuArr.get(sk);
                                        OrderDetailSku orderDetailSku = new OrderDetailSku();
                                        String skuId = skuObject.getString("skuId");
                                        String skuCode = skuObject.getString("skuCode");
                                        Integer counts = skuObject.getInteger("count");
                                        orderDetailSku.setGoodsId(Integer.valueOf(goodsId));
                                        orderDetailSku.setSkuId(Integer.valueOf(skuId));
                                        orderDetailSku.setSkuCode(skuCode);
                                        orderDetailSku.setOrderNo(orderNo);
                                        if (counts < 0) {
                                            orderDetailSku.setCounts(1);
                                        } else {
                                            orderDetailSku.setCounts(counts);
                                        }
                                        orderDetailSkuList.add(orderDetailSku);
                                    }
                                tradeorderDetail.setOrderDetailSkuList(orderDetailSkuList);
                                orderDetails.add(tradeorderDetail);
                                }
                        }

                }
                Integer goodsId = adjustCommand1.getInteger("goodsId");//商品
                String goodsName = adjustCommand1.getString("goodsName");//商品名
                Integer count = adjustCommand1.getInteger("goodsCount");//商品数量
                Double goodsPrice = adjustCommand1.getDouble("originPrice");//价格
                String goodsCode = adjustCommand1.getString("goodsCode");
                String reasonImg = adjustCommand1.getString("goodsImg");//图片
                String skuJson = adjustCommand1.getString("skuInfo");//skuJson
                String saleModels = adjustCommand1.getString("saleModel");
                String virtualType = adjustCommand1.getString("virtualType");//虚拟商品

                if (!saleModels.equals("0")) {//如果为非普通商品，同步给商品服务
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("count", count);
                    paramsMap.put("goodsId", goodsId);
                    String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/goods/goods/updateBookNumber/itf", RequestMethod.post, paramsMap, null);
                    System.out.println("预售商品，及抢购商品，同步到商品服务==》" + resultCode);
                }
                List<OrderDetailSku> orderDetailSkuList = new ArrayList<>();


                if (goodsType.equals("luckBage")) {
                    orderDetailSkuList = createLuckyOrderSku(count, goodsId, orderNo);
                    orderDetail.setOrderDetailSkuList(orderDetailSkuList);
                } else {
                    JSONArray arr2 = JSON.parseArray(skuJson);
                    for (int k = 0; k < arr2.size(); k++) {
                        com.alibaba.fastjson.JSONObject adjustCommand2 = (com.alibaba.fastjson.JSONObject) arr2.get(k);
                        OrderDetailSku orderDetailSku = new OrderDetailSku();
                        String skuId = adjustCommand2.getString("skuId");
                        String skuCode = adjustCommand2.getString("skuCode");
                        Integer counts = adjustCommand2.getInteger("count");
                        orderDetailSku.setGoodsId(goodsId);
                        orderDetailSku.setSkuId(Integer.valueOf(skuId));
                        orderDetailSku.setSkuCode(skuCode);
                        orderDetailSku.setOrderNo(orderNo);
                        if (counts < 0) {
                            orderDetailSku.setCounts(1);
                        } else {
                            orderDetailSku.setCounts(counts);
                        }
                        orderDetailSkuList.add(orderDetailSku);
                    }
                }
                String regulation = adjustCommand1.getString("regulation");//找活动规则id
                if (regulation != null) {
                    Integer activityRuleId = Integer.valueOf(ValueUtil.getFromJson(regulation, "id"));
                    orderDetail.setActivityRuleId(activityRuleId);
                }
                orderDetail.setOrderNo(orderNo);
                orderDetail.setGoodsId(goodsId);
                orderDetail.setGoodsName(goodsName);
                orderDetail.setOrderDetailSkuList(orderDetailSkuList);
                orderDetail.setGoodsPrice(goodsPrice);
                orderDetail.setCount(count);
                orderDetail.setComment(WhetherEnum.NO);
                orderDetail.setReasonImg(reasonImg);
                orderDetail.setGoodsCode(goodsCode);
                orderDetail.setSaleModel(saleModels);
                orderDetail.setGift(WhetherEnum.NO);
                orderDetail.setTradeIn(WhetherEnum.NO);
                orderDetail.setVirtualType(virtualType);
                orderDetails.add(orderDetail);
                saleModel = orderDetail.getSaleModel();
            }
            Integer totalNum = 0;
            order.setOrderDetails(orderDetails);//保存订单明细
            for (int i = 0; i < orderDetails.size(); i++) {
                Integer count = orderDetails.get(i).getCount();
                totalNum = totalNum + count;
            }
            order.setTotalNum(totalNum);//商品总数量
            String payType = param.get("payType");

            if (ValueUtil.notEmpity(type) && type.equals("WineStore")) {
                order.setOrderType(OrderType.WineStore);//存酒库订单
                orderPayinfo = createOrderPayinfo(param, orderNo, nowTotalPrice, userInfo, type, memberRivilege, totalGoodsAmount, activityBalance);//订单付款信息表
                orderPayinfoDao.save(orderPayinfo);//保存订单支付详情表
                order.setPaymentType(orderPayinfo.getPayType());
                order.setCopeWith(orderPayinfo.getCopeWith());
                order.setAmount(orderPayinfo.getAmount());
                order.setChannel(0);
                order.setStatus(0);//状态（0完成）
                ordersDao.save(order);
            } else if (goodsType.equals("luckBage")) {
                order.setOrderType(OrderType.LuckyBag);
                orderDeliver = createOrderDeliver(param, orderNo, payType);
                orderDeliverDao.save(orderDeliver);
                order.setStatus(1);//状态（1未支付）
                orderPayinfo = createOrderPayinfo(param, orderNo, nowTotalPrice, userInfo, type, memberRivilege, totalGoodsAmount, activityBalance);//订单付款信息表
                orderPayinfoDao.save(orderPayinfo);////保存订单支付详情表
                order.setPaymentType(orderPayinfo.getPayType());
                order.setCopeWith(orderPayinfo.getCopeWith());
                order.setAmount(orderPayinfo.getAmount());
                order.setChannel(0);
                order.setReceiver(orderDeliver.getReceiver());
                ordersDao.save(order);
            } else if (saleModel.equals("1")) {
                //预售订单
                order.setOrderType(OrderType.PreSale);//预售订单
                orderDeliver = createOrderDeliver(param, orderNo, payType);
                orderDeliverDao.save(orderDeliver);//保存订单配送表
                order.setStatus(1);//状态（1未支付）
                orderPayinfo = createOrderPayinfo(param, orderNo, nowTotalPrice, userInfo, type, memberRivilege, totalGoodsAmount, activityBalance);//订单付款信息表
                orderPayinfoDao.save(orderPayinfo);//保存订单支付详情表
                order.setPaymentType(orderPayinfo.getPayType());
                order.setCopeWith(orderPayinfo.getCopeWith());
                order.setAmount(orderPayinfo.getAmount());
                order.setChannel(0);
                order.setReceiver(orderDeliver.getReceiver());
                ordersDao.save(order);
            } else {
                if((orderDetails.get(0).getVirtualType()==null?(""):orderDetails.get(0).getVirtualType()).equals("giftCard")){
                    order.setOrderType(OrderType.VirtualGoods);//礼品卡虚拟商品
                }else {
                    order.setOrderType(OrderType.Ordinary);//普通订单
                }
                orderDeliver = createOrderDeliver(param, orderNo, payType);
                order.setChannel(0);
                order.setReceiver(orderDeliver.getReceiver());
                orderDeliverDao.save(orderDeliver);//保存订单配送表
                if (payType.equals("CashOnDelivery")) {//货到付款
                    order.setStatus(3);//状态（3待发货）
                    orderPayinfo = createOrderPayinfo(param, orderNo, nowTotalPrice, userInfo, type, memberRivilege, totalGoodsAmount, activityBalance);//订单付款信息表
                    orderPayinfoDao.save(orderPayinfo);
                    order.setPaymentType(orderPayinfo.getPayType());
                    order.setCopeWith(orderPayinfo.getCopeWith());
                    order.setAmount(orderPayinfo.getAmount());
                } else {
                    order.setStatus(1);//状态（1未支付）//
                    //保存订单支付详情表
                    orderPayinfo = createOrderPayinfo(param, orderNo, nowTotalPrice, userInfo, type, memberRivilege, totalGoodsAmount, activityBalance);//订单付款信息表
                    orderPayinfoDao.save(orderPayinfo);
                    order.setPaymentType(orderPayinfo.getPayType());
                    order.setCopeWith(orderPayinfo.getCopeWith());
                    order.setAmount(orderPayinfo.getAmount());
                }
                ordersDao.save(order);
            }

            if (ValueUtil.notEmpity(type) && !type.equals("PreSale")) {//不是预售的
                freeze(orderDetails, null);
            }
        }
        //保存配送信息后使用各种的扣除
        if (orderPayinfo.getCouponId() != null) {//使用优惠券
            otherUsePay.updateCoupon(order.getUserId(), orderPayinfo.getCouponId());
        }
        if (orderPayinfo.getCardNumber() != null) {//使用礼品卡
            otherUsePay.updateGiftCard(orderPayinfo.getCardNumber(), orderNo, orderPayinfo.getGiftCardAmount(),order.getUserId(),username);
        }
        if (orderPayinfo.getBeanAmount() != null) {//使用酒豆
            otherUsePay.updateBeanWine(orderNo, order.getUserId(), orderPayinfo.getBeanAmount());
        }
        if (orderPayinfo.getBalance() != null) {//使用余额
            String remainingSum = ValueUtil.getFromJson(userInfo, "remainingSum");
            String phoneNumber = ValueUtil.getFromJson(userInfo, "phoneNumber");
            otherUsePay.updateBalance(order.getUserId(), orderPayinfo.getBalance(), orderNo, remainingSum, phoneNumber);
        }
        return orderDeliver;
    }

    private List<OrderDetail> createMentionWineOrderDetailS(String json, Integer userId, Long orderNo) throws YesmywineException {//提酒订单明细
        List<OrderDetail> mapList = new ArrayList<>();
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/load/itf", RequestMethod.post);
        httpRequest.addParameter("json", json);
        httpRequest.addParameter("userId", userId);
//        httpRequest.addParameter("extractorderNumber",orderNo);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if (code.equals("201")) {
            String storeWineAttach = ValueUtil.getFromJson(temp, "data", "storeWineAttach");
            JSONArray arr = JSON.parseArray(storeWineAttach);
            for (int i = 0; i < arr.size(); i++) {
                com.alibaba.fastjson.JSONObject adjustCommand = (com.alibaba.fastjson.JSONObject) arr.get(i);
                Long orderNumber = adjustCommand.getLong("orderNumber");//存酒订单号
                Double fee = adjustCommand.getDouble("fee");//存酒费用
                Integer keepwineId = adjustCommand.getInteger("id");//存酒库id
                Integer count = adjustCommand.getInteger("count");//提酒数量
                Integer goodId = adjustCommand.getInteger("goodsId");//商品id
                String goodsName = adjustCommand.getString("goodsName");//商品名称
                String goodsImageUrl = adjustCommand.getString("goodsImageUrl");//商品图片


                OrderDetail ordersGoods = orderDetailDao.findByOrderNoAndGoodsId(orderNumber, goodId);
                if (ordersGoods != null) {
                    if (ordersGoods.getMentionWineCount() != null) {//原存酒订单提酒过酒
                        Integer metionWineCount = ordersGoods.getMentionWineCount() + count;
                        ordersGoods.setMentionWineCount(metionWineCount);
                    } else {
                        ordersGoods.setMentionWineCount(count);
                    }
                    orderDetailDao.save(ordersGoods);//跟新提酒数量
                }
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setWineStoreOrderNo(orderNumber);
                orderDetail.setWineStoreMoney(fee);
                orderDetail.setKeepwineId(keepwineId);
                orderDetail.setCount(count);
                orderDetail.setGoodsId(goodId);
                orderDetail.setGoodsCode(ordersGoods.getGoodsCode());
                orderDetail.setGoodsPrice(ordersGoods.getGoodsPrice());
                orderDetail.setOrderNo(orderNo);
                orderDetail.setReasonImg(goodsImageUrl);
                orderDetail.setSaleModel(ordersGoods.getSaleModel());
                orderDetail.setGoodsName(goodsName);

                List<OrderDetailSku> orderDetailSkuList = orderDetailSkuDao.findByGoodsIdAndOrderNo(goodId, orderNumber);
                List<OrderDetailSku> skus = new ArrayList<>();
                for (int j = 0; j < orderDetailSkuList.size(); j++) {
                    Integer skuId = orderDetailSkuList.get(j).getSkuId();
                    String skuCode = orderDetailSkuList.get(j).getSkuCode();
                    OrderDetailSku orderDetailSku = new OrderDetailSku();
                    orderDetailSku.setOrderNo(orderNo);
                    orderDetailSku.setGoodsId(goodId);
                    orderDetailSku.setSkuId(skuId);
                    orderDetailSku.setSkuCode(skuCode);
                    skus.add(orderDetailSku);
                }
                orderDetail.setOrderDetailSkuList(skus);
                mapList.add(orderDetail);
            }
        } else {
            String msg = ValueUtil.getFromJson(temp, "msg");
            ValueUtil.isError(msg);
        }
        return mapList;
    }

    private List<OrderDetailSku> createLuckyOrderSku(Integer count, Integer goodsId, Long orderNo) throws YesmywineException {//福袋订单明细
        List<OrderDetailSku> skus = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/goods/getSkuId", RequestMethod.get);
            httpRequest.addParameter("goodsId", goodsId);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            if (code.equals("200")) {
                String content = ValueUtil.getFromJson(temp, "data");
                JSONArray jsonArray = JSON.parseArray(content);
                for (int k = 0; k < jsonArray.size(); k++) {
                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) jsonArray.get(k);
                    Integer skuId = jsonObject.getInteger("skuId");//skuId
                    Integer counts = jsonObject.getInteger("count");//数量
                    String skuCode = jsonObject.getString("code");//sku编码
                    OrderDetailSku orderDetailSku = new OrderDetailSku();
                    orderDetailSku.setOrderNo(orderNo);
                    orderDetailSku.setGoodsId(goodsId);
                    orderDetailSku.setSkuId(skuId);
                    orderDetailSku.setSkuCode(skuCode);
                    orderDetailSku.setCounts(counts);
                    skus.add(orderDetailSku);
                }
            } else {
                String msg = ValueUtil.getFromJson(temp, "msg");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError(msg);
            }
        }
        return skus;
    }

    private OrderPayinfo createOrderPayinfo(Map<String, String> param, Long orderNo, Double nowTotalPrice, String userInfo, String type, Double memberRivilege, Double totalGoodsAmount, Double activityBalance) throws YesmywineException {//订单付款信息
        //settlement  type:{ coupon:优惠券 giftCards:礼品卡 beanWine:酒豆 balance:余额 }
        String userId = ValueUtil.getFromJson(userInfo, "id");
        OrderPayinfo orderPayinfo = new OrderPayinfo();
        orderPayinfo.setOrderNo(orderNo);
        Double copeWith ;
        if(nowTotalPrice>0.00){
            copeWith = nowTotalPrice;
        }else {
            copeWith=0.00;
        }
        Double amount=0.00;

        String provinceId = param.get("provinceId");//省id
        if (provinceId == null || provinceId.isEmpty()) {
            orderPayinfo.setFreight(0.00);
            orderPayinfo.setPreferentialFreight(0.00);
        } else if (type.equals("WineStore")) {
            orderPayinfo.setFreight(0.00);
            orderPayinfo.setPreferentialFreight(0.00);
        } else {
            com.alibaba.fastjson.JSONObject freightJson = new com.alibaba.fastjson.JSONObject();
            if (type.equals("MentionWine")) {
                freightJson = freightImpl.calculate(provinceId, totalGoodsAmount);
            } else {
                freightJson = freightImpl.calculate(provinceId, nowTotalPrice);
            }
            if (type.equals("WineStore")) {
                amount = copeWith;
            } else {
                Double courierfees = freightJson.getDouble("courierfees");//优惠运费
                Double areaPostage = freightJson.getDouble("areaPostage");//运费
                if (courierfees != 0.0) {
                    copeWith = copeWith + courierfees;
                    amount = copeWith;
                    orderPayinfo.setFreight(areaPostage);
                    orderPayinfo.setPreferentialFreight(0.00);
                } else {
                    amount = copeWith;
                    orderPayinfo.setFreight(0.00);
                    orderPayinfo.setPreferentialFreight(areaPostage);
                }
            }
        }
        String settlement = param.get("settlement");
        if (ValueUtil.notEmpity(settlement)) {
            JsonParser jsonParser1 = new JsonParser();
            JsonArray arr1 = jsonParser1.parse(settlement).getAsJsonArray();
            for (int i = 0; i < arr1.size(); i++) {
                String types = arr1.get(i).getAsJsonObject().get("type").getAsString();
                if (types.equals("coupon")) {
                    String couponId = arr1.get(i).getAsJsonObject().get("couponId").getAsString();
                    if (couponId != null) {
                        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/coupon/one/itf", RequestMethod.get);
                        httpRequest.addParameter("userId", Integer.valueOf(userId));
                        httpRequest.addParameter("userCouponId", Integer.valueOf(couponId));
                        httpRequest.run();
                        String temp = httpRequest.getResponseContent();
                        Double couponMoney = Double.valueOf(ValueUtil.getFromJson(temp, "data", "cut"));

                        if (nowTotalPrice > Double.valueOf(couponMoney)) {
                            Double price = nowTotalPrice - couponMoney;//原价-优惠券
                            BigDecimal c1 = new BigDecimal(nowTotalPrice);//实付金额
                            BigDecimal c2 = new BigDecimal(couponMoney);//优惠券
                            if(ValueUtil.notEmpity(provinceId)) {
                                com.alibaba.fastjson.JSONObject json = freightImpl.calculate(provinceId, price.doubleValue());
                                Double courierfees1 = json.getDouble("courierfees");//快递费
                                Double areaPostage1 = json.getDouble("areaPostage");//运费
                                BigDecimal c3 = new BigDecimal(areaPostage1);//运费

                                orderPayinfo.setFreight(areaPostage1);
                                if (courierfees1 != 0.0) {//不免运费
//                                        copeWith = nowTotalPrice + areaPostage1 - couponMoney;//实付金额+运费-优惠券
                                    BigDecimal result = c1.add(c3).subtract(c2);//实付金额+运费-优惠券
                                    copeWith = result.doubleValue();
//                                        amount=nowTotalPrice+ areaPostage1 - couponMoney;
                                    BigDecimal amountResult=c1.add(c3).subtract(c2);
                                    amount=amountResult.doubleValue();
                                    orderPayinfo.setFreight(areaPostage1);
                                    orderPayinfo.setPreferentialFreight(0.00);
                                } else {//免运费
//                                        copeWith = nowTotalPrice - couponMoney;//实付金额-优惠券
                                    BigDecimal result = c1.subtract(c2);//实付金额-优惠券
                                    copeWith = result.doubleValue();
//                                        amount= nowTotalPrice - couponMoney;//实付金额-优惠券
                                    BigDecimal amountResult=c1.subtract(c2);
                                    amount=amountResult.doubleValue();
                                    orderPayinfo.setFreight(0.00);
                                    orderPayinfo.setPreferentialFreight(areaPostage1);
                                }
                            } else {
//                                    copeWith = nowTotalPrice - couponMoney;//实付金额-优惠券
                                BigDecimal result = c1.subtract(c2);//实付金额-优惠券
                                copeWith = result.doubleValue();
//                                    amount= nowTotalPrice - couponMoney;//实付金额-优惠券
                                BigDecimal amountResult=c1.subtract(c2);
                                amount=amountResult.doubleValue();
                            }
                            orderPayinfo.setCouponId(Integer.valueOf(couponId));
                            orderPayinfo.setCouponAmount(Double.valueOf(couponMoney));
                        }
                    }
                } else if (types.equals("giftCards")) {
                    String cardNumber = arr1.get(i).getAsJsonObject().get("cardNumber").getAsString();
                    if (cardNumber != null) {
                        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/goods/giftCard/itf", RequestMethod.get);
                        httpRequest.addParameter("cardNumber", cardNumber);
                        httpRequest.run();
                        String temp = httpRequest.getResponseContent();
                        String remainingSum = ValueUtil.getFromJson(temp, "data", "remainingSum");
                        if (copeWith >= Double.valueOf(remainingSum)) {
                            BigDecimal b1 = new BigDecimal(copeWith);
                            BigDecimal b2 = new BigDecimal(remainingSum);
                            Double result = b1.subtract(b2).doubleValue();//应付价格-剩余金额
                            copeWith = result;
                            orderPayinfo.setCardNumber(Long.valueOf(cardNumber));
                            orderPayinfo.setGiftCardAmount(Double.valueOf(remainingSum));
                        } else {
                            orderPayinfo.setCardNumber(Long.valueOf(cardNumber));
                            orderPayinfo.setGiftCardAmount(copeWith);
                            copeWith = 0.00;
                            break;
                        }
                    }
                } else if (types.equals("beanWine")) {
//                        String bean = ValueUtil.getFromJson(userInfo, "bean");
//                        String[] beanArr = bean.toString().split("\\.");
//                        String beanfront=beanArr[0];
//                        if(Double.valueOf(bean)>0.00) {
//                            if (copeWith >= Double.valueOf(bean)) {
//                                BigDecimal b1 = new BigDecimal(copeWith.toString());
//                                BigDecimal b2 = new BigDecimal(beanfront);
//                                Double result = b1.subtract(b2).doubleValue();
//                                copeWith = result;
//                                orderPayinfo.setBeanAmount(b2.doubleValue());
//                            } else {
//                                String[] arr = copeWith.toString().split("\\.");
//                                String front=arr[0];
//                                BigDecimal frontb1 = new BigDecimal(front);
//                                orderPayinfo.setBeanAmount(frontb1.doubleValue());
//                                String xiaoshu = "0"+copeWith.toString().substring(copeWith.toString().indexOf("."));
//                                copeWith =  Double.valueOf(xiaoshu);
//                                break;
//                            }
//                        }

                    HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/user/rule/itf", RequestMethod.get);
                    httpRequest.addParameter("channelCode", "GW");
                    httpRequest.run();
                    String temp = httpRequest.getResponseContent();
                    String mobe = ValueUtil.getFromJson(temp, "data", "mobe");//人民币兑换酒豆
                    String[] mobeArray = mobe.split(":");
                    String mobeF=mobeArray[0];
                    String mobeE=mobeArray[1];
                    BigDecimal rmb = new BigDecimal(mobeF);
                    BigDecimal jd = new BigDecimal(mobeE);

                    String bean =ValueUtil.getFromJson(userInfo, "bean");
                    String[] beanArr = bean.toString().split("\\.");
                    String beanfront=beanArr[0];//可使用的酒豆
                    BigDecimal b1 = new BigDecimal(copeWith.toString());
                    BigDecimal beanbg = new BigDecimal(beanfront);

                    BigDecimal jdrmb=beanbg.divide(jd, 2, BigDecimal.ROUND_HALF_EVEN);//按照比例该使用多少酒逗 例如：8/3
                    String[] beanRmb = jdrmb.toString().split("\\.");
                    String beanRmbfront=beanRmb[0];
                    BigDecimal beanMoney = new BigDecimal(beanRmbfront);//最终可使用的酒豆
                    if(Double.valueOf(beanRmbfront)>0.00) {
                        if (b1.doubleValue() >= beanMoney.doubleValue()) {//实付金额大于酒豆的时候
                            Double result = b1.subtract(beanMoney).doubleValue();
                            copeWith = result;
//                                map.put("canBeanAmount",beanRmbfront);
                            orderPayinfo.setBeanAmount(Double.valueOf(beanRmbfront));

                        } else {
                            String[] arr = copeWith.toString().split("\\.");
                            String front = arr[0];
                            BigDecimal frontb1 = new BigDecimal(front);
                            BigDecimal result=frontb1.multiply(jd);//按照比例该使用多少酒逗
//                                map.put("canBeanAmount", result.doubleValue());
                            orderPayinfo.setBeanAmount(result.doubleValue());
                            String xiaoshu = "0" + copeWith.toString().substring(copeWith.toString().indexOf("."));
                            copeWith =Double.valueOf(xiaoshu);
                            if(copeWith==0.00){
                                break;
                            }
                        }
                    }
                } else if (types.equals("balance")) {
                    String remainingSum = ValueUtil.getFromJson(userInfo, "remainingSum");
                    if(Double.valueOf(remainingSum)>0.00) {
                        if (copeWith >= Double.valueOf(remainingSum)) {
                            BigDecimal b1 = new BigDecimal(copeWith);
                            BigDecimal b2 = new BigDecimal(remainingSum);
                            Double result = b1.subtract(b2).doubleValue();
                            copeWith = result;
                            orderPayinfo.setBalance(b2.doubleValue());
                        } else {
                            orderPayinfo.setBalance(copeWith);
                            copeWith = 0.00;
                            break;
                        }
                    }
                }
            }
        }

        orderPayinfo.setCopeWith(copeWith);
        BigDecimal amounts = new BigDecimal(amount);
        orderPayinfo.setAmount(amounts.doubleValue());
        orderPayinfo.setPreferentialAmount(memberRivilege);//
        orderPayinfo.setActivityAmount(activityBalance);
        orderPayinfo.setTotalGoodsAmount(totalGoodsAmount);
        if(copeWith==0.00){
            orderPayinfo.setPayType(Payment.Internal);
        }else {
            orderPayinfo.setPayType(Payment.getPayment(param.get("payType")));
        }
        return orderPayinfo;
    }

    private OrderDeliver createOrderDeliver(Map<String, String> param, Long orderNo, String payType) throws YesmywineException {//订单配送信息
        OrderDeliver orderDeliver = new OrderDeliver();
        orderDeliver.setOrderNo(orderNo);
        Integer deliverType = Integer.valueOf(param.get("deliverType"));
        Map<String, Object> payParams = new HashMap<>();
        String areaId=param.get("areaId");
        if(ValueUtil.isEmpity(areaId)){
            ValueUtil.isError("请选择收货地址");
        }
        payParams.put("id", Integer.valueOf(areaId));
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/receivingAddress/itf", RequestMethod.get, payParams, null);
        String code = ValueUtil.getFromJson(result, "code");
        if ("200".equals(code)) {
            String address = ValueUtil.getFromJson(result, "data", "detailedAddress");
            String receiver = ValueUtil.getFromJson(result, "data", "receiver");
            String phoneNumber = ValueUtil.getFromJson(result, "data", "phoneNumber");

            String province = ValueUtil.getFromJson(result, "data", "province");
            String city = ValueUtil.getFromJson(result, "data", "city");
            String area = ValueUtil.getFromJson(result, "data", "area");

            StringBuilder sb = new StringBuilder();
            sb.append(province);
            sb.append(city);
            sb.append(area);
            sb.append(address);

            if (deliverType == 1) {//上门自提
                orderDeliver.setWarehouseCode(param.get("warehouseCode"));
            }
            orderDeliver.setDeliverType(deliverType);
            orderDeliver.setReceiver(receiver);
            orderDeliver.setAreaId(Integer.valueOf(areaId));
            orderDeliver.setAddress(sb.toString());
            orderDeliver.setPhone(phoneNumber);
            if (payType.equals("CashOnDelivery")) {//货到付款
                orderDeliver.setCashOnDelivery(WhetherEnum.YES);
                orderDeliver.setTimeSlot(Integer.valueOf(param.get("timeSlot")));//0周一至周五，1双休
                orderDeliver.setCashorCard(Integer.valueOf(param.get("cashorCard")));//0现金，1刷卡
            } else {
                orderDeliver.setCashOnDelivery(WhetherEnum.NO);
            }
        }
        return orderDeliver;
    }

    public void createOrderDispatch(Long orderNo, Integer status) {//保存订单操作信息
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(orderNo);
        orderDispatch.setOperator(0);//0客户/1也买酒
        orderDispatch.setStatus(status);//1待付款
        orderDispatch.setLabel(0);//0订单，1退换货
        orderDispatchDao.save(orderDispatch);
    }


    public String cancel(Long id, String userInfo) throws YesmywineException {

        ValueUtil.verify(id, "idNull");
        Orders orders = ordersDao.findOne(id);
        if (orders.getStatus() == 1) {//未支付
            orders.setStatus(2);//2已取消
            orders.setCancelTime(new Date());
            ordersDao.save(orders);
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(orders.getOrderNo());
            orderDispatch.setOperator(0);//0客户/1也买酒
            orderDispatch.setStatus(2);
            orderDispatch.setLabel(0);//0订单1退换单
            orderDispatchDao.save(orderDispatch);
            if (orders.getOrderType() == OrderType.LuckyBag) {
                cancelluckyBag(orders.getOrderNo());
            }
            List<OrderDetailSku> orderDetailSkuList = orderDetailSkuDao.findByOrderNo(orders.getOrderNo());
            releaseFreeze(orderDetailSkuList, orders);

            //取消订单
            String phoneNumber = ValueUtil.getFromJson(userInfo, "phoneNumber");
            Double remainingSum = Double.valueOf(ValueUtil.getFromJson(userInfo, "remainingSum"));
            Map<String, Object> smsParams = new HashMap<>();
            JSONObject objects = new JSONObject();
            OrderDeliver orderDeliver = orderDeliverDao.findByOrderNo(orders.getOrderNo());
            if (orderDeliver != null) {
                objects.put("username", orders.getReceiver());
                smsParams.put("phones", orderDeliver.getPhone());
            } else {
                if (null != phoneNumber) {
                    String username = ValueUtil.getFromJson(userInfo, "userName");
                    objects.put("username", username);
                    smsParams.put("phones", phoneNumber);
                }
            }
            objects.put("orderNo", orders.getOrderNo());

            smsParams.put("json", objects);
            smsParams.put("code", "keZNhLiJdj");

            String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
            String code = ValueUtil.getFromJson(result, "code");
//            if (!"201".equals(code)) {
//                ValueUtil.isError(ValueUtil.getFromJson(result, "message"));
//            }
            //取消各种优惠券，礼品卡，酒豆，余额
            otherUsePay.cancelOtherPay(orders, phoneNumber, remainingSum);


            if (orders.getOrderType() == OrderType.MentionWine) {//提酒订单
                cancelMentionWine(orders.getOrderNo(), orders.getUserId());
            } else if (orders.getOrderType() == OrderType.PreSale) {//预售订单
                List<OrderDetail> orderDetails = orderDetailDao.findByOrderNo(orders.getOrderNo());
                for (int i = 0; i < orderDetails.size(); i++) {
                    cancelPreSaleOrders(orderDetails.get(i).getCount(), orderDetails.get(i).getGoodsId());
                }
            }


        } else if (orders.getStatus() == 3) {
//            if (orders.getOrderType().equals(OrderType.MentionWine)) {
//                com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
//                com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
//                List<OrderDetail> orderDetails = orderDetailDao.findByOrderNo(orders.getOrderNo());
//                for (int i = 0; i < orderDetails.size(); i++) {
//                    com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//                    jsonObject.put("keepwineId", orderDetails.get(i).getKeepwineId());
//                    jsonArray.add(jsonObject);
//                }
//                String userId = ValueUtil.getFromJson(userInfo, "id");
//                json.put("userId", userId);
//                json.put("content", jsonArray);
//
//                Map<String, Object> paramsMap = new HashMap<>();
//                paramsMap.put("json", json);
//                String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/keepWine/itf/cancel", RequestMethod.post, paramsMap, null);
//                String code = ValueUtil.getFromJson(result, "code");
//                if (!"201".equals(code)) {
//                    ValueUtil.isError(ValueUtil.getFromJson(result, "message"));
//                }
//            }
            orders.setStatus(4);//4取消订单 待审核
            ordersDao.save(orders);
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(orders.getOrderNo());
            orderDispatch.setOperator(0);//0客户/1也买酒
            orderDispatch.setStatus(4);//取消中
            orderDispatch.setLabel(0);//0订单1退换单
            orderDispatchDao.save(orderDispatch);
            //推送oms
            omsCancelOrder(orders);
        }

        return "success";
    }

    private void omsCancelOrder(Orders orders) {
        JSONObject jsonData = new JSONObject();
        jsonData.put("orderNo",orders.getOrderNo());
        String result = SynchronizeUtils.getOmsResult(Dictionary.OMS_HOST,"/cancelOmsOrder",RequestMethod.post,null,jsonData.toJSONString());
        if(result!=null){
            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.getString("status");
            String message = jsonObject.getString("message");
            if(code==null||!code.equals("success")){
                orders.setSynStatus(1);
                orders.setSynFailMassage(message);
                ordersDao.save(orders);
            }
        }else{
            orders.setSynStatus(1);
            ordersDao.save(orders);
        }
    }

    public void cancelluckyBag(Long orderNo) throws YesmywineException {
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderNo(orderNo);
        for (int i = 0; i < orderDetailList.size(); i++) {
            com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
            List<OrderDetailSku> orderDetailSkuList = orderDetailSkuDao.findByGoodsIdAndOrderNo(orderDetailList.get(i).getGoodsId(), orderNo);
            for (int j = 0; j < orderDetailSkuList.size(); j++) {
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                jsonObject.put("skuId", orderDetailSkuList.get(j).getSkuId());
                jsonObject.put("count", orderDetailSkuList.get(j).getCounts());
                jsonArray.add(jsonObject);
            }
            HttpBean httpBean1 = new HttpBean(Dictionary.MALL_HOST + "/web/goods/cancelSkuId", RequestMethod.post);
            httpBean1.addParameter("goodsId", orderDetailList.get(i).getGoodsId());
            httpBean1.addParameter("jsonArray", jsonArray);
            httpBean1.run();
            String temp = httpBean1.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(code)) {
                ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
            }
        }
    }

    public void cancelMentionWine(Long orderNo, Integer userId) throws YesmywineException {
        List<OrderDetail> orderDetailDaos = orderDetailDao.findByOrderNo(orderNo);
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for (int i = 0; i < orderDetailDaos.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
            jsonObject1.put("id", orderDetailDaos.get(i).getKeepwineId());
            jsonObject1.put("count", orderDetailDaos.get(i).getCount());
//            jsonObject1.put("fee", orderDetailDaos.get(i).getWineStoreMoney());
            jsonArray.add(jsonObject1);
        }

        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/cancel/itf", RequestMethod.post);
        httpRequest.addParameter("json", jsonArray);
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("extractorderNumber", orderNo);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if (!"201".equals(code)) {
            ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
        }
    }


    public void cancelPreSaleOrders(Integer count, Integer goodsId) {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("count", count);
        paramsMap.put("goodsId", goodsId);
        paramsMap.put("code", "1");
        String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/goods/goods/updateBookNumber/itf", RequestMethod.post, paramsMap, null);
        System.out.println("预售商品，及抢购商品，同步到商品服务==》" + resultCode);

    }
    public void  randomGiftCart(Long orderNo) {
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderNo(orderNo);
        for (int i = 0; i < orderDetailList.size(); i++) {
            List<OrderDetailSku> orderDetailSkuList = orderDetailList.get(i).getOrderDetailSkuList();
            Integer skuId = orderDetailSkuList.get(0).getSkuId();
            Integer counts = orderDetailList.get(i).getCount();
            Map<String, Object> map = new HashMap<>();
            map.put("skuId", skuId);
            map.put("counts", counts);
            String results = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/giftCard/randomGiftCard/itf", RequestMethod.post, map, null);
            String codes = ValueUtil.getFromJson(results, "code");
            if ("201".equals(codes)) {
                String data = ValueUtil.getFromJson(results, "data");
                JSONArray arr = JSON.parseArray(data);
                List<OrderGiftCard> orderGiftCardArrayList = new ArrayList<>();
                for (int k = 0; k < arr.size(); k++) {
                    com.alibaba.fastjson.JSONObject adjustCommand2 = (com.alibaba.fastjson.JSONObject) arr.get(k);
                    OrderGiftCard orderGiftCard = new OrderGiftCard();
                    String cardNumber = adjustCommand2.getString("cardNumber");
                    orderGiftCard.setOrderNo(orderNo);
                    orderGiftCard.setCardNumber(Long.valueOf(cardNumber));
                    Integer goodsId = orderDetailSkuList.get(0).getGoodsId();
                    orderGiftCard.setGoodsId(goodsId);
                    orderGiftCardArrayList.add(orderGiftCard);
                }
                orderGiftCardDao.save(orderGiftCardArrayList);
            }
        }
    }


    public Map<String, Object> updateLoad(Long id, HttpServletRequest request) throws YesmywineException {//加载订单内容
        ValueUtil.verify(id, "idNull");
        Orders orders = ordersDao.findOne(id);
        List list = new ArrayList();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(5);

        List<OrderDispatch> orderDispatch = orderDispatchDao.findByOrderNoAndStatusIn(orders.getOrderNo(), list);

        OrderDeliver orderDeliver = orderDeliverDao.findByOrderNo(orders.getOrderNo());
        OrderPayinfo orderPayinfo = orderPayinfoDao.findByOrderNo(orders.getOrderNo());
        List<OrderDetail> orderDetail = orderDetailDao.findByOrderNo(orders.getOrderNo());
        OrderInvoice orderInvoice=orderInvoiceDao.findByOrderNo(orders.getOrderNo());
        Map<String, Object> map = new HashMap<>();

        HttpBean httpBean1 = new HttpBean(Dictionary.MALL_HOST + "/userservice/userInfomation/show/itf", RequestMethod.get);
        httpBean1.addParameter("userId", orders.getUserId());
//        httpBean1.addParameter("request",request);
        httpBean1.run();
        String temp = httpBean1.getResponseContent();
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        String userName = ValueUtil.getFromJson(temp, "data", "userName");
        String email = ValueUtil.getFromJson(temp, "data", "email");
        String phoneNumber = ValueUtil.getFromJson(temp, "data", "phoneNumber");

        jsonObject.put("userName", userName);
        jsonObject.put("email", email);
        jsonObject.put("phone", phoneNumber);
        jsonObject.put("orderNo", orders.getOrderNo());
        jsonObject.put("channel", orders.getChannel());
        jsonObject.put("orderType", orders.getOrderType());
        jsonObject.put("userId", orders.getUserId());
        jsonObject.put("totalGoodsAmount", orders.getTotalGoodsAmount());
        jsonObject.put("copeWith", orders.getCopeWith());
        jsonObject.put("totalNum", orders.getTotalNum());
        jsonObject.put("wineStoreMoney", orders.getWineStoreMoney());
        jsonObject.put("paymentType", orders.getPaymentType());
        jsonObject.put("confirmTime", orders.getConfirmTime());
        jsonObject.put("payTime", orders.getPayTime());
        jsonObject.put("delieverTime", orders.getDelieverTime());
        jsonObject.put("receiveTime", orders.getReceiveTime());
        jsonObject.put("cancelTime", orders.getCancelTime());
        jsonObject.put("status", orders.getStatus());
        jsonObject.put("invoiceNeedFlag", orders.getInvoiceNeedFlag());
        jsonObject.put("note", orders.getNote());
        jsonObject.put("comment", orders.getComment());
        jsonObject.put("synStatus", orders.getSynStatus());
        jsonObject.put("id", orders.getId());
        jsonObject.put("createTime", orders.getCreateTime());


        map.put("order", jsonObject);
        map.put("orderDispatch", orderDispatch);
        map.put("orderDeliver", orderDeliver);
        map.put("orderPayinfo", orderPayinfo);
        map.put("orderDetail", orderDetail);
        map.put("orderInvoice",orderInvoice);
        return map;
    }

    public String cancels() {//定时30分钟取消订单
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        System.out.println("系统当前时间：" + df.format(date));
        c.setTime(date);//设置参数时间
        c.add(Calendar.MINUTE, -30);//把日期往后增加MINUTE.整数往后推,负数往前移动
        date = c.getTime();
        System.out.println("系统前30分钟时间：" + date);
        String str = df.format(date);
        List<Orders> orderss = ordersDao.findByStatus(1);
        List<Orders> orderss1 = new ArrayList<>();
        for (int i = 0; i < orderss.size(); i++) {
            Date createTime = orderss.get(i).getCreateTime();
            if (createTime.getTime() < date.getTime()) {
                orderss1.add(orderss.get(i));
            }
        }
        orderss1.forEach(r -> {
            r.setCancelTime(new Date());
            r.setStatus(2);//已取消
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(r.getOrderNo());
            orderDispatch.setOperator(1);//0客户/1也买酒
            orderDispatch.setStatus(2);//已取消
            orderDispatch.setLabel(0);//0订单1退换单
            orderDispatchDao.save(orderDispatch);

            //取消各种优惠券，礼品卡，酒豆，余额
            Map<String, Object> map = new HashMap<>();
            map.put("userId", r.getUserId());
            String results = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/userInfomation/show/itf", RequestMethod.get, map, null);
            String codes = ValueUtil.getFromJson(results, "code");
            if ("200".equals(codes)) {
                Double remainingSum = Double.valueOf(ValueUtil.getFromJson(results, "data", "remainingSum"));
                String phoneNumber = ValueUtil.getFromJson(results, "data", "phoneNumber");
                try {
                    otherUsePay.cancelOtherPay(r, phoneNumber, remainingSum);
                } catch (YesmywineException e) {
                    e.printStackTrace();
                }
            }
        });
        ordersDao.save(orderss1);
        return "success";
    }

    public String confirm(Long id) throws YesmywineException {//确认订单完成
        ValueUtil.verify(id, "idNull");
        Orders orders = ordersDao.findOne(id);
        orders.setStatus(0);//完成
        orders.setComment(WhetherEnum.NO);//完成未评价
        orders.setConfirmTime(new Date());
        ordersDao.save(orders);

        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(orders.getOrderNo());
        orderDispatch.setOperator(1);//0客户/1也买酒
        orderDispatch.setStatus(0);//完成
        orderDispatch.setLabel(0);//0订单1退换单
        orderDispatchDao.save(orderDispatch);
        return "true";
    }

    public String confirms() {//定时多少填后确认订单
        List<TaskConfigure> taskConfigure = taskConfigureDao.findAll();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = new GregorianCalendar();
        Date date = new Date();
        System.out.println("系统当前时间：" + df.format(date));
        c.setTime(date);//设置参数时间
        Integer time = taskConfigure.get(0).getTime();
        c.add(Calendar.DAY_OF_MONTH, -time);//把日期往后增加MINUTE.整数往后推,负数往前移动
        date = c.getTime();
        System.out.println("系统前30分钟时间：" + date);
        String str = df.format(date);
        List<Orders> orderss = ordersDao.findByStatus(5);//5待收货
        List<Orders> orderss1 = new ArrayList<>();
        for (int i = 0; i < orderss.size(); i++) {
            Date delieverTime = orderss.get(i).getDelieverTime();
            if (delieverTime.getTime() < date.getTime()) {
                orderss1.add(orderss.get(i));
            }
        }
        orderss1.forEach(r -> {
            r.setCancelTime(new Date());
            r.setStatus(0);//完成
        });
        ordersDao.save(orderss1);
        for (int j = 0; j < orderss1.size(); j++) {
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(orderss1.get(j).getOrderNo());
            orderDispatch.setOperator(1);//0客户/1也买酒
            orderDispatch.setStatus(0);//完成
            orderDispatch.setLabel(0);//0订单1退换单
            orderDispatchDao.save(orderDispatch);
        }
        return "success";
    }


    public String deleteCart(Integer userId) {//提交订单号删除购物车
        HttpBean cartHttp = new HttpBean(Dictionary.MALL_HOST + "/cart/cart/deleteOrderGoods/itf", RequestMethod.post);
        cartHttp.addParameter("userId", userId);
        cartHttp.run();
        String deleteCartTemp = cartHttp.getResponseContent();
        return deleteCartTemp;
    }

    public void freeze(List<OrderDetail> detailList, String a) throws YesmywineException {
        for (OrderDetail detail : detailList) {
            if (!detail.getSaleModel().equals("2")) {
                List<OrderDetailSku> orderDetailSkuLis = detail.getOrderDetailSkuList();
                freeze(orderDetailSkuLis);
            }
        }
    }

    public void freeze(List<OrderDetailSku> orderDetailSkuList) throws YesmywineException {

        StringBuilder skuIds = new StringBuilder();
        StringBuilder counts = new StringBuilder();

        for (int i = 0; i < orderDetailSkuList.size(); i++) {
            if (i == orderDetailSkuList.size() - 1)//当循环到最后一个的时候 就不添加逗号,
            {
                skuIds.append(orderDetailSkuList.get(i).getSkuId());
                counts.append(orderDetailSkuList.get(i).getCounts());
            } else {
                skuIds.append(orderDetailSkuList.get(i).getSkuId());
                skuIds.append(",");
                counts.append(orderDetailSkuList.get(i).getCounts());
                counts.append(",");
            }
        }
        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/inventory/channelInventory/itf/freeze", RequestMethod.post);//冻结库存
        httpBean.addParameter("skuIds", skuIds.toString());
        httpBean.addParameter("counts", counts.toString());
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if (!"201".equals(code)) {
            ValueUtil.isError(ValueUtil.getFromJson(temp, "message"));
        }
    }

    public void releaseFreeze(List<OrderDetailSku> orderDetailSkuList, Orders orders) throws YesmywineException {
        //商城解冻库存
        StringBuilder skuIds = new StringBuilder();
        StringBuilder counts = new StringBuilder();
        for (int i = 0; i < orderDetailSkuList.size(); i++) {
            if (i == orderDetailSkuList.size() - 1)//当循环到最后一个的时候 就不添加逗号,
            {
                skuIds.append(orderDetailSkuList.get(i).getSkuId());
                counts.append(orderDetailSkuList.get(i).getCounts());
            } else {
                skuIds.append(orderDetailSkuList.get(i).getSkuId());
                skuIds.append(",");
                counts.append(orderDetailSkuList.get(i).getCounts());
                counts.append(",");
            }
        }

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("skuIds", skuIds.toString());
        paramsMap.put("counts", counts.toString());
        String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/inventory/channelInventory/itf/releaseFreeze", RequestMethod.post, paramsMap, null);
        if (!"201".equals(resultCode)) {
            orders.setSynStatus(3);
            ordersDao.save(orders);
        }
    }


    public Map<String, Object> viewLogistics(Long orderNo, Integer userId) throws YesmywineException {
        OrderDeliver orderDeliver = orderDeliverDao.findByOrderNo(orderNo);
        List<OrderDetail> orderDetailList = orderDetailDao.findByOrderNo(orderNo);
        Map<String, Object> map = new HashMap<>();
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        if (orderDeliver != null) {
            String shipperCode = orderDeliver.getShipperCode();
            String waybillNumber = orderDeliver.getWaybillNumber();
            Map<String, Object> payParams = new HashMap<>();
            payParams.put("shipperCode", shipperCode);
            String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/logistics/shippers", RequestMethod.get, payParams, null);
            String shipperName = null;
            if (result != null) {
                JSONObject jsonObj = JSON.parseObject(result);
                String code1 = jsonObj.getString("code");
                if (code1.equals("200")) {
                    String data = jsonObj.getString("data");
                    shipperName = ValueUtil.getFromJson(data, "shipperName");
                } else {
                    String msg = jsonObj.getString("msg");
                    ValueUtil.isError(msg);
                }
            }
            jsonObject.put("waybillNumber", waybillNumber);
            jsonObject.put("shipperName", shipperName);
            jsonObject.put("shipperCode",shipperCode);
            jsonObject.put("reasonImg", orderDetailList.get(0).getReasonImg());
            jsonObject.put("goodsId", orderDetailList.get(0).getGoodsId());
        }
        map.put("shippers", jsonObject);
        return map;
    }

    public PageModel findgOrdersPage(String goodsName, Integer pageNo, Integer pageSize, Integer userId) {
        Query query = entityManager.createQuery("from Orders ag where ag.userId=:userId and ag.orderNo in (select od.orderNo from OrderDetail od where od.goodsName LIKE '%'||:goodsName||'%')");
        query.setParameter("goodsName", goodsName);
        query.setParameter("userId", userId);
        query.setFirstResult(pageNo == null ? 0 : pageNo - 1);
        query.setMaxResults(pageSize == null ? 10 : pageSize);
        List list = query.getResultList();
//        Integer totalCount = ordersDao.findByGoodsNameDiscountOrders(userId, goodsName);
        Integer totalCount=list.size();
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        pageModel.setTotalRows(Long.valueOf(totalCount));
        long tempTPd = pageModel.getTotalRows() % pageModel.getPageSize();
        Integer tempTp = Integer.valueOf((pageModel.getTotalRows() / pageModel.getPageSize()) + "");
        if (tempTPd == 0) {
            pageModel.setTotalPages(tempTp);
        } else {
            pageModel.setTotalPages(tempTp + 1);
        }
        pageModel.setContent(list);
        return pageModel;
    }

    public void salesVolume(Long orderNo) throws YesmywineException {
        List<OrderDetail> orderDetailDaos = orderDetailDao.findByOrderNo(orderNo);
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for (int i = 0; i < orderDetailDaos.size(); i++) {
            com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
            jsonObject1.put("goodsId", orderDetailDaos.get(i).getGoodsId());
            jsonObject1.put("count", orderDetailDaos.get(i).getCount());
            jsonArray.add(jsonObject1);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("jsonString", jsonArray);
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/goods/updateSales/itf", RequestMethod.post, params, null);
        String code = ValueUtil.getFromJson(result, "code");
        if (!"201".equals(code)||ValueUtil.isEmpity(code)) {
            String msg = ValueUtil.getFromJson(result, "msg");
            ValueUtil.isError(msg);
        }

    }
    public  String giftCardDetaile(Long orderNo,Integer goodsId) throws YesmywineException{//支付完单后礼品卡显示详情
        List<OrderGiftCard> orderGiftCardList=orderGiftCardDao.findByOrderNoAndGoodsId(orderNo,goodsId);
//        Map<String, Object> params = new HashMap<>();
//        params.put("cardNumbers", orderGiftCardList);
        String jsonObject=JSONUtil.objectToJsonStr(orderGiftCardList);
//        String parse = JSONArray.parse(jsonObject.toJSONString()).toString();
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/goods/giftCard/ordersGift/itf", RequestMethod.post, "cardNumbers", jsonObject);
        String code = ValueUtil.getFromJson(result, "code");
        if (!"201".equals(code)||ValueUtil.isEmpity(code)) {
            ValueUtil.isError("查看礼品卡详情失败，请联系管理员！");
        }
//        String data=ValueUtil.getFromJson(result, "data");
        return result;
    }
    private Set<String> getNoEnoughGoods(Map<String, Integer> skuMap, Map<String, String> goodsMap) {
        Set<String> noEnoughGoods = new HashSet<>();
        Iterator<Map.Entry<String, Integer>> skuIt = skuMap.entrySet().iterator();
        Iterator goodsIt = goodsMap.entrySet().iterator();
        while (skuIt.hasNext()){
            Map.Entry skuEntry = skuIt.next();
            String skuId = (String) skuEntry.getKey();
            Integer count = (Integer) skuEntry.getValue();

            Map.Entry goodsEntry = (Map.Entry) goodsIt.next();
            String goodsName = (String) goodsEntry.getValue();
            Map<String,Object> requestParams = new HashMap<>();
            requestParams.put("skuId",skuId);
            requestParams.put("count",count);
            String code = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/inventory/channelInventory/isInvEnough/itf",RequestMethod.get,requestParams,null);
            if(code!=null&&code.equals("500")){
                if(!noEnoughGoods.contains(goodsName)){
                    noEnoughGoods.add(goodsName);
                }
            }
        }
        return noEnoughGoods;
    }

}
