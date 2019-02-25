package com.yesmywine.pay.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.pay.entity.TransactionHistory;

import java.util.List;

/**
 * Created by SJQ on 2017/2/27.
 */
public interface TransactionService extends BaseService<TransactionHistory, Integer> {
    List<TransactionHistory> findByOrderNoAndType(String orderNo, Integer type);

    TransactionHistory findByOrderIdAndPayWay(String orderNumber, String payWay);

    TransactionHistory findBySerialNum(String serialNum);

    TransactionHistory findByReturnNo(String returnNo);
}
