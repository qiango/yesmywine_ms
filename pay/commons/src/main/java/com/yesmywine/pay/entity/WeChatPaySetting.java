package com.yesmywine.pay.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.util.enums.Active;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/2/23.
 */
@Entity
@Table(name = "wechatSetting")
public class WeChatPaySetting extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT '名称'")
    private String name;  //名称
    @Column(columnDefinition = "varchar(50) COMMENT '编码'")
    private String code;  //编码
    @Column(columnDefinition = "varchar(50) COMMENT '公众号'")
    private String appId;   //公众号
    @Column(columnDefinition = "varchar(50) COMMENT '商户号'")
    private String mchId;   //商户号
    @Column(columnDefinition = "varchar(50) COMMENT 'api密钥'")
    private String apiKey; //api密钥
    @Column(columnDefinition = "varchar(50) COMMENT '微信回掉地址'")
    private String notifyUrl; //微信回掉地址
    @Column(columnDefinition = "varchar(50) COMMENT '微信退款回掉地址'")
    private String refundUrl; //微信退款回掉地址
    @Column(columnDefinition = "varchar(50) COMMENT '支付说明'")
    private String body;//支付说明

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getRefundUrl() {
        return refundUrl;
    }

    public void setRefundUrl(String refundUrl) {
        this.refundUrl = refundUrl;
    }
}
