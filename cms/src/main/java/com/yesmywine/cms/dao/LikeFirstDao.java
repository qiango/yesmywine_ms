package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.LikeFirst;

/**
 * Created by hz on 2017/5/16.
 */
public interface LikeFirstDao extends BaseRepository<LikeFirst,Integer> {

    LikeFirst findByName(String name);
}
