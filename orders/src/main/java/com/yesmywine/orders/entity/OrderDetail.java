package com.yesmywine.orders.entity;

import com.yesmywine.base.record.entity.BaseEntity;
import com.yesmywine.orders.bean.WhetherEnum;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdiandian on 2016/12/19.
 */
@Entity
@Table(name = "orderDetail")
public class OrderDetail extends BaseEntity<Long> {//订单明细表
    @Column(columnDefinition = "int(11) COMMENT '活动规则id'")
    private Integer activityRuleId;
    @Column(columnDefinition = "BIGINT(20) COMMENT '订单编码'")
    private Long orderNo;//订单编码
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(50)COMMENT '渠道编码'")
    private String channelCode;//渠道编码
    @Column(columnDefinition = "varchar(50) COMMENT '商品名称'")
    private String goodsName;//商品名称
    @Column(columnDefinition = "varchar(50) COMMENT '商品编码'")
    private String goodsCode;//商品编码
    @Column(columnDefinition = "int(11) COMMENT '数量'")
    private Integer count;//数量
    @Column(columnDefinition = "double COMMENT '价格'")
    private Double goodsPrice;//价格
//    private Double newPrice;//现价
    @Enumerated(EnumType.STRING)
    private WhetherEnum tradeIn;//是否换购
    @Enumerated(EnumType.STRING)
    private WhetherEnum gift;//是否是赠品
    @Column(columnDefinition = "int(5) COMMENT '退酒数量'")
    private Integer returnWineCount;//退酒数量
    @Column(columnDefinition = "int(5) COMMENT '提酒数量'")
    private Integer mentionWineCount;//提酒数量
    @Column(columnDefinition = "int(10) COMMENT '存酒库id'")
    private Integer keepwineId;//存酒库id
    @Column(columnDefinition = "BIGINT(20) COMMENT '存酒订单编码'")
    private Long wineStoreOrderNo;//存酒订单编码
    @Column(columnDefinition = "double COMMENT '存酒费用'")
    private Double wineStoreMoney;//存酒费用
    @Enumerated(EnumType.STRING)
    private WhetherEnum comment;//是否评论
    @Column(columnDefinition = "varchar(255) COMMENT '商品图片'")
    private String reasonImg;//图片
    @Column(columnDefinition = "varchar(15) COMMENT '销售模式 销售模式 １为预售，０为普通商品,2抢购'")
    private String saleModel;
    @Column(columnDefinition = "varchar(20) COMMENT '虚拟商品类型,giftCard：礼品卡类型'")
    private String virtualType ;//虚拟商品类型,giftCard：礼品卡类型


    public WhetherEnum getTradeIn() {
        return tradeIn;
    }

    public void setTradeIn(WhetherEnum tradeIn) {
        this.tradeIn = tradeIn;
    }

    public String getVirtualType() {
        return virtualType;
    }

    public void setVirtualType(String virtualType) {
        this.virtualType = virtualType;
    }

    public Integer getActivityRuleId() {
        return activityRuleId;
    }

    public void setActivityRuleId(Integer activityRuleId) {
        this.activityRuleId = activityRuleId;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="orderNo",referencedColumnName = "orderNo",insertable = false,updatable = false)
    private List<OrderDetailSku> orderDetailSkuList = new ArrayList<>();//订单明细表

    public String getReasonImg() {
        return reasonImg;
    }

    public void setReasonImg(String reasonImg) {
        this.reasonImg = reasonImg;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

//    public Double getNewPrice() {
//        return newPrice;
//    }
//
//    public void setNewPrice(Double newPrice) {
//        this.newPrice = newPrice;
//    }

    public String getSaleModel() {
        return saleModel;
    }

    public void setSaleModel(String saleModel) {
        this.saleModel = saleModel;
    }

    public WhetherEnum getComment() {
        return comment;
    }

    public void setComment(WhetherEnum comment) {
        this.comment = comment;
    }

    public Double getWineStoreMoney() {
        return wineStoreMoney;
    }

    public void setWineStoreMoney(Double wineStoreMoney) {
        this.wineStoreMoney = wineStoreMoney;
    }

    public Long getWineStoreOrderNo() {
        return wineStoreOrderNo;
    }

    public void setWineStoreOrderNo(Long wineStoreOrderNo) {
        this.wineStoreOrderNo = wineStoreOrderNo;
    }

    public Integer getReturnWineCount() {
        return returnWineCount;
    }

    public void setReturnWineCount(Integer returnWineCount) {
        this.returnWineCount = returnWineCount;
    }

    public Integer getMentionWineCount() {
        return mentionWineCount;
    }

    public void setMentionWineCount(Integer mentionWineCount) {
        this.mentionWineCount = mentionWineCount;
    }

    public Integer getKeepwineId() {
        return keepwineId;
    }

    public void setKeepwineId(Integer keepwineId) {
        this.keepwineId = keepwineId;
    }

    public List<OrderDetailSku> getOrderDetailSkuList() {
        return orderDetailSkuList;
    }

    public void setOrderDetailSkuList(List<OrderDetailSku> orderDetailSkuList) {
        this.orderDetailSkuList = orderDetailSkuList;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public WhetherEnum getGift() {
        return gift;
    }

    public void setGift(WhetherEnum gift) {
        this.gift = gift;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


}
