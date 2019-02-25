
package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.dao.CategoryDao;
import com.yesmywine.goods.dao.SkuDao;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.service.CategoryService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/goods/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private CategoryDao categoryDao;

    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,String isShow,String deleteEnum,Integer id,String type) {   //查看
        MapUtil.cleanNull(params);
        if(id != null&&type==null){
            Category category = categoryService.findOne(id);
            category.setPropertyInfo(categoryService.getOne(id));
            return ValueUtil.toJson(category);
        }else if(id != null&&type!=null&&type.equals("isSku")){
            return ValueUtil.toJson(categoryService.getSKUProperty(id));
        }else if(id != null&&type!=null&&type.equals("notSku")){
            return ValueUtil.toJson(categoryService.getOrdinaryProperty(id));
        }
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(categoryService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        if(ValueUtil.notEmpity(isShow)){
            if(isShow.equals("all")){
                params.remove("isShow");
            }else {
                params.put("isShow_eq_com.yesmywine.goods.bean.IsShow",isShow);
                params.remove("isShow");
            }
        }else {
            params.remove("isShow");
        }
        if(ValueUtil.isEmpity(deleteEnum)){
            params.put("deleteEnum_eq_com.yesmywine.goods.bean.DeleteEnum","NOT_DELETE");
            params.remove("deleteEnum");
        }
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK,categoryService.findAll(pageModel));
    }

    //根据等级查找分类
    @RequestMapping(value = "/findByLevel",method = RequestMethod.GET)
    public String findByLevel(Integer level){
        try {
            ValueUtil.verify(level,"level");
            List<Category> categoryList = categoryService.findByLevel(level);
            return ValueUtil.toJson(categoryList);
        }catch (YesmywineException e){
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

    //根据等级查找分类
    @RequestMapping(value = "/showOne",method = RequestMethod.GET)
    public String getName(Integer categoryId){
        Category category = categoryDao.findOne(categoryId);
        return ValueUtil.toJson(category.getCategoryName());
    }
    //根据等级查找分类
    @RequestMapping(value = "/showOne/itf",method = RequestMethod.GET)
    public String getNewName(Integer categoryId){
        Category category = categoryDao.findOne(categoryId);
        return ValueUtil.toJson(category.getCategoryName());
    }

    @RequestMapping(value = "/getChildren/itf", method = RequestMethod.GET)
    public String findAllChildren(Integer parentId) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,categoryService.findAllChildrenByParentId(parentId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public String showCatogory(String all) {   //分类树结构显示
        try {
            List<Category> list=categoryService.showCategory();
            com.alibaba.fastjson.JSONArray firstChild = new com.alibaba.fastjson.JSONArray();
            for(int i=0;i<list.size();i++){
                Integer parentId=list.get(i).getId();
                Category c=new Category();
                c.setId(parentId);
                List<Category> list1=categoryDao.findByParentName(c);
                if(list1!=null){
                    com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
                    com.alibaba.fastjson.JSONArray secondChild = new com.alibaba.fastjson.JSONArray();
                    for(int j=0;j<list1.size();j++){
                        Category childCate = list1.get(j);
                        com.alibaba.fastjson.JSONObject second = new com.alibaba.fastjson.JSONObject();
                        second.put("value",childCate.getId());
                        second.put("label",childCate.getCategoryName());
                        secondChild.add(second);

                        if(all!=null&&all.equals("y")){
                            List<Category> grandChildList = categoryDao.findByParentName(childCate);
                            if(grandChildList!=null){
                                com.alibaba.fastjson.JSONArray thildChildren = new com.alibaba.fastjson.JSONArray();
                                for(Category grandChildCate:grandChildList){
                                    com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
                                    third.put("value",grandChildCate.getId());
                                    third.put("label",grandChildCate.getCategoryName());
                                    thildChildren.add(third);
                                }
                                if(thildChildren.size()>0){
                                    second.put("children",thildChildren);
                                }
                            }
                        }

                    }
                    first.put("value",list.get(i).getId()) ;
                    first.put("label",list.get(i).getCategoryName());
                    if(secondChild.size()>0){
                        first.put("children",secondChild);
                    }
                    firstChild.add(first);
                }

            }
            return ValueUtil.toJson(HttpStatus.SC_OK,firstChild);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(String jsonData) {//分类同步
        try {
                return ValueUtil.toJson(HttpStatus.SC_CREATED,categoryService.synchronous(jsonData));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}