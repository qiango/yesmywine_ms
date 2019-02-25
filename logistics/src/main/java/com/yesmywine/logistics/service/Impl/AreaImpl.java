package com.yesmywine.logistics.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.logistics.dao.AreaDao;
import com.yesmywine.logistics.entity.Area;
import com.yesmywine.logistics.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangdiandian on 2017/4/13.
 */
@Service
public class AreaImpl extends BaseServiceImpl<Area, Integer> implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'showArea'")
    public List<Area> showArea(){
        return areaDao.findByParentName(null);
    }
}
