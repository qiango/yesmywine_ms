package com.yesmywine.goods.dao;

import com.yesmywine.goods.entityProperties.PropGoods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by hz on 1/6/17.
 */
public interface PropGoodsDao extends JpaRepository<PropGoods, Integer> {
    List<PropGoods> findByPropValueId(String propValueId);

//    List<PropGoods> findByGoodsId(Long goodsId);
}
