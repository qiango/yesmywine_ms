package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.PlateSecentGoods;

import java.util.List;

/**
 * Created by hz on 2017/5/16.
 */
public interface PlateSecentGoodsDao extends BaseRepository<PlateSecentGoods,Integer> {

    PlateSecentGoods findByGoodsId(Integer goodsId);

    List<PlateSecentGoods> findByPlateFirstId(Integer plateFirstId);

    void deleteByPlateFirstId(Integer plateFirstId);
}
