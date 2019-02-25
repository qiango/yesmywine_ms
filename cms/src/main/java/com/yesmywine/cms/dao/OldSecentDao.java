package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.OldSecent;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface OldSecentDao extends BaseRepository<OldSecent,Integer> {

    OldSecent findByGoodsIdAndOldFirstId(Integer goodsId, Integer oldFirstId);

    List<OldSecent> findByOldFirstId(Integer oldFirstId);

    void deleteByOldFirstId(Integer oldFirstId);
}
