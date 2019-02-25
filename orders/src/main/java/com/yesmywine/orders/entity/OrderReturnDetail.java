package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hz on 2017/6/30.
 */
@Entity
@Table(name = "orderRefundDetail")
public class OrderReturnDetail extends BaseEntity<Integer>{
    @Column(columnDefinition = " varchar(50) COMMENT '商品ID'")
    private Integer goodId;
    @Column(columnDefinition = " varchar(50) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = " varchar(50) COMMENT '售价'")
    private Double salePrice;
    @Column(columnDefinition = " varchar(50) COMMENT '申请数量'")
    private Integer reNum;
    @Column(columnDefinition = " varchar(50) COMMENT '退货单号'")
    private String returnNo;

    public OrderReturnDetail() {
    }

    public OrderReturnDetail(String retrunNo,Integer goodId, String goodsName, Double salePrice, Integer reNum) {
        this.returnNo = retrunNo;
        this.goodId = goodId;
        this.goodsName = goodsName;
        this.salePrice = salePrice;
        this.reNum = reNum;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getReNum() {
        return reNum;
    }

    public void setReNum(Integer reNum) {
        this.reNum = reNum;
    }
}
