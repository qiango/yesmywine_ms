package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "plateSecentGoods")
public class PlateSecentGoods extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '板块id'")
    private Integer plateFirstId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(200) COMMENT '别名'")
    private String alias;//别名

    public Integer getPlateFirstId() {
        return plateFirstId;
    }

    public void setPlateFirstId(Integer plateFirstId) {
        this.plateFirstId = plateFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
