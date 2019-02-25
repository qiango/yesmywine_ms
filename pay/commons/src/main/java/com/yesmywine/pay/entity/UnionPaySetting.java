package com.yesmywine.pay.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.util.enums.Active;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/2/23.
 */
@Entity
@Table(name = "unionPaySetting")
public class UnionPaySetting extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT '名称'")
    private String name;  //名称
    @Column(columnDefinition = "varchar(50) COMMENT '编码'")
    private String code;  //编码
    @Column(columnDefinition = "varchar(50) COMMENT '商户号'")
    private String merId;   //商户号
    @Column(columnDefinition = "varchar(50) COMMENT '前台回掉地址'")
    private String frontUrl;    //前台回掉地址
    @Column(columnDefinition = "varchar(50) COMMENT '支付成功后，后台回掉地址'")
    private String payBackUrl;    //支付成功后，后台回掉地址
    @Column(columnDefinition = "varchar(50) COMMENT '退款成功后，后台毁掉地址'")
    private String refundBackUrl;   //退款成功后，后台毁掉地址
    @Transient
    @Enumerated(EnumType.STRING)
    private Active active;

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getFrontUrl() {
        return frontUrl;
    }

    public void setFrontUrl(String frontUrl) {
        this.frontUrl = frontUrl;
    }

    public String getPayBackUrl() {
        return payBackUrl;
    }

    public void setPayBackUrl(String payBackUrl) {
        this.payBackUrl = payBackUrl;
    }

    public String getRefundBackUrl() {
        return refundBackUrl;
    }

    public void setRefundBackUrl(String refundBackUrl) {
        this.refundBackUrl = refundBackUrl;
    }
}
