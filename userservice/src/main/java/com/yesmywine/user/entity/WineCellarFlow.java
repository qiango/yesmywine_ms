package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/6/20.
 */
@Entity
@Table(name = "wineCellarFlow")
public class WineCellarFlow extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用户id'")
    private Integer userId;
    @Column(columnDefinition = "int(11) COMMENT '商品id'")
    private Integer goodsId;
    @Column(columnDefinition = "varchar(10) COMMENT '状态：提酒,退货'")
    private String status;
    @Column(columnDefinition = "int(11) COMMENT '存酒库主键'")
    private Integer keepWineId;
    @Column(columnDefinition = "int(11) COMMENT '提酒数量'")
    private Integer counts;//已经提取数量
    @Column(columnDefinition = "double COMMENT '单价'")
    private Double perPrize;
    @Column(columnDefinition = "varchar(50) COMMENT '订单号'")
    private String orderNumber;
    @Column(columnDefinition = "int(10) COMMENT '免费存酒天数'")
    private Integer freeDays;
    @Column(columnDefinition = "varchar(50) COMMENT '商品名称'")
    private String goodsName;
    @Column(columnDefinition = "varchar(100) COMMENT '图片地址'")
    private String goodsImageUrl;//图片地址

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getFreeDays() {
        return freeDays;
    }

    public void setFreeDays(Integer freeDays) {
        this.freeDays = freeDays;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Double getPerPrize() {
        return perPrize;
    }

    public void setPerPrize(Double perPrize) {
        this.perPrize = perPrize;
    }

    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    public Integer getKeepWineId() {
        return keepWineId;
    }

    public void setKeepWineId(Integer keepWineId) {
        this.keepWineId = keepWineId;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
