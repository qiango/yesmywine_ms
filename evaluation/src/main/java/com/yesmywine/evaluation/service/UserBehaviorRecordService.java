package com.yesmywine.evaluation.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.evaluation.bean.UserBehaviorRecord;


/**
 * Created by light on 2017/1/9.
 */
public interface UserBehaviorRecordService extends BaseService<UserBehaviorRecord, Integer> {

    String saveUserBehaviorRecord(UserBehaviorRecord userBehaviorRecord);
}
