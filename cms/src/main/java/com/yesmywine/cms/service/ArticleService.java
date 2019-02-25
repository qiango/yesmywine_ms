package com.yesmywine.cms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.cms.entity.ArticleEntity;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;
import java.util.Map;

/**
 * Created by liqingqing on 2017/1/9.
 */

public interface ArticleService extends BaseService<ArticleEntity,Integer> {

    public String create(Map<String, String> parm) throws YesmywineException;

    public String update(Integer id, Map<String, String> parm) throws YesmywineException;

    public ArticleEntity show(Integer id) throws YesmywineException;

    JSONArray showContent(String code,Integer number)throws YesmywineException;

    public String deletes(Integer id) throws YesmywineException;

    public List<ArticleEntity> index(Integer columnsId) throws YesmywineException;

//    PageModel findByActivityId(String columnName, Integer pageNo, Integer pageSize);

    PageModel findByColumnName(Integer pageNo, Integer pageSize);

}
