package com.yesmywine.activity.bean;

import java.util.HashMap;

/**
 * Created by wangdiandian on 2017/1/9.
 */
public enum WareEnum {
    TradeIn(0), //换购商品
    Gift(1),  //赠品
    Coupon(2),//优惠券
    Main(3);//主商品
    private int value;

    WareEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, WareEnum> map = new HashMap<>();

    static {
        for (WareEnum wareEnum : WareEnum.values()) {
            map.put(wareEnum.getValue(), wareEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static WareEnum getWareEnum(int value) {
        return map.get(value);
    }
}
