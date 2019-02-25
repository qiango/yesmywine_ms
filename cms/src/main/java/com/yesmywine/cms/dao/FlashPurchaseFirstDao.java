package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.FlashPurchaseFirst;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface FlashPurchaseFirstDao extends BaseRepository<FlashPurchaseFirst,Integer> {
    FlashPurchaseFirst findByName(String name);

    List<FlashPurchaseFirst> findByActivityId(Integer activityId);
}
