package com.yesmywine.cart.dao;


import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cart.entity.Cart;
import org.springframework.stereotype.Repository;

/**
 * Created by whao on 2016/12/19.
 */
@Repository
public interface CartDao extends BaseRepository<Cart, Integer> {
    Cart findByUserId(Integer userId);
}
