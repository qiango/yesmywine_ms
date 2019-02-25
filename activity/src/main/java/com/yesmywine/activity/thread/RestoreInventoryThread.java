package com.yesmywine.activity.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.entity.Activity;
import com.yesmywine.activity.ifttt.service.GoodsService;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by by on 2017/7/10.
 */
@Component
public class RestoreInventoryThread implements Runnable {
    @Autowired
    private GoodsService goodsService;

    private String goodsIds;
    private Activity activity;

    public RestoreInventoryThread() {
    }

    public RestoreInventoryThread(String goodsIds, Activity activity) {
        this.goodsIds = goodsIds;
        this.activity = activity;
    }

    @Override
    public void run() {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("goodsIds",goodsIds);
        String result = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/goods/goods/itf/manyGoods", RequestMethod.get,paramsMap,null);
        JSONArray array = JSON.parseArray(ValueUtil.getFromJson(result,"data"));
        String skuIds = null;
        String counts = null;
        for(int i = 0;i<array.size();i++){
            JSONObject object = (JSONObject) array.get(i);
            String goodsId = object.getString("id");
            Integer remaining = Integer.valueOf(object.getString("remaining"));
            JSONArray skuArr = object.getJSONArray("goodsSku");
            for(int m = i;m<skuArr.size();m++){
                JSONObject skuObj = (JSONObject) skuArr.get(m);
                String skuId = skuObj.getString("skuId");
                String skuCount = String.valueOf(Integer.valueOf(skuObj.getString("count"))*remaining);
                skuIds += skuId +",";
                counts += skuCount +",";
            }
        }
        skuIds = skuIds.substring(0,skuIds.length()-1);
        counts = counts.substring(0,counts.length()-1);
        //解冻抢购库存
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("skuIds",skuIds);
        paramMap.put("counts",counts);
        String inventoryResult = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/inventory/channelInventory/itf/releaseFreeze", RequestMethod.post,paramMap,null);
        System.out.println("====================库存返回结果："+inventoryResult);
        Map<String,Object> cmsParams = new HashMap<>();
        paramMap.put("activityId",activity.getId());
        String cmsResult = SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/cms/delGoods/itf", RequestMethod.post,cmsParams,null);
        System.out.println("====================cms返回结果"+cmsResult);
    }
}
