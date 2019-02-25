package com.yesmywine.goods.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;

/**
 * Created by hz on 1/10/17.
 */
@Entity
@Table(name = "spu")
public class Spu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long goodsId;
    @Lob
    private String property;
    @Ignore
    @Transient
    private String propertyee;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPropertyee() {
        return propertyee;
    }

    public void setPropertyee(String propertyee) {
        this.propertyee = propertyee;
    }
}
