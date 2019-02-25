package com.yesmywine.activity.service;


import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.GoodsTypeEnum;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.entity.RegulationGoods;
import com.yesmywine.base.record.biz.BaseService;

import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
public interface RegulationGoodsService extends BaseService<RegulationGoods, Integer> {
    RegulationGoods findByTargetIdAndTypeAndWareAndActivityPriceIsNotNull(Integer integer, GoodsTypeEnum goods, WareEnum promoting);

    List<RegulationGoods> findByTargetIdAndTypeAndWare(Integer integer, GoodsTypeEnum goods, WareEnum promoting);

    List<RegulationGoods> findByRegulationIdAndTypeAndWare(Integer regulationId, GoodsTypeEnum goods, WareEnum promoting);

    List<RegulationGoods> findByRegulationId(Integer regulationId);

    List<RegulationGoods> findByRegulationIdAndWare(Integer regulationId, WareEnum coupon);

    List<RegulationGoods> findByRegulationIdAndTargetIdAndTypeAndWare(Integer regulationId, Integer integer, GoodsTypeEnum goods, WareEnum promoting);

    void deleteByRegulationId(Integer id);

    List<RegulationGoods> findByRegulationIdIn(List<Integer> regulationIdList);

    List<RegulationGoods> findByActivityIdAndTargetId(Integer id, Integer targetId);

    List<RegulationGoods> findByActivityId(Integer activityId);

    void deleteEntity(List<RegulationGoods> delRegulationGoodsList);

    String updateActivityStatusByActivityId(Integer activityId, ActivityStatus status);

    RegulationGoods findByActivityIdAndTargetIdAndWare(Integer activityId, Integer targetId, WareEnum ware);

    List<RegulationGoods> findByRegulationIdAndWareNoCache(Integer regulationId, WareEnum wareEnum);

    List<RegulationGoods> findByActivityIdAndTargetIdAndRegulationIdIsNot(Integer activityId, Integer integer);
}
