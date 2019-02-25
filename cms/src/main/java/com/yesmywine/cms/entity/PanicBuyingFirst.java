package com.yesmywine.cms.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wangdiandian on 2017/5/25.
 */
@Entity
@Table(name = "panicBuyingFirst")
public class PanicBuyingFirst extends BaseEntity<Integer>{//超值抢购

    @Column(columnDefinition = "varchar(200) COMMENT '栏目名称'")
    private String name;//栏目名称
    @Column(columnDefinition = "varchar(200) COMMENT '标签'")
    private String title;//标签
    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer acticityId;
    @Column(columnDefinition = "datetime COMMENT '开始时间'")
    private Date startTime;//开始时间
    @Column(columnDefinition = "datetime COMMENT '结束时间'")
    private Date endTime;

    public Integer getActicityId() {
        return acticityId;
    }

    public void setActicityId(Integer acticityId) {
        this.acticityId = acticityId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
