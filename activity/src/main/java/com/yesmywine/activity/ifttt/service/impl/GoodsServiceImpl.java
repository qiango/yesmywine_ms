package com.yesmywine.activity.ifttt.service.impl;

import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.ifttt.dao.GoodsDao;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.activity.ifttt.service.GoodsService;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
@Service
public class GoodsServiceImpl extends BaseServiceImpl<GoodsMirroring,Integer>
    implements GoodsService{

    @Autowired
    private GoodsDao goodsDao;

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'GoodsMirroring_'+#goodsId")
    public GoodsMirroring findById(String goodsId) {
        return goodsDao.findByGoodsId(goodsId);
    }

    @Override
    public List<GoodsMirroring> findByBrandIdAndGoStatusAndSaleModelAndGoodsTypeIn(String brandId) {
        List<String> goodsTypeList = new ArrayList<>();
        goodsTypeList.add("single");
        goodsTypeList.add("plural");
        return goodsDao.findByBrandIdAndGoStatusAndSaleModelAndGoodsTypeIn(brandId,"1","0",goodsTypeList);
    }

    @Override
    public List<GoodsMirroring> findByCategoryIdAndGoStatusAndSaleModelAndGoodsTypeIn(String categoryId) {
        List<String> goodsTypeList = new ArrayList<>();
        goodsTypeList.add("single");
        goodsTypeList.add("plural");
        return goodsDao.findByCategoryIdAndGoStatusAndSaleModelAndGoodsTypeIn(categoryId.toString(),"1","0",goodsTypeList);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'GoodsServiceImpl_findByGoodsIdIn'+#goodsIdList")
    public List<GoodsMirroring> findByGoodsIdIn(List<String> goodsIdList) {
        return goodsDao.findByGoodsIdIn(goodsIdList);
    }

    @Override
    public Integer CountByActivityIdAndWare(Integer activityId, WareEnum wareEnum) {
        return goodsDao.CountByActivityId(activityId,wareEnum.getValue());
    }
}
