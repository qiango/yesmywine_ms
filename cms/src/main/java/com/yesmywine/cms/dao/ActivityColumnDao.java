package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.ActivityColumn;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface ActivityColumnDao extends BaseRepository<ActivityColumn,Integer> {

    List<ActivityColumn> findByActivityId(Integer activityId);

    List<ActivityColumn> findByActivityFirstId(Integer activityFirstId);

    void deleteByActivityFirstId(Integer activityFirstId);

    List<ActivityColumn> findByPositionId(Integer positionId);

    ActivityColumn findByActivityFirstIdAndName(Integer id,String name);

}
