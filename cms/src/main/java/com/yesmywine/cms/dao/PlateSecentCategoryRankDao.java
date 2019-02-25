package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PlateSecentCategoryRank;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface PlateSecentCategoryRankDao extends BaseRepository<PlateSecentCategoryRank,Integer> {

    PlateSecentCategoryRank findByCategoryId(Integer categoryId);

    List<PlateSecentCategoryRank> findByPlateFirstId(Integer plateFirstId);

    void deleteByPlateFirstId(Integer plateFirstId);
}
