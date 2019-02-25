package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by hz on 4/12/17.
 */
@Entity
@Table(name = "historyPrice")
public class HistoryPrice extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(11) COMMENT '价格'")
    private String salePrice;//价格

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }
}
