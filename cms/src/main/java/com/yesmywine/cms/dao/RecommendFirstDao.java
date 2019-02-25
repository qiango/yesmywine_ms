package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.RecommendFirst;

/**
 * Created by hz on 2017/5/16.
 */
public interface RecommendFirstDao extends BaseRepository<RecommendFirst,Integer> {

    RecommendFirst findByName(String name);
}
