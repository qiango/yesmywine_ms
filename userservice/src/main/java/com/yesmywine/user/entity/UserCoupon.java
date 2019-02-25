package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by ${shuang} on 2017/4/13.
 */
@Entity
@Table(name = "userCoupon")
public class UserCoupon extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "int(11) COMMENT '优惠券Id'")
    private Integer couponId;
    @Column(columnDefinition = "DATETIME COMMENT '优惠券使用时间'")
    private Date userTime;
    @Column(columnDefinition = "int(1) COMMENT '状态：4可用，5已用'")
    private Integer status;//4可用，5已用
    @Column(columnDefinition = "varchar(200) COMMENT '优惠券编码'")
    private String codeNumber;

    public UserCoupon(){
        this.status=4;//可用
    }

    public String getCodeNumber() {
        return codeNumber;
    }

    public void setCodeNumber(String codeNumber) {
        this.codeNumber = codeNumber;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Date getUserTime() {
        return userTime;
    }

    public void setUserTime(Date userTime) {
        this.userTime = userTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
