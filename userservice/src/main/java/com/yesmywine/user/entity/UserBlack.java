package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/4/5.
 */
@Entity
@Table(name = "userBlack")
public class UserBlack extends BaseEntity<Integer> {

    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "int(5) COMMENT '禁用权限ID'")
    private Integer rightsId;//禁用权限ID
    @Column(columnDefinition = "varchar(100) COMMENT '禁用原因'")
    private String reason;
    @Column(columnDefinition = "int(1) COMMENT '状态：1解除2未解除'")
    private  Integer blackStatus;
    @Column(columnDefinition = "varchar(50) COMMENT '用户名'")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserBlack(){
        this.blackStatus =2;
    }
    public Integer getBlackStatus() {
        return blackStatus;
    }

    public void setBlackStatus(Integer blackStatus) {
        this.blackStatus = blackStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRightsId() {
        return rightsId;
    }

    public void setRightsId(Integer rightsId) {
        this.rightsId = rightsId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
