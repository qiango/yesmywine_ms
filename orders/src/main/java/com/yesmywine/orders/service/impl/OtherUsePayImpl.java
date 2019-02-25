package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.orders.dao.OrderPayinfoDao;
import com.yesmywine.orders.entity.OrderPayinfo;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/8/10.
 */
@Service
public class OtherUsePayImpl {
    @Autowired
    private OrderPayinfoDao orderPayinfoDao;

    //订单使用 优惠券，礼品卡，酒豆，余额
    public String updateCoupon(Integer userId ,Integer couponId)throws YesmywineException {//使用优惠券
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/coupon/use/itf", RequestMethod.post);
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("userCouponId",couponId);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用优惠券！");
        }
        return "success";
    }
    public String updateGiftCard(Long cardNumber,Long orderNo,Double usedAmount,Integer userId,String username)throws YesmywineException {//使用礼品卡
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("cardNumber",cardNumber);
        jsonObject.put("orderNo",orderNo);
        jsonObject.put("usedAmount",usedAmount);
        jsonObject.put("usedTime",new Date());
        jsonObject.put("channel",0);
        jsonObject.put("userId",userId);
        jsonObject.put("userName",username);
        jsonArray.add(jsonObject);
        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/goods/giftCard/spend/itf", RequestMethod.post);
        httpBean.addParameter("jsonData", jsonArray);
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用礼品卡！");
        }
        return "success";
    }

    public String updateBeanWine(Long orderNo,Integer userId ,Double beans) throws YesmywineException {//使用酒豆
        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/web/userservice/payment/beanConsume", RequestMethod.post);
        httpBean.addParameter("userId",userId);
        httpBean.addParameter("orderNumber",orderNo);
        Integer bean = beans.intValue();
        httpBean.addParameter("bean",bean);
        httpBean.addParameter("channelCode","GW");
        // TODO: 2017/7/4
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用酒豆！");
        }
        return "success";
    }


    public String updateBalance(Integer userId ,Double balance,Long orderNo,String remainingSum, String phoneNumber) throws YesmywineException {//使用余额
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/userservice/payment/remainConsume", RequestMethod.post);
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("payMoney",balance );
        httpRequest.addParameter("orderNumber",orderNo);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用余额！");
        }
        //发送短信余额变更

        Double smsBalance=Double.valueOf(remainingSum)-balance;
        Map<String, Object> smsParams = new HashMap<>();
        JSONObject objects = new JSONObject();
        if (null != phoneNumber) {
            objects.put("time", new Date());
            objects.put("type", "消费");
            objects.put("rechargeAmount",balance);
            objects.put("balance", smsBalance);
            smsParams.put("json", objects);
            smsParams.put("phones", phoneNumber);
            smsParams.put("code", "zgMWvgUbks");

            String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
            String smscode = ValueUtil.getFromJson(result, "code");
            if (!"201".equals(smscode)) {
                String msg = ValueUtil.getFromJson(result, "msg");
                ValueUtil.isError("发送短信失败"+msg);
            }
        }
        return "success";
    }


    //以下是取消订单后退还 优惠券，礼品卡，酒豆，余额
    //取消各种优惠券，礼品卡，酒豆，余额
    public String cancelOtherPay(Orders orders,String phoneNumber,Double remainingSum)throws YesmywineException {//使用优惠券

        OrderPayinfo orderPayinfo = orderPayinfoDao.findByOrderNo(orders.getOrderNo());
        if (orderPayinfo.getCouponId() != null) {//取消优惠券
            cancelCoupon(orders.getUserId(), orderPayinfo.getCouponId());
        }
        if (orderPayinfo.getCardNumber() != null) {//取消礼品卡
            cancelGiftCard(orderPayinfo.getCardNumber(), orders.getOrderNo(), orderPayinfo.getGiftCardAmount());
        }
        if (orderPayinfo.getBeanAmount() != null) {//取消酒豆
            cancelBeanWine(orders.getOrderNo(), orders.getUserId(), orderPayinfo.getBeanAmount());
        }
        if (orderPayinfo.getBalance() != null) {//取消余额

            cancelBalance(orders.getUserId(), orderPayinfo.getBalance(), orders.getOrderNo(), remainingSum, phoneNumber);
        }
        return "success";
    }

    public void cancelCoupon(Integer userId ,Integer couponId)throws YesmywineException {//使用优惠券
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/coupon/return/itf", RequestMethod.post);
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("userCouponId",couponId);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用优惠券！");
        }
    }
    public void cancelGiftCard(Long cardNumber,Long orderNo,Double usedAmount)throws YesmywineException {//使用礼品卡
//        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("cardNumber",cardNumber);
        jsonObject.put("orderNo",orderNo);
        jsonObject.put("usedAmount",usedAmount);
        jsonObject.put("channel",0);
//        jsonArray.add(jsonObject);
        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/goods/giftCard/return/itf", RequestMethod.post);
        httpBean.addParameter("jsonData", jsonObject);
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用礼品卡！");
        }
    }

    public void cancelBeanWine(Long orderNo,Integer userId ,Double beans) throws YesmywineException {//退还酒豆
        HttpBean httpBean = new HttpBean(Dictionary.MALL_HOST + "/web/userservice/payment/returnsandpoint", RequestMethod.post);
        httpBean.addParameter("userId",userId);
        httpBean.addParameter("orderNumber",orderNo);
        Integer bean = beans.intValue();
        httpBean.addParameter("returnBean",bean);
//        httpBean.addParameter("channelCode","SC");
        // TODO: 2017/7/4
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用酒豆！");
        }
    }


    public void cancelBalance(Integer userId ,Double balance,Long orderNo,Double remainingSum, String phoneNumber) throws YesmywineException {//退还余额
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/userservice/payment/returnsandpoint", RequestMethod.post);
        httpRequest.addParameter("userId", userId);
        httpRequest.addParameter("returnMoney",balance );
        httpRequest.addParameter("orderNumber",orderNo);
        httpRequest.run();
        String temp = httpRequest.getResponseContent();
        String code = ValueUtil.getFromJson(temp, "code");
        if(!code.equals("201")){
            ValueUtil.isError("系统错误，暂时不能使用余额！");
        }
        //发送短信余额变更

        Double smsBalance=Double.valueOf(remainingSum)+balance;
        Map<String, Object> smsParams = new HashMap<>();
        JSONObject objects = new JSONObject();
        if (null != phoneNumber) {
            objects.put("time", new Date());
            objects.put("type", "退还");
            objects.put("rechargeAmount",balance);
            objects.put("balance", smsBalance);
            smsParams.put("json", objects);
            smsParams.put("phones", phoneNumber);
            smsParams.put("code", "zgMWvgUbks");

            String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/sms/send/sendSms/itf", RequestMethod.post, smsParams, null);
            String smscode = ValueUtil.getFromJson(result, "code");
            if (!"201".equals(smscode)) {
                String msg = ValueUtil.getFromJson(result, "msg");
                ValueUtil.isError("发送短信失败"+msg);
            }
        }
    }

}
