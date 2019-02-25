package com.yesmywine.evaluation.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.evaluation.bean.UserBehaviorRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by light on 2017/1/9.
 */
@Repository
public interface UserBehaviorRecordDao extends BaseRepository<UserBehaviorRecord, Integer> {
}
