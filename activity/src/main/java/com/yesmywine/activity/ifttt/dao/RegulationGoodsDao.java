package com.yesmywine.activity.ifttt.dao;

import com.yesmywine.activity.bean.GoodsTypeEnum;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.entity.RegulationGoods;
import com.yesmywine.base.record.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wangdiandian on 2017/1/10.
 */
@Repository
public interface RegulationGoodsDao extends BaseRepository<RegulationGoods, Integer> {
    List<RegulationGoods> findByRegulationIdIn(List<Integer> regulationIds);

    List<RegulationGoods> findByRegulationId(Integer regulationId);

    List<RegulationGoods> findByTargetIdAndTypeAndWare(Integer targetId, GoodsTypeEnum type, WareEnum ware);

    List<RegulationGoods> findByRegulationIdAndWare(Integer regulationId, WareEnum coupon);

    List<RegulationGoods> findByRegulationIdAndTargetIdAndTypeAndWare(Integer regulationId, Integer targetId, GoodsTypeEnum goods, WareEnum promoting);

    List<RegulationGoods> findByRegulationIdAndTypeAndWare(Integer regulationId, GoodsTypeEnum goods, WareEnum promoting);

    RegulationGoods findByTargetIdAndTypeAndWareAndActivityPriceIsNotNull(Integer targetId, GoodsTypeEnum goods, WareEnum promoting);

    void deleteByRegulationId(Integer id);

    List<RegulationGoods> findByActivityIdAndTargetId(Integer activityId, Integer targetId);

    List<RegulationGoods> findByActivityId(Integer activityId);

    RegulationGoods findByActivityIdAndTargetIdAndWare(Integer activityId, Integer targetId, WareEnum ware);

    void deleteByActivityId(Integer activityId);

    List<RegulationGoods> findByActivityIdAndTargetIdAndRegulationIdIsNull(Integer activityId, Integer targetId);
}
