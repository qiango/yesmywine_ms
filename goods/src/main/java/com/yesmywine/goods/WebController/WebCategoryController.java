package com.yesmywine.goods.WebController;

import com.yesmywine.goods.dao.CategoryDao;
import com.yesmywine.goods.dao.SkuDao;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.service.CategoryService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by by on 2017/7/12.
 */
@RestController
@RequestMapping("/web/goods/categories")
public class WebCategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryDao categoryDao;

//    @RequestMapping(value = "/getChildren", method = RequestMethod.GET)
//    public String findAllChildren(Integer parentId) {
//        List<Category> categoryList = categoryService.findAllChildrenByParentId(parentId);
//        HttpBean httpBean = new HttpBean();
//        return ValueUtil.toJson(categoryList);
//    }

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
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/getChildren",method = RequestMethod.GET)
    public String getChildren(Integer parentId) {   
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, categoryService.findAllChildrenByParentIdWeb(parentId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/findByLevel",method = RequestMethod.GET)
    public String findByLevel(Integer level){
        try {
            ValueUtil.verify(level,"level");
            List<Category> categoryList = categoryService.findByLevel(level);
            return ValueUtil.toJson(categoryList);
        }catch (YesmywineException e){
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

    //根据等级查找分类
    @RequestMapping(value = "/showOne",method = RequestMethod.GET)
    public String getName(Integer categoryId){
        Category category = categoryDao.findOne(categoryId);
        return ValueUtil.toJson(category.getCategoryName());
    }

    @RequestMapping(value = "/getParents", method = RequestMethod.GET)
    public String getParents(Integer categoryId) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,categoryService.getParents(categoryId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
}
