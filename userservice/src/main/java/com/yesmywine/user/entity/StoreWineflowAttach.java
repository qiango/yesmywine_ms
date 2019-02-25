package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/8/11.
 */
@Entity
@Table(name = "storeWineflowAttach")
public class StoreWineflowAttach  extends BaseEntity<Integer>{

    @Column(columnDefinition = "int(11) COMMENT '商品Id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(100) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = "varchar(255) COMMENT '图片地址'")
    private  String goodsImageUrl;//图片地址
    @Column(columnDefinition = "double COMMENT '买入单价'")
    private String perPrice;//买入价格
    @Column(columnDefinition = "varchar(50) COMMENT '订单号'")
    private  String orderNumber;
    @Column(columnDefinition = "varchar(50) COMMENT '存酒费用'")
    private  String fee;//费用
    @Column(columnDefinition = "varchar(50) COMMENT '提酒订单'")
    private  String extractorderNumber;//提酒订单号
    @Column(columnDefinition = "int(10) COMMENT '提酒数量'")
    private  Integer amount;//提酒数量


    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }


    public String getExtractorderNumber() {
        return extractorderNumber;
    }

    public void setExtractorderNumber(String extractorderNumber) {
        this.extractorderNumber = extractorderNumber;
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

    public String getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(String perPrice) {
        this.perPrice = perPrice;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
