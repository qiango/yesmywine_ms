package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;


import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/8/10.
 */
@Entity
@Table(name = "storeWineAttach")
public class StoreWineAttach extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '商品Id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(100) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = "varchar(255) COMMENT '图片地址'")
    private  String goodsImageUrl;//图片地址
    @Column(columnDefinition = "double COMMENT '买入单价'")
    private Double perPrice;//买入价格
    @Column(columnDefinition = "int(10) COMMENT '总数数量'")
    private  Integer total;//总数
    @Column(columnDefinition = "int(10) COMMENT '可提取数量'")
    private Integer extractable;//可提取
    @Column(columnDefinition = "int(10) COMMENT '已经提取数量'")
    private Integer AlreadyGet;//已经提取数量
    @Column(columnDefinition = "varchar(50) COMMENT '订单号'")
    private  String orderNumber;
    @Column(columnDefinition = "int(10) COMMENT '冻结数量'")
    private  Integer frozen;//冻结
    @Column(columnDefinition = "int(10) COMMENT '退货数量'")
    private  Integer returnAmount;//冻结
    @Column(columnDefinition = "varchar(50) COMMENT '存酒单价'")
    private  String univalent;//存酒单价

    public StoreWineAttach(){
        this.returnAmount=0;
    }

    public Integer getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(Integer returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getUnivalent() {
        return univalent;
    }

    public void setUnivalent(String univalent) {
        this.univalent = univalent;
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

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public Double getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(Double perPrice) {
        this.perPrice = perPrice;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getExtractable() {
        return extractable;
    }

    public void setExtractable(Integer extractable) {
        this.extractable = extractable;
    }

    public Integer getAlreadyGet() {
        return AlreadyGet;
    }

    public void setAlreadyGet(Integer alreadyGet) {
        AlreadyGet = alreadyGet;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getFrozen() {
        return frozen;
    }

    public void setFrozen(Integer frozen) {
        this.frozen = frozen;
    }
}
