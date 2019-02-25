package com.yesmywine.activity.ifttt.service.impl;

import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.ifttt.dao.IftttConfigDao;
import com.yesmywine.activity.ifttt.entity.IftttConfig;
import com.yesmywine.activity.ifttt.service.IftttConfigService;
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
public class IftttConfigServiceImpl extends BaseServiceImpl<IftttConfig,Integer>
        implements IftttConfigService {
    @Autowired
    private IftttConfigDao iftttConfigDao;

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttConfig_'+#discountId")
    public List<IftttConfig> findByDiscountId(Integer discountId){
        return iftttConfigDao.findByDiscountId( discountId);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttConfigService_findByDiscountIdAndIsDelete_'+#discountId+#isDelete")
    public IftttConfig findByDiscountIdAndIsDelete(Integer discountId, DeleteEnum isDelete) {
        return iftttConfigDao.findByDiscountIdAndIsDelete(discountId,isDelete);
    }
}
