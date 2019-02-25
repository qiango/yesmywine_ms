package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "oldHotSearchFirst")
public class OldHotSearchFirst extends BaseEntity<Integer>{

    @Column(columnDefinition = "varchar(255) COMMENT '热搜名称'")
    private String hotSearchFirstName;

    public String getHotSearchFirstName() {
        return hotSearchFirstName;
    }

    public void setHotSearchFirstName(String hotSearchFirstName) {
        this.hotSearchFirstName = hotSearchFirstName;
    }
}
