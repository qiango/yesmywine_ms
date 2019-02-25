package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.Enshrine;

/**
 * Created by ${shuang} on 2017/6/13.
 */
public interface EnshrineDao extends BaseRepository<Enshrine, Integer> {

    Enshrine findByGoodsId(Integer goodsId);

    Enshrine findByUserIdAndGoodsId(Integer userId, Integer goodsId);
}
