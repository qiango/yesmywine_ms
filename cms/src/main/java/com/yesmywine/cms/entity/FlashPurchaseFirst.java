package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Entity
@Table(name = "flashPurchaseFirst")
public class FlashPurchaseFirst extends BaseEntity<Integer>{//精品闪购

    @Column(columnDefinition = "varchar(255) COMMENT '栏目名称'")
    private String name;//栏目名称
    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer activityId;
    @Column(columnDefinition = "datetime COMMENT '开始时间'")
    private Date startTime;//开始时间
    @Column(columnDefinition = "datetime COMMENT '结束时间'")
    private Date endTime;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
