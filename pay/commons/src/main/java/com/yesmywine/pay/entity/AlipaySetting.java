package com.yesmywine.pay.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.util.enums.Active;

import javax.persistence.*;

/**
 * Created by SJQ on 2017/2/23.
 */
@Entity
@Table(name = "alipaySetting")
public class AlipaySetting extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(50) COMMENT '名称'")
    private String name;  //名称
    @Column(columnDefinition = "varchar(50) COMMENT '编码'")
    private String code;  //编码
    @Column(columnDefinition = "varchar(50) COMMENT 'pid,合作身份者ID'")
    private String partner;  //pid,合作身份者ID
    @Column(columnDefinition = "varchar(200) COMMENT '收款支付宝账号，一般情况下收款账号就是签约账号'")
    private String sellerEmail; //收款支付宝账号，一般情况下收款账号就是签约账号
    @Column(columnDefinition = "varchar(200) COMMENT '订单说明'")
    private String body;    //订单说明
    @Column(columnDefinition = "varchar(255) COMMENT '商户的私钥'")
    private String MD5Key; //商户的私钥
    @Column(columnDefinition = "varchar(200) COMMENT '签名方式'")
    private String sign_type;//签名方式
    @Column(columnDefinition = "varchar(50) COMMENT '编码格式'")
    private String input_charset;//编码格式
    @Column(columnDefinition = "varchar(50) COMMENT '服务器异步通知页面路径'")
    private String notifyUrl;   //服务器异步通知页面路径
    @Column(columnDefinition = "varchar(50) COMMENT '退款回掉地址'")
    private String refundBackUrl;   //退款回掉地址
    @Column(columnDefinition = "varchar(50) COMMENT '页面跳转同步通知页面路径'")
    private String returnUrl;  //页面跳转同步通知页面路径
    @Transient
    @Enumerated(EnumType.STRING)
    private Active active;

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

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMD5Key() {
        return MD5Key;
    }

    public void setMD5Key(String MD5Key) {
        this.MD5Key = MD5Key;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getRefundBackUrl() {
        return refundBackUrl;
    }

    public void setRefundBackUrl(String refundBackUrl) {
        this.refundBackUrl = refundBackUrl;
    }

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }
}
