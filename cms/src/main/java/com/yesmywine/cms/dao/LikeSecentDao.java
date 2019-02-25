package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.LikeSecent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface LikeSecentDao extends BaseRepository<LikeSecent,Integer> {

    List<LikeSecent> findByGoodsIdAndLikeFirstId(Integer goodsId, Integer likeFirstId);

    List<LikeSecent> findByLikeFirstId(Integer likeFirstId);

    Page<LikeSecent> findByLikeFirstId(Integer likeFirstId, Pageable pageable);

    void deleteByLikeFirstId(Integer likeFirstId);
    @Query("select DISTINCT goodsId from LikeSecent")
    List<Integer> findByDistinct();
}
