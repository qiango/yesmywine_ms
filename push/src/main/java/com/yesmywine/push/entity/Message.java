package com.yesmywine.push.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by light on 2017/4/5.
 */
@Entity
@Table(name = "message")
public class Message extends BaseEntity<Integer> {

    @Column(columnDefinition = "varchar(255) COMMENT '标题'")
    private String title;
    @Column(columnDefinition = "int(11) COMMENT '关联活动页id，分类id，商品id'")
    private Integer relevance;
    @Column(columnDefinition = "int(11) COMMENT '关联类型：1是活动页id，2是商品id，3是分类id,10物流消息'")
    private Integer relevancyType;
    @Column(columnDefinition = "varchar(255) COMMENT '关联名称'")
    private String name;
    @Column(columnDefinition = "int(11) COMMENT '状态:0是未发送过，1是发送过'")
    private Integer status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRelevance() {
        return relevance;
    }

    public void setRelevance(Integer relevance) {
        this.relevance = relevance;
    }

    public Integer getRelevancyType() {
        return relevancyType;
    }

    public void setRelevancyType(Integer relevancyType) {
        this.relevancyType = relevancyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
