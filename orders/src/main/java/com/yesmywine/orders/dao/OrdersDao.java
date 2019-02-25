package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by wangdiandian on 2016/12/20.
 */
@Repository
public interface OrdersDao extends BaseRepository<Orders, Long> {
    Orders findByOrderNo(Long orderNo);
    List<Orders> findByStatus(Integer status);

    @Query("select orderNo from Orders  ORDER BY confirmTime DESC")
    List<Long> findOrder();

    @Query(value = "select count(ag.orderNo)  from orders ag where ag.userId=:userId and ag.orderNo in (select od.orderNo from orderDetail od where od.goodsName LIKE '%'||:goodsName||'%') ",nativeQuery = true )
    Integer findByGoodsNameDiscountOrders(@Param("userId") Integer userId,@Param("goodsName") String goodsName);

}
