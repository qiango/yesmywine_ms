package com.yesmywine.base.record.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG, RUIQING on 12/20/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class VerifyBean {

    private VerifyType type;
    private List<String> fields = new ArrayList<>();

    private Long minValue;
    private Long maxValue;


    public VerifyBean() {
    }

    public VerifyBean(VerifyType type, String... fields) {
        this.type = type;
        for (int i = 0; i < fields.length; i++) {
            addField(fields[i]);
        }

    }

    public VerifyType getType() {
        return type;
    }

    public void setType(VerifyType type) {
        this.type = type;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void addField(String field) {
        this.fields.add(field);
    }

    public Long getMinValue() {
        if (null == minValue) {
            return Long.MIN_VALUE;
        } else {
            return minValue;
        }
    }

    public void setMinValue(Long minValue) {
        this.minValue = minValue;
    }

    public Long getMaxValue() {
        if (null == maxValue) {
            return Long.MAX_VALUE;
        } else {
            return maxValue;
        }
    }

    public void setMaxValue(Long maxValue) {
        this.maxValue = maxValue;
    }
}
