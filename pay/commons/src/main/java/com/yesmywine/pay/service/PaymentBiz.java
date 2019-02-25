package com.yesmywine.pay.service;


import com.yesmywine.pay.bean.PaymentResult;
import com.yesmywine.util.error.YesmywineException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by WANG, RUIQING on 12/1/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public interface PaymentBiz {

    // 查询
    PaymentResult queryDetail(String orderId);

    // 支付
    PaymentResult pay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request);

    PaymentResult refund(String orderNumber, String refundNumber, Double payAmount, Double refundAmount, String title, String description);

    // 支付
    PaymentResult appPay(String orderNumber, Double amount, String title, String description, String userId, HttpServletRequest request) throws YesmywineException;

}
