package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PlateSecentCategoryLabel;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface PlateSecentCategoryLabelDao extends BaseRepository<PlateSecentCategoryLabel,Integer> {

    PlateSecentCategoryLabel findByCategoryId(Integer categoryId);

    List<PlateSecentCategoryLabel> findByPlateFirstId(Integer plateFirstId);

    void deleteByPlateFirstId(Integer plateFirstId);
}
