package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/6/1.
 */
@Entity
@Table(name = "boutiqueSecent")
public class BoutiqueSecent extends BaseEntity<Integer> {

    @Column(columnDefinition = "int(11) COMMENT '精品闪购主键id'")
    private Integer boutiqueFirstId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;

    public Integer getBoutiqueFirstId() {
        return boutiqueFirstId;
    }

    public void setBoutiqueFirstId(Integer boutiqueFirstId) {
        this.boutiqueFirstId = boutiqueFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
