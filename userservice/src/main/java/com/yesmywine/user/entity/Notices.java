package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/6/30.
 */
@Entity
@Table(name = "notices")
public class Notices extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(255) COMMENT '订单号'")
    private  String orderNumber;
    @Column(columnDefinition = "varchar(100) COMMENT '商品名称'")
    private  String goodsName;
    @Column(columnDefinition = "varchar(100) COMMENT '图片url'")
    private  String goodsImageUrl;
    @Column(columnDefinition = "varchar(100) COMMENT '物流订单'")
    private  String LogisticsNumber;
    @Column(columnDefinition = "varchar(10) COMMENT '物流公司'")
    private  String LogisticsName;
    @Column(columnDefinition = "int(1) COMMENT '状态：0未读，1已读'")
    private  Integer status;//状态0w未读
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private  Integer userId;
    @Column(columnDefinition = "varchar(255) COMMENT '订单Id'")
    private  String orderId;
    @Column(columnDefinition = "varchar(255) COMMENT '商品Id'")
    private  String goodsId;


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Notices(){
        this.status= 0;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getLogisticsNumber() {
        return LogisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        LogisticsNumber = logisticsNumber;
    }

    public String getLogisticsName() {
        return LogisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        LogisticsName = logisticsName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
