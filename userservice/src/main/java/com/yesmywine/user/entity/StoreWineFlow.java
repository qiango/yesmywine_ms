package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/8/10.
 */
@Entity
@Table(name = "storeWineFlow")
public class StoreWineFlow  extends BaseEntity<Integer> {

    @Column(columnDefinition = "varchar(50) COMMENT '订单号'")
    private  String orderNumber;
    @Column(columnDefinition = "varchar(50) COMMENT '存酒费用'")
    private  String fee;//费用
    @Column(columnDefinition = "varchar(50) COMMENT '提取时间'")
    private  String extractTime;//提酒时间
    @Column(columnDefinition = "varchar(50) COMMENT '提酒订单'")
    private  String extractorderNumber;//提酒订单号
    @Column(columnDefinition = "int(11) COMMENT '用户Id'")
    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getExtractTime() {
        return extractTime;
    }

    public void setExtractTime(String extractTime) {
        this.extractTime = extractTime;
    }

    public String getExtractorderNumber() {
        return extractorderNumber;
    }

    public void setExtractorderNumber(String extractorderNumber) {
        this.extractorderNumber = extractorderNumber;
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
