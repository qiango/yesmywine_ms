package com.yesmywine.pay.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 11/30/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public enum Payment {

    Alipay, WeChat, UnionPay, CashOnDelivery;

    private static Map<String, Payment> map = new HashMap<>();

    static {
        Payment[] payments = Payment.values();
        for (int i = 0; i < payments.length; i++) {
            map.put(payments[i].name(), payments[i]);
        }
    }

    public static Payment getPayment(String name) {
        return map.get(name);
    }


}
