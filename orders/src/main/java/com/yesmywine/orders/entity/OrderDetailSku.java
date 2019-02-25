package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/6/19.
 */
@Entity
@Table(name = "orderDetailSku")
public class OrderDetailSku extends BaseEntity<Long> {//订单明细Sku
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "int(11) COMMENT 'skuId'")
    private Integer skuId;
    @Column(columnDefinition = "varchar(50) COMMENT '商品编码'")
    private String skuCode;
    @Column(columnDefinition = "int(11) COMMENT '数量'")
    private Integer counts;//数量

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
}
