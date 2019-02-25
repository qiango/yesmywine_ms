package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldMenuFirst;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldMenuFirstDao extends BaseRepository<OldMenuFirst,Integer> {

    @Query("from OldMenuFirst Order by firstIndex")
    List<OldMenuFirst> findAllOrderByFirstIndex();

    OldMenuFirst findByFirstIndex(Integer firstIndex);

    OldMenuFirst findByFirstCategoryId(Integer firstCategoryId);
}
