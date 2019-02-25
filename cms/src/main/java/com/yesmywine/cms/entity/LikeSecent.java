package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "likeSecent")
public class LikeSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '猜你喜欢id'")
    private Integer likeFirstId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;

    public Integer getLikeFirstId() {
        return likeFirstId;
    }

    public void setLikeFirstId(Integer likeFirstId) {
        this.likeFirstId = likeFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
