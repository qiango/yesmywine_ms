package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.MenuFirst;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface MenuFirstDao extends BaseRepository<MenuFirst,Integer> {

    @Query("from MenuFirst Order by firstIndex")
    List<MenuFirst> findAllOrderByFirstIndex();

    MenuFirst findByFirstIndex(Integer firstIndex);

    MenuFirst findByFirstCategoryId(Integer firstCategoryId);
}
