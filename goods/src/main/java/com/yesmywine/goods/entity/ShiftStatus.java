//package com.yesmywine.goods.entity;
//
//import com.yesmywine.goods.bean.DeleteEnum;
//import com.yesmywine.goods.bean.GoodsStatus;
//
//import javax.persistence.*;
//import java.util.Date;
//
///**
// * Created by WANG, RUIQING on 12/7/16
// * Twitter : @taylorwang789
// * E-mail : i@wrqzn.com
// */
//@Entity
//@Table(name = "shiftstatus")
//public class ShiftStatus {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    @Column(columnDefinition = "DATETIME COMMENT '标签'")
//    private Date shiftTime;             //改变时间
//    @Column(columnDefinition = "int(11) COMMENT 'goodsId'")
//    private Integer goodsId;
//    @Enumerated(EnumType.STRING)
//    private GoodsStatus targetStatus;//改成什么状态
//    @Column(columnDefinition = "DATETIME COMMENT '结束时间'")
//    private Date endTime;
//    @Enumerated(EnumType.ORDINAL)
//    private DeleteEnum deleteEnum;
//
//    public Date getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Date endTime) {
//        this.endTime = endTime;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Date getShiftTime() {
//        return shiftTime;
//    }
//
//    public void setShiftTime(Date shiftTime) {
//        this.shiftTime = shiftTime;
//    }
//
//    public Integer getGoodsId() {
//        return goodsId;
//    }
//
//    public void setGoodsId(Integer goodsId) {
//        this.goodsId = goodsId;
//    }
//
//    public GoodsStatus getTargetStatus() {
//        return targetStatus;
//    }
//
//    public void setTargetStatus(GoodsStatus targetStatus) {
//        this.targetStatus = targetStatus;
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
