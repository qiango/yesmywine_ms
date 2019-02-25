package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.HomePagePosition;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface HomePagePositionDao extends BaseRepository<HomePagePosition,Integer> {

    List<HomePagePosition> findByBannerPositionIdOrPositionIdOneOrPositionIdTwoOrPositionIdThree(Integer bannerPositionId, Integer positionIdOne,
                                                                 Integer positionIdTwo, Integer positionIdThree);

}
