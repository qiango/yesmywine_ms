package com.yesmywine.ware.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.ware.Utils.InventoryCache;
import com.yesmywine.ware.dao.ChannelsInventoryDao;
import com.yesmywine.ware.entity.ChannelsInventory;
import com.yesmywine.ware.service.ChannelsInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SJQ on SUCCESS7/2/10.
 */
@Service
@Transactional
public class ChannelsInventoryServiceImpl extends BaseServiceImpl<ChannelsInventory, Integer>
        implements ChannelsInventoryService {
    @Autowired
    private ChannelsInventoryDao channelsInventoryDao;

    public ChannelsInventory findBySkuId(Integer skuId) {
        return channelsInventoryDao.findBySkuId(skuId);
    }

    public String create(String jsonData)throws YesmywineException {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String code = result.getString("code");
        if(!code.equals("201")){
            ValueUtil.isError("传输参数格式有误");
        }
        JSONObject channelInventoryJson = result.getJSONObject("data");
        String skuId = channelInventoryJson.getString("skuId");
        String skuCode = channelInventoryJson.getString("skuCode");
        String skuName = channelInventoryJson.getString("skuName");
        String count = channelInventoryJson.getString("count");
        ChannelsInventory channelsInventory = new ChannelsInventory();
        channelsInventory.setSkuId(Integer.valueOf(skuId));
        channelsInventory.setSkuCode(skuCode);
        channelsInventory.setSkuName(skuName);
        channelsInventory.setUseCount(Integer.valueOf(count));
        channelsInventory.setFreezeCount(0);
        channelsInventory.setEnRouteCount(0);
        channelsInventory.setAllCount(Integer.valueOf(count));
        channelsInventoryDao.save(channelsInventory);
        return "SUCCESS";
    }

    public String stock(String jsonData) throws YesmywineException {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String code = result.getString("code");
        if(!code.equals("201")){
            ValueUtil.isError("传输参数格式有误");
        }
        JSONObject channelInventoryJson = result.getJSONObject("data");
        String skuId = channelInventoryJson.getString("skuId");
        String count = channelInventoryJson.getString("count");

        Integer iSkuId = Integer.valueOf(skuId);
        Integer iCount = Integer.valueOf(count);

        ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(iSkuId);
        if(channelsInventory==null){
            return create(jsonData);
        }

        channelsInventory.setUseCount(channelsInventory.getUseCount()+iCount);
        channelsInventory.setAllCount(channelsInventory.getAllCount()+iCount);
        channelsInventoryDao.save(channelsInventory);
        //更新库存缓存
        InventoryCache.update(skuId,channelsInventory);
        return "SUCCESS";
    }

    public String subToCommon(String jsonData)throws YesmywineException {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String code = result.getString("code");
        if(!code.equals("201")){
            ValueUtil.isError("传输参数格式有误");
        }
        JSONObject channelInventoryJson = result.getJSONObject("data");

        String skuId = channelInventoryJson.getString("skuId");
        String count = channelInventoryJson.getString("count");

        Integer iSkuId = Integer.valueOf(skuId);
        Integer iCount = Integer.valueOf(count);

        ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(iSkuId);
        channelsInventory.setUseCount(channelsInventory.getUseCount()-iCount);
        channelsInventory.setAllCount(channelsInventory.getAllCount()-iCount);
        channelsInventoryDao.save(channelsInventory);
        //更新库存缓存
        InventoryCache.update(skuId,channelsInventory);
        return "SUCCESS";
    }

    public String addFreeze(String jsonData) throws YesmywineException {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String code = result.getString("code");
        if(!code.equals("201")){
            ValueUtil.isError("传输参数格式有误");
        }
        JSONObject channelInventoryJson = result.getJSONObject("data");
        String skuId = channelInventoryJson.getString("skuId");
        String count = channelInventoryJson.getString("count");

        Integer iSkuId = Integer.valueOf(skuId);
        Integer iCount = Integer.valueOf(count);

        ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(iSkuId);
        if(channelsInventory==null){
            return create(jsonData);
        }

        channelsInventory.setFreezeCount(channelsInventory.getFreezeCount()+iCount);
        channelsInventory.setAllCount(channelsInventory.getAllCount()+iCount);
        channelsInventoryDao.save(channelsInventory);
        //更新库存缓存
        InventoryCache.update(skuId,channelsInventory);
        return "SUCCESS";
    }

    public String sale(String jsonData)throws YesmywineException {
        JSONArray array = null;
        try {
            array = JSON.parseArray(ValueUtil.getFromJson(jsonData,"data"));
        }catch (Exception e){
            ValueUtil.isError("json格式错误");
        }
        for(int i=0;i<array.size();i++){
            JSONObject channelInventoryJson = (JSONObject) array.get(i);
            String skuId = channelInventoryJson.getString("skuId");
            String count = channelInventoryJson.getString("count");

            Integer iSkuId = Integer.valueOf(skuId);
            Integer iCount = Integer.valueOf(count);

            ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(iSkuId);
            channelsInventory.setFreezeCount(channelsInventory.getFreezeCount()-iCount);
            channelsInventory.setAllCount(channelsInventory.getAllCount()-iCount);
            channelsInventoryDao.save(channelsInventory);
            //更新库存缓存
            InventoryCache.update(skuId,channelsInventory);
        }
        return "SUCCESS";
    }

    public String freeze(Integer[] skuIds, Integer[] counts)throws YesmywineException {
        ValueUtil.verify(skuIds);
        ValueUtil.verify(counts);
        if(skuIds.length!=counts.length){
            ValueUtil.isError("传递参数有误");
        }
        System.out.println(skuIds.toString());
        for(int i=0;i<skuIds.length;i++){
            ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(skuIds[i]);
            channelsInventory.setFreezeCount(channelsInventory.getFreezeCount()+counts[i]);
            channelsInventory.setUseCount(channelsInventory.getUseCount()-counts[i]);
            channelsInventoryDao.save(channelsInventory);
            //更新库存缓存
            InventoryCache.update(String.valueOf(skuIds[i]),channelsInventory);
        }
        return "SUCCESS";
    }

    public String releaseFreeze(Integer[] skuIds, Integer[] counts)throws YesmywineException {
        ValueUtil.verify(skuIds);
        ValueUtil.verify(counts);
        if(skuIds.length!=counts.length){
            ValueUtil.isError("传递参数有误");
        }
        for(int i=0;i<skuIds.length;i++){
            ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(skuIds[i]);
            channelsInventory.setFreezeCount(channelsInventory.getFreezeCount()-counts[i]);
            channelsInventory.setUseCount(channelsInventory.getUseCount()+counts[i]);
            channelsInventoryDao.save(channelsInventory);
            //更新库存缓存
            InventoryCache.update(String.valueOf(skuIds[i]),channelsInventory);
        }
        return "SUCCESS";
    }

    public String addEnRoute(String jsonData) {
        try {
            JSONObject result = (JSONObject) JSON.parseObject(jsonData);
            String code = result.getString("code");
            if(!code.equals("201")){
                ValueUtil.isError("传输参数格式有误");
            }
            JSONObject enRoutJson = result.getJSONObject("data");

            String skuId = enRoutJson.getString("skuId");
            String count = enRoutJson.getString("count");

            Integer iSkuId = Integer.valueOf(skuId);
            Integer iCount = Integer.valueOf(count);

            ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(iSkuId);
            channelsInventory.setEnRouteCount(channelsInventory.getEnRouteCount()+iCount);
            channelsInventory.setAllCount(channelsInventory.getAllCount()+iCount);
            channelsInventoryDao.save(channelsInventory);
            //更新库存缓存
            InventoryCache.update(skuId,channelsInventory);
            return "SUCCESS";
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    public String subEnRoute(String jsonData) {
        JSONObject result = (JSONObject) JSON.parseObject(jsonData);
        String skuId = result.getString("skuId");
        String count = result.getString("count");

        Integer iSkuId = Integer.valueOf(skuId);
        Integer iCount = Integer.valueOf(count);

        ChannelsInventory channelsInventory = channelsInventoryDao.findBySkuId(iSkuId);
        channelsInventory.setEnRouteCount(channelsInventory.getEnRouteCount()-iCount);
        channelsInventory.setUseCount(channelsInventory.getUseCount()+iCount);
        channelsInventoryDao.save(channelsInventory);
        //更新库存缓存
        InventoryCache.update(skuId,channelsInventory);
        return "SUCCESS";
    }
}
