package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by hz on 7/14/17.
 */
@Entity
@Table(name = "userEmail")
public class UserEmail extends BaseEntity<Integer>{
    @Column(columnDefinition = "DATETIME COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "DATETIME COMMENT '用户邮箱'")
    private String email;
    @Column(columnDefinition = "DATETIME COMMENT '邮箱激活时间'")
    private Date activationTime;//邮箱激活时间
    @Column(columnDefinition = "varchar(50) COMMENT '随机码'")
    private String codeEmail;//随机码

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }

    public String getCodeEmail() {
        return codeEmail;
    }

    public void setCodeEmail(String codeEmail) {
        this.codeEmail = codeEmail;
    }
}
