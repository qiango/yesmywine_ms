package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PlateFirst;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface PlateFirstDao extends BaseRepository<PlateFirst,Integer> {

    @Query("from PlateFirst Order by firstIndex")
    List<PlateFirst> findAllOrderByFirstIndex();

    @Query("from PlateFirst Order by firstIndex")
    List<PlateFirst> findByIsShowOrderByFirstIndex(Integer isShow);
    @Query(value = "SELECT * FROM plateFirst ORDER BY firstIndex",nativeQuery = true)
    List<PlateFirst> findByFirst();

    PlateFirst findByFirstIndex(Integer index);

    PlateFirst findByFirstCategoryId(Integer firstCategoryId);

    List<PlateFirst> findByIsShow(Integer isShow);

    List<PlateFirst> findByFirstPositionIdOrSecentPositionIdOrThirdPositionIdOrFourthPositionIdOrAppPositionId(Integer firstPositionId, Integer secentPositionId,
                                                             Integer thirdPositionId, Integer fourthPositionId, Integer appPositionId);
}
