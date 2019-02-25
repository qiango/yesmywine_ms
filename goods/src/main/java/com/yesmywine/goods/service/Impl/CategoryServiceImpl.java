
package com.yesmywine.goods.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.bean.DeleteEnum;
import com.yesmywine.goods.bean.IsShow;
import com.yesmywine.goods.bean.IsSku;
import com.yesmywine.goods.dao.CategoryDao;
import com.yesmywine.goods.dao.CategoryPropertyDao;
import com.yesmywine.goods.dao.ProperValueDao;
import com.yesmywine.goods.dao.PropertiesDao;
import com.yesmywine.goods.entity.CategoryProperty;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.CategoryService;
import com.yesmywine.util.basic.JSONUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wangdiandian on 2016/12/9.
 */
@Service
@Transactional
public class CategoryServiceImpl extends BaseServiceImpl<Category, Integer> implements CategoryService {

    @Autowired
    private CategoryDao categoryRepository;

    @Autowired
    private CategoryPropertyDao categoryPropertyDao;

    @Autowired
    private ProperValueDao properValueDao;

    @Autowired
    private PropertiesDao propertiesDao;

    public String insert(String categoryName, Integer parentId, String code, String isShow,String url) { //插入分类
        if (null == categoryRepository.findIdByCategoryName(categoryName)) {
            Category category = new Category();
            if (isShow.equals("0")) {
                category.setIsShow(IsShow.yes);
            } else {
                category.setIsShow(IsShow.no);
            }
            category.setCategoryName(categoryName);
            category.setDeleteEnum(DeleteEnum.NOT_DELETE);
//            category.setUrl(url);
            category.setCode(code);
            Category category1 = null;
            if(parentId!=null){
                category1 = categoryRepository.findOne(parentId);
            }
            category.setParentName(category1);
            categoryRepository.save(category);
            return "success";
        }
        return "该类型已存在";
    }

    public List findByDeleteEnumAndIsShow(DeleteEnum deleteEnum, IsShow isShow) {//找出未被删除可查看的分类
        return categoryRepository.findByDeleteEnumAndIsShow(deleteEnum, isShow);
    }

    public List<Category> showCategory(){
        return categoryRepository.findByParentName(null);
    }

    private void updateChildCategoryProperty(Category category, JSONArray delPJ) throws YesmywineException {
        List<Category> childCategoryList = categoryRepository.findByParentName(category);
        if(childCategoryList != null&&delPJ!=null){
            for(int i=0;i<delPJ.size();i++ ){
                JSONObject jsonObject = (JSONObject)delPJ.get(i);
                Integer propertyId = jsonObject.getInteger("propertyId");
                String valueIds = jsonObject.getString("valueIds");
                if(valueIds == null||valueIds.equals("")){//如果爲空删除直接根据属性ID删除关联
                    for(Category childCategory:childCategoryList){
                        categoryPropertyDao.deleteByCategoryIdAndPropertyId(childCategory.getId(),propertyId);
                        List<Category> grandchildCategoryList = categoryRepository.findByParentName(category);
                        if(grandchildCategoryList!=null){
                            for (Category grandchild:grandchildCategoryList) {
                                categoryPropertyDao.deleteByCategoryIdAndPropertyId(grandchild.getId(),propertyId);
                            }
                        }
                    }

                }else{//否则根据属性ID及属性值ID
                    String[] valueArr = valueIds.split(";");
                    for(String valueId:valueArr){
                        for(Category childCategory:childCategoryList){
                            PropertiesValue propertiesValue = null;
                            if(valueId!=null&&!valueId.equals("")){
                                propertiesValue = properValueDao.findOne(Integer.valueOf(valueId));
                                if(propertiesValue==null){
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    ValueUtil.isError("包含不存在的属性值");
                                }
                            }
                            categoryPropertyDao.deleteByCategoryIdAndPropertyIdAndPropertyValue(childCategory.getId(),propertyId,propertiesValue);

                            List<Category> grandchildCategoryList = categoryRepository.findByParentName(childCategory);
                            if(grandchildCategoryList!=null){
                                for (Category grandchild:grandchildCategoryList) {
                                    PropertiesValue propertiesValueChild = null;
                                    if(valueId!=null&&!valueId.equals("")){
                                        propertiesValueChild = properValueDao.findOne(Integer.valueOf(valueId));
                                        if(propertiesValue==null){
                                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                            ValueUtil.isError("包含不存在的属性值");
                                        }
                                    }
                                    categoryPropertyDao.deleteByCategoryIdAndPropertyIdAndPropertyValue(grandchild.getId(),propertyId,propertiesValueChild);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private JSONArray updateParentCategoryProperty(Category category, JSONArray pJson) throws YesmywineException {
        JSONArray correlationArray = new JSONArray();

        Category parentCategory = category.getParentName();
        for(int i=0;i<pJson.size();i++ ){
            JSONObject jsonObject = (JSONObject)pJson.get(i);
            Integer propertyId = jsonObject.getInteger("propertyId");
            String valueIds = jsonObject.getString("valueIds");
            String[] valueArr = valueIds.split(";");
            for(String valueId:valueArr){
                JSONObject correlationObject = new JSONObject();
                CategoryProperty categoryProperty = new CategoryProperty();
                PropertiesValue propertiesValue = null;
                if(valueId!=null&&!valueId.equals("")){
                    propertiesValue = properValueDao.findOne(Integer.valueOf(valueId));
                    if(propertiesValue==null){
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        ValueUtil.isError("包含不存在的属性值");
                    }
                }
                categoryProperty.setCategoryId(category.getId());
                categoryProperty.setPropertyId(propertyId);
                categoryProperty.setPropertyValue(propertiesValue);
                categoryProperty.setType(0);
                categoryPropertyDao.save(categoryProperty);
                correlationObject.put("categoryId",String.valueOf(category.getId()));
                correlationObject.put("propertyId",String.valueOf(propertyId));
                correlationObject.put("propertyValueId",valueId);
                correlationArray.add(correlationObject);
                if(parentCategory != null){
                    Category parent = categoryRepository.findOne(parentCategory.getId());
                    while (parent!=null){
                        PropertiesValue queryPV = null;
                        if(valueId!=null&&!valueId.equals("")){
                            queryPV = new PropertiesValue();
                            queryPV.setId(Integer.valueOf(valueId));
                        }
                        CategoryProperty isExist = categoryPropertyDao.findByCategoryIdAndPropertyIdAndPropertyValue(parent.getId(),propertyId,queryPV);
                        if(isExist == null){
                            CategoryProperty saveCP = new CategoryProperty();
                            saveCP.setCategoryId(parent.getId());
                            saveCP.setPropertyId(propertyId);
                            PropertiesValue findPV = null;
                            if(valueId!=null&&!valueId.equals("")){
                                findPV = properValueDao.findOne(Integer.valueOf(valueId));
                                if(findPV==null){
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    ValueUtil.isError("包含不存在的属性值");
                                }
                            }
                            saveCP.setPropertyValue(findPV);
                            categoryPropertyDao.save(saveCP);
                            correlationObject.put("categoryId",String.valueOf(category.getId()));
                            correlationObject.put("propertyId",String.valueOf(propertyId));
                            correlationObject.put("propertyValueId",valueId);
                            correlationArray.add(correlationObject);
                        }

                        Category pparent = parent.getParentName();
                        if(pparent==null){
                            parent = null;
                        }else{
                            parent = categoryRepository.findOne(pparent.getId());
                        }
                    }
                }else{
                    PropertiesValue queryPV = null;
                    if(valueId!=null&&!valueId.equals("")){
                        queryPV = new PropertiesValue();
                        queryPV.setId(Integer.valueOf(valueId));
                    }
                    CategoryProperty isExist = categoryPropertyDao.findByCategoryIdAndPropertyIdAndPropertyValue(category.getId(),propertyId,queryPV);
                    if(isExist == null){
                        CategoryProperty saveCP = new CategoryProperty();
                        saveCP.setCategoryId(category.getId());
                        saveCP.setPropertyId(propertyId);
                        PropertiesValue findPV = null;
                        if(valueId!=null&&!valueId.equals("")){
                            findPV = properValueDao.findOne(Integer.valueOf(valueId));
                            if(findPV==null){
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                ValueUtil.isError("包含不存在的属性值");
                            }
                        }
                        saveCP.setPropertyValue(findPV);
                        categoryPropertyDao.save(saveCP);
                        correlationObject.put("categoryId",String.valueOf(category.getId()));
                        correlationObject.put("propertyId",String.valueOf(propertyId));
                        correlationObject.put("propertyValueId",valueId);
                        correlationArray.add(correlationObject);
                    }
                }
            }
        }

        return correlationArray;
    }

    public String synchronous(String jsondata)throws YesmywineException {
        JSONObject dataJSON = JSON.parseObject(jsondata);
        String msg = dataJSON.getString("msg");
        if(msg.equals("save")){
            saveCategory(jsondata);
        }else if(msg.equals("update")){
            updateCategory(jsondata);
        }else if(msg.equals("delete")){
            deleteCategory(jsondata);
        }

        return "success";
    }

    private String deleteCategory(String jsondata) {
        JSONObject dataJSON = JSON.parseObject(jsondata);
        String id = dataJSON.getString("data");
        Category category=categoryRepository.findOne(Integer.parseInt(id));
        categoryRepository.delete(category);
        categoryPropertyDao.deleteByCategoryId(category.getId());
        return "delete success";
    }

    private String updateCategory(String jsondata)throws YesmywineException {
        try {
            JSONObject dataJSON = JSON.parseObject(jsondata);
            JSONObject data = dataJSON.getJSONObject("data");
            String categoryJSON = data.getString("category");
            String propertyJson = data.getString("propertyJson");
            String delPropertyJson = data.getString("delPropertyJson");
            Object obj = JSONUtil.jsonStrToObject(categoryJSON,Category.class);
            Category category = (Category)obj;
            categoryRepository.save(category);

            categoryPropertyDao.deleteByCategoryId(category.getId());
            updateParentCategoryProperty(category,JSON.parseArray(propertyJson));
            updateChildCategoryProperty(category,JSON.parseArray(delPropertyJson));
            return "update success";

        }catch (Exception e){
            e.printStackTrace();
            ValueUtil.isError("json转换错误");
        }

        return null;

    }
    public JSONArray getSKUProperty(Integer categoryId) {
        List<CategoryProperty> cpList = categoryPropertyDao.getSKUProperty(categoryId, IsSku.yes);
        Collections.sort(cpList, new Comparator<CategoryProperty>() {
            @Override
            public int compare(CategoryProperty o1, CategoryProperty o2) {
                return o1.getPropertyId().compareTo(o2.getPropertyId());
            }
        });
        JSONArray resultJSON = new JSONArray();
        JSONArray valueJson = new JSONArray();
        Integer propertyId = 0;
        int i = 0;
        JSONObject jsonObject = new JSONObject();
        int j = 0;
        for (CategoryProperty cp : cpList) {
            Integer newPropertyId = cp.getPropertyId();
            PropertiesValue propertiesValue = cp.getPropertyValue();
            if (!propertyId.equals(newPropertyId)) {
                if (i > 0) {
                    jsonObject.put("values", valueJson);
                    resultJSON.add(jsonObject);
                    jsonObject = new JSONObject();
                    valueJson = new JSONArray();
                }
                propertyId = cp.getPropertyId();
                Properties property = propertiesDao.findOne(propertyId);
                jsonObject.put("property", property);
                valueJson.add(propertiesValue);
                i = 0;
            } else {
                valueJson.add(propertiesValue);
            }
            i++;
            j++;
            if (j == cpList.size()) {
                jsonObject.put("values", valueJson);
                resultJSON.add(jsonObject);
            }
        }
        return resultJSON;
    }

    public JSONArray getOrdinaryProperty(Integer categoryId) {
        List<CategoryProperty> cpList = categoryPropertyDao.getOrdinaryProperty(categoryId, IsSku.no);
        JSONArray resultJSON = new JSONArray();
        JSONArray valueJson = new JSONArray();
        Integer propertyId = 0;
        int i = 0;
        JSONObject jsonObject = new JSONObject();
        int j = 0;
        for (CategoryProperty cp : cpList) {

            Integer newPropertyId = cp.getPropertyId();
            PropertiesValue propertiesValue = cp.getPropertyValue();
            if (!propertyId.equals(newPropertyId)) {
                if (i > 0) {
                    jsonObject.put("values", valueJson);
                    resultJSON.add(jsonObject);
                    jsonObject = new JSONObject();
                    valueJson = new JSONArray();
                }
                propertyId = cp.getPropertyId();
                Properties property = propertiesDao.findOne(propertyId);
                jsonObject.put("property", property);
                valueJson.add(propertiesValue);
                i = 0;
            } else {
                valueJson.add(propertiesValue);
            }
            i++;
            j++;
            if (j == cpList.size()) {
                jsonObject.put("values", valueJson);
                resultJSON.add(jsonObject);
            }
        }
        return resultJSON;
    }
    public JSONArray getOne(Integer categoryId) {
        List<CategoryProperty> cpList = categoryPropertyDao.findByCategoryIdOrderByPropertyId(categoryId);
        Collections.sort(cpList, new Comparator<CategoryProperty>() {
            @Override
            public int compare(CategoryProperty o1, CategoryProperty o2) {
                return o1.getPropertyId().compareTo(o2.getPropertyId());
            }
        });
        JSONArray resultJSON = new JSONArray();
        JSONArray valueJson = new JSONArray();
        Integer propertyId = 0;
        int i = 0;
        JSONObject jsonObject = new JSONObject();
        int j = 0;
        for (CategoryProperty cp : cpList) {
            Integer newPropertyId = cp.getPropertyId();
            PropertiesValue propertiesValue = cp.getPropertyValue();
            if (!propertyId.equals(newPropertyId)) {
                if (i > 0) {
                    jsonObject.put("values", valueJson);
                    resultJSON.add(jsonObject);
                    jsonObject = new JSONObject();
                    valueJson = new JSONArray();
                }
                propertyId = cp.getPropertyId();
                Properties property = propertiesDao.findOne(propertyId);
                jsonObject.put("property", property);
                valueJson.add(propertiesValue);
                i = 0;
            } else {
                valueJson.add(propertiesValue);
            }
            i++;
            j++;
            if (j == cpList.size()) {
                jsonObject.put("values", valueJson);
                resultJSON.add(jsonObject);
            }
        }
        return resultJSON;
    }

    public List<Category> findByLevel(Integer level) {

        return categoryRepository.findByLevel(level);
    }


    public JSONArray findAllChildrenByParentId(Integer parentId) {
        Category category = new Category();
        category.setId(parentId);
//        List<Category> categoryList = categoryRepository.getAllChildren(parentId);
//        Map categoryList = categoryRepository.getAllChildrenStr(parentId);
        List<Category> categoryList=categoryRepository.findByParentName(category);
        JSONArray jsonArray=new JSONArray();
        if(categoryList.size()!=0){
            for(Category c:categoryList){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("value",c.getId());
                jsonObject.put("label",c.getCategoryName());
                JSONArray jsonArray1=new JSONArray();
                List<Category> categoryList1=categoryRepository.findByParentName(categoryRepository.findOne(c.getId()));
                if(categoryList1.size()!=0) {
                    for (Category b : categoryList1) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("value",b.getId());
                        jsonObject1.put("label",b.getCategoryName());
                        jsonArray1.add(jsonObject1);
                    }
                    jsonObject.put("children",jsonArray1);
                }
                jsonArray.add(jsonObject);
            }
        }
//        System.out.println(categoryList);
        return jsonArray;
    }

    public JSONObject findAllChildrenByParentIdWeb(Integer parentId) {
        JSONObject jsonObjectFirst=new JSONObject();
        Category cd=categoryRepository.findOne(parentId);
        jsonObjectFirst.put("id",cd.getId());
        jsonObjectFirst.put("name",cd.getCategoryName());
        jsonObjectFirst.put("image",cd.getImage());
        Category category = new Category();
        category.setId(parentId);
        List<Category> categoryList=categoryRepository.findByParentName(category);
        JSONArray jsonArray=new JSONArray();
        if(categoryList.size()!=0){
            for(Category c:categoryList){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("id",c.getId());
                jsonObject.put("name",c.getCategoryName());
                jsonObject.put("image",c.getImage());
                JSONArray jsonArray1=new JSONArray();
                List<Category> categoryList1=categoryRepository.findByParentName(categoryRepository.findOne(c.getId()));
                if(categoryList1.size()!=0) {
                    for (Category b : categoryList1) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("id",b.getId());
                        jsonObject1.put("name",b.getCategoryName());
                        jsonObject1.put("image",b.getImage());
                        jsonArray1.add(jsonObject1);
                    }
                    jsonObject.put("children",jsonArray1);
                }
                jsonArray.add(jsonObject);
            }
            jsonObjectFirst.put("children",jsonArray);
        }
        return jsonObjectFirst;
    }

    @Override
    public JSONObject getParents(Integer categoryId) throws YesmywineException {
        JSONObject jsonObject=new JSONObject();
        Category category=categoryRepository.findOne(categoryId);
        if(null!=category) {
            String categoryName = category.getCategoryName();
            jsonObject.put("categoryId", category.getId());
            jsonObject.put("categoryName", categoryName);
            Category categorySecond = category.getParentName();
            if (null != categorySecond) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("categoryId", categorySecond.getId());
                jsonObject1.put("categoryName", categorySecond.getCategoryName());
                jsonObject.put("parents", jsonObject1);
                Category categoryFirst = categorySecond.getParentName();
                if (null != categoryFirst) {
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("categoryId", categoryFirst.getId());
                    jsonObject2.put("categoryName", categoryFirst.getCategoryName());
                    jsonObject1.put("parents", jsonObject2);
                }
            }
        }
        return jsonObject;
    }

    private String saveCategory(String jsondata)throws YesmywineException {
        try {
            JSONObject dataJSON = JSON.parseObject(jsondata);
            JSONObject data = dataJSON.getJSONObject("data");
            String categoryJSON = data.getString("category");
            String propertyJson = data.getString("propertyJson");
            Object obj = JSONUtil.jsonStrToObject(categoryJSON,Category.class);
            Category category = (Category)obj;
            categoryRepository.save(category);
            updateParentCategoryProperty(category, JSON.parseArray(propertyJson));
            return "add success";
        }catch (Exception e){

            e.printStackTrace();
            ValueUtil.isError("json转换错误");
        }

        return null;

    }
}
