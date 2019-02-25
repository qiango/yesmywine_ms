package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "oldSecent")
public class OldSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '老酒馆板块id'")
    private Integer oldFirstId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;

    public Integer getOldFirstId() {
        return oldFirstId;
    }

    public void setOldFirstId(Integer oldFirstId) {
        this.oldFirstId = oldFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
