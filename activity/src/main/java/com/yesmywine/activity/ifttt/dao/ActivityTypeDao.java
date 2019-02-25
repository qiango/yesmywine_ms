package com.yesmywine.activity.ifttt.dao;


import com.yesmywine.activity.entity.ActivityType;
import com.yesmywine.base.record.repository.BaseRepository;

/**
 * Created by by on 2017/7/10.
 */
public interface ActivityTypeDao extends BaseRepository<ActivityType,Integer> {
    ActivityType findByCode(String type);
}
