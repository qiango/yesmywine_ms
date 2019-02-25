package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.SignFlow;

/**
 * Created by ${shuang} on 2017/7/11.
 */
public interface SignFlowDao extends BaseRepository<SignFlow, Integer> {



    SignFlow findByUserIdAndSignTime(Integer userId, String format);
}
