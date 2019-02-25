package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.ActivityFirst;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface ActivityFirstDao extends BaseRepository<ActivityFirst,Integer> {

    ActivityFirst findByName(String name);

//    @Query("select a from ActivityFirst a where a.appPosition like %?1% order by a.appPosition")
    List<ActivityFirst> findByAppPositionLikeOrderByAppPosition(String appPosition);

    List<ActivityFirst> findByAppPosition(String appPosition);

    List<ActivityFirst> findByPositionIdBanner(Integer positionIdBanner);

}
