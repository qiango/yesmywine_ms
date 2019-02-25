package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldFirst;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldFirstDao extends BaseRepository<OldFirst,Integer> {

    OldFirst findByName(String name);

    List<OldFirst> findByPositionId(Integer positionId);
}
