package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.GoodsChannel;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by ${shuang} on 2017/3/16.
 */

public interface GoodsChannelService extends BaseService<GoodsChannel,Integer> {
//    //下发
//    List setGoodsChannel(Integer goodsId, String[] params) throws YesmywineException;

    //更改预售
    boolean exchange(Integer goodsId, Integer channelId) throws YesmywineException;

    String http(String skuId, Integer channelId);

    String http(String skuId);

    String synchronous(Map<String,String> map)throws YesmywineException;

    Integer inventory(String skuId, Integer channelId, String item);

}
