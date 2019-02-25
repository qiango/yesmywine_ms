package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 1/6/17.
 */
public enum CanSearch {
    yes(0), no(1);

    private int value;

    CanSearch(int v) {
        value = v;
    }

    private static HashMap<Integer, CanSearch> map = new HashMap<>();

    static {
        for (CanSearch deleteEnum : CanSearch.values()) {
            map.put(deleteEnum.getValue(), deleteEnum);
        }
    }

    public int getValue() {
        return value;
    }

    public static CanSearch getCanSearchName(int value) {
        return map.get(value);
    }
}
