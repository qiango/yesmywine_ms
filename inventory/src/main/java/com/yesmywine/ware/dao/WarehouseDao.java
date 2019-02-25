package com.yesmywine.ware.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.ware.entity.Warehouses;
import org.springframework.cache.annotation.CacheConfig;

/**
 * Created by SJQ on 2017/1/9.
 *
 * @Description:
 */
@CacheConfig(cacheNames = "warehouse")
public interface WarehouseDao extends BaseRepository<Warehouses, Integer> {
    Warehouses findByWarehouseCode(String exportWarehouseCode);

    Warehouses findByWarehouseName(String warehouseName);
}
