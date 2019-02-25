package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by wangdiandian on 2017/3/16.
 */
public enum SupplierTypeEnum {
    distribution(0),//经销
    consignment(1),//代销
    seaAmoy(2);//海淘。

    private int value;

    SupplierTypeEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, SupplierTypeEnum> map = new HashMap<>();

    static {
        for (SupplierTypeEnum supplierTypeEnum : SupplierTypeEnum.values()) {
            map.put(supplierTypeEnum.getValue(), supplierTypeEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static SupplierTypeEnum getsupplierTypeName(int value) {
        return map.get(value);
    }
}

