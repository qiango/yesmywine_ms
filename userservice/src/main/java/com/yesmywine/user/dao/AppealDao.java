package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.Appeal;

/**
 * Created by ${shuang} on 2017/4/6.
 */
public interface AppealDao extends BaseRepository<Appeal,Integer> {
    Appeal findByUserId(Integer userId);


    Appeal findByUserIdAndStatus(Integer userId, int i);
}
