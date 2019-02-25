package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by SJQ on 2017/4/25.
 */
@Entity
@Table(name = "homePagePanicBuying")
public class HomePagePanicBuying extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer activityId;
    @Column(columnDefinition = "int(11) COMMENT '0:显示  1:不显示'")
    private Integer status;//0:显示  1:不显示
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
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
}
