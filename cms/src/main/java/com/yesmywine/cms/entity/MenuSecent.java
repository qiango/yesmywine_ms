package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "menuSecent")
public class MenuSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '导航id'")
    private Integer firstMenuId;
    @Column(columnDefinition = "int(11) COMMENT '分类id'")
    private Integer secentCategoryId;
    @Column(columnDefinition = "int(11) COMMENT '权重'")
    private Integer secentIndex;

    public Integer getFirstMenuId() {
        return firstMenuId;
    }

    public void setFirstMenuId(Integer firstMenuId) {
        this.firstMenuId = firstMenuId;
    }

    public Integer getSecentCategoryId() {
        return secentCategoryId;
    }

    public void setSecentCategoryId(Integer secentCategoryId) {
        this.secentCategoryId = secentCategoryId;
    }

    public Integer getSecentIndex() {
        return secentIndex;
    }

    public void setSecentIndex(Integer secentIndex) {
        this.secentIndex = secentIndex;
    }
}
