package com.yesmywine.pay.dao;

import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by WANG, RUIQING on 11/30/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public interface PaymentDao extends JpaRepository<PaymentEntity, Integer> {
    PaymentEntity findByPaymentCode(Payment alipay);
}
