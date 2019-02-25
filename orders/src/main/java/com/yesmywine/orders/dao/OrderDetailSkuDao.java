package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderDetailSku;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/6/20.
 */
@Repository
public interface OrderDetailSkuDao extends BaseRepository<OrderDetailSku, Long> {
    List<OrderDetailSku> findByGoodsIdAndOrderNo(Integer goodsId,Long orderNo);
    List<OrderDetailSku> findByOrderNo(Long orderNo);
//    select sum(count), OrderDetailSku WHERE
//    @Query("SELECT skuId,sum(counts) as counts from OrderDetailSku where goodsId =:goodsId ,orderNo =:orderNo GROUP BY skuId")
//    List<OrderDetailSku>findByLuckyGoodsId(@Param("goodsId") Integer goodsId,@Param("orderNo") Long orderNo);
}
