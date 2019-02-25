package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderReturnExchange;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/4/12.
 */
@Repository
public interface OrderReturnExchangeDao extends BaseRepository<OrderReturnExchange, Integer> {
    List<OrderReturnExchange > findByOrderNo(Long orderNo);

    OrderReturnExchange findByReturnNo(String returnNo);
}
