package com.yesmywine.evaluation.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "reply")
public class Reply extends BaseEntity<Integer> {//回复
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(11) COMMENT '用户名称'")
    private String userName;
    @Column(columnDefinition = "varchar(200) COMMENT '用户图片'")
    private String userImage;
    @Column(columnDefinition = "varchar(200) COMMENT '回复'")
    private String reply;//回复
    @Column(columnDefinition = "int(11) COMMENT '审核标识'")
    private Integer status;//审核标识



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
