package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.orders.bean.Payment;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderPayinfo")
public class OrderPayinfo extends BaseEntity<Long> {//订单付款信息
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Enumerated(EnumType.STRING)
    private Payment payType;//付款方式
    @Column(columnDefinition = "double COMMENT '商品总额'")
    private Double totalGoodsAmount;//商品总额
    @Column(columnDefinition = "double COMMENT '商品实付总额'")
    private Double amount;//商品实付总额
    @Column(columnDefinition = "double COMMENT '运费'")
    private Double freight;//运费
    @Column(columnDefinition = "double COMMENT '优惠运费'")
    private Double preferentialFreight;//优惠运费
    @Column(columnDefinition = "double COMMENT '调拨费用'")
    private Double transfersAmount;//调拨费用
    @Column(columnDefinition = "int(11) COMMENT '优惠券id'")
    private Integer couponId;//优惠券id
    @Column(columnDefinition = "double COMMENT '优惠券金额'")
    private Double couponAmount;//优惠券金额
    @Column(columnDefinition = "BIGINT(20) COMMENT '礼品卡号'")
    private Long cardNumber;//礼品卡号
    @Column(columnDefinition = "double COMMENT '礼品卡金额'")
    private Double giftCardAmount;//礼品卡金额
    @Column(columnDefinition = "double COMMENT '酒豆抵扣额'")
    private Double beanAmount;//酒豆抵扣额
    @Column(columnDefinition = "double COMMENT '余额'")
    private Double balance;//余额
    @Column(columnDefinition = "DATETIME COMMENT '付款时间'")
    private Date paymentTime;//付款时间
    @Column(columnDefinition = "double COMMENT '会员优惠金额'")
    private Double preferentialAmount;//会员优惠金额
    @Column(columnDefinition = "double COMMENT '差额'")
    private Double difference;//差额
    @Column(columnDefinition = "double COMMENT '应付'")
    private Double copeWith;//应付
    @Column(columnDefinition = "int(5) COMMENT '发货后送的积分'")
    private Integer integral;//发货后送的积分
    @Column(columnDefinition = "double COMMENT '活动优惠多少金额'")
    private Double activityAmount;//活动优惠多少金额

    public Double getActivityAmount() {
        return activityAmount;
    }

    public void setActivityAmount(Double activityAmount) {
        this.activityAmount = activityAmount;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDifference() {
        return difference;
    }

    public void setDifference(Double difference) {
        this.difference = difference;
    }

    public Double getTransfersAmount() {
        return transfersAmount;
    }

    public void setTransfersAmount(Double transfersAmount) {
        this.transfersAmount = transfersAmount;
    }

    public Double getPreferentialAmount() {
        return preferentialAmount;
    }

    public void setPreferentialAmount(Double preferentialAmount) {
        this.preferentialAmount = preferentialAmount;
    }

    public Double getPreferentialFreight() {
        return preferentialFreight;
    }

    public void setPreferentialFreight(Double preferentialFreight) {
        this.preferentialFreight = preferentialFreight;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Payment getPayType() {
        return payType;
    }

    public void setPayType(Payment payType) {
        this.payType = payType;
    }

    public Double getTotalGoodsAmount() {
        return totalGoodsAmount;
    }

    public void setTotalGoodsAmount(Double totalGoodsAmount) {
        this.totalGoodsAmount = totalGoodsAmount;
    }

    public Double getFreight() {
        return freight;
    }

    public void setFreight(Double freight) {
        this.freight = freight;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public Double getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Double couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getGiftCardAmount() {
        return giftCardAmount;
    }

    public void setGiftCardAmount(Double giftCardAmount) {
        this.giftCardAmount = giftCardAmount;
    }

    public Double getBeanAmount() {
        return beanAmount;
    }

    public void setBeanAmount(Double beanAmount) {
        this.beanAmount = beanAmount;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Double getCopeWith() {
        return copeWith;
    }

    public void setCopeWith(Double copeWith) {
        this.copeWith = copeWith;
    }

}