package com.yesmywine.activity.service.impl;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.GoodsTypeEnum;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.ifttt.dao.RegulationGoodsDao;
import com.yesmywine.activity.entity.RegulationGoods;
import com.yesmywine.activity.service.RegulationGoodsService;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by SJQ on 2017/6/16.
 */
@Service
public class RegulationGoodsServiceImpl extends BaseServiceImpl<RegulationGoods, Integer>
        implements RegulationGoodsService {
    @Autowired
    private RegulationGoodsDao regulationGoodsDao;

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'RegulationGoodsDao_findByTargetIdAndTypeAndWareAndIsDeleteAndStatusAndActivityPriceIsNotNull_'+#targetId+#goodsType+#ware")
    public RegulationGoods findByTargetIdAndTypeAndWareAndActivityPriceIsNotNull(Integer targetId, GoodsTypeEnum goodsType, WareEnum ware) {
        return regulationGoodsDao.findByTargetIdAndTypeAndWareAndActivityPriceIsNotNull(targetId,goodsType,ware);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'RegulationGoodsDao_findByTargetIdAndTypeAndWareAndIsDeleteAndStatus_'+#targetId+#goodsType+#ware")
    public List<RegulationGoods> findByTargetIdAndTypeAndWare(Integer targetId, GoodsTypeEnum goodsType, WareEnum ware) {
        return regulationGoodsDao.findByTargetIdAndTypeAndWare(targetId,goodsType,ware);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'RegulationGoodsDao_findByRegulationIdAndTypeAndWareAndIsDeleteAndStatus_'+#regulationId+#goods+#wareEnum")
    public List<RegulationGoods> findByRegulationIdAndTypeAndWare(Integer regulationId, GoodsTypeEnum goods, WareEnum wareEnum) {
        return regulationGoodsDao.findByRegulationIdAndTypeAndWare(regulationId,goods,wareEnum);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'RegulationGoodsDao_findByRegulationIdAndIsDeleteAndStatus_'+#regulationId")
    public List<RegulationGoods> findByRegulationId(Integer regulationId) {
        return regulationGoodsDao.findByRegulationId(regulationId);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'RegulationGoodsDao_findByRegulationIdAndIsDeleteAndWareAndStatus_'+#regulationId+#wareEnum")
    public List<RegulationGoods> findByRegulationIdAndWare(Integer regulationId, WareEnum wareEnum) {
        return regulationGoodsDao.findByRegulationIdAndWare(regulationId,wareEnum);
    }

    @Override
    public List<RegulationGoods> findByRegulationIdAndTargetIdAndTypeAndWare(Integer regulationId, Integer targetid, GoodsTypeEnum goods, WareEnum promoting) {
        return regulationGoodsDao.findByRegulationIdAndTargetIdAndTypeAndWare(regulationId,targetid,goods,promoting);
    }

    @Override
    public void deleteByRegulationId(Integer id) {
        regulationGoodsDao.deleteByRegulationId(id);
    }

    @Override
    public List<RegulationGoods> findByRegulationIdIn(List<Integer> regulationIdList) {
        return regulationGoodsDao.findByRegulationIdIn(regulationIdList);
    }

    @Override
    public List<RegulationGoods > findByActivityIdAndTargetId(Integer activityId, Integer targetId) {
        return regulationGoodsDao.findByActivityIdAndTargetId(activityId,targetId);
    }

    @Override
    public List<RegulationGoods> findByActivityId(Integer activityId) {
        return regulationGoodsDao.findByActivityId(activityId);
    }

    @Override
    public void deleteEntity(List<RegulationGoods> delRegulationGoodsList) {
        regulationGoodsDao.delete(delRegulationGoodsList);
    }

    @Override
    public String updateActivityStatusByActivityId(Integer activityId, ActivityStatus status) {
        List<RegulationGoods> regulationGoodsList = regulationGoodsDao.findByActivityId(activityId);
        for (RegulationGoods regulationGoods:regulationGoodsList){
            regulationGoods.setStatus(status);
        }
        regulationGoodsDao.save(regulationGoodsList);
        return "SUCCESS";
    }

    @Override
    public RegulationGoods findByActivityIdAndTargetIdAndWare(Integer activityId, Integer targetId, WareEnum ware) {
        return regulationGoodsDao.findByActivityIdAndTargetIdAndWare(activityId,  targetId,  ware);
    }

    @Override
    public List<RegulationGoods> findByRegulationIdAndWareNoCache(Integer regulationId, WareEnum wareEnum) {
        return regulationGoodsDao.findByRegulationIdAndWare(regulationId,wareEnum);
    }

    @Override
    public List<RegulationGoods> findByActivityIdAndTargetIdAndRegulationIdIsNot(Integer activityId, Integer targetId) {
        return regulationGoodsDao.findByActivityIdAndTargetIdAndRegulationIdIsNull(activityId,targetId);
    }

}
