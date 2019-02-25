package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Entity
@Table(name = "homePagePanicBuyingSecent")
public class HomePagePanicBuyingSecent extends BaseEntity<Integer> {//精品闪购

    @Column(columnDefinition = "int(11) COMMENT '精品闪购id'")
    private Integer homePagePanicBuyingFirstId;//精品闪购id
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id

    public Integer getHomePagePanicBuyingFirstId() {
        return homePagePanicBuyingFirstId;
    }

    public void setHomePagePanicBuyingFirstId(Integer homePagePanicBuyingFirstId) {
        this.homePagePanicBuyingFirstId = homePagePanicBuyingFirstId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}