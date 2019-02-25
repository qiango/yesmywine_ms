package com.yesmywine.orders.bean;

import java.util.HashMap;

/**
 * Created by wangdiandian on 2016/12/19.
 */
public enum HeadType {
    Ordinary(0)//普通
    , Seckill(1)//秒杀
    , Buy(2)//换购
    , Gift(3)//赠品
    , Exchange(4);//积分兑换

    private int value;

    HeadType(int v) {
        value = v;
    }

    private static HashMap<Integer, HeadType> map = new HashMap<>();

    static {
        for (HeadType headType : HeadType.values()) {
            map.put(headType.getValue(), headType);
        }
    }

    public int getValue() {
        return value;
    }

    public static HeadType getHeadTypeName(int value) {
        return map.get(value);
    }
}
