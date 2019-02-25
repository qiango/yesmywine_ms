package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderReturnExchange")
public class OrderReturnExchange extends BaseEntity<Integer> {//订单退换货
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Column(columnDefinition = "varchar(150) COMMENT '退货单号'")
    private String returnNo;//退货单号

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "returnNo",referencedColumnName = "returnNo")
//    private Set<OrderReturnDetail> returnGoodsDetail = new HashSet<>();
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodId;
    @Column(columnDefinition = "varchar(150) COMMENT '商品名称'")
    private String goodsName;
//    private Double costPrice;//成本价
    @Column(columnDefinition = "double COMMENT '销售价格'")
    private Double salePrice;//销售价格
    @Column(columnDefinition = "int(11) COMMENT '申请数量'")
    private Integer reNum;// 申请数量
    @Column(columnDefinition = "DATETIME COMMENT '上门取件时间'")
    private Date pickupTime;//上门取件时间
    @Column(columnDefinition = "int(11) COMMENT '1.退货/2.换货'")
    private Integer type;// /1.退货/2.换货
    @Column(columnDefinition = "int(11) COMMENT '渠道id'")
    private Integer channelId;//渠道id
    @Column(columnDefinition = "varchar(200) COMMENT '退货原因'")
    private String returnReason;//退货原因
    @Column(columnDefinition = "varchar(200) COMMENT '问题描述'")
    private String reasonDesc;//问题描述
    @Column(columnDefinition = "varchar(150) COMMENT '图片'")
    private String reasonImg;//退货上传图片
    @Column(columnDefinition = "varchar(255) COMMENT '图片'")
    private String goodsReasonImg;//商品图片
    @Column(columnDefinition = "int(1) COMMENT '返回方式：/0.上门取货/1.送至门店/2.快递至公司'")
    private Integer rebackType;// 返回方式：/0.上门取货/1.送至门店/2.快递至公司
    @Column(columnDefinition = "int(10) COMMENT '收货地址Id'")
    private Integer areaId;//收货地址Id
    @Column(columnDefinition = "varchar(150) COMMENT '收货地址'")
    private String receiveAddress;//收货地址
    @Column(columnDefinition = "varchar(50) COMMENT '客户姓名'")
    private String customerName;// 客户姓名
    @Column(columnDefinition = "varchar(50) COMMENT '客户联系电话'")
    private String customerPhone;// 客户联系电话
    @Column(columnDefinition = "varchar(50) COMMENT '用户Id'")
    private String userId;//用户Id
    @Column(columnDefinition = "int(1) COMMENT '当前状态（0.待审核/1.已完成/2.审核中/3.审核未通过/4.取消）'")
    private Integer status;//当前状态(0.待审核/1.已完成/2.审核中/3.审核未通过/4.取消）
    @Column(columnDefinition = "varchar(50) COMMENT '审核驳回原因'")
    private String rejectReason;//审核驳回原因
    @Column(columnDefinition = "DATETIME COMMENT '退换货完成时间'")
    private Date dealTime;//退换货完成时间
    @Column(columnDefinition = "double COMMENT '退回的金额（退回用户余额）'")
    private Double returnPayAmount;//退回的金额（退回用户余额）
    @Column(columnDefinition = "double COMMENT '支付渠道退还'")
    private Double returnPayWay;//支付渠道退还
    @Column(columnDefinition = "double COMMENT '退回的豆子'")
    private Double returnBeanAmount;// 退回的豆子
    @Column(columnDefinition = "double COMMENT '退还礼品卡金额'")
    private Double returnGiftCardAmount;//退还礼品卡金额
    @Column(columnDefinition = "int(10) COMMENT '退积分'")
    private Integer returnIntegral;//退积分
    @Column(columnDefinition = "varchar(50) COMMENT '审核人'")
    private String auditor;//审核人
    @Column(columnDefinition = "BIT(1) COMMENT '是否是质量问题'")
    private Boolean isQualityProblem ;//是否是质量问题
    private Double keepWineFee;//存酒费



    ////////////////////////////////////
    /**
     * 添加
     */
//    public void addReturnGoods(OrderReturnDetail detail) {
//        Set<OrderReturnDetail> detailSet = getReturnGoodsDetail();
//        if (detailSet == null) {
//            detailSet = new HashSet<OrderReturnDetail>();
//            setReturnGoodsDetail(detailSet);
//        }
//        detailSet.add(detail);
//    }

//    public Set<OrderReturnDetail> getReturnGoodsDetail() {
//        return returnGoodsDetail;
//    }
//
//    public void setReturnGoodsDetail(Set<OrderReturnDetail> returnGoodsDetail) {
//        this.returnGoodsDetail = returnGoodsDetail;
//    }


    public Double getKeepWineFee() {
        return keepWineFee;
    }public void setKeepWineFee(Double keepWineFee) {
        this.keepWineFee = keepWineFee;
    }public String getGoodsReasonImg() {
        return goodsReasonImg;
    }public void setGoodsReasonImg(String goodsReasonImg) {
        this.goodsReasonImg = goodsReasonImg;
    }public String getRejectReason() {
        return rejectReason;
    }public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }public String getAuditor() {
        return auditor;
    }public void setAuditor(String auditor) {
        this.auditor = auditor;
    }public Integer getReturnIntegral() {
        return returnIntegral;
    }public void setReturnIntegral(Integer returnIntegral) {
        this.returnIntegral = returnIntegral;
    }public Double getReturnPayWay() {
        return returnPayWay;
    }public void setReturnPayWay(Double returnPayWay) {
        this.returnPayWay = returnPayWay;
    }public Double getReturnGiftCardAmount() {
        return returnGiftCardAmount;
    }public void setReturnGiftCardAmount(Double returnGiftCardAmount) {
        this.returnGiftCardAmount = returnGiftCardAmount;
    }public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

//    public Set<OrderReturnDetail> getReturnGoodsDetail() {
//        return returnGoodsDetail;
//    }
//
//    public void setReturnGoodsDetail(Set<OrderReturnDetail> returnGoodsDetail) {
//        this.returnGoodsDetail = returnGoodsDetail;
//    }

    public Date getDealTime() {
        return dealTime;
    }

    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public Double getCostPrice() {
//        return costPrice;
//    }
//
//    public void setCostPrice(Double costPrice) {
//        this.costPrice = costPrice;
//    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getReNum() {
        return reNum;
    }

    public void setReNum(Integer reNum) {
        this.reNum = reNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getReasonImg() {
        return reasonImg;
    }

    public void setReasonImg(String reasonImg) {
        this.reasonImg = reasonImg;
    }

    public Integer getRebackType() {
        return rebackType;
    }

    public void setRebackType(Integer rebackType) {
        this.rebackType = rebackType;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getReturnPayAmount() {
        return returnPayAmount;
    }

    public void setReturnPayAmount(Double returnPayAmount) {
        this.returnPayAmount = returnPayAmount;
    }

    public Double getReturnBeanAmount() {
        return returnBeanAmount;
    }

    public void setReturnBeanAmount(Double returnBeanAmount) {
        this.returnBeanAmount = returnBeanAmount;
    }

    public Boolean getQualityProblem() {
        return isQualityProblem;
    }

    public void setQualityProblem(Boolean qualityProblem) {
        isQualityProblem = qualityProblem;
    }
}
