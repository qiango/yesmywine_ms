package com.yesmywine.evaluation.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户行为记录
 * Created by light on 2017/1/6.
 */
@Entity
@Table(name = "userBehaviorRecord")
public class UserBehaviorRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;//用户id
    @Column(columnDefinition = "int(11) COMMENT '评论id'")
    private Integer evaluationId;//评论id
    @Column(columnDefinition = "int(11) COMMENT '咨询id'")
    private Integer adviceId;//咨询id
    @Column(columnDefinition = "int(11) COMMENT '有用或没用标识'")//无用1
    private Integer status;//有用或没用标识
    @Column(columnDefinition = "DATETIME COMMENT '创建时间'")
    private Date createTime;//创建时间

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

    public Integer getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Integer evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Integer getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(Integer adviceId) {
        this.adviceId = adviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
