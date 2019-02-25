package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by hz on 7/11/17.
 */
@Entity
@Table(name = "skuCommonProp")
public class SkuCommonProp extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '属性id'")
    private Integer propId;
    @Column(columnDefinition = "varchar(10) COMMENT '属性值id'")
    private String propValueId;
    @Ignore
    @Transient
    private String propName;
    @Ignore
    @Transient
    private String propValueName;

    public Integer getPropId() {
        return propId;
    }

    public void setPropId(Integer propId) {
        this.propId = propId;
    }

    public String getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(String propValueId) {
        this.propValueId = propValueId;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValueName() {
        return propValueName;
    }

    public void setPropValueName(String propValueName) {
        this.propValueName = propValueName;
    }
}
