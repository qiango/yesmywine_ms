package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsMirror;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 4/12/17.
 */
@Repository
public interface GoodsMirrorDao extends BaseRepository<GoodsMirror, Integer> {
        GoodsMirror findByGoodsId(Integer goodsId);
}
