package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderPayinfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2016/12/20.
 */
@Repository
public interface OrderPayinfoDao extends BaseRepository<OrderPayinfo, Long> {
    OrderPayinfo findByOrderNo(Long orderNo);

    List<OrderPayinfo> findByOrderNoIn(List<Long> orderNos);
}
