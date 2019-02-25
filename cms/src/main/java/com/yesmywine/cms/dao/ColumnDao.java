package com.yesmywine.cms.dao;

import com.yesmywine.base.record.repository.BaseRepository;
import com.yesmywine.cms.entity.ColumnsEntity;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Created by liqingqing on 2017/1/4.
 */
@Repository
public interface ColumnDao extends BaseRepository<ColumnsEntity, Integer> {
    ColumnsEntity findById(Integer id);

    List<ColumnsEntity> findByColumnsNameContaining(String name);

    ColumnsEntity findByColumnsNameAndPId(String name,Integer pid);

    ColumnsEntity findByCode(String code);

    List<ColumnsEntity> findByPId(Integer pId);
}
