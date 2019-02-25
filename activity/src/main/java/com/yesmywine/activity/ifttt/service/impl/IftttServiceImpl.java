package com.yesmywine.activity.ifttt.service.impl;

import com.yesmywine.activity.ifttt.dao.IftttDao;
import com.yesmywine.activity.ifttt.entity.IftttEntity;
import com.yesmywine.activity.ifttt.entity.IftttEnum;
import com.yesmywine.activity.ifttt.service.IftttService;
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
public class IftttServiceImpl extends BaseServiceImpl<IftttEntity,Integer> implements IftttService {
    @Autowired
    private IftttDao iftttDao;

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttServiceImpl_findById_'+#actionId")
    public IftttEntity findById(Integer actionId) {
        return iftttDao.findById(actionId);
    }

    @Override
    public List<IftttEntity> findAll(List<Integer> taIds) {
        return iftttDao.findAll(taIds);
    }

    @Override
    public List<IftttEntity> findByType(String type) {
        System.out.println(IftttEnum.getIftttEnum(type));
        return  iftttDao.findByType(IftttEnum.getIftttEnum(type));
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttServiceImpl_findBytaIds_'+#taIds")
    public List<IftttEntity> findBytaIds(List<Integer> taIds) {
        return iftttDao.findAll(taIds);
    }
}
