
package com.yesmywine.goods.service.Impl;

import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.goods.bean.CanSearch;
import com.yesmywine.goods.entityProperties.*;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.service.ProService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.bean.IsSku;
import com.yesmywine.goods.bean.IsUse;
import com.yesmywine.goods.dao.*;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by hz on 1/6/17.
 */
@Service
@Transactional
public class PropServiceImpl extends BaseServiceImpl<com.yesmywine.goods.entityProperties.Properties, Integer> implements ProService {
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private PropGoodsDao propGoodsDao;
    @Autowired
    private GoodsDao goodsRepository;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private CategoryDao categoryDao;

    public String addPrpo(Map<String, String> parm) {   //新增属性
//        Integer categoryId = Integer.parseInt(parm.get("categoryId"));
        String isSku = parm.get("isSku");   //0是
        String canSearch = parm.get("canSearch");//0是
        String cnName = parm.get("cnName");
        String id = parm.get("id");


        com.yesmywine.goods.entityProperties.Properties properties = new com.yesmywine.goods.entityProperties.Properties();
        properties.setCnName(cnName);
        properties.setCode(parm.get("code"));
        properties.setIsUse(IsUse.no);
        if (isSku.equals("yes")) {
            properties.setIsSku(IsSku.yes);
            if (canSearch.equals("no")) {
                properties.setCanSearch(CanSearch.no);
            } else
                properties.setCanSearch(CanSearch.yes);
            properties.setEntryMode(EntryMode.nullall);
            properties.setId(Integer.valueOf(id));
            propertiesDao.save(properties);
            return "success";
        } else
            properties.setIsSku(IsSku.no);
        if (canSearch.equals("no")) {
            properties.setCanSearch(CanSearch.no);
        } else {
            properties.setCanSearch(CanSearch.yes);
        }
        String entryMode = parm.get("entryMode");
        if(entryMode.equals("manual")){
            properties.setEntryMode(EntryMode.manual);
        }else {
            properties.setEntryMode(EntryMode.lists);
        }
        properties.setId(Integer.valueOf(id));
        propertiesDao.save(properties);
        return "success";
    }

    public String updateProp(Integer id,String canSearch,String cnName,String isSku,String entryMode,String isCanShow) {
        com.yesmywine.goods.entityProperties.Properties entity = propertiesDao.findOne(id);
        if (entity.getIsUse() == IsUse.no) {
            entity.setCnName(cnName);
            if (canSearch.equals("yes")) {
                entity.setCanSearch(CanSearch.yes);
            } else {
                entity.setCanSearch(CanSearch.no);
            }
            if (isSku.equals("yes")) {
                entity.setIsSku(IsSku.yes);
                entity.setEntryMode(EntryMode.nullall);
            } else if (entryMode.equals("manual")) {  //手动
                entity.setIsSku(IsSku.no);
                entity.setEntryMode(EntryMode.manual);
            } else {
                entity.setIsSku(IsSku.no);
                entity.setEntryMode(EntryMode.lists);
            }
            propertiesDao.save(entity);
            return "success";
        } else if (canSearch.equals("yes")) {
            entity.setCanSearch(CanSearch.yes);
        } else {
            entity.setCanSearch(CanSearch.no);
        }
        entity.setCnName(cnName);
        propertiesDao.save(entity);
//        if (valueJson == null) {
//            return "update success";
//        } else if (entity.getIsSku() == IsSku.yes) {
//            JsonParser jsonParser = new JsonParser();
//            JsonArray arr = jsonParser.parse(valueJson).getAsJsonArray();
//            for (int i = 0; i < arr.size(); i++) {
//                String value = arr.get(i).getAsJsonObject().get("value").getAsString();
//                String code = arr.get(i).getAsJsonObject().get("code").getAsString();
//                List<PropertiesValue> p=properValueDao.findByCnValueAndPropertiesId(value, id);
//                if (0==p.size()) {
//                    PropertiesValue propertiesValue = new PropertiesValue();
//                    propertiesValue.setCnName(cnName);
//                    propertiesValue.setPropertiesId(id);
//                    propertiesValue.setCode(code);
//                    propertiesValue.setCnValue(value);
//                    properValueDao.save(propertiesValue);
//                }else {
//                    return "该属性下属性值已存在";
//                }
//            }
//        } else {
//            String[] arr = valueJson.split(",");
//            for (int i = 0; i < arr.length; i++) {
//                List<PropertiesValue> propertiesValue1=properValueDao.findByCnValueAndPropertiesId(arr[i],id);
//                if (0 == propertiesValue1.size()){
//                    PropertiesValue propertiesValue = new PropertiesValue();
//                    propertiesValue.setPropertiesId(id);
//                    propertiesValue.setCnName(cnName);
//                    propertiesValue.setCnValue(arr[i]);
//                    properValueDao.save(propertiesValue);
//                }else{
//                    return "该属性下属性值已经存在";
//                }
//            }
//        }
        return "success";
    }

    @CacheEvict(value= CacheStatement.ACTIVITY_VALUE)
    public String updateAdd(Integer propId, String code, String value){
        PropertiesValue propertiesValue = new PropertiesValue();
        propertiesValue.setCnValue(value);
        propertiesValue.setCode(code);
        propertiesValue.setPropertiesId(propId);
        properValueDao.save(propertiesValue);
        return "success";
    }

    @CacheEvict(value= CacheStatement.ACTIVITY_VALUE)
    public String deleteProp( Map<String, String> parm){
        String id1 = parm.get("id");
        com.yesmywine.goods.entityProperties.Properties properties = propertiesDao.findOne(Integer.valueOf(id1));
        if(null==properties){
            return "success";
        }
        propertiesDao.delete(properties);
        return "success";
    }

//    public Map<String, List<String>> getProperByCategory(Integer categoryId) {    //通过分类获取可查询的属性和值（前台用）
//        Category category=new Category();
//        category.setId(categoryId);
//        List<Properties> list = propertiesDao.findByCategoryAndCanSearch(category,CanSearch.yes);
//        Map<String, List<String>> properyName = new HashMap<>();
//        for (int i = 0; i < list.size(); i++) {
//            List<PropertiesValue> listValue = properValueDao.findByPropertiesId(list.get(i).getId());
//            String name = propertiesDao.findOne(list.get(i).getId()).getCnName();
//            if (null == properyName.get(name)) {
//                properyName.put(name, new ArrayList<>());
//            }
//            for (int j = 0; j < listValue.size(); j++) {
//                String cnValue = listValue.get(j).getCnValue();
//                properyName.get(name).add(cnValue);
//            }
//        }
//        return properyName;
//    }

//    @Override
    public String synchronous(Map<String, String> map) throws YesmywineException {
        if(0==Integer.parseInt(String.valueOf(map.get("synchronous")))){
            addPrpo(map);
            return "add success";
        }else if(2==Integer.parseInt(String.valueOf(map.get("synchronous")))){
            deleteProp(map);
            return "delete success";
        }else{
            Integer id=Integer.parseInt(String.valueOf(map.get("id")));
            String canSearch=String.valueOf(map.get("canSearch"));
            String cnName=String.valueOf(map.get("cnName"));
            String isSku=String.valueOf(map.get("isSku"));
            String entryMode=String.valueOf(map.get("entryMode"));
            String isCanShow=map.get("isCanShow");
//            String valueJson=String.valueOf(map.get("valueJson"));
            updateProp(id,canSearch, cnName, isSku, entryMode,isCanShow);
            return "update success";
        }

    }


//    public Map<String, List<String>> getGeneralProp(Integer categoryId) {   //通过分类属性及部分属性的值
//        Map<String, List<String>> prop = new HashMap<>();
//        Category category=new Category();
//        category.setId(categoryId);
//        List<Properties> propties = propertiesDao.findByCategoryAndDeleteEnum(category,DeleteEnum.NOT_DELETE);
//        propties.forEach(l -> {
//            String cnName = l.getCnName();
//            if (null == prop.get(cnName)) {
//                prop.put(cnName, new ArrayList<>());
//            }
//            List<PropertiesValue> values = properValueDao.findByPropertiesId(l.getId());
//            if (null != values) {
//                values.forEach(k -> {
//                    prop.get(cnName).add(k.getCnValue());
//                });
//            }
//        });
//        return prop;
//    }

//    public String getRepertory(String skuId) {  //查看库存
//        HttpBean httpRequest = new HttpBean("http://88.88.88.211:8191/inventory/goodsInventory" + "/" + skuId, RequestMethod.get);
//        httpRequest.run();
//        String amount = httpRequest.getResponseContent();
//        return amount;
//    }


    @Override
    @Cacheable(value= CacheStatement.ACTIVITY_VALUE,key = "'Properties_'+#id")
    public Properties findById(Integer id){
        return this.propertiesDao.findOne(id);
    }
}
