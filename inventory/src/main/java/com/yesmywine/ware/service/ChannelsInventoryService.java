package com.yesmywine.ware.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.ware.entity.ChannelsInventory;

/**
 * Created by SJQ on 2017/1/9.
 *
 * @Description:
 */

public interface ChannelsInventoryService extends BaseService<ChannelsInventory, Integer> {

    ChannelsInventory findBySkuId(Integer skuId);

    String create(String jsonData)throws YesmywineException;

    String stock(String jsonData)throws YesmywineException;

    String sale(String jsonData)throws YesmywineException;

    String freeze(Integer[] skuIds, Integer[] counts)throws YesmywineException;

    String releaseFreeze(Integer[] skuIds, Integer[] counts)throws YesmywineException;

    String addEnRoute(String jsonData);

    String subEnRoute(String jsonData);

    String subToCommon(String jsonData)throws YesmywineException;

    String addFreeze(String jsonData) throws YesmywineException;
}
