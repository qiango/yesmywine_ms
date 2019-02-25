package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "recommendSecent")
public class RecommendSecent extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '推荐id'")
    private Integer recommendFirstId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;

    public Integer getRecommendFirstId() {
        return recommendFirstId;
    }

    public void setRecommendFirstId(Integer recommendFirstId) {
        this.recommendFirstId = recommendFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
