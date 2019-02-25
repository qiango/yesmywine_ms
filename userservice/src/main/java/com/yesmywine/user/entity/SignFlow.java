package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/7/11.
 */
@Entity
@Table(name = "signFlow")
public class SignFlow extends BaseEntity<Integer> {

    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
  private   Integer userId;
    @Column(columnDefinition = "int(11) COMMENT '签到积分'")
  private  Integer point ;
    @Column(columnDefinition = "varchar(100) COMMENT '签到时间'")
  private  String signTime;
    @Column(columnDefinition = "varchar(100) COMMENT '用户名'")
    private String  userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getSignTime() {
        return signTime;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }
}
