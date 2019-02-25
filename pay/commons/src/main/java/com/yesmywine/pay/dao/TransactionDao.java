package com.yesmywine.pay.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.pay.bean.Payment;
import com.yesmywine.pay.entity.TransactionHistory;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * Created by SJQ on 2017/2/27.
 */
@CacheConfig(cacheNames = "transactionHistory")
public interface TransactionDao extends BaseRepository<TransactionHistory, Integer> {
    TransactionHistory findByOrderNo(String orderId);

    TransactionHistory findByOrderNoAndPayWay(String orderNumber, Payment payWay);

    TransactionHistory findBySerialNum(String serialNum);

    TransactionHistory findByReturnNo(String returnNo);

    List<TransactionHistory> findByOrderNoAndType(String orderNo, Integer type);
}
