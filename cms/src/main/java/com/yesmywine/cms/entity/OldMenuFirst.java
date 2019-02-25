package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "oldMenuFirst")
public class OldMenuFirst extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '分类id'")
    private Integer firstCategoryId;
    @Column(columnDefinition = "int(11) COMMENT '权重'")
    private Integer firstIndex;

    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public Integer getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(Integer firstIndex) {
        this.firstIndex = firstIndex;
    }

}
