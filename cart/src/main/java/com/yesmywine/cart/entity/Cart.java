package com.yesmywine.cart.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by whao on 2016/12/19.
 */
@Entity
@Table(name = "cart")
public class Cart extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;//用户id


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
