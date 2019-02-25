
package com.yesmywine.cms.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.CategoryDao;
import com.yesmywine.cms.entity.Category;
import com.yesmywine.cms.service.CategoryService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by hz on 1/6/17.
 */
@Service
@Transactional
public class CategoryServiceImpl extends BaseServiceImpl<Category, Integer> implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Boolean synchronous(Integer id, String name, Integer parentId, Integer synchronous) {
        Boolean resutl = false;
        if(0 == synchronous || 1 == synchronous){
            resutl = this.save(id, name, parentId);
        }else {
            resutl = this.delete(id);
        }
        return resutl;
    }

    public Boolean save(Integer id, String name, Integer parentId){
        try {
            Category category = new Category();
            category.setId(id);
            category.setCategoryName(name);
            if(ValueUtil.notEmpity(parentId)) {
                category.setParentId(parentId);
            }
            this.categoryDao.save(category);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public Boolean delete(Integer id){
        try {
            this.categoryDao.delete(id);
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
