package com.yesmywine.activity.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by by on 2017/6/30.
 */
@Entity
@Table(name = "goodsSku")
public class GoodsSku extends BaseEntity<Integer>{
    @Column(columnDefinition = "varchar(10) COMMENT '商品id'")
    private String goodsId;
    @Column(columnDefinition = "int(10) COMMENT 'skuId'")
    private Integer skuId;
    @Column(columnDefinition = "varchar(50) COMMENT 'sku编码'")
    private String skuCode;
    @Column(columnDefinition = "int(10) COMMENT '数量'")
    private Integer count;

    public GoodsSku() {
    }


    public GoodsSku(String goodsId, Integer skuId,String skuCode, Integer count) {
        this.goodsId = goodsId;
        this.skuId = skuId;
        this.skuCode = skuCode;
        this.count = count;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
