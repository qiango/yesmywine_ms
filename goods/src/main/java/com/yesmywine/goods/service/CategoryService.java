
package com.yesmywine.goods.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseService;
import com.yesmywine.goods.bean.DeleteEnum;
import com.yesmywine.goods.bean.IsShow;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.util.error.YesmywineException;

import java.util.List;

/**
 * Created by hz on 2/10/17.
 */
public interface CategoryService extends BaseService<Category, Integer> {
    String insert(String categoryName, Integer parentId, String code, String isShow, String url) throws YesmywineException;

    List findByDeleteEnumAndIsShow(DeleteEnum deleteEnum, IsShow isShow) throws YesmywineException;

    List<Category> showCategory() throws YesmywineException;

    String synchronous(String jsonData) throws YesmywineException;

    JSONArray getSKUProperty(Integer categoryId);

    JSONArray getOrdinaryProperty(Integer categoryId);

    JSONArray getOne(Integer categoryId);

    List<Category> findByLevel(Integer level);

    JSONArray findAllChildrenByParentId(Integer parentId)throws YesmywineException;

    JSONObject findAllChildrenByParentIdWeb(Integer parentId)throws YesmywineException;

    JSONObject getParents(Integer categoryId)throws YesmywineException;
}



