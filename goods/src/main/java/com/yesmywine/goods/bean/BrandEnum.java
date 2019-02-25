package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 12/8/16.
 */
public enum BrandEnum {
    available(0)    //可用
    , disabled(1);   //不可用

    private int value;

    BrandEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, BrandEnum> map = new HashMap<>();

    static {
        for (BrandEnum deleteEnum : BrandEnum.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static BrandEnum getDeleteName(int value) {
        return map.get(value);
    }


}

