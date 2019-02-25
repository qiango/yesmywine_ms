package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "plateSecentCategoryLabel")
public class PlateSecentCategoryLabel extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '板块id'")
    private Integer plateFirstId;
    @Column(columnDefinition = "int(11) COMMENT '分类id'")
    private Integer categoryId;

    public Integer getPlateFirstId() {
        return plateFirstId;
    }

    public void setPlateFirstId(Integer plateFirstId) {
        this.plateFirstId = plateFirstId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
