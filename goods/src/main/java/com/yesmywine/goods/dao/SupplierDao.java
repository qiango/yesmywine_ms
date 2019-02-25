package com.yesmywine.goods.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.goods.bean.DeleteEnum;
import com.yesmywine.goods.entityProperties.Supplier;

import java.util.List;

/**
 * Created by wangdiandian on 2017/3/15.
 */
public interface SupplierDao extends BaseRepository<Supplier, Integer> {
    List<Supplier> findByDeleteEnum(DeleteEnum deleteEnum);
    Supplier findBySupplierCode(String supplierCode);

}
