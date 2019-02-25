package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderDispatch;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by wangdiandian on 2017/4/13.
 */
@Repository
public interface OrderDispatchDao extends BaseRepository<OrderDispatch,Integer> {
    List<OrderDispatch> findByOrderNo(Long orderNo);
    List<OrderDispatch> findByOrderNoAndStatusIn(Long orderNo,List status);

}
