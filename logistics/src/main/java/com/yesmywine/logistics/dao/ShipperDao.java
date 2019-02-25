package com.yesmywine.logistics.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.logistics.entity.Shippers;
import org.springframework.stereotype.Repository;

/**
 * Created by wangdiandian on 2017/3/27.
 */
@Repository
public interface ShipperDao extends BaseRepository<Shippers,Integer> {
    Shippers findByShipperCode(String shipperCode);
}
