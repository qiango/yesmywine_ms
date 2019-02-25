package com.yesmywine.sso.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.sso.bean.UserInformation;

/**
 * Created by SJQ on 2017/6/18.
 */
public interface UserInfoDao extends BaseRepository<UserInformation,Integer> {
    UserInformation findByUserName(String loginName);
}
