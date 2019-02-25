
package com.yesmywine.user.entity;

import com.yesmywine.base.record.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by ${shuang} on 2017/4/5.
 */
@Entity
@Table(name = "chargeFlow")
public class ChargeFlow extends BaseEntity<Integer> {
    @Column(columnDefinition = "int(11) COMMENT '用Id'")
    private  Integer userId;
    @Column(columnDefinition = "varchar(200) COMMENT '用户名称'")
    private String userName;
    @Column(columnDefinition = "double COMMENT '充值金额'")
    private Double chargeMoney;//充值金额
    @Column(columnDefinition = "varchar(10) COMMENT '状态：0:未支付状态，1消费，2充值，3退款'")
    private String status;//状态
    @Column(columnDefinition = "double COMMENT '余额'")
    private Double remianMonney;//余额
    @Column(columnDefinition = "varchar(10) COMMENT '充值方式'")
    private  String chargeWay;
    @Column(columnDefinition = "varchar(255) COMMENT '消费订单号'")
    private String orderNumber;//消费订单号

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getChargeWay() {
        return chargeWay;
    }

    public void setChargeWay(String chargeWay) {
        this.chargeWay = chargeWay;
    }

    public Double getRemianMonney() {
        return remianMonney;
    }

    public void setRemianMonney(Double remianMonney) {
        this.remianMonney = remianMonney;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Double getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(Double chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

