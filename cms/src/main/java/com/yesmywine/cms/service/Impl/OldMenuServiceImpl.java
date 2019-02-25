package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.CategoryDao;
import com.yesmywine.cms.dao.OldMenuFirstDao;
import com.yesmywine.cms.entity.MenuSecent;
import com.yesmywine.cms.entity.OldMenuSecent;
import com.yesmywine.cms.service.OldMenuService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.cms.dao.OldMenuSecentDao;
import com.yesmywine.cms.entity.Category;
import com.yesmywine.cms.entity.OldMenuFirst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class OldMenuServiceImpl implements OldMenuService {

    @Autowired
    private OldMenuFirstDao menuFirstDao;
    @Autowired
    private OldMenuSecentDao menuSecentDao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public Object findOne(Integer menuFirstId) {
        OldMenuFirst one = this.menuFirstDao.findOne(menuFirstId);
        if(ValueUtil.isEmpity(one)){
            return "没有此导航";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("firstIndex", one.getFirstIndex());
        Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
        jsonObject.put("firstCategoryId", one.getFirstCategoryId());
        jsonObject.put("firstCategoryName", one1.getCategoryName());
        List<OldMenuSecent> byFirstMenuId = this.menuSecentDao.findByFirstMenuId(menuFirstId);
        for(OldMenuSecent menuSecent: byFirstMenuId){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", menuSecent.getId());
            jsonObjectSecent.put("secentIndex", menuSecent.getSecentIndex());
            Category one2 = this.categoryDao.findOne(menuSecent.getSecentCategoryId());
            jsonObjectSecent.put("secentCategoryId", menuSecent.getSecentCategoryId());
            jsonObjectSecent.put("secentCategoryName", one2.getCategoryName());

            JSONArray jsonArrayChild = new JSONArray();
            List<Category> byParentId = this.categoryDao.findByParentId(menuSecent.getSecentCategoryId());
            for(Category category: byParentId){
                JSONObject categoryChild = new JSONObject();
                categoryChild.put("categoryId", category.getId());
                categoryChild.put("categoryName", category.getCategoryName());
                jsonArrayChild.add(categoryChild);
            }
            jsonObjectSecent.put("categoryChild", jsonArrayChild);

            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("secentMenu", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<OldMenuFirst> all = this.menuFirstDao.findAllOrderByFirstIndex();
        JSONArray jsonArrayRe = new JSONArray();
        for(OldMenuFirst one: all) {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("firstIndex", one.getFirstIndex());
            Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
            jsonObject.put("firstCategoryId", one.getFirstCategoryId());
            jsonObject.put("firstCategoryName", one1.getCategoryName());
            List<OldMenuSecent> byFirstMenuId = this.menuSecentDao.findByFirstMenuId(one.getId());
            for (OldMenuSecent menuSecent : byFirstMenuId) {
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", menuSecent.getId());
                jsonObjectSecent.put("secentIndex", menuSecent.getSecentIndex());
                Category one2 = this.categoryDao.findOne(menuSecent.getSecentCategoryId());
                jsonObjectSecent.put("secentCategoryId", menuSecent.getSecentCategoryId());
                jsonObjectSecent.put("secentCategoryName", one2.getCategoryName());

                JSONArray jsonArrayChild = new JSONArray();
                List<Category> byParentId = this.categoryDao.findByParentId(menuSecent.getSecentCategoryId());
//                if(0!=byParentId.size()) {
                    for (Category category : byParentId) {
                        JSONObject categoryChild = new JSONObject();
                        categoryChild.put("categoryId", category.getId());
                        categoryChild.put("categoryName", category.getCategoryName());
                        jsonArrayChild.add(categoryChild);
                    }
                    jsonObjectSecent.put("categoryChild", jsonArrayChild);
//                }

                jsonArray.add(jsonObjectSecent);
            }
            jsonObject.put("secentMenu", jsonArray);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(Integer firstCategoryId, Integer firstIndex, String jsonString) {
        try {
            OldMenuFirst byFirstIndex = this.menuFirstDao.findByFirstIndex(firstIndex);
            if(ValueUtil.notEmpity(byFirstIndex)){
                return "此一级导航权重已存在";
            }

            OldMenuFirst menuFirst = new OldMenuFirst();
            menuFirst.setFirstCategoryId(firstCategoryId);
            menuFirst.setFirstIndex(firstIndex);
            Integer menuFirstId = null;
            OldMenuFirst byFirstCategoryId = this.menuFirstDao.findByFirstCategoryId(firstCategoryId);
            if(ValueUtil.isEmpity(byFirstCategoryId)){
                this.menuFirstDao.save(menuFirst);
                menuFirstId = menuFirst.getId();
            }else {
                return "此一级导航已存在";
            }

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer secentCategoryId = Integer.valueOf(jsonObject.get("categoryId").toString());
                    Integer secentIndex = Integer.valueOf(jsonObject.get("index").toString());
                    OldMenuSecent menuSecent = new OldMenuSecent();
                    menuSecent.setFirstMenuId(menuFirstId);
                    menuSecent.setSecentCategoryId(secentCategoryId);
                    OldMenuSecent bySecentIndex = this.menuSecentDao.findBySecentIndexAndFirstMenuId(secentIndex, menuFirstId);
                    if(ValueUtil.notEmpity(bySecentIndex)){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return "此二级导航权重已存在";
                    }
                    menuSecent.setSecentIndex(secentIndex);
                    OldMenuSecent bySecent= this.menuSecentDao.findBySecentCategoryIdAndFirstMenuId(secentCategoryId, menuFirstId);
                    if(ValueUtil.notEmpity(bySecent)){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return "此二级导航已存在";
                    }
                    this.menuSecentDao.save(menuSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, Integer firstCategoryId, Integer firstIndex, String jsonString) {
        try {
            OldMenuFirst byFirstIndex = this.menuFirstDao.findByFirstIndex(firstIndex);
            if(ValueUtil.notEmpity(byFirstIndex) && byFirstIndex.getId() != id){
                return "此一级导航权重已存在";
            }
            OldMenuFirst byFirstCategoryId = this.menuFirstDao.findByFirstCategoryId(firstCategoryId);
            if(ValueUtil.notEmpity(byFirstCategoryId) && id!=byFirstCategoryId.getId()){
                return "此一级导航已存在";
            }
            OldMenuFirst menuFirst = new OldMenuFirst();
            menuFirst.setFirstCategoryId(firstCategoryId);
            menuFirst.setFirstIndex(firstIndex);
            menuFirst.setId(id);
            this.menuFirstDao.save(menuFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer secentCategoryId = Integer.valueOf(jsonObject.get("categoryId").toString());
                    Integer secentIndex = Integer.valueOf(jsonObject.get("index").toString());

                    Object id1 = jsonObject.get("id");
                    OldMenuSecent menuSecent = new OldMenuSecent();
                    if(ValueUtil.notEmpity(id1)){
                        Integer secentId = Integer.valueOf(id1.toString());//一级主键id
                        menuSecent = this.menuSecentDao.findOne(secentId);
                        OldMenuSecent bySecentIndex = this.menuSecentDao.findBySecentIndexAndFirstMenuId(secentIndex, id);
                        if(ValueUtil.notEmpity(bySecentIndex)&& bySecentIndex.getId() != secentId){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return "此二级导航权重已存在";
                        }
                        OldMenuSecent bySecent= this.menuSecentDao.findBySecentCategoryIdAndFirstMenuId(secentCategoryId, id);
                        if(ValueUtil.notEmpity(bySecent)&&bySecent.getId()!=secentId){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return "此二级导航已存在";
                        }

                    }else {
                        OldMenuSecent bySecentIndex = this.menuSecentDao.findBySecentIndexAndFirstMenuId(secentIndex, id);
                        if(ValueUtil.notEmpity(bySecentIndex)){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return "此二级导航权重已存在";
                        }
                        OldMenuSecent bySecent= this.menuSecentDao.findBySecentCategoryIdAndFirstMenuId(secentCategoryId, id);
                        if(ValueUtil.notEmpity(bySecent)){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return "此二级导航已存在";
                        }
                    }

                    menuSecent.setFirstMenuId(id);
                    menuSecent.setSecentCategoryId(secentCategoryId);
                    menuSecent.setSecentIndex(secentIndex);
//                    MenuSecent bySecentIndex = this.menuSecentDao.findBySecentIndex(secentIndex);
//                    if(ValueUtil.notEmpity(bySecentIndex)&& bySecentIndex.getId() != menuFirstId){
//                        return "此二级导航权重已存在";
//                    }
                    OldMenuSecent bySecentCategoryId = this.menuSecentDao.findBySecentCategoryId(secentCategoryId);
                    if(ValueUtil.notEmpity(bySecentCategoryId)){
                        menuSecent.setId(bySecentCategoryId.getId());
                    }
                    this.menuSecentDao.save(menuSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String deleteFirst(Integer id) {
        try{
            this.menuFirstDao.delete(id);
            this.menuSecentDao.deleteByFirstMenuId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.menuSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
