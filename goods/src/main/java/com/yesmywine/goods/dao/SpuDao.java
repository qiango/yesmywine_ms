package com.yesmywine.goods.dao;

import com.yesmywine.goods.entity.Spu;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by hz on 1/10/17.
 */
public interface SpuDao extends JpaRepository<Spu, Integer> {
//    Spu findByGoodsId(Long goodsId);
//
//    void deleteByGoodsId(Long goodsId);
}
