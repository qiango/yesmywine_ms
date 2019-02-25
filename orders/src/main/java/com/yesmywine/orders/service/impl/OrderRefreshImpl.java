package com.yesmywine.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.orders.dao.OrderDetailDao;
import com.yesmywine.orders.entity.OrderDetail;
import com.yesmywine.orders.entity.Orders;
import com.yesmywine.orders.service.OrderRefreshService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/7/10.
 */
@Service
public class OrderRefreshImpl extends BaseServiceImpl<Orders, Long> implements OrderRefreshService {
    @Autowired
    private FreightImpl freightImpl;
    @Autowired
    private OrderDetailDao orderDetailDao;

    public Map<String, Object> submitPage(@RequestParam Map<String, String> param, String userInfo) throws YesmywineException {
        String userId = ValueUtil.getFromJson(userInfo, "id");
        String json = param.get("json");
        Double nowTotalPrice = 0.00;//存酒费
        Double keepWineGoodsAmount = 0.00;//存酒酒金额
        if (ValueUtil.notEmpity(json)) {
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/storeWine/load/itf", RequestMethod.post);
            httpRequest.addParameter("json", json);
            httpRequest.addParameter("userId", userId);
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
                    Integer goodsId = adjustCommand.getInteger("goodsId");
                    OrderDetail ordersGoods = orderDetailDao.findByOrderNoAndGoodsId(orderNumber, goodsId);
                    Double goodsPrice = ordersGoods.getGoodsPrice();
                    keepWineGoodsAmount = keepWineGoodsAmount + goodsPrice;
                    nowTotalPrice = nowTotalPrice + fee;
                    BigDecimal bg = new BigDecimal(nowTotalPrice);
                    nowTotalPrice = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        } else {
            String username = ValueUtil.getFromJson(userInfo, "userName");
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/cart/order", RequestMethod.get);
            httpRequest.addParameter("userId", userId);
            httpRequest.addParameter("username", username);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String code = ValueUtil.getFromJson(temp, "code");
            if (code.equals("200")) {
                nowTotalPrice = Double.valueOf(ValueUtil.getFromJson(temp, "data", "nowTotalPrice"));//调用接口得到实付的金额
            }
        }
        Map<String, Object> map = refresh(param, nowTotalPrice, userInfo, keepWineGoodsAmount, json);
        return map;
    }

    private Map<String, Object> refresh(Map<String, String> param, Double nowTotalPrice, String userInfo, Double keepWineGoodsAmount, String json) throws YesmywineException {
        //settlement  type:{ coupon:优惠券 giftCards:礼品卡 beanWine:酒豆 balance:余额 }
        String userId = ValueUtil.getFromJson(userInfo, "id");
        Map<String, Object> map = new HashMap<>();
        Double copeWith;
        if (nowTotalPrice > 0.00) {
            copeWith = nowTotalPrice;
        } else {
            copeWith = 0.00;
        }
        String provinceId = param.get("provinceId");//省id
        String orderType = param.get("orderType");
        if (provinceId == null || provinceId.isEmpty() || "WineStore".equals(orderType)) {
            map.put("freight", 0.00);//运费金额
            map.put("preferentialFreight", 0.00);//优惠运费金额
        }else{
            com.alibaba.fastjson.JSONObject freightJson = new com.alibaba.fastjson.JSONObject();
            if (ValueUtil.notEmpity(json)) {
                freightJson = freightImpl.calculate(provinceId, keepWineGoodsAmount);
            } else {
                freightJson = freightImpl.calculate(provinceId, nowTotalPrice);
            }
            Double courierfees = freightJson.getDouble("courierfees");//快递费
            Double areaPostage = freightJson.getDouble("areaPostage");//原本运费
            if (courierfees != 0.0) {
                BigDecimal b1 = new BigDecimal(Double.toString(copeWith));
                BigDecimal b2 = new BigDecimal(Double.toString(courierfees));
                copeWith = b1.add(b2).doubleValue();
                map.put("freight", areaPostage);//运费
                map.put("preferentialFreight", 0.00);//优惠运费金额
            } else {
                map.put("freight", 0.00);//运费
                map.put("preferentialFreight", areaPostage);//优惠运费金额
            }
        }

        String settlement = param.get("settlement");
        if (ValueUtil.notEmpity(settlement) && !settlement.equals("null")) {
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

                        if (nowTotalPrice > couponMoney) {
                            Double price = nowTotalPrice - couponMoney;//原价-优惠券
                            BigDecimal c1 = new BigDecimal(nowTotalPrice);//实付金额
                            BigDecimal c2 = new BigDecimal(couponMoney);//优惠券
                            if (ValueUtil.notEmpity(provinceId)) {
                                com.alibaba.fastjson.JSONObject freightJson_sl = freightImpl.calculate(provinceId, price.doubleValue());
                                Double courierfees_sl = freightJson_sl.getDouble("courierfees");//快递费
                                Double areaPostage_sl = freightJson_sl.getDouble("areaPostage");//本身运费
                                BigDecimal c3 = new BigDecimal(areaPostage_sl);//运费
                                if (courierfees_sl != 0.0) {//不免运费
                                    BigDecimal result = c1.add(c3).subtract(c2);//实付金额+运费-优惠券
                                    copeWith = result.doubleValue();
                                    map.put("freight", areaPostage_sl);//运费
                                    map.put("preferentialFreight", 0.00);//优惠运费
                                } else {//免运费
                                    BigDecimal result = c1.subtract(c2);//实付金额-优惠券
                                    copeWith = result.doubleValue();
                                    map.put("freight", 0.00);//运费
                                    map.put("preferentialFreight", areaPostage_sl);//优惠运费
                                }
                            } else {
                                BigDecimal result = c1.subtract(c2);//实付金额-优惠券
                                copeWith = result.doubleValue();
                            }
                            map.put("canCouponAmount", couponMoney);
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
                            BigDecimal b1 = new BigDecimal(Double.valueOf(copeWith));
                            BigDecimal b2 = new BigDecimal(Double.valueOf(remainingSum));
                            Double result = b1.subtract(b2).doubleValue();
                            copeWith = result;
                            map.put("canGiftCardAmount", Double.valueOf(remainingSum));
                        } else {
                            map.put("canGiftCardAmount", copeWith);
                            copeWith = 0.00;
                            break;
                        }
                    }
                } else if (types.equals("beanWine")) {

                    HttpBean httpRequest = new HttpBean(Dictionary.PAAS_HOST + "/user/rule/itf", RequestMethod.get);
                    httpRequest.addParameter("channelCode", "GW");
                    httpRequest.run();
                    String temp = httpRequest.getResponseContent();
                    String mobe = ValueUtil.getFromJson(temp, "data", "mobe");//人民币兑换酒豆
                    String[] mobeArray = mobe.split(":");
                    String mobeE = mobeArray[1];
                    BigDecimal jd = new BigDecimal(mobeE);

                    String bean = ValueUtil.getFromJson(userInfo, "bean");
                    String[] beanArr = bean.toString().split("\\.");
                    String beanfront = beanArr[0];//可使用的酒豆
                    BigDecimal b1 = new BigDecimal(copeWith.toString());
                    BigDecimal beanbg = new BigDecimal(beanfront);

                    BigDecimal jdrmb = beanbg.divide(jd, 2, BigDecimal.ROUND_HALF_EVEN);//按照比例该使用多少酒逗 例如：8/3
                    String[] beanRmb = jdrmb.toString().split("\\.");
                    String beanRmbfront = beanRmb[0];
                    BigDecimal beanMoney = new BigDecimal(beanRmbfront);//最终可使用的酒豆
                    if (Double.valueOf(beanRmbfront) > 0.00) {
                        if (b1.doubleValue() >= beanMoney.doubleValue()) {//实付金额大于酒豆的时候
                            Double result = b1.subtract(beanMoney).doubleValue();
                            copeWith = result;
                            map.put("canBeanAmount", beanRmbfront);
                        } else {
                            String[] arr = copeWith.toString().split("\\.");
                            String front = arr[0];
                            BigDecimal frontb1 = new BigDecimal(front);
                            BigDecimal result = frontb1.multiply(jd);//按照比例该使用多少酒逗
                            map.put("canBeanAmount", result.doubleValue());
                            String xiaoshu = "0" + copeWith.toString().substring(copeWith.toString().indexOf("."));
                            copeWith = Double.valueOf(xiaoshu);
                            if (copeWith == 0.00) {
                                break;
                            }
                        }
                    }
                } else if (types.equals("balance")) {
                    String remainingSum = ValueUtil.getFromJson(userInfo, "remainingSum");
                    if (copeWith >= Double.valueOf(remainingSum)) {

                        BigDecimal b1 = new BigDecimal(Double.valueOf(copeWith));
                        BigDecimal b2 = new BigDecimal(Double.valueOf(remainingSum));
                        Double result = b1.subtract(b2).doubleValue();
                        copeWith = result;
                        map.put("canBalance", Double.valueOf(remainingSum));
                    } else {
                        map.put("canBalance", copeWith);
                        copeWith = 0.00;
                        break;
                    }
                }
            }
        }

        map.put("copeWith", copeWith);
        return map;
    }

//
//    public String giftCard(Long cardNumber, String password, String userInfo) throws YesmywineException {
//        Map<String, Object> payParams = new HashMap<>();
//        payParams.put("cardNumber", cardNumber);
//        payParams.put("password", password);
//        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/web/goods/giftCard", RequestMethod.get, payParams, null);
//        if (result != null) {
//            JSONObject jsonObj = JSON.parseObject(result);
//            String code1 = jsonObj.getString("code");
//            if (code1.equals("200")) {
//                String content = jsonObj.getString("content");
////                Double amounts = Double.valueOf(ValueUtil.getFromJson(content, "amounts"));
////                Double remainingSum = Double.valueOf(ValueUtil.getFromJson(content, "remainingSum"));
////                Map<String, String> param=new HashMap<>();
////                param.put("type","giftCards");
////                param.put("cardNumber",cardNumber.toString());
////                submitPage(param,userInfo);
//                return content;
//            } else {
//                String msg = jsonObj.getString("msg");
//                ValueUtil.isError(msg);
//            }
//        }
//        return "success";
//    }

}