package com.yesmywine.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.ifttt.dao.GoodsDao;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.activity.entity.GoodsSku;
import com.yesmywine.activity.service.SynService;
import com.yesmywine.db.base.ehcache.CacheStatement;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SJQ on 2017/6/9.
 */
@Service
@Transactional
public class SynServiceImpl implements SynService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private CacheManager cacheManager;

    @Override
    @CachePut(value=CacheStatement.ACTIVITY_VALUE,key = "'GoodsMirroring_'+#goodsId")//更新缓存
    public GoodsMirroring saveGoodsInfo(String jsonData, String goodsId) {
        JSONObject goodsJson = JSON.parseObject(jsonData);
        String id = goodsJson.getString("id");
        String goodsCode = goodsJson.getString("goodsCode");
        String goodsImageUrl = goodsJson.getString("goodsImageUrl");
        String goodsName = goodsJson.getString("goodsName");
        String goodsOriginalName = goodsJson.getString("goodsOriginalName");
        String goodsEnName = goodsJson.getString("goodsEnName");
        String goodsDetails = goodsJson.getString("goodsDetails");
        String price = goodsJson.getString("price");
        String salePrice = goodsJson.getString("salePrice");
        String referencePrice = goodsJson.getString("referencePrice");
        String categoryId = goodsJson.getString("categoryId");
        String brandId = goodsJson.getString("brandId");
        String goStatus = goodsJson.getString("goStatus");
        String isMember = goodsJson.getString("isMember");
        String isKeep = goodsJson.getString("isKeep");
        String goodsType = goodsJson.getString("item");
        String saleModel = goodsJson.getString("operate");
        String virtualType = goodsJson.getString("virtualType");
        JSONArray array = goodsJson.getJSONArray("goodsSku");
        GoodsMirroring goods = goodsDao.findByGoodsId(id);
        String isCheck = goodsJson.getString("status");
        if(isCheck.equals("5")&&goods==null){
            return goods;
        }
        if(goods!=null){
            goods.setGoodsImageUrl(goodsImageUrl);
            goods.setGoodsCode(goodsCode);
            goods.setGoodsName(goodsName);
            goods.setGoodsOriginalName(goodsOriginalName);
            goods.setGoodsEnName(goodsEnName);
            goods.setGoodsDetails(goodsDetails);
            goods.setPrice(price);
            goods.setSalePrice(salePrice);
            goods.setReferencePrice(referencePrice);
            goods.setCategoryId(categoryId);
            goods.setBrandId(brandId);
            goods.setVirtualType(virtualType);
            goods.setGoStatus(goStatus);
            goods.setMember(Boolean.valueOf(isMember));
            goods.setKeep(Boolean.valueOf(isKeep));
        }else{
            Set<GoodsSku> set = new HashSet<>();
            for(int i = 0;i<array.size();i++){
                JSONObject obj = (JSONObject) array.get(i);
                String skuId = obj.getString("skuId");
                String skuCode = obj.getString("code");
                String count = obj.getString("count");
                GoodsSku goodsSku = new GoodsSku(id,Integer.valueOf(skuId),skuCode,Integer.valueOf(count));
                set.add(goodsSku);
            }
            goods = new GoodsMirroring(id,goodsCode,goodsImageUrl,goodsName,
                    goodsOriginalName,goodsEnName,goodsDetails,price,salePrice,referencePrice,
                    categoryId,brandId,goStatus,set,Boolean.valueOf(isMember),
                    Boolean.valueOf(isKeep),saleModel,goodsType,virtualType);
        }
        goodsDao.save(goods);
        return goods;
    }
}
