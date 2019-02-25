package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 12/30/16.
 */
public enum IsShow {
    yes(0), no(1);

    private int value;

    IsShow(int v) {
        value = v;
    }

    private static HashMap<Integer, IsShow> map = new HashMap<>();

    static {
        for (IsShow deleteEnum : IsShow.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static IsShow getShowName(int value) {
        return map.get(value);
    }

}
