package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.RecommendSecent;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface RecommendSecentDao extends BaseRepository<RecommendSecent,Integer> {

    RecommendSecent findByGoodsIdAndRecommendFirstId(Integer goodsId, Integer recommendFirstId);

    List<RecommendSecent> findByRecommendFirstId(Integer recommendFirstId);

    void deleteByRecommendFirstId(Integer recommendFirstId);
}
