package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderInvoice;
import org.springframework.stereotype.Repository;

/**
 * Created by wangdiandian on 2016/12/20.
 */
@Repository
public interface OrderInvoiceDao extends BaseRepository<OrderInvoice, Integer> {
    OrderInvoice findByOrderNo(Long orderNo);
}
