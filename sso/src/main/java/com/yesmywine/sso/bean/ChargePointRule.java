package com.yesmywine.sso.bean;


import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/7/10.
 */
@Entity
@Table(name = "chargePointRule")
public class ChargePointRule extends BaseEntity<Integer> {
    @Column(columnDefinition = "double COMMENT '倍数'")
    private  Integer multiple;
    @Column(columnDefinition = "int(1) COMMENT '状态：0未启用，1启用'")
    private  Integer status;

    public ChargePointRule(){
        this.status =1;
    }
    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
