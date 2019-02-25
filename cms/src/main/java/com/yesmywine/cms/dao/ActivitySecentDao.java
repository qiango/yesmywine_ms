package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.ActivitySecent;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface ActivitySecentDao extends BaseRepository<ActivitySecent,Integer> {

    List<ActivitySecent> findByColumnId(Integer columnId);

    void deleteByColumnId(Integer columnId);
}
