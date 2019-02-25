package com.yesmywine.ware.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.ware.entity.ChannelsInventory;
import com.yesmywine.ware.service.ChannelsInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Administrator on 2017/7/15 0015.
 */
@RestController
@RequestMapping("/inventory/cache")
public class TaskController {

    @Autowired
    private ChannelsInventoryService inventoryService;

    @RequestMapping(value = "/task")
    public String inventoryCache(){

        //获取首页及热销商品sku
        String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/goods/itf/topGoods", RequestMethod.get, (Map<String, Object>) null,null);
        if(result!=null){
            JSONObject object = JSON.parseObject(result);
            JSONArray dataArr = object.getJSONArray("data");
            if(dataArr!=null){
                for(int i = 0;i<dataArr.size();i++){
                    JSONObject skuObj = (JSONObject) dataArr.get(i);
                    String skuCode = skuObj.getString("skuCode");
                    String skuId = skuObj.getString("skuId");
                    //将sku加入缓存
                    ChannelsInventory inventory = inventoryService.findBySkuId(Integer.valueOf(skuId));
                    RedisCache.set(Constants.SKU_ID + skuId,inventory);
                }
            }else{
                return ValueUtil.toError("500",null);
            }
        }else{
            return ValueUtil.toError("500",null);
        }
        return ValueUtil.toJson("SUCCESS");
    }
}
