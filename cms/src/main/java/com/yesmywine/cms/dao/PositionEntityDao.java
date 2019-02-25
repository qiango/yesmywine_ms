package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PositionEntity;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

/**
 * Created by yly on 2017/1/16.
 */
@CacheConfig(cacheNames = "position")
public interface PositionEntityDao extends BaseRepository<PositionEntity, Integer> {
    PositionEntity findByPositionName(String positionName);

    List<PositionEntity> findByPositionTypeAndStatus(Integer positionType1, int i);

    List<PositionEntity> findByPositionType(int positionType1);

    List<PositionEntity> findByStatus(int status1);
}
