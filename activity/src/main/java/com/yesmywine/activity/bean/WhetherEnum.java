package com.yesmywine.activity.bean;

import java.util.HashMap;

/**
 * Created by wangdiandian on 2016/1/4.
 */
public enum WhetherEnum {
    YES(0), NO(1);

    private int value;

    WhetherEnum(int v) {
        value = v;
    }

    private static HashMap<Integer, WhetherEnum> map = new HashMap<>();

    static {
        for (WhetherEnum whetherEnum : WhetherEnum.values()) {
            map.put(whetherEnum.getValue(), whetherEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static WhetherEnum getWhetherName(int value) {
        return map.get(value);
    }
}
