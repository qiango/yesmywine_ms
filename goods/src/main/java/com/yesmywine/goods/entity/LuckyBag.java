//package com.yesmywine.goods.entity;
//
//
//import com.yesmywine.base.record.entity.BaseEntity;
//import com.yesmywine.goods.bean.DeleteEnum;
//import com.yesmywine.goods.bean.GoodsStatus;
//
//import javax.persistence.*;
//
///**
// * Created by WANG, RUIQING on 12/7/16
// * Twitter : @taylorwang789
// * E-mail : i@wrqzn.com
// */
//@Entity
//@Table(name = "luckyBag")
//public class LuckyBag extends BaseEntity<Long> {
//    @Column(columnDefinition = "varchar(20) COMMENT '福袋名称'")
//    private String luckBagName;
//    @Column(columnDefinition = "int(11) COMMENT '数量'")
//    private Integer amount;//数量
//    @Column(columnDefinition = "int(11) COMMENT '已卖数量'")
//    private Integer soldAmount;//已卖数量
//    @Column(columnDefinition = "double COMMENT '价格'")
//    private Double price;//价格
//    @Column(columnDefinition = "varchar(20) COMMENT '福袋简介'")
//    private String luckyBagSynopsis;//福袋简介
//    @Column(columnDefinition = "varchar(20) COMMENT '福袋详情'")
//    private String luckyBagDetails;//福袋详情
//    @Enumerated(EnumType.ORDINAL)
//    private GoodsStatus status;
//    @Enumerated(EnumType.ORDINAL)
//    private DeleteEnum deleteEnum;
//
//    public String getLuckBagName() {
//        return luckBagName;
//    }
//
//    public void setLuckBagName(String luckBagName) {
//        this.luckBagName = luckBagName;
//    }
//
//    public Integer getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Integer amount) {
//        this.amount = amount;
//    }
//
//    public Integer getSoldAmount() {
//        return soldAmount;
//    }
//
//    public void setSoldAmount(Integer soldAmount) {
//        this.soldAmount = soldAmount;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public void setPrice(Double price) {
//        this.price = price;
//    }
//
//    public String getLuckyBagSynopsis() {
//        return luckyBagSynopsis;
//    }
//
//    public void setLuckyBagSynopsis(String luckyBagSynopsis) {
//        this.luckyBagSynopsis = luckyBagSynopsis;
//    }
//
//    public String getLuckyBagDetails() {
//        return luckyBagDetails;
//    }
//
//    public void setLuckyBagDetails(String luckyBagDetails) {
//        this.luckyBagDetails = luckyBagDetails;
//    }
//
//    public GoodsStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(GoodsStatus status) {
//        this.status = status;
//    }
//
//    public DeleteEnum getDeleteEnum() {
//        return deleteEnum;
//    }
//
//    public void setDeleteEnum(DeleteEnum deleteEnum) {
//        this.deleteEnum = deleteEnum;
//    }
//}
