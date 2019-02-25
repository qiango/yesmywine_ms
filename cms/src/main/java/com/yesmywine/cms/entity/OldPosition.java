package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "oldPosition")
public class OldPosition extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '广告位id'")
    private Integer firstPositionId;
    @Column(columnDefinition = "int(11) COMMENT '广告位id'")
    private Integer secentPositionId;

    public Integer getFirstPositionId() {
        return firstPositionId;
    }

    public void setFirstPositionId(Integer firstPositionId) {
        this.firstPositionId = firstPositionId;
    }

    public Integer getSecentPositionId() {
        return secentPositionId;
    }

    public void setSecentPositionId(Integer secentPositionId) {
        this.secentPositionId = secentPositionId;
    }
}
