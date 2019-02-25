package com.yesmywine.goods.entityProperties;

import javax.persistence.*;

/**
 * Created by hz on 1/6/17.
 */
@Entity
@Table(name = "propGoods")
public class PropGoods {   //属性商品表
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cnName;
    private String propValueId;
    private Integer goodsId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(String propValueId) {
        this.propValueId = propValueId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }
}
