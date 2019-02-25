package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by ${shuang} on 2017/8/10.
 */
@Entity
@Table(name = "storeWine")
 public class StoreWine extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "int(5) COMMENT '免费存酒天数'")
    private Integer freeDays;//免费存酒天数
    @Column(columnDefinition = "DATETIME COMMENT '计费开始时间'")
    private Date chargingTime;//计费开始时间
    @Column(columnDefinition = "varchar(50) COMMENT '存酒单价'")
    private  String univalent;
    @Column(columnDefinition = "varchar(50) COMMENT '订单号'")
    private  String orderNumber;
    @Column(columnDefinition = "varchar(1) COMMENT '是否提完0：未提完，1：提完'")
    private  String isOver;

    public String getUnivalent() {
        return univalent;
    }

    public void setUnivalent(String univalent) {
        this.univalent = univalent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(Integer freeDays) {
        this.freeDays = freeDays;
    }

    public Date getChargingTime() {
        return chargingTime;
    }

    public void setChargingTime(Date chargingTime) {
        this.chargingTime = chargingTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

}
