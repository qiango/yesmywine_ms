package com.yesmywine.activity.entity;

import java.util.HashMap;

/**
 * Created by hz on 12/19/16.
 */
public enum StatusEnum {
    available(0), disabled(1);

    private int value;

    StatusEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, StatusEnum> map = new HashMap<>();

    static {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            map.put(statusEnum.getValue(), statusEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static StatusEnum getStatusName(int value) {
        return map.get(value);
    }

}
