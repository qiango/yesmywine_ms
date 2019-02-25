package com.yesmywine.goods.bean;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public enum GoodsType {

    normalGoods        //普通商品
    , luckyBag;        //福袋

    private static Map<String, GoodsType> map = new HashMap<>();

    static {
        GoodsType[] types = GoodsType.values();
        for (int i = 0; i < types.length; i++) {
            map.put(types[i].name(), types[i]);
        }
    }

    public GoodsType getFromString(String name) {
        return map.get(name);
    }
}
