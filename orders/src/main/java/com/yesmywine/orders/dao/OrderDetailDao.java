package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.OrderDetail;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by wangdiandian on 2016/12/20.
 */
@Repository
public interface OrderDetailDao extends BaseRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderNo(Long orderNo);
    OrderDetail  findByOrderNoAndGoodsId(Long orderNo,Integer goodsId);
    List<OrderDetail> findByGoodsNameContaining(String goodsName);


//    @Query("select sum(counts) from OrderDetail where goodsId =:goodsId and orderNo in(select orderNo from Orders where status= 'DeliveryComplete')")
//    Integer findSumGoodsId(@Param("goodsId")Integer goodsId);

    //    @Query("select distinct goodsId ,sum(counts) from OrderDetail where  orderNo in(select OrderNo from orders where status= 'DeliveryComplete') GROUP BY goodsId")
//    List<OrderDetail> findSales();

}
