package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/6/1.
 */
@Entity
@Table(name = "boutiqueFirst")
public class BoutiqueFirst extends BaseEntity<Integer> {//精品闪购

    @Column(columnDefinition = "varchar(200) COMMENT '名称'")
    private String name;//renqi ,jingpin,不允许修改
    @Column(columnDefinition = "int(11) COMMENT '状态'")
    private Integer status;//0buneng 1 neng
    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer activityId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }
}
