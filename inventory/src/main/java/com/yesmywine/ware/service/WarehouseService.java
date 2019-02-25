package com.yesmywine.ware.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.ware.entity.Warehouses;

/**
 * Created by SJQ on 2017/1/9.
 *
 * @Description:
 */
public interface WarehouseService extends BaseService<Warehouses, Integer> {
    void delete(Integer warehouseId);

    Boolean findByWarehouseName(String warehouseName);

    Boolean checkIsNull(Integer warehouseId);
}
