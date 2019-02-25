package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PanicBuyingFirst;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface PanicBuyingFirstDao extends BaseRepository<PanicBuyingFirst,Integer> {
    PanicBuyingFirst findByName(String name);

    List<PanicBuyingFirst> findByActicityId(Integer activityId);

}
