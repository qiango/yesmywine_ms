package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/4/6.
 */
@Entity
@Table(name = "appeal")
public class Appeal extends BaseEntity<Integer> {

//•	用户
//•	申诉内容
//•	申诉时间
//•	状态（4已处理，5未处理）
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;
    @Column(columnDefinition = "varchar(255) COMMENT '用户申诉文本'")
    private String content;
    @Column(columnDefinition = "int(1) COMMENT '状态（4已处理，5未处理）'")
    private Integer status;
    @Column(columnDefinition = "varchar(255) COMMENT '管理员处理意见'")
    private  String rejectContent;
    @Column(columnDefinition = "varchar(200) COMMENT '用户名称'")
    private String userName;
    public Appeal(){
        this.status=5;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRejectContent() {
        return rejectContent;
    }

    public void setRejectContent(String rejectContent) {
        this.rejectContent = rejectContent;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
