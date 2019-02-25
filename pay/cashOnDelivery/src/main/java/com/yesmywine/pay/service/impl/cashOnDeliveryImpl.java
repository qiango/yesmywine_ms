package com.yesmywine.pay.service.impl;

import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.bean.PaymentResult;
import com.yesmywine.pay.service.PaymentBiz;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wangdiandian on 2017/2/21.
 */
@Service
public class cashOnDeliveryImpl implements PaymentBiz {

    @Override
    public PaymentResult pay(String orderNumber, Double amount, String title, String description, String username, HttpServletRequest request) {
        PaymentResult result = new PaymentResult();
        result.setCode("OK");
        result.setPayment(Payment.CashOnDelivery);
        return result;
    }

    @Override
    public PaymentResult queryDetail(String orderNumber) {
        return null;
    }

    @Override
    public PaymentResult refund(String orderNumber, String refundNumber, Double payAmount, Double refundAmount, String title, String description) {
        return null;
    }

    @Override
    public PaymentResult appPay(String orderNumber, Double amount, String title, String description, String userId, HttpServletRequest request) {
        return null;
    }

}
