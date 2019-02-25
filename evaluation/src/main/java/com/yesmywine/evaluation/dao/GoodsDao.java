package com.yesmywine.evaluation.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.evaluation.bean.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hz on 12/8/16.
 */
@Repository
public interface GoodsDao extends BaseRepository<Goods, Integer> {
    Goods findByGoodsId(Integer goodsId);

    List<Goods> findByNameLike(String goodsName);
}
