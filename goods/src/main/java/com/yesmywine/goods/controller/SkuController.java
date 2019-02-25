
package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.dao.ProperValueDao;
import com.yesmywine.goods.dao.PropertiesDao;
import com.yesmywine.goods.dao.SkuDao;
import com.yesmywine.goods.dao.SkuPropDao;
import com.yesmywine.goods.entity.Sku;
import com.yesmywine.goods.entity.SkuCommonProp;
import com.yesmywine.goods.entity.SkuProp;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.entityProperties.Supplier;
import com.yesmywine.goods.service.ProService;
import com.yesmywine.goods.service.ProperValueService;
import com.yesmywine.goods.service.SkuService;
import com.yesmywine.goods.util.Utils;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hz on 3/15/17.
 */
@RestController
@RequestMapping("/goods/sku")
public class SkuController {

    @Autowired
    private SkuService skuService;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private SkuPropDao skuPropDao;
    @Autowired
    private ProService proService;
    @Autowired
    private ProperValueService properValueService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {   //查看所有Sku
        MapUtil.cleanNull(params);
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            List<Sku> all = skuService.findAll();
            List<Sku> content = new ArrayList<>();
            for(Sku sku:all){
                List<SkuProp> skuProp = sku.getSkuProp();
                com.alibaba.fastjson.JSONObject RejsonObject = new com.alibaba.fastjson.JSONObject();
                for(SkuProp s:skuProp){
                    Properties properties = this.proService.findById(s.getPropId());
                    PropertiesValue propertiesValue = this.properValueService.findById(s.getPropValueId());
                    RejsonObject.put(properties.getCnName(), propertiesValue.getCnValue());
                }
                sku.setProperty(RejsonObject.toJSONString());
                content.add(sku);
            }
            return ValueUtil.toJson(content);
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }

        if(ValueUtil.notEmpity(params.get("skuId"))){
            Sku sku = skuService.findOne(Integer.valueOf(params.get("skuId").toString()));
            List<SkuProp> skuProp = sku.getSkuProp();
            com.alibaba.fastjson.JSONObject RejsonObject = new com.alibaba.fastjson.JSONObject();
            for(SkuProp s:skuProp){
                Properties properties = this.proService.findById(s.getPropId());
                PropertiesValue propertiesValue = this.properValueService.findById(s.getPropValueId());
                RejsonObject.put(properties.getCnName(), propertiesValue.getCnValue());
            }
            sku.setProperty(RejsonObject.toJSONString());
            List<SkuCommonProp> list=sku.getSkuCommonProp();
            for(SkuCommonProp skuCommonProp:list){
                String propValueName=null;
                Properties properties = this.propertiesDao.findOne(skuCommonProp.getPropId());
                String propValueId=skuCommonProp.getPropValueId();
                if(null!=propValueId&&!propValueId.equals("")){
                    if(properties.getEntryMode()== EntryMode.lists) {
                        PropertiesValue propertiesValue = this.properValueDao.findOne(Integer.parseInt(propValueId));
                        propValueName = propertiesValue.getCnValue();
                    }
                }
                skuCommonProp.setPropName(properties.getCnName());
                skuCommonProp.setPropValueName(propValueName);
            }
            return ValueUtil.toJson(HttpStatus.SC_OK,sku);
        }

        if(ValueUtil.notEmpity(params.get("categoryId"))&& Utils.isNum(params.get("categoryId").toString())){
            Integer categoryId = Integer.valueOf(params.get("categoryId").toString()) ;
            Category category = new Category();
            category.setId(categoryId);
            params.remove(params.remove("categoryId").toString());
            params.put("category",category);
        }else {
            params.remove("categoryId");
        }
        if(ValueUtil.notEmpity(params.get("supplierId"))&& Utils.isNum(params.get("supplierId").toString())){
            Integer supplierId = Integer.valueOf(params.get("supplierId").toString()) ;
            Supplier supplier = new Supplier();
            supplier.setId(supplierId);
            params.remove(params.remove("supplierId").toString());
            params.put("supplier",supplier);
        }else {
            params.remove("supplierId");
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        if(ValueUtil.isEmpity(params.get("skuName_l"))){
            params.remove("skuName_l");
        }
        if(ValueUtil.isEmpity(params.get("code_l"))){
            params.remove("code_l");
        }
        pageModel.addCondition(params);
        PageModel all = skuService.findAll(pageModel);
//        List<Sku> content = all.getContent();
//        List<Sku> content2 = new ArrayList<>();
//        for(Sku sku:content){
//            List<SkuProp> skuProp = sku.getSkuProp();
//            com.alibaba.fastjson.JSONObject RejsonObject = new com.alibaba.fastjson.JSONObject();
//            for(SkuProp s:skuProp){
//                Properties properties = this.propertiesDao.findOne(s.getPropId());
//                PropertiesValue propertiesValue = this.properValueDao.findOne(s.getPropValueId());
//                RejsonObject.put(properties.getCnName(), propertiesValue.getCnValue());
//            }
//            sku.setProperty(RejsonObject.toJSONString());
//            content2.add(sku);
//        }
//        all.setContent(content2);
        return ValueUtil.toJson(HttpStatus.SC_OK,all);
    }

    @RequestMapping(method = RequestMethod.PUT)  //同步成本价
    public String updateCostPrice(String jsonArray) {
        try {
            ValueUtil.verify(jsonArray,"jsonArray");
            JSONArray jsonArrayNew = new JSONArray(jsonArray);
            List<Sku> skuList=new ArrayList<>();
            for (int i = 0; i < jsonArrayNew.length(); i++) {
                JSONObject jsonObject = jsonArrayNew.getJSONObject(i);
                Sku sku=skuService.findOne(Integer.parseInt(jsonObject.get("skuId") + ""));
                sku.setCostPrice((double) jsonObject.get("price"));
                skuList.add(sku);
            }
            skuService.save(skuList);
            return ValueUtil.toJson(HttpStatus.SC_OK,"success");
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/itf",method = RequestMethod.PUT)  //同步成本价
    public String updateCostPriceitf(String jsonArray) {
        try {
            ValueUtil.verify(jsonArray,"jsonArray");
            JSONArray jsonArrayNew = new JSONArray(jsonArray);
            List<Sku> skuList=new ArrayList<>();
            for (int i = 0; i < jsonArrayNew.length(); i++) {
                JSONObject jsonObject = jsonArrayNew.getJSONObject(i);
                Sku sku=skuService.findOne(Integer.parseInt(jsonObject.get("skuId") + ""));
                sku.setCostPrice((double)jsonObject.get("price"));
                skuList.add(sku);
            }
            skuService.save(skuList);
            return ValueUtil.toJson(HttpStatus.SC_OK,"success");
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(@RequestParam Map<String,String> map){//sku同步
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,skuService.synchronous(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "itf/getCostPrice", method = RequestMethod.GET)
    public String getCostPrice(Integer skuId,String skuCode){//获取成本价
        if(null==skuCode){
            Sku sku=skuDao.findOne(skuId);
            String price=String.valueOf(sku.getCostPrice());
            return ValueUtil.toJson(HttpStatus.SC_OK,price);
        }else
            return ValueUtil.toJson(HttpStatus.SC_OK,skuDao.findByCode(skuCode).getCostPrice());
    }

}
