package com.yesmywine.activity.bean;

import java.util.HashMap;

/**
 * Created by wangdiandian on 2017/1/9.
 */
public enum GoodsTypeEnum {
    Goods(0),
    Category(1),
    Brand(2),
    All(3),
    Other(4);

    private int value;

    GoodsTypeEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, GoodsTypeEnum> map = new HashMap<>();

    static {
        for (GoodsTypeEnum goodsTypeEnum : GoodsTypeEnum.values()) {
            map.put(goodsTypeEnum.getValue(), goodsTypeEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static GoodsTypeEnum getGoodsTypeEnum(int value) {
        return map.get(value);
    }
}
