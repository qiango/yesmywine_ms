package com.yesmywine.user.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.user.entity.UserBlack;
import com.yesmywine.util.error.YesmywineException;

/**
 * Created by light on 2016/12/19.
 */
public interface UserBlackService extends BaseService<UserBlack, Integer> {

     UserBlack findByUserId(Integer userId);
     String  disable(Integer userId,String reason) throws YesmywineException;//加入黑名单
     String update(Integer userId,String reason) throws YesmywineException;//更改原因
     String recover(String userIdList) throws YesmywineException;//解除
     String isBlack(Integer userId) throws YesmywineException;//查看是否黑名单
}
