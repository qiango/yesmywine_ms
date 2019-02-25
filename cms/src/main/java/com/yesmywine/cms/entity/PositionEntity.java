package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by yly on 2017/1/4.
 */
@Entity
@Table(name = "position")//广告位表
public class PositionEntity extends BaseEntity<Integer> {

    @Column(columnDefinition = "varchar(255) COMMENT '广告位名称'")
    private String positionName;//广告位名称
    @Column(columnDefinition = "int(11) COMMENT '广告位类型,0单图，1多图'")
    private Integer positionType;//广告位类型,0单图，1多图
    @Column(columnDefinition = "varchar(255) COMMENT '广告位描述'")
    private String positionDesc;//广告位描述
    @Column(columnDefinition = "int(11) COMMENT '广告位宽度'")
    private Integer width;//广告位宽度
    @Column(columnDefinition = "int(11) COMMENT '广告位高度'")
    private Integer height;//广告位高度
    @Column(columnDefinition = "int(11) COMMENT '是否启用广告位:0表示启用，1表示禁用'")
    private Integer status;//是否启用广告位:0表示启用，1表示禁用


    public PositionEntity() {
        this.status = 1;//默认不启用广告位
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getPositionType() {
        return positionType;
    }

    public void setPositionType(Integer positionType) {
        this.positionType = positionType;
    }

    public String getPositionDesc() {
        return positionDesc;
    }

    public void setPositionDesc(String positionDesc) {
        this.positionDesc = positionDesc;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
