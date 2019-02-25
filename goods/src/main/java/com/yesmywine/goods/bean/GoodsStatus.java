package com.yesmywine.goods.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/7/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public enum GoodsStatus {

    pending(0)    //未上架
    , preSell(1)    //预售
    , onShelf(2)    //已上架
    , offShelf(3)    //下架
    , delete(4);     //删除

    private int value;

    GoodsStatus(int v) {
        value = v;
    }

    private static HashMap<Integer, GoodsStatus> map = new HashMap<>();
    private static Map<String, GoodsStatus> stringMap = new HashMap<>();

    static {
        for (GoodsStatus goodsStatus : GoodsStatus.values()) {
            map.put(goodsStatus.getValue(), goodsStatus);
            stringMap.put(goodsStatus.name(), goodsStatus);
        }
    }

    public int getValue() {
        return value;
    }

    public static GoodsStatus getGoodsStatus(int value) {
        return map.get(value);
    }

    public GoodsStatus getGoodsStatus(String name) {
        return stringMap.get(name);
    }

}
