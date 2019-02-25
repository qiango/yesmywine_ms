package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hz on 6/19/17.
 */
@Entity
@Table(name = "propMirror")
public class PropMirror extends BaseEntity<Integer> {//属性镜像表
    @Column(columnDefinition = "int(11) COMMENT '属性id'")
    private Integer propId;
    @Column(columnDefinition = "varchar(20) COMMENT '属性值'")
    private String propValue;

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
}
