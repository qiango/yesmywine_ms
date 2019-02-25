package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/6/7.
 */
@Entity
@Table(name = "taskConfigure")
public class TaskConfigure extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(10) COMMENT '确认（天）'")
    private Integer time;//确认（天）

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
