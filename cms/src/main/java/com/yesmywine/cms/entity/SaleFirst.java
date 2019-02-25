package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Entity
@Table(name = "saleFirst")
public class SaleFirst extends BaseEntity<Integer> {//名庄特卖

    @Column(columnDefinition = "varchar(255) COMMENT '栏目名称'")
    private String name;//栏目名称
    @Column(columnDefinition = "varchar(255) COMMENT '标签'")
    private String title;//标签
    @Column(columnDefinition = "int(11) COMMENT '广告位id'")
    private Integer positionId;//广告位id

    public String getName() {
        return name;
    }

    public void setName(String name) {
            this.name = name;
        }

    public String getTitle() {
            return title;
        }

    public void setTitle(String title) {
            this.title = title;
        }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }
}


