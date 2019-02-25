package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by yly on 2017/1/4.
 */
@Entity
@Table(name = "adver")//广告素材表
public class AdverEntity extends BaseEntity<Integer> {

    @Column(columnDefinition = "varchar(255) COMMENT '广告名称'")
    private String adverName;//广告名称
    @Column(columnDefinition = "DATETIME COMMENT '广告开始时间'")
    private Date startTime;//广告开始时间
    @Column(columnDefinition = "DATETIME COMMENT '广告结束时间'")
    private Date endTime;//广告结束时间
    @Column(columnDefinition = "int(11) COMMENT '广告位宽度'")
    private Integer width;//广告位宽度
    @Column(columnDefinition = "int(11) COMMENT '广告位高度'")
    private Integer height;//广告位高度
    @Column(columnDefinition = "varchar(255) COMMENT '广告链接'")
    private String url;//广告链接
    @Column(columnDefinition = "varchar(255) COMMENT '广告图片'")
    private String image;//广告图片
    @Column(columnDefinition = "int(11) COMMENT '是否显示：0表示显示，1表示不显示'")
    private Integer isDisplay;//是否显示：0表示显示，1表示不显示
    @Column(columnDefinition = "int(11) COMMENT '内部或外部的,0内部的，1外部的'")
    private Integer inOrOut;//内部或外部的,0内部的，1外部的
    @Column(columnDefinition = "varchar(255) COMMENT '广告位关联url或着活动页id，分类id，商品id'")
    private String relevancy;//广告位关联url或着活动页id，分类id，商品id
    @Column(columnDefinition = "int(11) COMMENT '关联类型；0是url，1是活动页id，2是商品id，3是分类id'")
    private Integer relevancyType;//关联类型；0是url，1是活动页id，2是商品id，3是分类id
    @Column(columnDefinition = "varchar(255) COMMENT '备注'")
    private String remark;//备注

    public AdverEntity() {
        this.startTime = new Date();
        this.endTime = new Date();
        this.isDisplay = 0;//默认显示
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public String getAdverName() {
        return adverName;
    }

    public void setAdverName(String adverName) {
        this.adverName = adverName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(Integer isDisplay) {
        this.isDisplay = isDisplay;
    }

    public Integer getInOrOut() {
        return inOrOut;
    }

    public void setInOrOut(Integer inOrOut) {
        this.inOrOut = inOrOut;
    }

    public String getRelevancy() {
        return relevancy;
    }

    public void setRelevancy(String relevancy) {
        this.relevancy = relevancy;
    }

    public Integer getRelevancyType() {
        return relevancyType;
    }

    public void setRelevancyType(Integer relevancyType) {
        this.relevancyType = relevancyType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}


