package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/7/27.
 */
@Entity
@Table(name = "freightAttach")
public class FreightAttach extends BaseEntity<Integer> {
    
    @Column(columnDefinition = "int(5) COMMENT '关联区Id'")
    private Integer freightId;
    @Column(columnDefinition = "varchar(50) COMMENT '省市名称Id'")
    private  String areaId;
    @Column(columnDefinition = "varchar(20) COMMENT '省市名称'")
    private  String   name;

    public Integer getFreightId() {
        return freightId;
    }

    public void setFreightId(Integer freightId) {
        this.freightId = freightId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
