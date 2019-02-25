package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsDetail;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 6/27/17.
 */
@Repository
public interface GoodsDetailDao extends BaseRepository<GoodsDetail,Integer>{
    GoodsDetail findByGoodsId(Integer goodsId);
}
