package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderGiftCard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/8/27.
 */
@Repository
public interface OrderGiftCardDao extends BaseRepository<OrderGiftCard,Integer> {
    List<OrderGiftCard> findByOrderNoAndGoodsId(Long orderNo,Integer goodsId);

}
