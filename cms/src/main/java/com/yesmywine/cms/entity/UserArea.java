package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "userArea")
public class UserArea extends BaseEntity<Integer>{

    @Column(unique = true ,columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;
    @Column(columnDefinition = "int(11) COMMENT '城市id'")
    private Integer areaId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
