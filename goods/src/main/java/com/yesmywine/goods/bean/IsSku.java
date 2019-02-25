package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 1/6/17.
 */
public enum IsSku {
    yes(0), no(1);

    private int value;

    IsSku(int v) {
        value = v;
    }

    private static HashMap<Integer, IsSku> map = new HashMap<>();

    static {
        for (IsSku deleteEnum : IsSku.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static IsSku getSkuName(int value) {
        return map.get(value);
    }
}
