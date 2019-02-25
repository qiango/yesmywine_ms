package com.yesmywine.goods.entity;


import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by hz on 6/19/17.
 */
@Entity
@Table(name = "goodsProp")
public class GoodsProp extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '属性id'")
    private Integer propId;
    @Column(columnDefinition = "varchar(10) COMMENT '属性值id'")
    private String propValue;
    @Column(columnDefinition = "int(11) COMMENT '是否显示'")
    private Integer type;
    private boolean isUpdate;//是否在商品被修改,true是改动了


    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
