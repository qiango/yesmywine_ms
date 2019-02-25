package com.yesmywine.cart.dao;


import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cart.entity.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by whao on 2016/12/20.
 */
@Repository
public interface CartItemDao extends BaseRepository<CartItem, Integer> {
    CartItem findByCartIdAndGoodsId(Integer cartId, Integer goodsId);
    List<CartItem> findByCartId(Integer cartId);
    List<CartItem> findByCartIdOrderByCreateTimeDesc(Integer cartId);
    List<CartItem> findByCartIdAndGoodsIdIn(Integer cartId, List goodsId);
    List<CartItem> findByCartIdAndStatus(Integer cartId, Integer status);

    @Query("select count(*) from CartItem where cartId=:cartId")
    Integer countUserIdGoodsCart(@Param("cartId") Integer cartId);

}
