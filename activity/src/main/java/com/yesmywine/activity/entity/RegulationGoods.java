package com.yesmywine.activity.entity;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.GoodsTypeEnum;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by wangdiandian on 2017/1/6.
 */
@Entity
@Table(name = "regulationGoods")
public class RegulationGoods extends BaseEntity<Integer> {
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int(2) COMMENT '活动类别 0-商品 1-分类 2-品牌'")
    private GoodsTypeEnum type;//1:goods 2:goods category 3:goods brand
    @Column(columnDefinition = "int(11) COMMENT '目标id'")
    private Integer targetId;//goods Id or category id or brand id
    @Column(columnDefinition = "int(11) COMMENT '规则id'")
    private Integer regulationId;
    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer activityId;
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "int(2) COMMENT '商品类型 0-主商品 1-赠品 2-优惠券'")
    private WareEnum ware;//1:促销商品 2:赠品
    @Enumerated(EnumType.ORDINAL)
    @Column(columnDefinition = "varchar(10) COMMENT '是否删除'")
    private DeleteEnum isDelete;//删除
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10) COMMENT '商品原价'")
    private ActivityStatus status;//状态
    @Transient
    private Boolean isShare;
    @Column(columnDefinition = "double COMMENT '商品原价'")
    private Double originPrice;//商品原价
    @Column(columnDefinition = "double COMMENT '商品活动价'")
    private Double activityPrice;//商品活动价
    @Column(columnDefinition = "int(11) COMMENT '总共限制销售数量'")
    private Integer allLimitCount;//总共限制销售数量
    @Column(columnDefinition = "int(11) COMMENT '个人限制购买量'")
    private Integer personalLimitCount;//个人限制购买量

    @Transient
    private String targetName;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public GoodsTypeEnum getType() {
        return type;
    }

    public void setType(GoodsTypeEnum type) {
        this.type = type;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Integer getRegulationId() {
        return regulationId;
    }

    public void setRegulationId(Integer regulationId) {
        this.regulationId = regulationId;
    }

    public WareEnum getWare() {
        return ware;
    }

    public void setWare(WareEnum ware) {
        this.ware = ware;
    }

    public Boolean getIsShare() {
        return isShare;
    }

    public void setIsShare(Boolean share) {
        isShare = share;
    }

    public Double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Double originPrice) {
        this.originPrice = originPrice;
    }

    public Double getActivityPrice() {
        return activityPrice;
    }

    public void setActivityPrice(Double activityPrice) {
        this.activityPrice = activityPrice;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public RegulationGoods cloneOneSelf() {
        try {
            return clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected RegulationGoods clone() throws CloneNotSupportedException {
        return (RegulationGoods) super.clone();
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public Boolean getShare() {
        return isShare;
    }

    public void setShare(Boolean share) {
        isShare = share;
    }

    public Integer getAllLimitCount() {
        return allLimitCount;
    }

    public void setAllLimitCount(Integer allLimitCount) {
        this.allLimitCount = allLimitCount;
    }

    public Integer getPersonalLimitCount() {
        return personalLimitCount;
    }

    public void setPersonalLimitCount(Integer personalLimitCount) {
        this.personalLimitCount = personalLimitCount;
    }
}
