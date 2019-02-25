package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.TestEntity;
import org.springframework.cache.annotation.CacheConfig;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@CacheConfig(cacheNames = "test")
public interface TestRepositoy extends BaseRepository<TestEntity, Long> {
}
