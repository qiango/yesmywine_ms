package com.yesmywine.ware.Utils;

import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.ware.entity.ChannelsInventory;

/**
 * Created by Administrator on 2017/7/16 0016.
 */
public class InventoryCache {

    public static  void update(String skuId, ChannelsInventory inventory){
        String result = RedisCache.get(Constants.SKU_ID + skuId);
        if(result!=null){
            RedisCache.set(Constants.SKU_ID + skuId,inventory);
        }
    }
}
