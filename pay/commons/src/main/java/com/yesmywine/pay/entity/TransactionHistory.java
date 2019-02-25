package com.yesmywine.pay.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.pay.bean.Payment;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by SJQ on 2017/2/22.
 * 交易历史记录表
 */
@Entity
@Table(name = "transactionHistory")
public class TransactionHistory extends BaseEntity<Integer> {
    @Column(columnDefinition = "varchar(200) COMMENT '银行或第三方交易流水号 eg：银行流水号（queryId），支付宝或微信订单号'")
    private String serialNum; //银行或第三方交易流水号 eg：银行流水号（queryId），支付宝或微信订单号
    @Column(columnDefinition = "varchar(50) COMMENT '本平台订单号'")
    private String orderNo; //本平台订单号
    @Column(columnDefinition = "varchar(50) COMMENT '退货单号'")
    private String returnNo;//退货单号
    @Enumerated(EnumType.STRING)
    private Payment payWay;  //交易方式 unionpay wechat alipay
    @Column(columnDefinition = "double COMMENT '交易金额'")
    private Double dealSum; //交易金额
    @Column(columnDefinition = "double COMMENT '退款金额'")
    private Double refundSum; //退款金额
    @Column(columnDefinition = "DATETIME COMMENT '退款时间'")
    private Date refundTime;    //退款时间
    @Column(columnDefinition = "DATETIME COMMENT '付款时间'")
    private Date payTime; //付款时间
    @Column(columnDefinition = "varchar(50) COMMENT '描述'")
    private String description;//描述
    @Column(columnDefinition = "varchar(50) COMMENT '交易人账号'")
    private String userName;//交易人账号
    @Column(columnDefinition = "varchar(50) COMMENT '若为微信支付则为 openId'")
    private String customParam1;// 若为微信支付则为 openId
    @Column(columnDefinition = "int(11) COMMENT '1 支付订单  2 退款订单'")
    private Integer type;//1 支付订单  2 退款订单

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public Payment getPayWay() {
        return payWay;
    }

    public void setPayWay(Payment payWay) {
        this.payWay = payWay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Double getDealSum() {
        return dealSum;
    }

    public void setDealSum(Double dealSum) {
        this.dealSum = dealSum;
    }

    public Double getRefundSum() {
        return refundSum;
    }

    public void setRefundSum(Double refundSum) {
        this.refundSum = refundSum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustomParam1() {
        return customParam1;
    }

    public void setCustomParam1(String customParam1) {
        this.customParam1 = customParam1;
    }
}
