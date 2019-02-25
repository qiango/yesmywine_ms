package com.yesmywine.cms.service;

import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.ColumnsEntity;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by liqingqing on 2017/1/4.
 */

public interface ColumnService extends BaseService<ColumnsEntity,Integer> {


     String create(Map<String, String> parm) throws YesmywineException;
     String initialize() throws YesmywineException;

     String update(Integer id, String columnsName, Integer pId) throws YesmywineException;

     String delete(Integer id) throws YesmywineException;

     ColumnsEntity show(Integer id) throws YesmywineException;

     List<ColumnsEntity> index() throws YesmywineException;
}
