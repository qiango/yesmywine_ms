package com.yesmywine.sso.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.sso.bean.UserInformation;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by SJQ on 2017/6/18.
 */
public interface UserInfoService extends BaseService<UserInformation,Integer> {
    UserInformation findByUsername(String loginName);

    UserInformation register(String userName, String password, String registerChannel,String phoneVerifyCode) throws YesmywineException;

    void delete(UserInformation userInformation);

    UserInformation findById(Integer id);
}
