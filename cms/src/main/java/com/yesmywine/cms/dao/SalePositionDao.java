package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.SalePosition;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface SalePositionDao  extends BaseRepository<SalePosition,Integer> {

    List<SalePosition> findByPositionId(Integer positionId);
}
