package com.yesmywine.ware.service.impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.ware.dao.WarehouseDao;
import com.yesmywine.ware.entity.Warehouses;
import com.yesmywine.ware.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SJQ on 2017/2/10.
 */
@Service
@Transactional
public class WarehouseServiceImpl extends BaseServiceImpl<Warehouses, Integer>
        implements WarehouseService {
    @Autowired
    private WarehouseDao warehouseDao;

    public void delete(Integer warehouseId) {
        warehouseDao.delete(warehouseId);
    }

    public Boolean findByWarehouseName(String warehouseName) {
        return null;
    }

    public Boolean checkIsNull(Integer warehouseId) {
        return null;
    }
}
