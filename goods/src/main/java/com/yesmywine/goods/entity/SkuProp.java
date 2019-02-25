package com.yesmywine.goods.entity;


import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hz on 6/19/17.
 */
@Entity
@Table(name = "skuProp")
public class SkuProp extends BaseEntity<Integer> {//sku属性表
    @Column(columnDefinition = "int(11) COMMENT '属性id'")
    private Integer propId;
    @Column(columnDefinition = "int(11) COMMENT '属性值id'")
    private Integer propValueId;


    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public Integer getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(Integer propValueId) {
        this.propValueId = propValueId;
    }
}
