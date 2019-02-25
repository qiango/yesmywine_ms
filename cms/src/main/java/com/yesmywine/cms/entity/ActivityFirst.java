package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "activityFirst")
public class ActivityFirst extends BaseEntity<Integer>{

    @Column(columnDefinition = "varchar(255) COMMENT '活动页名'")
    private String name;
    @Column(columnDefinition = "varchar(255) COMMENT '背景图片'")
    private String imageBack;
    @Column(columnDefinition = "int(11) COMMENT '横幅广告位'")
    private Integer positionIdBanner;
    @Column(columnDefinition = "int(11) COMMENT '模板'")
    private Integer templateId;//模板
    @Column(columnDefinition = "varchar(255) COMMENT '标签'")
    private String label;
    @Column(columnDefinition = "varchar(255) COMMENT '副标题'")
    private String subtitle;//副标题
    @Column(columnDefinition = "varchar(255) COMMENT 'app位置'")
    private String appPosition;//
    @Column(columnDefinition = "varchar(255) COMMENT 'app图片'")
    private String appImage;//app图片
    @Column(columnDefinition = "varchar(255) COMMENT 'app标题'")
    private String appTitle;//app标题


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPositionIdBanner() {
        return positionIdBanner;
    }

    public void setPositionIdBanner(Integer positionIdBanner) {
        this.positionIdBanner = positionIdBanner;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageBack() {
        return imageBack;
    }

    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }

    public String getAppPosition() {
        return appPosition;
    }

    public void setAppPosition(String appPosition) {
        this.appPosition = appPosition;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }
}
