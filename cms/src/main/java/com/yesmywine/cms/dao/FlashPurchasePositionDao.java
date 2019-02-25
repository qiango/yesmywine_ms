package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.FlashPurchasePosition;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface FlashPurchasePositionDao  extends BaseRepository<FlashPurchasePosition,Integer> {

    List<FlashPurchasePosition> findByPositionId(Integer positionId);

}
