package com.yesmywine.goods.service.Impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.bean.*;
import com.yesmywine.goods.entity.Sku;
import com.yesmywine.goods.entity.SkuCommonProp;
import com.yesmywine.goods.entity.SkuProp;
import com.yesmywine.goods.service.SkuService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.*;
import com.yesmywine.goods.dao.*;
import com.yesmywine.goods.entityProperties.*;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.error.YesmywineException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;

/**
 * Created by hz on 2/13/17.
 */
@Service
public class SkuServiceImpl extends BaseServiceImpl<Sku, Integer> implements SkuService {
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SupplierDao supplierDao;
    @Autowired
    private SkuPropDao skuPropDao;
    @Autowired
    private SkuCommonPropDao skuCommonPropDao;


    public String deleteSku(Integer skuId) {//删除ｓｋｕ
        if(skuDao.findOne(skuId).getIsUse()== IsUse.yes){
            return "该sku被用,不可删除";
        }
        skuDao.delete(skuId);
        return "success";
    }

    public Sku showSku(Integer skuId){
        Sku sku=skuDao.findOne(skuId);
        String propty=sku.getProperty();
        Map<String,String> map=new HashMap<>();
        JSONObject jsonObject = new JSONObject(propty);
        Iterator iterator = jsonObject.keys();
        for (int j = 0; j <jsonObject.length() ; j++) {
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Integer value = Integer.valueOf(jsonObject.getString(key));
                String propName = propertiesDao.findOne(Integer.parseInt(key)).getCnName();
                String propValue = properValueDao.findOne(value).getCnValue();
                map.put(propName, propValue);
            }
        }
//        sku.setCategory(categoryDao.findOne(sku.getCategoryId()));
//        sku.setSupplier(supplierDao.findOne(sku.getSupplierId()));
        sku.setProperty(map.toString().replace("=",":"));
        return sku;

    }

    public com.alibaba.fastjson.JSONArray getSku(Integer categoryId, Integer type) {   //通过分类id拿到属性及值
        Map<String, List<PropertiesValue>> sku = new HashMap<>();//(0:sku属性及值,1:普通属性,2:全部属性)
        List<com.yesmywine.goods.entityProperties.Properties> prop = null;
        if(ValueUtil.isEmpity(categoryId)){
            switch (type){
//                case 0:prop=propertiesDao.findByIsSkuAndDeleteEnum(IsSku.yes,DeleteEnum.NOT_DELETE);break;
//                case 1:prop=propertiesDao.findByIsSkuAndDeleteEnum(IsSku.no,DeleteEnum.NOT_DELETE);break;
//                case 2:prop=propertiesDao.findByDeleteEnum(DeleteEnum.NOT_DELETE);break;
            }
        }else {
            Category category=new Category();
            category.setId(categoryId);
            switch (type) {
                case 0:
//                    prop = propertiesDao.findByCategoryAndIsSkuAndDeleteEnum(category, IsSku.yes, DeleteEnum.NOT_DELETE);
                    break;
                case 1:
//                    prop = propertiesDao.findByCategoryAndIsSkuAndDeleteEnum(category, IsSku.no, DeleteEnum.NOT_DELETE);
                    break;
                case 2:
//                    prop = propertiesDao.findByCategoryAndDeleteEnum(category, DeleteEnum.NOT_DELETE);
                    break;
            }
        }
        com.alibaba.fastjson.JSONArray jsonArray1 = new com.alibaba.fastjson.JSONArray();
        prop.forEach(k -> {
            List<PropertiesValue> values = properValueDao.findByPropertiesId(k.getId());
            com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
            com.alibaba.fastjson.JSONArray jsonArray2 = new com.alibaba.fastjson.JSONArray();
            jsonObject1.put("value",k.getId());
            jsonObject1.put("label",k.getCnName());
            jsonArray1.add(jsonObject1);
            for(int i=0;i<values.size();i++){
                com.alibaba.fastjson.JSONObject jsonObject2 = new com.alibaba.fastjson.JSONObject();
                jsonObject2.put("value",values.get(i).getId());
                jsonObject2.put("label",values.get(i).getCnValue());
                jsonArray2.add(jsonObject2);
            }
            if(jsonArray2.size()>0){
                jsonObject1.put("children",jsonArray2);
            }
        });
        return jsonArray1;
    }

    public String create(Integer suppierId,String skuName,Integer categoryId,String skuJsonArray, Integer type) {  //保存sku
//// [{"id":"id","color":"红色","repertoryNumber":"10"},{"price":"100","color":"暗红色","repertoryNumber":"9"}]

        List<Sku> skuList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(skuJsonArray);
        Supplier supplier = supplierDao.findOne(suppierId);
        String suppierCode=supplier.getSupplierCode();
        Category category = categoryDao.findOne(categoryId);
        String categoryCode=category.getCode();
        for (int i = 0; i < jsonArray.length(); i++) {
            StringBuilder stringBuilder=new StringBuilder();
            Sku sku = new Sku();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            sku.setSupplier(supplier);
//            sku.setProperty(jsonObject+"");
            sku.setCategory(category);
            sku.setIsExpensive(1);
            sku.setSkuName(skuName);
            sku.setIsUse(IsUse.no);
            String code = jsonObject.get("code").toString();
            String skuString = jsonObject.get("sku").toString();
//            Integer skuId = Integer.valueOf(jsonObject.get("skuId").toString());
            String jsonProp = jsonObject.get("jsonProp").toString();
            JSONObject jsonObject1 = new JSONObject(jsonProp);
            Iterator iterator = jsonObject1.keys();
            List<SkuProp> list=new ArrayList<>();
            for (int j = 0; j <jsonObject1.length();j++) {
                while(iterator.hasNext()){
                    String key = (String) iterator.next();

                        com.yesmywine.goods.entityProperties.Properties properties = propertiesDao.findOne(Integer.valueOf(key));
                        properties.setIsUse(IsUse.yes);
                        propertiesDao.save(properties);
                        Integer value = Integer.valueOf(jsonObject1.getString(key));
                        SkuProp skuProp = new SkuProp();
                        skuProp.setPropId(Integer.parseInt(key));
                        skuProp.setPropValueId(value);
                        list.add(skuProp);
//                        PropertiesValue one = properValueDao.findOne(value);
//                        String code = one.getCode();
//                        skuString = skuString + " " + one.getCnValue();
//                        stringBuilder.append(code);

                }

            }

            Sku byCode = this.skuDao.findByCode(code);
            if(ValueUtil.notEmpity(byCode)){
                continue;
            }

            skuPropDao.save(list);
//            sku.setId(skuId);
            sku.setSkuProp(list);
            stringBuilder.append(suppierCode);
            stringBuilder.append(categoryCode);
            sku.setCode(code);
            sku.setSku(skuString);
            sku.setType(type);
//            skuList.add(sku);
            skuDao.save(sku);
        }
//        skuDao.save(skuList);
        return "success";
    }

    public String updateSkuProp(Integer skuId, String valueJson, String imgIds, String skuName,Integer isExpensive){
        Sku sku = skuDao.findOne(skuId);
        List<SkuCommonProp> list = new ArrayList<>();
        JSONObject obj = new JSONObject(valueJson);
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = obj.getString(key);
            if(ValueUtil.notEmpity(value)) {
                SkuCommonProp goodsProp = new SkuCommonProp();
                goodsProp.setPropId(Integer.parseInt(key));
                goodsProp.setPropValueId(value);
                list.add(goodsProp);
            }
        }
        skuCommonPropDao.save(list);
        sku.setSkuCommonProp(list);
        sku.setSkuName(skuName);
        sku.setIsExpensive(isExpensive);
        skuDao.save(sku);
        String goodImg = null;
        if (ValueUtil.notEmpity(imgIds)) {
            String[] imgArr = imgIds.split(";");
            Integer[] arr = new Integer[imgArr.length];
            for (int i = 0; i < imgArr.length; i++) {
                arr[i] = Integer.parseInt(imgArr[i]);
            }
            if (imgIds != null && !imgIds.equals("")) {
                try {
                    goodImg = saveGoodsImg(skuId, arr);
                } catch (YesmywineException e) {
                    e.printStackTrace();
                }
            }
        }
        sku.setImage(goodImg);
        skuDao.save(sku);
        return "success";
    }

    private String saveGoodsImg(Integer goodsId, Integer[] imgIds) throws YesmywineException {
        try{
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "sku");
            httpRequest.addParameter("mId", goodsId);
            String ids = "";
            String imageIds = "";
            for (int i = 0; i < imgIds.length; i++) {
                if (i == 0) {
                    ids = ids + imgIds[i];
//                    imageIds=imageIds+imageId[i];
                } else {
                    ids = ids + "," + imgIds[i];
//                    imageIds=imageIds+":"+imageId[i];
                }
//                category.setImageId(imageIds);
                httpRequest.addParameter("id", ids);
            }
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String cd = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(cd) || ValueUtil.isEmpity(cd)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("图片上传失败");
            } else {
                com.alibaba.fastjson.JSONArray maps = new com.alibaba.fastjson.JSONArray(imgIds.length);
                String result = ValueUtil.getFromJson(temp, "data");
                JsonParser jsonParser = new JsonParser();
                JsonArray image = jsonParser.parse(result).getAsJsonArray();
                for (int f = 0; f < image.size(); f++) {
                    String id = image.get(f).getAsJsonObject().get("id").getAsString();
                    String name = image.get(f).getAsJsonObject().get("name").getAsString();
                    com.alibaba.fastjson.JSONObject map1 = new com.alibaba.fastjson.JSONObject();
                    map1.put("id", id);
                    map1.put("name", name);
                    maps.add(map1);
                }

                String result1 =   maps.toJSONString().replaceAll( "\"", "\'");

//                com.alibaba.fastjson.JSONObject map = new com.alibaba.fastjson.JSONObject();
//                for (int i = 0; i < maps.size(); i++) {
//                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) maps.get(i);
//                    map.put("id" + i, jsonObject.getString("id"));
//                    map.put("name" + i, jsonObject.getString("name"));
//                }
//                map.put("num", String.valueOf(maps.size()));
                return result1;
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError("图片服务出现问题！");
        }
        return null;
    }

    @Override
    public String synchronous(Map<String, String> map){//sku同步
        if(0==Integer.parseInt(String.valueOf(map.get("synchronous")))){
            Integer suppierId=Integer.parseInt(String.valueOf(map.get("suppierId")));
            Integer categoryId =Integer.parseInt(String.valueOf(map.get("categoryId")));
            String skuName=String.valueOf(map.get("skuName"));
            Integer type=Integer.valueOf(map.get("type"));
            String skuJsonArray=map.get("skuJsonArray");
            create(suppierId,skuName,categoryId,skuJsonArray,type);
            return "add success";
        }else if(2==Integer.parseInt(String.valueOf(map.get("synchronous")))){
            skuDao.delete(Integer.parseInt(String.valueOf(map.get("id"))));
            return "delete success";
        }else {
            String valueJson=map.get("valueJson");
            Integer skuId=Integer.parseInt(map.get("skuId"));
            Integer isExpensive=Integer.parseInt(map.get("isExpensive"));
            String image=map.get("imgIds");
            String skuName=map.get("skuName");
            updateSkuProp(skuId,valueJson,image,skuName,isExpensive);
            return "update success";
        }
    }
}
