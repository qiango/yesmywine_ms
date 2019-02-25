package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "plateFirst")
public class PlateFirst extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '分类板块id'")
    private Integer firstCategoryId;//分类板块id
    @Column(columnDefinition = "int(11) COMMENT '权重'")
    private Integer firstIndex;//权重
    @Column(columnDefinition = "int(11) COMMENT '广告位'")
    private Integer firstPositionId;
    @Column(columnDefinition = "int(11) COMMENT '广告位'")
    private Integer secentPositionId;
    @Column(columnDefinition = "int(11) COMMENT '广告位'")
    private Integer thirdPositionId;
    @Column(columnDefinition = "int(11) COMMENT '广告位'")
    private Integer fourthPositionId;
    @Column(columnDefinition = "int(11) COMMENT 'app广告位'")
    private Integer appPositionId;
    @Column(columnDefinition = "int(11) COMMENT '是否显示'")
    private Integer isShow;//是否显示

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

    public Integer getThirdPositionId() {
        return thirdPositionId;
    }

    public void setThirdPositionId(Integer thirdPositionId) {
        this.thirdPositionId = thirdPositionId;
    }

    public Integer getFourthPositionId() {
        return fourthPositionId;
    }

    public void setFourthPositionId(Integer fourthPositionId) {
        this.fourthPositionId = fourthPositionId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getAppPositionId() {
        return appPositionId;
    }

    public void setAppPositionId(Integer appPositionId) {
        this.appPositionId = appPositionId;
    }
}
