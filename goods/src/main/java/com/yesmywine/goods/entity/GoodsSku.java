package com.yesmywine.goods.entity;


import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by hz on 6/19/17.
 */
@Entity
@Table(name = "goodsSku")
public class GoodsSku extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT 'skuId'")
    private Integer skuId;
    @Column(columnDefinition = "int(11) COMMENT '数量'")
    private Integer count;
    @Column(columnDefinition = "varchar(200) COMMENT 'skuCode'")
    private String code;
    @Column(columnDefinition = "int(11) COMMENT '福袋已售数量'")
    private Integer soldNumber;//福袋已售数量

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getSoldNumber() {
        return soldNumber;
    }

    public void setSoldNumber(Integer soldNumber) {
        this.soldNumber = soldNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
