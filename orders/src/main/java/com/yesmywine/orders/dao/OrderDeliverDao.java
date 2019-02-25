package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderDeliver;

/**
 * Created by wangdiandian on 2017/2/13.
 */
public interface OrderDeliverDao extends BaseRepository<OrderDeliver, Long> {
    OrderDeliver findByOrderNo(Long orderNo);

}
