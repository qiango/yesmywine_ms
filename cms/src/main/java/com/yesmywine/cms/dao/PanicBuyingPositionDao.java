package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PanicBuyingPosition;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface PanicBuyingPositionDao extends BaseRepository<PanicBuyingPosition,Integer> {

    List<PanicBuyingPosition> findByPositionId(Integer positionId);

}
