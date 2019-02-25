//package com.yesmywine.goods.entity;
//
//import com.yesmywine.base.record.entity.BaseEntity;
//import com.yesmywine.goods.bean.DeleteEnum;
//
//import javax.persistence.*;
//
///**
// * Created by wangdiandian on 2017/2/13.
// */
//@Entity
//@Table(name = "luckyBagGoods")
//public class LuckyBagGoods extends BaseEntity<Long> {
//    @Column(columnDefinition = "Long COMMENT '福袋id'")
//    private Long luckyBagId;
//    @Column(columnDefinition = "int(11) COMMENT 'skuId'")
//    private Integer skuId;
//    @Enumerated(EnumType.ORDINAL)
//    private DeleteEnum deleteEnum;
//
//    public DeleteEnum getDeleteEnum() {
//        return deleteEnum;
//    }
//
//    public void setDeleteEnum(DeleteEnum deleteEnum) {
//        this.deleteEnum = deleteEnum;
//    }
//
//    public Long getLuckyBagId() {
//        return luckyBagId;
//    }
//
//    public void setLuckyBagId(Long luckyBagId) {
//        this.luckyBagId = luckyBagId;
//    }
//
//    public Integer getSkuId() {
//        return skuId;
//    }
//
//    public void setSkuId(Integer skuId) {
//        this.skuId = skuId;
//    }
//
//}
