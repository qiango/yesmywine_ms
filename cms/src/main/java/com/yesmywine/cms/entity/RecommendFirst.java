package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "recommendFirst")
public class RecommendFirst extends BaseEntity<Integer>{

    @Column(columnDefinition = "varchar(255) COMMENT '名称'")
    private String name;
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(255) COMMENT '推荐理由'")
    private String reasons;//推荐理由

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
    }
}
