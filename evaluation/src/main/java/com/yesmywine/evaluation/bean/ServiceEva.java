package com.yesmywine.evaluation.bean;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by hz on 6/25/17.
 */
@Entity
@Table(name = "serviceEva")
public class ServiceEva extends BaseEntity<Integer> {//服务评价
    @Column(columnDefinition = "varchar(11) COMMENT '用户名称'")
    private String userName;
    @Column(columnDefinition = "varchar(50) COMMENT '订单编号'")
    private String orderNumber;
    @Column(columnDefinition = "int(11) COMMENT '送货速度'")
    private Integer logistics;//物流服务
    @Column(columnDefinition = "int(11) COMMENT '服务态度'")
    private Integer serviceAttitude;//服务态度
//    @Column(columnDefinition = "int(11) COMMENT '商品满意度'")
//    private Integer goodsAttitude;//
    @Column(columnDefinition = "int(11) COMMENT '商品包装'")
    private Integer commodityPackaging;//



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getLogistics() {
        return logistics;
    }

    public void setLogistics(Integer logistics) {
        this.logistics = logistics;
    }

    public Integer getServiceAttitude() {
        return serviceAttitude;
    }

    public void setServiceAttitude(Integer serviceAttitude) {
        this.serviceAttitude = serviceAttitude;
    }

//    public Integer getGoodsAttitude() {
//        return goodsAttitude;
//    }
//
//    public void setGoodsAttitude(Integer goodsAttitude) {
//        this.goodsAttitude = goodsAttitude;
//    }

    public Integer getCommodityPackaging() {
        return commodityPackaging;
    }

    public void setCommodityPackaging(Integer commodityPackaging) {
        this.commodityPackaging = commodityPackaging;
    }
}
