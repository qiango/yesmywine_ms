package com.yesmywine.activity.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by by on 2017/7/10.
 */
@Table(name = "activityType")
@Entity
public class ActivityType extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(10) COMMENT '类型名称  eg:满减'")
    private String name;
    @Column(columnDefinition = "varchar(20) COMMENT '类型编码  eg:fullT-reductionA'")
    private String code;
    @Column(columnDefinition = "varchar(20) COMMENT '类型别名'")
    private String alias;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
