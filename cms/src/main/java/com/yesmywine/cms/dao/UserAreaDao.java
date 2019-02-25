package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.UserArea;

/**
 * Created by hz on 2017/5/16.
 */
public interface UserAreaDao extends BaseRepository<UserArea,Integer> {

    UserArea findByUserId(Integer userId);
}
