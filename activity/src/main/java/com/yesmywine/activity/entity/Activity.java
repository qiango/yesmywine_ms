package com.yesmywine.activity.entity;



import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdiandian on 2017/1/3.
 */
@Entity
@Table(name = "activity")
public class Activity extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT '活动名称'")
    private String name;//
    @Column(columnDefinition = "int(11) COMMENT '执行类id'")
    private Integer actionId;
    @Column(columnDefinition = "varchar(10) COMMENT '执行类code'")
    private String actionCode;
    @Column(columnDefinition = "varchar(10) COMMENT '触发类code'")
    private String triggerCode;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "int(2) COMMENT '状态  current-正在进行的活动 notCurrent-待进行的活动  overdue-过期的活动 '")
    private ActivityStatus status;
    @Column(columnDefinition = "datetime COMMENT '开始时间'")
    private Date startTime;//开始时间
    @Column(columnDefinition = "datetime COMMENT '结束时间'")
    private Date endTime;
    @Column(columnDefinition = "int(11) COMMENT '优先级'")
    private Integer priority;
    private Integer userLevel;//是否全场 等级0:all
    @Column(columnDefinition = "varchar(10) COMMENT '创建人'")
    private String creator;
    @Column(columnDefinition = "varchar(10) COMMENT '修改人'")
    private String editor;
    @Column(columnDefinition = "varchar(10) COMMENT '审核人'")
    private String auditor;
    @Column(columnDefinition = "datetime COMMENT '审核时间'")
    private Date auditTime;
    @Column(columnDefinition = "datetime COMMENT '修改时间'")
    private Date modifyTime;
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int(2) COMMENT '删除'")
    private DeleteEnum isDelete;
    @Column(columnDefinition = "int(2) COMMENT '审核状态  0-待审核  1-审核 -1-驳回  2-取消 3-草稿'")
    private Integer auditStatus;
    @Column(columnDefinition = "varchar(15) COMMENT '备注'")
    private String comment;
    @Column(columnDefinition = "varchar(15) COMMENT '活动类型编码 eg fullT-discountA'")
    private String type;
    @Column(columnDefinition = "varchar(15) COMMENT '活动类型名称 eg 满折 '")
    private String typeName;
    @Column(columnDefinition = "varchar(15) COMMENT '活动类型别名 '")
    private String typeAlias;
    @Column(columnDefinition = "varchar(15) COMMENT '共享活动Id'")
    private String shareId;
    @Column(columnDefinition = "bit(1) COMMENT '活动中的商品能否参加会员折扣'")
    private Boolean isMember;
    @Column(columnDefinition = "bit(1) COMMENT '是否是共享活动'")
    private Boolean isShare;

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(Integer userLevel) {
        this.userLevel = userLevel;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(String triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getMember() {
        return isMember;
    }

    public void setMember(Boolean member) {
        isMember = member;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public String getTypeAlias() {
        return typeAlias;
    }

    public void setTypeAlias(String typeAlias) {
        this.typeAlias = typeAlias;
    }
}