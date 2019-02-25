package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sjq on 2016/12/22.
 * 订单生成后的礼品卡详情表
 */
@Entity
@Table(name = "giftCard")
public class GiftCard extends BaseEntity<Long> {
    @Column(columnDefinition = "varchar(255) COMMENT '礼品卡名称'")
    private String cardName;//礼品卡名称
    @Column(columnDefinition = "int(2) COMMENT '礼品卡类型（0,电子/1,实体）'")
    private Integer type;//礼品卡类型（0,电子/1,实体）
    @Column(columnDefinition = "varchar(255) COMMENT '批次编号'")
    private String batchNumber;//批次编号
    @Column(columnDefinition = "bigint(20) COMMENT '卡号'")
    private Long cardNumber;//卡号
    @Column(columnDefinition = "varchar(50) COMMENT '密码'")
    private String password;//密码
    @Column(columnDefinition = "DOUBLE COMMENT '礼品卡面值'")
    private Double amounts;//礼品卡面值
    @Column(columnDefinition = " DOUBLE COMMENT '礼品卡余额'")
    private Double remainingSum;//礼品卡余额
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date latestTime;//最迟激活时间;
    @Column(columnDefinition = "int(11) COMMENT '有效期（单位：天）'")
    private Integer inDate;//有效期（单位：天）
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualMaturity;//实际到期时间
    @Column(columnDefinition = "Bigint(20) COMMENT '生成记录id'")
    private Long giftCardRecordId;//生成记录id
    @Column(columnDefinition = "int(11) COMMENT '活动id'")
    private Integer activityId;//活动id
    @Column(columnDefinition = "int(11) COMMENT '激活状态（0待激活/1已激活）'")
    private Integer status;//激活状态（0待激活/1已激活）
    @Column(columnDefinition = "DATETIME COMMENT '激活时间'")
    private Date activationTime;//激活时间
    @Column(columnDefinition = "int(11) COMMENT '绑定状态（0未绑定/1已绑定）'")
    private Integer boundStatus;//绑定状态（0未绑定/1已绑定）
    @Column(columnDefinition = "DATETIME COMMENT '绑定时间'")
    private Date boundTime;//绑定时间
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;//用户id
    @Column(columnDefinition = "varchar(50) COMMENT '用户名'")
    private String userName;//用户名
    @Column(columnDefinition = "int(11) COMMENT '是否购买(0否/1是)'")
    private Integer ifBuy;//是否购买(0否/1是)
    @Column(columnDefinition = "int(11) COMMENT 'skuId'")
    private Integer skuId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getIfBuy() {
        return ifBuy;
    }

    public void setIfBuy(Integer ifBuy) {
        this.ifBuy = ifBuy;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAmounts() {
        return amounts;
    }

    public void setAmounts(Double amounts) {
        this.amounts = amounts;
    }

    public Double getRemainingSum() {
        return remainingSum;
    }

    public void setRemainingSum(Double remainingSum) {
        this.remainingSum = remainingSum;
    }

    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    public Integer getInDate() {
        return inDate;
    }

    public void setInDate(Integer inDate) {
        this.inDate = inDate;
    }

    public Date getActualMaturity() {
        return actualMaturity;
    }

    public void setActualMaturity(Date actualMaturity) {
        this.actualMaturity = actualMaturity;
    }

    public Long getGiftCardRecordId() {
        return giftCardRecordId;
    }

    public void setGiftCardRecordId(Long giftCardRecordId) {
        this.giftCardRecordId = giftCardRecordId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }

    public Integer getBoundStatus() {
        return boundStatus;
    }

    public void setBoundStatus(Integer boundStatus) {
        this.boundStatus = boundStatus;
    }

    public Date getBoundTime() {
        return boundTime;
    }

    public void setBoundTime(Date boundTime) {
        this.boundTime = boundTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
