package com.yesmywine.user.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.user.entity.PerDayPrize;

/**
 * Created by ${shuang} on 2017/6/15.
 */
public interface PerDayPrizeDao extends BaseRepository<PerDayPrize, Integer> {

    PerDayPrize findByPrizeId(int i);
}
