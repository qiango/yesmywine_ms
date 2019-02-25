package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by ${shuang} on 2017/2/10.
 */
public interface UserLevelService extends BaseService<UserInformation,Integer> {

    UserInformation vipUp(Integer userId, Integer growthValue ) throws YesmywineException;//用户升级

    String voluntarily() throws YesmywineException;//自动降级

    String update(Integer userId ,Integer levelId) throws YesmywineException;//更新






}
