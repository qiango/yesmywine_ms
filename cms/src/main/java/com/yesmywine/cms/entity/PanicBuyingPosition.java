package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Entity
@Table(name = "panicBuyingPosition")
public class PanicBuyingPosition extends BaseEntity<Integer> {

    @Column(columnDefinition = "int(11) COMMENT '广告位'")
    private Integer positionId;//广告位

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
}
