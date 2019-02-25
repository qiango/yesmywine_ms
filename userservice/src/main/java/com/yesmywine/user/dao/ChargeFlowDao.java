package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.ChargeFlow;

/**
 * Created by ${shuang} on 2017/4/5.
 */
public interface ChargeFlowDao extends BaseRepository<ChargeFlow,Integer> {

    ChargeFlow findByUserId(Integer userId);

    ChargeFlow findByUserIdAndOrderNumber(Integer userId, String orderNumber);

    ChargeFlow findByOrderNumber(String chargeNumber);
}
