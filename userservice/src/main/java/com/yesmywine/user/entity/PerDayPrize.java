package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/6/15.
 */
@Entity
@Table(name = "perDayPrize")
public class PerDayPrize extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(1) COMMENT '价格Id'")
    private Integer prizeId;
    @Column(columnDefinition = "DOUBLE COMMENT '存酒库价格'")
    private double prize;

    public  PerDayPrize(){
        this.prizeId=1;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public double getPrize() {
        return prize;
    }
    public void setPrize(double prize) {
        this.prize = prize;
    }
}
