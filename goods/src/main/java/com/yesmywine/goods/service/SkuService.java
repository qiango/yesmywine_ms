package com.yesmywine.goods.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.entity.Sku;
import com.yesmywine.util.error.YesmywineException;

import java.util.Map;

/**
 * Created by hz on 2/13/17.
 */
public interface SkuService extends BaseService<Sku, Integer> {
//    Map<Integer, Integer> getSku(Long goodsId) throws YesmywineException;
com.alibaba.fastjson.JSONArray getSku(Integer categoryId, Integer type) throws YesmywineException;

//    Map<String, List<Object>> showSku(Long goodsId) throws YesmywineException;

//    String deleteSkuSpu(Long goodsId) throws YesmywineException;

    Sku showSku(Integer skuId) throws YesmywineException;

    String deleteSku(Integer skuId)throws YesmywineException;

    String updateSkuProp(Integer skuId, String valueJson, String imgIds, String skuName,Integer isYourProduct)throws YesmywineException;

    String create(Integer suppierId, String skuName, Integer categoryId, String skuJsonArray, Integer type)throws YesmywineException;

    String synchronous(Map<String,String> map)throws YesmywineException;
}
