package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "activityColumn")
public class ActivityColumn extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '活动页id'")
    private Integer activityFirstId;
    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer activityId;
    @Column(columnDefinition = "varchar(200) COMMENT '栏目名'")
    private String name;
    @Column(columnDefinition = "varchar(200) COMMENT '图片'")
    private String image;
    @Column(columnDefinition = "int(11) COMMENT '广告位id'")
    private Integer positionId;

    public Integer getActivityFirstId() {
        return activityFirstId;
    }

    public void setActivityFirstId(Integer activityFirstId) {
        this.activityFirstId = activityFirstId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
}
