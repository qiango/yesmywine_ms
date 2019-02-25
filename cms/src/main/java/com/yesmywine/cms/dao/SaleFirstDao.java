package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.SaleFirst;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
public interface SaleFirstDao  extends BaseRepository<SaleFirst,Integer> {
    SaleFirst findByName(String name);

    List<SaleFirst> findByPositionId(Integer positionId);
}
