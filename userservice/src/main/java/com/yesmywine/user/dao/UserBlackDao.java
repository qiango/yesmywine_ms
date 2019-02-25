package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.UserBlack;
import org.springframework.stereotype.Repository;

/**
 * Created by ${shuang} on 2017/4/10.
 */
@Repository
public interface UserBlackDao extends BaseRepository<UserBlack, Integer> {
    UserBlack findByUserId(Integer userId);
}