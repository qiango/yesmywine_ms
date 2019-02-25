package com.yesmywine.goods.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.dao.*;
import com.yesmywine.goods.entity.*;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.service.GmService;
import com.yesmywine.goods.service.GoodsStandService;
import com.yesmywine.goods.service.HistoryPriceService;
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
 * Created by hz on 4/14/17.
 */
@RestController
@RequestMapping("/goods/check")
public class CheckController {
    @Autowired
    private GmService gmService;
    @Autowired
    private GoodsStandService goodsStandService;
    @Autowired
    private HistoryPriceService historyPriceService;
    @Autowired
    private HistoryPriceDao historyPriceDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private GoodsStandDownDao goodsStandDownDao;
    @Autowired
    private GoodsMirrorDao goodsMirrorDao;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private GoodsMirrorDetailDao goodsMirrorDetailDao;
    @Autowired
    private GoodsDetailDao goodsDetailDao;

    @RequestMapping(value = "/showGoodsMirror", method = RequestMethod.GET)
    public String showGoodsMirror(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {//分页查询商品
        MapUtil.cleanNull(params);
        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK,gmService.findOne(id));
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        if(ValueUtil.isEmpity(params.get("goodsName"))){
            params.remove("goodsName");
        }
        if(ValueUtil.isEmpity(params.get("goodsCode"))){
            params.remove("goodsCode");
        }
        if(ValueUtil.isEmpity(params.get("checkState"))){
            params.remove("checkState");
        }
        pageModel.addCondition(params);
        pageModel = gmService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }
    @RequestMapping(value = "/showGoodsStandDown", method = RequestMethod.GET)
    public String showGoodsStandDown(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {//分页查询商品
        MapUtil.cleanNull(params);
        if(id!=null){
            GoodsStandDown goodsStandDown=goodsStandService.findOne(id);
            Goods goods = goodsStandDown.getGoods();
            goods.setCategoryName(categoryDao.findOne(goods.getCategoryId()).getCategoryName());
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsStandDown);
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if(ValueUtil.notEmpity(params.get("goodsName"))){
            String goodsName = params.get("goodsName").toString();
            Goods goods=goodsDao.findByGoodsName(goodsName);
            params.remove(params.remove("goodsName").toString());
            params.put("goods",goods);
        }else {
            params.remove("goodsName");
        }
        if(ValueUtil.notEmpity(params.get("goodsCode"))){
            String goodsCode = params.get("goodsCode").toString();
            Goods goods=goodsDao.findByGoodsCode(goodsCode);
            params.remove(params.remove("goodsCode").toString());
            params.put("goods",goods);
        }else {
            params.remove("goodsCode");
        }
        if(ValueUtil.isEmpity(params.get("checkState"))){
            params.remove("checkState");
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = goodsStandService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }
    @RequestMapping(value = "/showHistoryPrice", method = RequestMethod.GET)
    public String showHistoryPrice(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {//分页查询商品
        MapUtil.cleanNull(params);
        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK,historyPriceService.findOne(id));
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = historyPriceService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }

    @RequestMapping(value = "/showPriceArray", method = RequestMethod.GET)
    public String showPriceArray(Integer goodsId) {
        List<HistoryPrice> byGoodsId = historyPriceDao.findByGoodsId(goodsId);
        return ValueUtil.toJson(HttpStatus.SC_OK,byGoodsId);
    }

    @RequestMapping(value = "/standDown",method = RequestMethod.GET)
    public String showDetail(Integer standDownId) {
       Goods goods=goodsStandDownDao.findOne(standDownId).getGoods();
        List<GoodsProp> property = goods.getGoodsProp();
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray1=new JSONArray();
        for(GoodsProp goodsProp:property){
            com.alibaba.fastjson.JSONObject RejsonObject = new com.alibaba.fastjson.JSONObject();
            Integer propId=goodsProp.getPropId();
            String propValueId=goodsProp.getPropValue();
            if(ValueUtil.isEmpity(propValueId)){

            }
            Properties properties = this.propertiesDao.findOne(propId);
            String propertiesValue=null;
            if(properties.getEntryMode()== EntryMode.manual){
                propertiesValue=propValueId;
            }else{
                propertiesValue = this.properValueDao.findOne(Integer.parseInt(propValueId)).getCnValue();
            }
            RejsonObject.put("prop",properties.getCnName());
            RejsonObject.put("propValue",propertiesValue);
            jsonArray1.add(RejsonObject);
        }
        jsonObject.put("prop",jsonArray1);
        List<GoodsSku> skuList=goods.getGoodsSku();
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for (GoodsSku goodsSku:skuList) {
            Integer skuId=goodsSku.getSkuId();
            Integer count=goodsSku.getCount();
            Sku sku=skuDao.findOne(skuId);
            com.alibaba.fastjson.JSONObject json1 = new com.alibaba.fastjson.JSONObject();
            json1.put("count",count);
            json1.put("skuName",sku.getSkuName());
            json1.put("sku",sku.getSku());
            json1.put("skuCode",sku.getCode());
            jsonArray.add(json1);
        }
        jsonObject.put("sku",jsonArray);
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonObject);
    }

    @RequestMapping(value = "/showMirro",method = RequestMethod.GET)
    public String showMirro(Integer standDownId) {
        GoodsMirror goodsMirror=goodsMirrorDao.findOne(standDownId);
        List<PropMirror> property = goodsMirror.getPropMirrors();
        JSONObject jsonObject=new JSONObject();
        JSONArray jsonArray1=new JSONArray();
        for(PropMirror goodsProp:property){
            com.alibaba.fastjson.JSONObject RejsonObject = new com.alibaba.fastjson.JSONObject();
            Integer propId=goodsProp.getPropId();
            String propValueId=goodsProp.getPropValue();
            if(ValueUtil.isEmpity(propValueId)){
                break;
            }
            Properties properties = this.propertiesDao.findOne(propId);
            String propertiesValue=null;
            if(properties.getEntryMode()== EntryMode.manual){
                propertiesValue=propValueId;
            }else{
                propertiesValue = this.properValueDao.findOne(Integer.parseInt(propValueId)).getCnValue();
            }
            RejsonObject.put("prop",properties.getCnName());
            RejsonObject.put("propValue",propertiesValue);
            jsonArray1.add(RejsonObject);
        }
        jsonObject.put("prop",jsonArray1);
        Goods goods=goodsDao.findOne(goodsMirror.getGoodsId());
        List<GoodsSku> skuList=goods.getGoodsSku();
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for (GoodsSku goodsSku:skuList) {
            Integer skuId=goodsSku.getSkuId();
            Integer count=goodsSku.getCount();
            Sku sku=skuDao.findOne(skuId);
            com.alibaba.fastjson.JSONObject json1 = new com.alibaba.fastjson.JSONObject();
            json1.put("count",count);
            json1.put("skuName",sku.getSkuName());
            json1.put("sku",sku.getSku());
            json1.put("skuCode",sku.getCode());
            jsonArray.add(json1);
        }
        jsonObject.put("sku",jsonArray);
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonObject);
    }

    @RequestMapping(value = "/showMirrorDetail",method = RequestMethod.GET)
    public String showDetailMirror(Integer goodsMirrorId) {//展示商品详情(父文本)
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        GoodsMirrorDetail goodsDetail=goodsMirrorDetailDao.findByGoodsMirrorId(goodsMirrorId);
        if(null!=goodsDetail){
            jsonObject.put("goodsDetail",goodsDetail.getGoodsDetail());
        }else
        {
            Integer goodsId=goodsMirrorDao.findOne(goodsMirrorId).getGoodsId();
            String goodsDetails=goodsDetailDao.findByGoodsId(goodsId).getGoodsDetail();
            jsonObject.put("goodsDetail",goodsDetails);
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonObject);
    }

    @RequestMapping(value = "/getStand",method = RequestMethod.GET)
    public String getStand(Integer pageNo,Integer pageSize,String goodsName,String goodsCode,Integer checkStatus){
        try {
            return ValueUtil.toJson(goodsStandService.findByGoods(goodsName,goodsCode, checkStatus,pageNo,pageSize));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
}
