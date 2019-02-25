package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/25.
 */
@Entity
@Table(name = "panicBuyingSecent")
public class PanicBuyingSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '超值抢购栏目id'")
    private Integer panicBuyingFirstId;//超值抢购栏目id
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id

    public Integer getPanicBuyingFirstId() {
        return panicBuyingFirstId;
    }

    public void setPanicBuyingFirstId(Integer panicBuyingFirstId) {
        this.panicBuyingFirstId = panicBuyingFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
