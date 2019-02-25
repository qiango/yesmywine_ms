package com.yesmywine.activity.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/6/9.
 */
@Entity
@Table(name = "activityGoods")
public class ActivityGoods extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '活动Id'")
    private Integer activityId;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "goodsId",referencedColumnName = "goodsId")
    private GoodsMirroring goods;
    private Integer regulationId;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public GoodsMirroring getGoods() {
        return goods;
    }

    public void setGoods(GoodsMirroring goods) {
        this.goods = goods;
    }

    public Integer getRegulationId() {
        return regulationId;
    }

    public void setRegulationId(Integer regulationId) {
        this.regulationId = regulationId;
    }
}
