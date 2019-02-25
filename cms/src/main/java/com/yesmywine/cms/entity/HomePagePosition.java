package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "homePagePosition")
public class HomePagePosition extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '横幅广告位'")
    private Integer bannerPositionId;
    @Column(columnDefinition = "int(11) COMMENT '广告位1'")
    private Integer positionIdOne;
    @Column(columnDefinition = "int(11) COMMENT '广告位2'")
    private Integer positionIdTwo;
    @Column(columnDefinition = "int(11) COMMENT '广告位3'")
    private Integer positionIdThree;

    public Integer getBannerPositionId() {
        return bannerPositionId;
    }

    public void setBannerPositionId(Integer bannerPositionId) {
        this.bannerPositionId = bannerPositionId;
    }

    public Integer getPositionIdOne() {
        return positionIdOne;
    }

    public void setPositionIdOne(Integer positionIdOne) {
        this.positionIdOne = positionIdOne;
    }

    public Integer getPositionIdTwo() {
        return positionIdTwo;
    }

    public void setPositionIdTwo(Integer positionIdTwo) {
        this.positionIdTwo = positionIdTwo;
    }

    public Integer getPositionIdThree() {
        return positionIdThree;
    }

    public void setPositionIdThree(Integer positionIdThree) {
        this.positionIdThree = positionIdThree;
    }
}
