package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.orders.bean.OrderType;
import com.yesmywine.orders.dao.*;
import com.yesmywine.orders.entity.OrderDispatch;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.orders.service.OrderReturnExchangeService;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.orders.entity.OrderDetail;
import com.yesmywine.orders.entity.OrderReturnExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by wangdiandian on 2017/4/12.
 */
@Service
@Transactional
public class OrderReturnExchangeImpl extends BaseServiceImpl<OrderReturnExchange, Integer> implements OrderReturnExchangeService {
    @Autowired
    private OrderReturnExchangeDao orderReturnExchangeDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private OrderPayinfoDao orderPayinfoDao;
    @Autowired
    private OrderDispatchDao orderDispatchDao;

    public String creatOrderReturnExchange(@RequestParam Map<String, String> param, Integer userId) throws YesmywineException {//新增退换货
        Long orderNo = Long.valueOf(param.get("orderNo"));
        Orders orders = ordersDao.findByOrderNo(orderNo);
        String goodsIds = param.get("goodsId");//商品id
        String reNums = param.get("reNum");// 申请数量

        Integer goodsId = Integer.valueOf(goodsIds);
        Integer reNum = Integer.valueOf(reNums);
        OrderDetail orderDetail = orderDetailDao.findByOrderNoAndGoodsId(orderNo, goodsId);
        if (orderDetail.getCount() - (orderDetail.getReturnWineCount() == null ? 0 : orderDetail.getReturnWineCount()) - (orderDetail.getMentionWineCount() == null ? 0 : orderDetail.getMentionWineCount()) < reNum) {
            ValueUtil.isError("申请数量大于可退数量！！");
        } else {
            if (orderDetail.getReturnWineCount() != null) {
                Integer num = reNum + orderDetail.getReturnWineCount();
                orderDetail.setReturnWineCount(num);
            } else {
                orderDetail.setReturnWineCount(reNum);
            }
        }
        ordersDao.save(orders);

        OrderReturnExchange orderReturnExchange = new OrderReturnExchange();
        orderReturnExchangeDao.save(orderReturnExchange);
        String returnNo = String.valueOf(OrderImpl.generateOrderNumber(Long.valueOf(orderReturnExchange.getId())));
        orderReturnExchange.setReturnNo(returnNo);

        Integer type = Integer.valueOf(param.get("type"));// 1.退货/2.换货
        String channelId = param.get("channelId");
        String returnReason = param.get("returnReason");//退货原因
        String reasonDesc = param.get("reasonDesc");//问题描述

        if (orders.getOrderType() == OrderType.WineStore) {//存酒退货保存存酒费用
            JSONObject objects = new JSONObject();
            objects.put("goodsId", goodsId);
            objects.put("orderNumber", orderNo);
            objects.put("count", reNums);
            JSONArray array = new JSONArray();
            array.add(objects);
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/returnload/itf", RequestMethod.post);

            httpRequest.addParameter("json", array);
            httpRequest.addParameter("userId", userId);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String keepCode = ValueUtil.getFromJson(temp, "code");
            if (keepCode.equals("201")) {
                String storeWineAttach = ValueUtil.getFromJson(temp, "data", "storeWineAttach");
                JSONArray arr = JSON.parseArray(storeWineAttach);
                Double fee = 0.00;
                for (int i = 0; i < arr.size(); i++) {
                    com.alibaba.fastjson.JSONObject adjustCommand = (com.alibaba.fastjson.JSONObject) arr.get(i);
                    Long orderNumber = adjustCommand.getLong("orderNumber");//存酒订单号
                    fee = adjustCommand.getDouble("fee");//存酒费用
                }
                orderReturnExchange.setKeepWineFee(fee);//保存存酒订单存酒费

            } else {
                ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
            }
        } else {//                Integer rebackType = Integer.valueOf(param.get("rebackType"));// 返回方式：/0.上门取货/1.送至门店/2.快递至公司
               String areaId =param.get("areaId");
               if(ValueUtil.isEmpity(areaId)){
                   ValueUtil.isError("请选择取件地址");
               }
                Map<String, Object> payParams = new HashMap<>();
                payParams.put("id", Integer.valueOf(areaId));
                String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/userservice/receivingAddress/itf", RequestMethod.get, payParams, null);
                String code = ValueUtil.getFromJson(result, "code");
                if ("200".equals(code)) {
                    String receiveAddress = ValueUtil.getFromJson(result, "data", "detailedAddress");
                    String customerName = ValueUtil.getFromJson(result, "data", "receiver");
                    String customerPhone = ValueUtil.getFromJson(result, "data", "phoneNumber");
                    orderReturnExchange.setRebackType(0);//暂定0 上门取件 返回方式：/0.上门取货/1.送至门店/2.快递至公司
                    orderReturnExchange.setAreaId(Integer.valueOf(areaId));
                    orderReturnExchange.setReceiveAddress(receiveAddress);
                    orderReturnExchange.setCustomerName(customerName);
                    orderReturnExchange.setCustomerPhone(customerPhone);
                } else {
                    ValueUtil.isError(ValueUtil.getFromJson(result, "msg"));
                }
            }
        orderReturnExchange.setOrderNo(orderNo);
        orderReturnExchange.setGoodId(goodsId);
        orderReturnExchange.setGoodsName(orderDetail.getGoodsName());
        orderReturnExchange.setSalePrice(orderDetail.getGoodsPrice());
        orderReturnExchange.setReNum(reNum);
        orderReturnExchange.setType(type);
        if (ValueUtil.notEmpity(channelId)) {
            orderReturnExchange.setChannelId(Integer.valueOf(channelId));
        }
        orderReturnExchange.setReturnReason(returnReason);
        orderReturnExchange.setReasonDesc(reasonDesc);
        orderReturnExchange.setUserId(userId.toString());
        orderReturnExchange.setStatus(0);//当前状态(0.待审核/1.已完成/2.退款中/3.上门取件)
//            orderReturnExchange.setQualityProblem(Boolean.valueOf(param.get("1")));
        orderReturnExchange.setGoodsReasonImg(orderDetail.getReasonImg());//商品图

        orderReturnExchangeDao.save(orderReturnExchange);

        String reasonImg = param.get("reasonImg");//图片
        if (ValueUtil.notEmpity(reasonImg)) {

        JSONArray reasonImgArr = JSON.parseArray(reasonImg);
        Integer[] arr = new Integer[reasonImgArr.size()];
        for (int i = 0; i < reasonImgArr.size(); i++) {
            com.alibaba.fastjson.JSONObject adjustCommand = (com.alibaba.fastjson.JSONObject) reasonImgArr.get(i);
            Integer id = adjustCommand.getInteger("id");
            arr[i] = id;
        }
            String goodImg = null;
            if (reasonImg != null && !reasonImg.equals("")) {
                try {
                    goodImg = saveOrderReturnImg(orderReturnExchange.getId(), arr);
                } catch (YesmywineException e) {
                    e.printStackTrace();
                }
            }
            orderReturnExchange.setReasonImg(goodImg);
        }


        orderReturnExchangeDao.save(orderReturnExchange);
        //保存退货单单操作信息
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(Long.valueOf(returnNo));
        orderDispatch.setOperator(0);//0客户/1也买酒
        orderDispatch.setStatus(6);//6（退换货）审核中
        orderDispatch.setLabel(1);//退换货
        orderDispatchDao.save(orderDispatch);


        if (orders.getOrderType() == OrderType.WineStore) {//存酒退货冻结存酒库
            JSONObject objects = new JSONObject();
            objects.put("goodsId", goodsId);
            objects.put("orderNumber", orderNo);
            objects.put("count", reNums);
            JSONArray array = new JSONArray();
            array.add(objects);
            HttpBean frozenKeep = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/return/itf", RequestMethod.post);

            frozenKeep.addParameter("json", array);
            frozenKeep.addParameter("userId", userId);
            frozenKeep.run();
            String temp = frozenKeep.getResponseContent();
            String keep = ValueUtil.getFromJson(temp, "code");
            if (!keep.equals("201")) {
                ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
            }
        }

        return "success";
    }

    private String saveOrderReturnImg(Integer returnId, Integer[] imgIds) throws YesmywineException {
        try {
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "returnOrder");
            httpRequest.addParameter("mId", returnId);
            httpRequest.addParameter("type", "1");
            String ids = "";
            String imageIds = "";
            for (int i = 0; i < imgIds.length; i++) {
                if (i == 0) {
                    ids = ids + imgIds[i];
                } else {
                    ids = ids + "," + imgIds[i];
                }
                httpRequest.addParameter("id", ids);
            }
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String cd = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(cd) || ValueUtil.isEmpity(cd)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("图片上传失败");
            } else {
                JSONArray maps = new JSONArray(imgIds.length);
                String result = ValueUtil.getFromJson(temp, "data");
                JsonParser jsonParser = new JsonParser();
                JsonArray image = jsonParser.parse(result).getAsJsonArray();
                for (int f = 0; f < image.size(); f++) {
                    String id = image.get(f).getAsJsonObject().get("id").getAsString();
                    String name = image.get(f).getAsJsonObject().get("name").getAsString();
                    com.alibaba.fastjson.JSONObject map1 = new com.alibaba.fastjson.JSONObject();
                    map1.put("id", id);
                    map1.put("name", name);
                    maps.add(map1);
                }

                String result1 = maps.toJSONString().replaceAll("\"", "\'");
                return result1;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError("图片服务出现问题！");
        }
        return null;
    }

    public OrderReturnExchange updateLoad(Integer id) throws YesmywineException {//加载显示订单退货单
        ValueUtil.verify(id, "idNull");
        OrderReturnExchange orderReturnExchange = orderReturnExchangeDao.findOne(id);
        return orderReturnExchange;
    }

    public String auditOrders(String returnNo, Integer type,  Boolean isQualityProblem,String rejectReason) throws YesmywineException {//退换货审核
        //退货单信息审核
        OrderReturnExchange returnExchange = orderReturnExchangeDao.findByReturnNo(returnNo);
        if (!returnExchange.getStatus().equals(0)) {
            ValueUtil.isError("非待审核的数据，无法审核");
        }

        ValueUtil.verifyNotExist(returnExchange, "退货单不存在");
        Orders orders = ordersDao.findByOrderNo(returnExchange.getOrderNo());

        if (type != null && type.equals(3)) {//3审核失败
            returnExchange.setStatus(3);
            returnExchange.setRejectReason(rejectReason);
            returnExchange.setQualityProblem(isQualityProblem);
            orderReturnExchangeDao.save(returnExchange);
            OrderDetail orderDetail = orderDetailDao.findByOrderNoAndGoodsId(returnExchange.getOrderNo(), returnExchange.getGoodId());
            orderDetail.setReturnWineCount(orderDetail.getReturnWineCount() - returnExchange.getReNum());
            orderDetailDao.save(orderDetail);

            if (orders.getOrderType() == OrderType.WineStore) {//存酒订单审核失败释放存酒库
                JSONObject objects = new JSONObject();
                objects.put("goodsId", returnExchange.getGoodId());
                objects.put("orderNumber", returnExchange.getOrderNo());
                objects.put("count", returnExchange.getReNum());
                JSONArray array = new JSONArray();
                array.add(objects);
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/returncancel/itf", RequestMethod.post);

                httpRequest.addParameter("json", array);
                httpRequest.addParameter("userId", returnExchange.getUserId());
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String keepCode = ValueUtil.getFromJson(temp, "code");
                if (!keepCode.equals("201")) {
                    ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
                }
            }
            //保存退货单单操作信息
            OrderDispatch orderDispatch = new OrderDispatch();
            orderDispatch.setOrderNo(Long.valueOf(returnNo));
            orderDispatch.setOperator(1);//0客户/1也买酒
            orderDispatch.setStatus(3);//0.待审核/1.已完成/2.审核中/3.审核未通过
            orderDispatch.setLabel(1);//退换货
            orderDispatchDao.save(orderDispatch);

            return "success";
        } else {
            if (orders.getOrderType() == OrderType.WineStore) {//存酒订单审核成功
                OrderDetail detail = orderDetailDao.findByOrderNoAndGoodsId(orders.getOrderNo(), returnExchange.getGoodId());
                returnExchange.setStatus(2);//0.待审核/1.已完成/2.审核中/3.审核未通过
                returnExchange.setQualityProblem(isQualityProblem);
                orderReturnExchangeDao.save(returnExchange);
                //调用存酒库确认退货
                JSONObject objects = new JSONObject();
                objects.put("goodsId", returnExchange.getGoodId());
                objects.put("orderNumber", returnExchange.getOrderNo());
                objects.put("count", returnExchange.getReNum());
                JSONArray array = new JSONArray();
                array.add(objects);
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/returnConfirm/itf", RequestMethod.post);

                httpRequest.addParameter("json", array);
                httpRequest.addParameter("userId", returnExchange.getUserId());
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String keepCode = ValueUtil.getFromJson(temp, "code");
                if (!keepCode.equals("201")) {
                    ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
                }
                //保存退货单单操作信息
                OrderDispatch orderDispatch = new OrderDispatch();
                orderDispatch.setOrderNo(Long.valueOf(returnNo));
                orderDispatch.setOperator(1);//0客户/1也买酒
                orderDispatch.setStatus(2);//0.待审核/1.已完成/2.审核中/3.审核未通过
                orderDispatch.setLabel(1);//退换货
                orderDispatchDao.save(orderDispatch);
                //存酒退货成功直接调用退款

                return "success";
            } else {
                returnExchange.setStatus(2);//0.待审核/1.已完成/2.审核中/3.审核未通过
                returnExchange.setQualityProblem(isQualityProblem);
                orderReturnExchangeDao.save(returnExchange);
                //订单信息
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
//        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
                jsonObject.put("orderNo", returnExchange.getOrderNo());
                jsonObject.put("relatedNo", returnNo);
                switch (returnExchange.getType()) {
                    case 1:
                        jsonObject.put("orderType", "退货");
                        break;
                    case 2:
                        jsonObject.put("orderType", "换货");
                }
                com.alibaba.fastjson.JSONArray returGoodsArray = new com.alibaba.fastjson.JSONArray();
                List<OrderDetail> detailList = orderDetailDao.findByOrderNo(returnExchange.getOrderNo());
                for (OrderDetail detail : detailList) {
                    Integer goodsId = detail.getGoodsId();
                    //判断是否为退货商品
                    if (goodsId.equals(returnExchange.getGoodId())) {
                        com.alibaba.fastjson.JSONObject returnGoods = new com.alibaba.fastjson.JSONObject();
                        returnGoods.put("goodsCode", detail.getGoodsCode());
                        returnGoods.put("price", detail.getGoodsPrice());
                        returnGoods.put("quantity", detail.getCount());
                        returnGoods.put("beGifts", detail.getGift() == null ? "N" : detail.getGift());
                        returGoodsArray.add(returnGoods);
                    }
                }
                jsonObject.put("orderDetailGoods", returGoodsArray);

                String result = SynchronizeUtils.getOmsResult(Dictionary.OMS_HOST, "/returnOmsOrder", RequestMethod.post, null, jsonObject.toJSONString());
                if (result != null) {
                    JSONObject resJson = JSON.parseObject(result);
                    String status = resJson.getString("status");
                    String message = resJson.getString("message");
                    if (!"success".equals(status)) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        ValueUtil.isError("推送OMS失败,无法审核，请联系维护人员,原因："+message);
                    }
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("推送OMS失败,无法审核，请联系维护人员");
                }
                //保存退货单单操作信息
                OrderDispatch orderDispatch = new OrderDispatch();
                orderDispatch.setOrderNo(Long.valueOf(returnNo));
                orderDispatch.setOperator(1);//0客户/1也买酒
                orderDispatch.setStatus(2);//0.待审核/1.已完成/2.审核中/3.审核未通过
                orderDispatch.setLabel(1);//退换货
                orderDispatchDao.save(orderDispatch);
                return "success";
            }
        }
    }


    @Override
    public String refundCancel(Integer refundId) throws YesmywineException {
        OrderReturnExchange returnExchange = orderReturnExchangeDao.findOne(refundId);
        returnExchange.setStatus(4);//4取消
        orderReturnExchangeDao.save(returnExchange);
        Integer goodsId = returnExchange.getGoodId();
        OrderDetail orderDetail = orderDetailDao.findByOrderNoAndGoodsId(returnExchange.getOrderNo(), goodsId);
        orderDetail.setReturnWineCount(orderDetail.getReturnWineCount() - returnExchange.getReNum());
        orderDetailDao.save(orderDetail);

        Orders orders = ordersDao.findByOrderNo(returnExchange.getOrderNo());
        if (orders.getOrderType() == OrderType.WineStore) {//存酒订单申退货取消释放存酒库
            JSONObject objects = new JSONObject();
            objects.put("goodsId", returnExchange.getGoodId());
            objects.put("orderNumber", returnExchange.getOrderNo());
            objects.put("count", returnExchange.getReNum());
            JSONArray array = new JSONArray();
            array.add(objects);
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/returncancel/itf", RequestMethod.post);

            httpRequest.addParameter("json", array);
            httpRequest.addParameter("userId", returnExchange.getUserId());
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String keepCode = ValueUtil.getFromJson(temp, "code");
            if (!keepCode.equals("201")) {
                ValueUtil.isError(ValueUtil.getFromJson(temp, "msg"));
            }
        }
        //保存退货单单操作信息
        OrderDispatch orderDispatch = new OrderDispatch();
        orderDispatch.setOrderNo(Long.valueOf(returnExchange.getReturnNo()));
        orderDispatch.setOperator(1);//0客户/1也买酒
        orderDispatch.setStatus(4);//0.待审核/1.已完成/2.审核中/3.审核未通过/4取消
        orderDispatch.setLabel(1);//退换货
        orderDispatchDao.save(orderDispatch);
        return "success";
    }

    @Override
    public Map<String, Object> application(Long orderNo, Integer goodsId, Integer userId) throws YesmywineException {


        OrderDetail orderDetail = orderDetailDao.findByOrderNoAndGoodsId(orderNo, goodsId);
        Integer enoughAmount = 0;

        Integer count = orderDetail.getCount();//原本数量
        Integer returnWineCount = orderDetail.getReturnWineCount();//已退酒数量
        Integer mentionWineCount = orderDetail.getMentionWineCount();//已提酒数量
        enoughAmount = count;
        if (returnWineCount != null) {
            enoughAmount = enoughAmount - returnWineCount;
        }
        if (mentionWineCount != null) {
            enoughAmount = enoughAmount - mentionWineCount;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderDetail", orderDetail);
        map.put("enoughAmount", enoughAmount);
        return map;
    }

}
