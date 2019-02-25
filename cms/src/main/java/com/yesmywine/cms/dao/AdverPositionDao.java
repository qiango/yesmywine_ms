package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.entity.PositionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yly on 2017/1/16.
 */
@Repository
public interface AdverPositionDao extends BaseRepository<AdverPositionEntity, Integer> {

    List<AdverPositionEntity> findByPositionEntity(PositionEntity positionEntity);

    List<AdverPositionEntity> findByAdverEntity(AdverEntity adverEntity);

    List<AdverPositionEntity> findByAdverEntityAndPositionEntity(AdverEntity adverEntity, PositionEntity positionEntity);

    void deleteByPositionEntity(PositionEntity positionEntity);

//    void deleteByPositionEntity(Integer positionId);

    void deleteByAdverEntity(AdverEntity adverEntity);

    void deleteByAdverEntityAndPositionEntity(AdverEntity adverEntity, PositionEntity positionEntity);

}
