package com.yesmywine.goods.WebController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.bean.IsSku;
import com.yesmywine.goods.dao.*;
import com.yesmywine.goods.entity.*;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.GoodsService;
import com.yesmywine.goods.service.ProService;
import com.yesmywine.goods.service.ProperValueService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hz on 6/13/17.
 */
@RestController
@RequestMapping("/web/goods")
public class WebGoodsController {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private CategoryPropertyDao categoryPropertyDao;
    @Autowired
    private ProService proService;
    @Autowired
    private ProperValueService properValueService;

   @RequestMapping(value = "/updateGoodsLoad",method = RequestMethod.GET)
   public String updateGoodsLoad(Integer goodsId) {//编辑商品加载
      Goods goods= goodsDao.findOne(goodsId);
      List<GoodsProp> list=goods.getGoodsProp();
      List<Properties> listProp=propertiesDao.findByIsSku(IsSku.no);
    JSONArray jsonArray=new JSONArray();
    for(Properties properties:listProp){
        JSONObject jsonObject=new JSONObject();
        EntryMode e=properties.getEntryMode();
        if(e==EntryMode.lists){
            List<PropertiesValue> list1=properValueDao.findByPropertiesId(properties.getId());
            for(GoodsProp goodsProp:list){
                if(goodsProp.getPropId()==properties.getId()){
                    jsonObject.put("value",goodsProp.getPropValue());
                }
            }
            jsonObject.put("propList",list1);

        }else{
            for(GoodsProp goodsProp:list){
                if(goodsProp.getPropId()==properties.getId()){
                    jsonObject.put("value",goodsProp.getPropValue());
                }
            }
        }
        jsonObject.put("propName",properties.getCnName());
        jsonObject.put("id",properties.getId());
        jsonArray.add(jsonObject);
    }
    return ValueUtil.toJson(HttpStatus.SC_CREATED,jsonArray);

}

    @RequestMapping(value = "/getSkuId",method = RequestMethod.GET)
    public String getSkuId(Integer goodsId) {//购买福袋随机发放skuId
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, goodsService.getSkuId(goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/cancelSkuId",method = RequestMethod.POST)
    public String cancelSkuId(Integer goodsId,String jsonArray) {//取消福袋
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, goodsService.cancelSkuId(goodsId,jsonArray));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
//    @RequestMapping( method = RequestMethod.GET)
//    public Object index(Integer categoryId, Integer type, Integer pageNumber){//0:综合,1:销量,2:评论数量,3:新品
//        Page<Goods> goodsList = null;
//        switch (type){
//            case 0:
//            case 1:Sort sort = new Sort(Sort.Direction.DESC, "Comments");
//                Pageable pageable = new PageRequest(pageNumber, defaultPageSize, sort);
//                List<Goods> list=goodsDao.findByCategoryId(categoryId);
//                goodsList = goodsDao.findByCategoryId(pageable,categoryId);
//        }
//            return ValueUtil.toPageJson(goodsList);7
//        return null;
//    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String details(Integer goodsId) {   //商品详情
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.details(goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/showDetail",method = RequestMethod.GET)
    public String showDetail(Integer goodsId) {//展示商品详情(父文本)
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.showOne(goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/combination", method = RequestMethod.GET)
    public String combination(Integer goodsId,Integer size) {   //商品组合推荐
        try {
            return ValueUtil.toJson(goodsService.combination(goodsId,size));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/parameter", method = RequestMethod.GET)
    public String parameter(Integer goodsId) {   //商品参数
        try {
            return ValueUtil.toJson(goodsService.parameter(goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/showProp", method = RequestMethod.GET)
    public String indexShow(Integer goodsId) throws YesmywineException {//查询商品属性
        Goods one = this.goodsService.findOne(goodsId);
        if(ValueUtil.isEmpity(one)){
            ValueUtil.isError("无此商品");
        }
        List<CategoryProperty> byCategoryIdAndType = this.categoryPropertyDao.findByCategoryIdAndType(one.getCategoryId(), 1);
        List<Integer> propList = new ArrayList<>();
        for(CategoryProperty categoryProperty: byCategoryIdAndType){
            propList.add(categoryProperty.getPropertyId());
        }
        List<GoodsSku> goodsSkuList = one.getGoodsSku();
        JSONArray jsonArraySku = new JSONArray();
        for(GoodsSku goodsSku: goodsSkuList){
            Integer skuId = goodsSku.getSkuId();
            Sku sku = this.skuDao.findOne(skuId);
            JSONObject jsonObjectSku = new JSONObject();
            JSONArray jsonArraySkuProp = new JSONArray();
            List<SkuProp> skuPropList = sku.getSkuProp();
            for(SkuProp skuProp: skuPropList){
                JSONObject jsonObjectSkuProp = new JSONObject();
//                Properties properties = this.propertiesDao.findOne(skuProp.getPropId());
                Properties properties = this.proService.findById(skuProp.getPropId());
//                PropertiesValue propertiesValue = this.properValueDao.findOne(skuProp.getPropValueId());
                PropertiesValue propertiesValue = this.properValueService.findById(skuProp.getPropValueId());
                jsonObjectSkuProp.put("propertiesId", properties.getId());
                if(propList.contains(properties.getId())){
                    continue;
                }
                jsonObjectSkuProp.put("propertiesName", properties.getCnName());
                jsonObjectSkuProp.put("propertiesValueId", propertiesValue.getId());
                jsonObjectSkuProp.put("propertiesValueName", propertiesValue.getCnValue());
                jsonArraySkuProp.add(jsonObjectSkuProp);
            }
            jsonObjectSku.put("goodsSkuProp", jsonArraySkuProp);
            jsonObjectSku.put("skuName", sku.getSkuName());
            jsonArraySku.add(jsonObjectSku);
        }
        JSONArray jsonArrayProp = new JSONArray();
        List<GoodsProp> goodsPropList = one.getGoodsProp();
        for(GoodsProp goodsProp: goodsPropList){
//            if(0==goodsProp.getType()) {
                JSONObject jsonObjectProp = new JSONObject();
//                Properties properties = this.propertiesDao.findOne(goodsProp.getPropId());
            Properties properties = this.proService.findById(goodsProp.getPropId());
                jsonObjectProp.put("propertiesId", properties.getId());
                if(propList.contains(properties.getId())){
                    continue;
                }
                jsonObjectProp.put("propertiesName", properties.getCnName());
                if (EntryMode.manual == properties.getEntryMode()) {
                    jsonObjectProp.put("propertiesValueName", goodsProp.getPropValue());
                } else {
//                    PropertiesValue propertiesValue = this.properValueDao.findOne(Integer.valueOf(goodsProp.getPropValueId()));
                    PropertiesValue propertiesValue = this.properValueService.findById(Integer.valueOf(goodsProp.getPropValue()));
                    jsonObjectProp.put("propertiesValueId", propertiesValue.getId());
                    jsonObjectProp.put("propertiesValueName", propertiesValue.getCnValue());
                }
                jsonArrayProp.add(jsonObjectProp);
//            }
        }
        one.setJsonArraySku(jsonArraySku);
        one.setJsonArrayProp(jsonArrayProp);
        return ValueUtil.toJson(HttpStatus.SC_OK, one);
    }

}
