package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.AdverEntity;
import org.springframework.cache.annotation.CacheConfig;

/**
 * Created by yly on 2017/1/16.
 */
@CacheConfig(cacheNames = "adver")
public interface AdverDao extends BaseRepository<AdverEntity, Integer> {
    AdverEntity findByAdverName(String adverName);

    AdverEntity findByRelevancyTypeAndRelevancy(Integer type,String rele);
}
