package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsChannel;

/**
 * Created by ${shuang} on 2017/3/16.
 */
public interface GoodsChannelDao extends BaseRepository<GoodsChannel, Integer> {

    GoodsChannel findByGoodsId(Integer goodsId);
    GoodsChannel findBySkuId(String skuId);

    GoodsChannel findByGoodsIdAndChannelId(Integer goodsId, Integer channelId);
}
