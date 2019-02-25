package com.yesmywine.activity.ifttt.service;

import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.entity.GoodsMirroring;
import com.yesmywine.base.record.biz.BaseService;

import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
public interface GoodsService extends BaseService<GoodsMirroring,Integer> {
    GoodsMirroring findById(String goodsId);

    List<GoodsMirroring> findByBrandIdAndGoStatusAndSaleModelAndGoodsTypeIn(String brandId);

    List<GoodsMirroring> findByCategoryIdAndGoStatusAndSaleModelAndGoodsTypeIn(String categoryId);

    List<GoodsMirroring> findByGoodsIdIn(List<String> goodsIdList);

    Integer CountByActivityIdAndWare(Integer activityId, WareEnum wareEnum);
}
