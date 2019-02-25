package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 3/21/17.
 */
public enum IsUse {
    yes(0), no(1);

    private int value;

    IsUse(int v) {
        value = v;
    }

    private static HashMap<Integer, IsUse> map = new HashMap<>();

    static {
        for (IsUse deleteEnum : IsUse.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static IsUse getSkuName(int value) {
        return map.get(value);
    }
}
