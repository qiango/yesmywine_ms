package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.Category;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface CategoryDao extends BaseRepository<Category,Integer> {
        List<Category> findByParentId(Integer parentId);
}
