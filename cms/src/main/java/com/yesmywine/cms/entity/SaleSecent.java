package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Entity
@Table(name = "saleSecent")
public class SaleSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '名庄特卖id'")
    private Integer saleFirstId;//名庄特卖id
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id

    public Integer getSaleFirstId() {
        return saleFirstId;
    }

    public void setSaleFirstId(Integer saleFirstId) {
        this.saleFirstId = saleFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}

