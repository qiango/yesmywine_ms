package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ${shuang} on 2016/12/20.
 */
@Entity
@Table(name = "levelHistory")
public class LevelHistory extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(50) COMMENT '用户名称'")
    private  String userName;
    @Column(columnDefinition = "DATETIME COMMENT '操作时间'")
    private Date operateTimes;
    @Column(columnDefinition = "varchar(50) COMMENT '描述'")
    private String remarks;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LevelHistory() {
        this.operateTimes = new Date();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getOperateTimes() {
        return operateTimes;
    }

    public void setOperateTimes(Date operateTimes) {
        this.operateTimes = operateTimes;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
