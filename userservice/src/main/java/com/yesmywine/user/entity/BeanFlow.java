package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/4/12.
 */

@Entity
@Table(name = "beanFlow")
public class BeanFlow extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(200) COMMENT '用户名称'")
    private String userName;
    @Column(columnDefinition = "int(3) COMMENT '是否需要同步'")
    private String synStatus;//(0-需要同步，)
    @Column(columnDefinition = "varchar(255) COMMENT '订单号'")
    private String orderNumber;
    @Column(columnDefinition = "double COMMENT '酒豆'")
    private Double beans;
    @Column(columnDefinition = "int(11) COMMENT '积分'")
    private Integer points;
    @Column(columnDefinition = "varchar(100) COMMENT '描述'")
    private String description;
    @Column(columnDefinition = "varchar(50) COMMENT '渠道名称'")
    private String ChannelName;
    @Column(columnDefinition = "varchar(50) COMMENT '渠道编码'")
    private String channelCode;
    @Column(columnDefinition = "varchar(10) COMMENT '状态'")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String channelName) {
        ChannelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBeans() {
        return beans;
    }

    public void setBeans(Double beans) {
        this.beans = beans;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
