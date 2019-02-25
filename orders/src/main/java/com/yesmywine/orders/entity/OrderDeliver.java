package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.orders.bean.WhetherEnum;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderDeliver")
public class OrderDeliver extends BaseEntity<Long> {//订单配送信息

    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Column(columnDefinition = "int(2) COMMENT '配送方式：0 普通快递,1 上门自提'")
    private Integer deliverType;//配送方式：   0 普通快递',1 上门自提
    @Column(columnDefinition = "varchar(50) COMMENT '上门自提仓库编码'")
    private String warehouseCode;//上门自提仓库编码
    @Column(columnDefinition = "varchar(50) COMMENT '收货人'")
    private String receiver;//收货人
    @Column(columnDefinition = "int(10) COMMENT '地址id'")
    private Integer areaId;//地址id
    @Column(columnDefinition = "varchar(50) COMMENT '收货地址'")
    private String address;//收货地址
    @Column(columnDefinition = "varchar(50) COMMENT '联系电话'")
    private String phone;//联系电话
//    @Column(columnDefinition = "varchar(50) COMMENT '邮编'")
//    private String postCode;//邮编
    @Column(columnDefinition = "DATETIME COMMENT '发货日期'")
    private Date deliverDate;//发货日期
    @Enumerated(EnumType.STRING)
    private WhetherEnum cashOnDelivery;//是否货到付款
    @Column(columnDefinition = "varchar(50) COMMENT '运单号'")
    private String waybillNumber;//运单号
    @Column(columnDefinition = "varchar(50) COMMENT '承运商编码'")
    private String shipperCode;//承运商编码
    @Column(columnDefinition = "int(1) COMMENT '0周一至周五，1双休,2任意时间'")
    private Integer timeSlot;//0周一至周五，1双休
    @Column(columnDefinition = "int(1) COMMENT '0现金，1刷卡'")
    private Integer cashorCard;//0现金，1刷卡

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public Integer getCashorCard() {
        return cashorCard;
    }

    public void setCashorCard(Integer cashorCard) {
        this.cashorCard = cashorCard;
    }

    public Integer getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(Integer timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getWaybillNumber() {
        return waybillNumber;
    }

    public void setWaybillNumber(String waybillNumber) {
        this.waybillNumber = waybillNumber;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(Integer deliverType) {
        this.deliverType = deliverType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    public String getPostCode() {
//        return postCode;
//    }
//
//    public void setPostCode(String postCode) {
//        this.postCode = postCode;
//    }

    public Date getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(Date deliverDate) {
        this.deliverDate = deliverDate;
    }

    public WhetherEnum getCashOnDelivery() {
        return cashOnDelivery;
    }

    public void setCashOnDelivery(WhetherEnum cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }
}
