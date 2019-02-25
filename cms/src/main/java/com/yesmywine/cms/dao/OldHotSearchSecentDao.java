package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldHotSearchSecent;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldHotSearchSecentDao extends BaseRepository<OldHotSearchSecent,Integer> {

    List<OldHotSearchSecent> findByHotSearchFirstId(Integer hotSearchFirstId);

    OldHotSearchSecent findByHotSearchFirstIdAndHotSearchSecentName(Integer hotSearchFirstId, String hotSearchSecentName);

    void deleteByHotSearchFirstId(Integer hotSearchFirstId);
}
