package com.yesmywine.activity.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "iftttRegulation")
public class IftttRegulation extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '活动主键id'")
    private Integer activityId;//活动主键id
    @Column(columnDefinition = "varchar(20) COMMENT '规则名称'")
    private String name;//规则名称
    @Column(columnDefinition = "int(11) COMMENT '触发id'")
    private Integer triggerId;//触发id
    @Column(columnDefinition = "varchar(50) COMMENT '触发编码'")
    private String triggerCode;//触发id
    @Column(columnDefinition = "varchar(20) COMMENT '触发值'")
    private String triggerValue;//触发值
    @Column(columnDefinition = "int(20) COMMENT '执行Id'")
    private Integer actionId;//类型id
    @Column(columnDefinition = "varchar(50) COMMENT '执行编码'")
    private String actionCode;//类型id
    @Column(columnDefinition = "varchar(20) COMMENT '执行值'")
    private String actionValue;//执行值
    @Enumerated(EnumType.ORDINAL)
    private DeleteEnum isDelete;//删除
    @Column(columnDefinition = "int(11) COMMENT '优先级'")
    private Integer priority;//优先级

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "shareId")
    private Set<IftttRegulation>  children = new HashSet<>();//共享规则id

    public void addRegulation(IftttRegulation regulation){
        Set<IftttRegulation> regulationSet = getChildren();
        if(regulationSet == null){
            regulationSet = new HashSet<>();
            regulationSet.add(regulation);
        }
        regulationSet.add(regulation);
    }

    public Set<IftttRegulation> getChildren() {
        return children;
    }

    public void setChildren(Set<IftttRegulation> children) {
        this.children = children;
    }

    public String getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(String triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

//    public Integer getShareId() {
//        return shareId;
//    }
//
//    public void setShareId(Integer shareId) {
//        this.shareId = shareId;
//    }

    public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Integer triggerId) {
        this.triggerId = triggerId;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }

    public List<Integer> getTTTIds() {

        return Arrays.asList(triggerId, actionId);
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getTriggerValue() {
        return triggerValue;
    }

    public void setTriggerValue(String triggerValue) {
        this.triggerValue = triggerValue;
    }

    public String getActionValue() {
        return actionValue;
    }

    public void setActionValue(String actionValue) {
        this.actionValue = actionValue;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}
