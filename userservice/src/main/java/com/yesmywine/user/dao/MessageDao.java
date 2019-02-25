package com.yesmywine.user.dao;


import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.Notices;

/**
 * Created by ${shuang} on 2017/6/30.
 */
public interface MessageDao extends BaseRepository<Notices, Integer> {
    Notices findByUserIdAndId(Integer userId, Integer messageId);
}
