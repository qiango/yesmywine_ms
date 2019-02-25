package com.yesmywine.goods.bean;

import java.util.HashMap;

/**
 * Created by hz on 3/15/17.
 */
public enum Item {  //商品单品多品

    single   //单品
    ,plural  //多品
    ,luckBage //福袋
    ,fictitious;  //虚拟的

    private String value;

    private static HashMap<String, Item> map = new HashMap<>();

    static {
        for (Item activityStatus : Item.values()) {
            map.put(activityStatus.name(), activityStatus);
        }
    }

    public String getValue() {
        return value;
    }

    public static Item getValue(String value) {
        return map.get(value);
    }
}
