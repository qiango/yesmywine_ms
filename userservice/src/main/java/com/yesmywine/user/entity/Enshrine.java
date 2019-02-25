package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/6/13.
 */
@Entity
@Table(name = "enshrine")
public class Enshrine extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private  Integer userId;
    @Column(columnDefinition = "int(11) COMMENT '商品Id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(100) COMMENT '商品名称'")
    private  String goodsName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
