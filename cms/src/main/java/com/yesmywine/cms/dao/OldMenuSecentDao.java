package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldMenuSecent;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldMenuSecentDao extends BaseRepository<OldMenuSecent,Integer> {

    OldMenuSecent findBySecentCategoryId(Integer secentCategoryId);

    OldMenuSecent findBySecentIndexAndFirstMenuId(Integer secentIndex, Integer firstMenuId);

    OldMenuSecent findBySecentCategoryIdAndFirstMenuId(Integer secentCategoryId, Integer firstMenuId);

    List<OldMenuSecent> findByFirstMenuId(Integer firstMenuId);

    void deleteByFirstMenuId(Integer firstMenuId);
}
