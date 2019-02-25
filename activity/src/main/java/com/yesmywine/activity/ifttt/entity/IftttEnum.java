package com.yesmywine.activity.ifttt.entity;

import java.util.HashMap;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public enum IftttEnum {
    trigger, action;

    private String value;

    private static HashMap<String, IftttEnum> map = new HashMap<>();

    static {
        for (IftttEnum iftttEnum : IftttEnum.values()) {
            map.put(iftttEnum.name(), iftttEnum);
        }
    }

    public String getValue() {
        return value;
    }

    public static IftttEnum getIftttEnum(String value) {
        return map.get(value);
    }
}
