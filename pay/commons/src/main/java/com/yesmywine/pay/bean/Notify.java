package com.yesmywine.pay.bean;

import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by by on 2017/8/14.
 */
public class Notify {
    private static final Logger log = LoggerFactory
            .getLogger(Notify.class);

    public static void toOrderService(Map<String, Object> paramsMap,String payment){
        paramsMap.put("paymentType",payment);
        log.info("下单支付成功！回调订单服务");
        String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/orders/finish/paymentSuccess/itf", com.yesmywine.httpclient.bean.RequestMethod.post,paramsMap,null);
        log.info("订单服务返回的结果为==》"+resultCode);

    }

    public static void toUserService(Map<String, Object> paramsMap){
        paramsMap.put("chargeNumber",paramsMap.get("orderNo"));
        paramsMap.put("chargeWay",paramsMap.get("paymentType"));
        log.info("充值成功！回调用户服务");
        String resultCode = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/userservice/charge/web", com.yesmywine.httpclient.bean.RequestMethod.post,paramsMap,null);
        log.info("用户服务返回结果==》"+resultCode);
    }
}
