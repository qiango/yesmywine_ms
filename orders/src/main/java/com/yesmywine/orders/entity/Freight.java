package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hz on 6/8/17.
 */
@Entity
@Table(name = "freight")
public class Freight extends BaseEntity<Integer> {//运费配置

    @Column(columnDefinition = "varchar(20) COMMENT '地区名字'")
    private String areaName;//地区名字
    @Column(columnDefinition = "double COMMENT '快递费'")
    private double courierfees;//快递费
    @Column(columnDefinition = "double COMMENT '免运费订单金额'")
    private double orderFree;//免运费订单金额
    @Column(columnDefinition = "varchar(10) COMMENT '是否需要调拨费用,0需要，1不需要'")
    private String transfers;//是否需要调拨费用
    @Column(columnDefinition = "double COMMENT '满多少金额需要调拨费'")
    private double trasfersOrder;//需调拨费订单金额
    @Column(columnDefinition = "double COMMENT '调拨费用'")
    private double transfersAmount;//调拨费用
    @Column(columnDefinition = "varchar(10) COMMENT '是否支持货到付款0支持，1不支持'")
    private String cashOnDelivery;//是否支持货到付款

    @OneToMany
    @JoinColumn(name ="freightId")
    private  Set<FreightAttach> FreightAttachs= new HashSet<>();

    public Set<FreightAttach> getFreightAttachs() {
        return FreightAttachs;
    }

    public void setFreightAttachs(Set<FreightAttach> freightAttachs) {
        FreightAttachs = freightAttachs;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public double getCourierfees() {
        return courierfees;
    }

    public void setCourierfees(double courierfees) {
        this.courierfees = courierfees;
    }

    public double getOrderFree() {
        return orderFree;
    }

    public void setOrderFree(double orderFree) {
        this.orderFree = orderFree;
    }

    public String getTransfers() {
        return transfers;
    }

    public void setTransfers(String transfers) {
        this.transfers = transfers;
    }

    public double getTrasfersOrder() {
        return trasfersOrder;
    }

    public void setTrasfersOrder(double trasfersOrder) {
        this.trasfersOrder = trasfersOrder;
    }

    public double getTransfersAmount() {
        return transfersAmount;
    }

    public void setTransfersAmount(double transfersAmount) {
        this.transfersAmount = transfersAmount;
    }

    public String getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(String cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }
}
