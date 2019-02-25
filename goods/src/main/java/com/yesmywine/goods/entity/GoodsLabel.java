package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by hz on 8/4/17.
 */
@Entity
@Table(name = "GoodsLabel")
public class GoodsLabel extends BaseEntity<Integer>{

    private Integer labelId;
    private Integer goodsId;
    @Ignore
    @Transient
    private String goodsName;

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
