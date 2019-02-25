package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "oldHotSearchSecent")
public class OldHotSearchSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '热搜id'")
    private Integer hotSearchFirstId;
    @Column(columnDefinition = "varchar(255) COMMENT '热搜名称'")
    private String hotSearchSecentName;

    public Integer getHotSearchFirstId() {
        return hotSearchFirstId;
    }

    public void setHotSearchFirstId(Integer hotSearchFirstId) {
        this.hotSearchFirstId = hotSearchFirstId;
    }

    public String getHotSearchSecentName() {
        return hotSearchSecentName;
    }

    public void setHotSearchSecentName(String hotSearchSecentName) {
        this.hotSearchSecentName = hotSearchSecentName;
    }
}
