package com.yesmywine.ware.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.ware.entity.ChannelsInventory;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * Created by SJQ on 2017/1/9.
 *
 * @Description:
 */
@CacheConfig(cacheNames = "channelsInventory")
public interface ChannelsInventoryDao extends BaseRepository<ChannelsInventory, Integer> {
    ChannelsInventory findBySkuId(Integer skuId);

    List<ChannelsInventory> findBySkuIdIn(String[] goodsSKUIds);


}
