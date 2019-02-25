package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldPosition;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldPositionDao extends BaseRepository<OldPosition,Integer> {

    List<OldPosition> findByFirstPositionIdOrSecentPositionId(Integer firstPositionId, Integer secentPositionId);

}
