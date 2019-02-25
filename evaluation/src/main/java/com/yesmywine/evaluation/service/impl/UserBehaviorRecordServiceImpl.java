package com.yesmywine.evaluation.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.evaluation.bean.UserBehaviorRecord;
import com.yesmywine.evaluation.dao.UserBehaviorRecordDao;
import com.yesmywine.evaluation.service.UserBehaviorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户行为记录
 * Created by light on 2017/2/10.
 */
@Service
public class UserBehaviorRecordServiceImpl extends BaseServiceImpl<UserBehaviorRecord, Integer> implements UserBehaviorRecordService {

    @Autowired
    private UserBehaviorRecordDao userBehaviorRecordRep;

    //插入
    public String saveUserBehaviorRecord(UserBehaviorRecord userBehaviorRecord) {
        if (null == userBehaviorRecord.getId()) {
            userBehaviorRecord.setCreateTime(new Date());
        }
        this.userBehaviorRecordRep.save(userBehaviorRecord);
        return "success";
    }
}
