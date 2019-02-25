package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.orders.bean.OrderType;
import com.yesmywine.orders.bean.Payment;
import com.yesmywine.orders.bean.WhetherEnum;

import javax.persistence.*;
import java.util.*;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orders")
public class Orders extends BaseEntity<Long> {
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编号
    @Column(columnDefinition = "int(1) COMMENT '渠道（0，官方，1海淘）'")
    private Integer channel;//渠道（0，官方，1海淘）
    @Column(columnDefinition = "varchar(150) COMMENT '订单类型'")
    @Enumerated(EnumType.STRING)
    private OrderType orderType;//订单类型
    @Column(columnDefinition = "int(10) COMMENT '买家id'")
    private Integer userId;//买家id
    @Column(columnDefinition = "double COMMENT '商品总金额'")
    private Double totalGoodsAmount;//商品总金额
    @Column(columnDefinition = "double COMMENT '商品实付总额'")
    private Double amount;//商品实付总额
    @Column(columnDefinition = "double COMMENT '应付'")
    private Double copeWith;//应付
    @Column(columnDefinition = "int(10) COMMENT '商品总数量'")
    private Integer totalNum;//商品总数量
    @Column(columnDefinition = "double COMMENT '存酒费用(只提酒订单需要)'")
    private Double wineStoreMoney;//存酒费用(只提酒订单需要)
    @Enumerated(EnumType.STRING)
    private Payment paymentType;//支付方式
    @Column(columnDefinition = "DATETIME COMMENT '订单确认时间'")
    private Date confirmTime;//订单确认时间
    @Column(columnDefinition = "DATETIME COMMENT '订单支付时间'")
    private Date payTime;//订单支付时间
    @Column(columnDefinition = "DATETIME COMMENT '订单发货时间'")
    private Date delieverTime;//订单发货时间
    @Column(columnDefinition = "DATETIME COMMENT '订单签收时间'")
    private Date receiveTime;//订单签收时间
    @Column(columnDefinition = "DATETIME COMMENT '订单取消时间'")
    private Date cancelTime;//订单取消时间
    @Column(columnDefinition = "int(1) COMMENT '订单状态(0完成，1待付款，2已取消，3待发货，4（支付未发货的取消）处理中，5待收货,9取消失败)'")
    private Integer status;//订单状态(0完成，1待付款，2已取消，3待发货，4（支付未发货的取消）处理中，5待收货,9取消失败)
    @Enumerated(EnumType.STRING)
    private WhetherEnum invoiceNeedFlag;//是否需要发票
    @Column(columnDefinition = "varchar(150) COMMENT '备注'")
    private String note;//备注
//    @OneToOne(cascade = CascadeType.REFRESH)
//    private OrderDeliver orderDeliver;//订单配送信息
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="orderNo",referencedColumnName = "orderNo")
    private List<OrderDetail> orderDetails = new ArrayList<>();//订单明细表
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "orderNo",referencedColumnName = "orderNo",insertable=false, updatable=false)
//    private OrderInvoice orderInvoice;//订单发票信息
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "orderNo",referencedColumnName = "orderNo",insertable = false,updatable = false)
//    private OrderPayinfo orderPayinfo;//订单付款信息
    @Enumerated(EnumType.STRING)
    private WhetherEnum comment;//是否评论
    @Column(columnDefinition = "varchar(150) COMMENT '同步状态  0-下单同步失败  1-取消订单同步失败  2-同步到商城扣减库存失败 3-同步到商城释放冻结失败'")
    private Integer synStatus ;//
    @Column(columnDefinition = "varchar(150) COMMENT '同步失败原因'")
    private String  synFailMassage ;
    @Column(columnDefinition = "varchar(50) COMMENT '收货人'")
    private String receiver;//收货人

    public String getSynFailMassage() {
        return synFailMassage;
    }

    public void setSynFailMassage(String synFailMassage) {
        this.synFailMassage = synFailMassage;
    }

    public void addOrderDetail(OrderDetail detail){
        List<OrderDetail> list = getOrderDetails();
        if(list ==null){
            new ArrayList<>();
            list.add(detail);
        }
        list.add(detail);
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Integer getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(Integer synStatus) {
        this.synStatus = synStatus;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Double getWineStoreMoney() {
        return wineStoreMoney;
    }

    public void setWineStoreMoney(Double wineStoreMoney) {
        this.wineStoreMoney = wineStoreMoney;
    }
//
//    public OrderInvoice getOrderInvoice() {
//        return orderInvoice;
//    }
//
//    public void setOrderInvoice(OrderInvoice orderInvoice) {
//        this.orderInvoice = orderInvoice;
//    }

//    public OrderDeliver getOrderDeliver() {
//        return orderDeliver;
//    }
//
//    public void setOrderDeliver(OrderDeliver orderDeliver) {
//        this.orderDeliver = orderDeliver;
//    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getTotalGoodsAmount() {
        return totalGoodsAmount;
    }

    public void setTotalGoodsAmount(Double totalGoodsAmount) {
        this.totalGoodsAmount = totalGoodsAmount;
    }

    public Double getCopeWith() {
        return copeWith;
    }

    public void setCopeWith(Double copeWith) {
        this.copeWith = copeWith;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Payment getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Payment paymentType) {
        this.paymentType = paymentType;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDelieverTime() {
        return delieverTime;
    }

    public void setDelieverTime(Date delieverTime) {
        this.delieverTime = delieverTime;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public WhetherEnum getInvoiceNeedFlag() {
        return invoiceNeedFlag;
    }

    public void setInvoiceNeedFlag(WhetherEnum invoiceNeedFlag) {
        this.invoiceNeedFlag = invoiceNeedFlag;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
//
//    public OrderPayinfo getOrderPayinfo() {
//        return orderPayinfo;
//    }
//
//    public void setOrderPayinfo(OrderPayinfo orderPayinfo) {
//        this.orderPayinfo = orderPayinfo;
//    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public WhetherEnum getComment() {
        return comment;
    }

    public void setComment(WhetherEnum comment) {
        this.comment = comment;
    }
}