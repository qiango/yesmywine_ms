package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderInvoice")
public class OrderInvoice extends BaseEntity<Long> {//订单发票信息
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Column(columnDefinition = "int(1) COMMENT '发票类型：0,普通发票/1,电子发/2,增值税发票'")
    private Integer invoiceType;//发票类型：0,普通发票/1,电子发/2,增值税发票
    @Column(columnDefinition = "varchar(50) COMMENT '发票抬头:个人/（公司内容等）'")
    private String receiptHeader;//发票抬头:个人/（公司内容等）
    @Column(columnDefinition = "varchar(150) COMMENT '发票内容'")
    private String receiptContent;//发票内容
    @Column(columnDefinition = "varchar(50) COMMENT '收票人手机'")
    private String receiverPhone;//收票人手机
    @Column(columnDefinition = "varchar(50) COMMENT '收票人邮箱'")
    private String receiverEmail;//收票人邮箱
    @Column(columnDefinition = "varchar(50) COMMENT '增值税发票信息'")
    private String alueAddedInfo;//增值税发票信息

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getReceiptHeader() {
        return receiptHeader;
    }

    public void setReceiptHeader(String receiptHeader) {
        this.receiptHeader = receiptHeader;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getAlueAddedInfo() {
        return alueAddedInfo;
    }

    public void setAlueAddedInfo(String alueAddedInfo) {
        this.alueAddedInfo = alueAddedInfo;
    }
}
