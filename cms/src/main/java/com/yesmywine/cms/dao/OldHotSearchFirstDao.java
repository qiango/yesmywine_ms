package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldHotSearchFirst;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldHotSearchFirstDao extends BaseRepository<OldHotSearchFirst,Integer> {

    OldHotSearchFirst findByHotSearchFirstName(String hotSearchFirstName);
}
