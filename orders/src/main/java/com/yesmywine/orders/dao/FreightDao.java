package com.yesmywine.orders.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.orders.entity.Freight;
import org.springframework.stereotype.Repository;

/**
 * Created by hz on 6/8/17.
 */
@Repository
public interface FreightDao extends BaseRepository<Freight,Integer> {
    Freight findByAreaName(String areaName);
}
