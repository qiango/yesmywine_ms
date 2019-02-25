package com.yesmywine.activity.entity;

import java.util.HashMap;

/**
 * Created by hz on 12/20/16.
 */
public enum TicketType {
    coupons(0)         //优惠券
    , goodCoupons(1);   //提货券


    private int value;

    TicketType(int v) {
        value = v;
    }

    private static HashMap<Integer, TicketType> map = new HashMap<>();

    static {
        for (TicketType statusEnum : TicketType.values()) {
            map.put(statusEnum.getValue(), statusEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static TicketType getStatusName(int value) {
        return map.get(value);
    }
}
