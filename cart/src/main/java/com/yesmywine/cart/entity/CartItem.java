package com.yesmywine.cart.entity;


import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by by on 2016/12/20.
 */
@Entity
@Table(name = "cartItem")
public class CartItem extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '购物车id'")
    private Integer cartId;//
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id
    @Column(columnDefinition = "int(11) COMMENT '商品数量'")
    private Integer goodsCounts;//商品数量
    @Column(columnDefinition = "int(11) COMMENT '活动规则id'")
    private Integer regulationId;//活动规则id;
    @Column(columnDefinition = "int(11) COMMENT '状态（0未选中，1选中）'")
    private Integer status;//状态（0未选中，1选中）
    @Column(columnDefinition = "int(11) COMMENT '是否是赠品（0不是，1是）'")
    private Integer gift;//是否是赠品（0不是，1是）
    @Column(columnDefinition = "int(11) COMMENT '子商品id'")
    private Integer childGoodsId;


    public Integer getRegulationId() {
        return regulationId;
    }

    public void setRegulationId(Integer regulationId) {
        this.regulationId = regulationId;
    }

    public Integer getGift() {
        return gift;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }

    public Integer getChildGoodsId() {
        return childGoodsId;
    }

    public void setChildGoodsId(Integer childGoodsId) {
        this.childGoodsId = childGoodsId;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsCounts() {
        return goodsCounts;
    }

    public void setGoodsCounts(Integer goodsCounts) {
        this.goodsCounts = goodsCounts;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
