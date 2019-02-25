package com.yesmywine.goods.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sjq on 2016/12/22.
 * 礼品卡使用历史表
 */
@Entity
@Table(name = "giftCardHistory")
public class GiftCardHistory  extends BaseEntity<Long> {
    @Column(columnDefinition = "Bigint(20) COMMENT '礼品卡id'")
    private Long giftCardId;//礼品卡id
    @Column(columnDefinition = "int(11) COMMENT '0消费，1退还'")
    private Integer type;//0消费，1退还
    @Column(columnDefinition = "Bigint(20) COMMENT '卡号'")
    private Long cardNumber;//卡号
    @Column(columnDefinition = "Bigint(20) COMMENT '订单编号'")
    private Long orderNo;//订单编号
    @Column(columnDefinition = "double COMMENT '消费金额'")
    private Double usedAmount;//消费金额
    @Column(columnDefinition = "DATETIME COMMENT '消费时间'")
    private Date usedTime;//消费时间
    @Column(columnDefinition = "int(11) COMMENT '消费渠道（0官网、1门店）'")
    private Integer channel;//消费渠道（0官网、1门店）

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getGiftCardId() {
        return giftCardId;
    }

    public void setGiftCardId(Long giftCardId) {
        this.giftCardId = giftCardId;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Double getUsedAmount() {
        return usedAmount;
    }

    public void setUsedAmount(Double usedAmount) {
        this.usedAmount = usedAmount;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
