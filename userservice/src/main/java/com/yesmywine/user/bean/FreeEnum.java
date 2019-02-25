package com.yesmywine.user.bean;

import java.util.HashMap;

/**
 * Created by liqingqing on 2016/12/8.
 */
public enum FreeEnum {
    UNREAD(0), READ(1);

    private int value;

    FreeEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, FreeEnum> map = new HashMap<>();

    static {
        for (FreeEnum deleteEnum : FreeEnum.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static FreeEnum getDeleteName(int value) {
        return map.get(value);
    }
}
