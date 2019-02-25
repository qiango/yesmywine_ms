package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2017/8/25.
 */
@Entity
@Table(name = "orderGiftCard")
public class OrderGiftCard  extends BaseEntity<Integer> {//订单礼品卡
    @Column(columnDefinition = "bigint(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Column(columnDefinition = "bigint(20) COMMENT '卡号'")
    private Long cardNumber;//卡号
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;//商品id

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}
