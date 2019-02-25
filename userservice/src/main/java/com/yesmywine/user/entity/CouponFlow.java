package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/4/18.
 */
@Entity
@Table(name = "couponFlow")
public class CouponFlow extends BaseEntity<Integer> {
    private  Integer couponId;

}
