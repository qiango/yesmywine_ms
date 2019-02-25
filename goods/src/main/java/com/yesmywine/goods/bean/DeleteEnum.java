package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 12/8/16.
 */
public enum DeleteEnum {
    NOT_DELETE(0), DELETED(1);

    private int value;

    DeleteEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, DeleteEnum> map = new HashMap<>();

    static {
        for (DeleteEnum deleteEnum : DeleteEnum.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static DeleteEnum getDeleteName(int value) {
        return map.get(value);
    }

}
