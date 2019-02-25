package com.yesmywine.activity.bean;

import java.util.HashMap;

/**
 * Created by wangdiandian on 2017/1/10.
 */
public enum ActivityStatus {
    current//正在进行的活动
    , notCurrent//待进行的活动
    , overdue;//过期的活动

    private String value;

    private static HashMap<String, ActivityStatus> map = new HashMap<>();

    static {
        for (ActivityStatus activityStatus : ActivityStatus.values()) {
            map.put(activityStatus.name(), activityStatus);
        }
    }

    public String getValue() {
        return value;
    }

    public static ActivityStatus getValue(String value) {
        return map.get(value);
    }
}
