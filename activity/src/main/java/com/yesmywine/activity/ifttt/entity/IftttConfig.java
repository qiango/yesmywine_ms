package com.yesmywine.activity.ifttt.entity;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "iftttConfig")
public class IftttConfig extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(10) COMMENT '折扣id'")
    private Integer discountId;
    @Column(columnDefinition = "varchar(10) COMMENT '折扣名称'")
    private String dicsountName;
    @Enumerated(EnumType.STRING)
    private IftttEnum type;
    @Column(columnDefinition = "varchar(10) COMMENT '活动商品'")
    private String configKey;//活动商品
    @Column(columnDefinition = "varchar(10) COMMENT '活动条件'")
    private String configValue;//活动条件
    @Enumerated(EnumType.ORDINAL)
    private DeleteEnum isDelete;//删除
    @Enumerated(EnumType.STRING)
    private ActivityStatus status;//状态

    public DeleteEnum getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(DeleteEnum isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Integer discountId) {
        this.discountId = discountId;
    }

    public String getDicsountName() {
        return dicsountName;
    }

    public void setDicsountName(String dicsountName) {
        this.dicsountName = dicsountName;
    }

    public IftttEnum getType() {
        return type;
    }

    public void setType(IftttEnum type) {
        this.type = type;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }
}
