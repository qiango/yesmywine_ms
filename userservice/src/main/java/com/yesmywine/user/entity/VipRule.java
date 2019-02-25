package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by ${shuang} on 2017/4/10.
 */
@Entity
@Table(name = "vipRule")
public class VipRule  extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(10) COMMENT '等级id'")
    private Integer level;
    @Column(columnDefinition = "varchar(50) COMMENT '等级名称'")
    private String vipName;
    @Column(columnDefinition = "int(11) COMMENT '要求积分'")
    private Integer requireValue;
    @Column(columnDefinition = "int(11) COMMENT '保级要求积分'")
    private Integer keep;
    @Column(columnDefinition = "varchar(100) COMMENT '等级图片'")
    private String url;//图片url
    @Column(columnDefinition = "int(5) COMMENT '免费存酒天数'")
    private Integer keepDays;//免费存酒天数
    @Column(columnDefinition = "double COMMENT '折扣'")
    private Double discount;//折扣



    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public Integer getRequireValue() {
        return requireValue;
    }

    public void setRequireValue(Integer requireValue) {
        this.requireValue = requireValue;
    }

    public Integer getKeep() {
        return keep;
    }

    public void setKeep(Integer keep) {
        this.keep = keep;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getKeepDays() {
        return keepDays;
    }

    public void setKeepDays(Integer keepDays) {
        this.keepDays = keepDays;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
