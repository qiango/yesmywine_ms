package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Entity
@Table(name = "flashPurchaseSecent")
public class FlashPurchaseSecent extends BaseEntity<Integer> {//精品闪购

    @Column(columnDefinition = "int(11) COMMENT '精品闪购id'")
    private Integer flashPurchaseFirstId;//精品闪购id
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id

    public Integer getFlashPurchaseFirstId() {
        return flashPurchaseFirstId;
    }

    public void setFlashPurchaseFirstId(Integer flashPurchaseFirstId) {
        this.flashPurchaseFirstId = flashPurchaseFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}