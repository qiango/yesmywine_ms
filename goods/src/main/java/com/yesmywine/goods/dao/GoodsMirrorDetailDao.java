package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.entity.GoodsMirrorDetail;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 6/27/17.
 */
@Repository
public interface GoodsMirrorDetailDao extends BaseRepository<GoodsMirrorDetail,Integer>{
    GoodsMirrorDetail findByGoodsMirrorId(Integer goodsMirrorId);
}
