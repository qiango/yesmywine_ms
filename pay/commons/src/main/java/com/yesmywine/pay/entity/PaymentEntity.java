package com.yesmywine.pay.entity;

import com.yesmywine.pay.bean.Payment;
import com.yesmywine.util.enums.Active;

import javax.persistence.*;

/**
 * Created by WANG, RUIQING on 11/30/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Entity
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Payment paymentCode;

    @Enumerated(EnumType.STRING)
    private Active active;
    @Column(columnDefinition = "varchar(50) COMMENT '支付描述'")
    private String description;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Payment getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(Payment paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
