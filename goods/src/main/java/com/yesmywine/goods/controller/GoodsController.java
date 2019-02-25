package com.yesmywine.goods.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.DBConfig;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.bean.Item;
import com.yesmywine.goods.entity.*;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.GoodsService;
import com.yesmywine.goods.service.ProService;
import com.yesmywine.goods.service.ProperValueService;
import com.yesmywine.goods.service.SearchService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.goods.dao.*;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hz on 12/8/16.
 *
 */
@RestController
@RequestMapping("/goods/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private GoodsPropDao goodsPropDao;
    @Autowired
    private GoodsSkudDao goodsSkudDao;
    @Autowired
    private SearchService searchService;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ProService proService;
    @Autowired
    private ProperValueService properValueService;


    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params,Integer pageNo,Integer pageSize,String item) throws YesmywineException {//分页查询商品
        MapUtil.cleanNull(params);
        if(ValueUtil.notEmpity(params.get("id"))){
            Goods goods = goodsService.findOne(Integer.valueOf(params.get("id").toString()));
            List<GoodsProp> property = goods.getGoodsProp();
            JSONArray jsonArray1=new JSONArray();
            for(GoodsProp goodsProp:property){
                com.alibaba.fastjson.JSONObject RejsonObject = new com.alibaba.fastjson.JSONObject();
                Integer propId=goodsProp.getPropId();
                String propValueId=goodsProp.getPropValue();
                Properties properties = this.proService.findById(propId);
                String propertiesValue=null;
                if(properties.getEntryMode()== EntryMode.manual){
                    propertiesValue=propValueId;
                }else {
                    propertiesValue = this.properValueService.findById(Integer.parseInt(propValueId)).getCnValue();
                }
                    RejsonObject.put("prop",properties.getCnName());
                    RejsonObject.put("propValue",propertiesValue);
                jsonArray1.add(RejsonObject);
            }
            goods.setCategoryName(this.categoryDao.findOne(Integer.valueOf(goods.getCategoryId().toString())).getCategoryName());
            goods.setJsonArrayProp(jsonArray1);
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
            goods.setJsonArraySku(jsonArray);
            return ValueUtil.toJson(HttpStatus.SC_OK,goods);
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(goodsService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);

        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());


//        if(params.get("item")!=null&&!params.get("item").equals("")){
//            params.put("item",Item.getValue(params.get("item").toString()));
//        }else if(params.get("item_ne")!=null&&!params.get("item_ne").equals("")){}{
//            params.put("item_ne",Item.getValue(params.get("item").toString()));
//        }


        if(ValueUtil.notEmpity(item)){
            if(item.equals("all")){
                params.remove("item");
            }else {
                params.put("item_eq_com.yesmywine.goods.bean.Item",item);
                params.remove("item");
            }
        }else {
            params.remove("item");
        }
        if(ValueUtil.notEmpity(params.get("categoryId_l"))){
            params.put("categoryGroup_l", "L" + params.get("categoryId_l"));
            params.remove(params.remove("categoryId_l").toString());
        }

        pageModel.addCondition(params);
        pageModel = goodsService.findAll(pageModel);
        List<Goods> conditions = pageModel.getContent();
//        List conditions2 = new ArrayList();
        for(int i=0; i< pageModel.getContent().size(); i ++){
            conditions.get(i).setCategoryName(this.categoryDao.findOne(Integer.valueOf(conditions.get(i).getCategoryId().toString())).getCategoryName());
        }
        pageModel.setContent(conditions);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);

    }

    @RequestMapping(value = "/showGoods", method = RequestMethod.GET)
    public String showGoods(Integer pageNo,Integer pageSize,String goodsName_l) {
        PageModel pageModel= null;
        try {
            pageModel = goodsService.findByGoods(pageNo,pageSize,goodsName_l);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }

    @RequestMapping(value = "/showProp", method = RequestMethod.GET)
    public String indexShow(Integer goodsId) throws YesmywineException {//查询商品属性
        Goods one = this.goodsService.findOne(goodsId);
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
                Properties properties = this.proService.findById(skuProp.getPropId());
                PropertiesValue propertiesValue = this.properValueService.findById(skuProp.getPropValueId());
                jsonObjectSkuProp.put("propertiesId", properties.getId());
                jsonObjectSkuProp.put("propertiesName", properties.getCnName());
                jsonObjectSkuProp.put("propertiesValueId", propertiesValue.getId());
                jsonObjectSkuProp.put("propertiesValueName", propertiesValue.getCnValue());
                jsonArraySkuProp.add(jsonObjectSkuProp);
            }
            jsonObjectSku.put("goodsSkuProp", jsonArraySkuProp);
            jsonArraySku.add(jsonObjectSku);
        }
        JSONArray jsonArrayProp = new JSONArray();
        List<GoodsProp> goodsPropList = one.getGoodsProp();
        for(GoodsProp goodsProp: goodsPropList){
            JSONObject jsonObjectProp = new JSONObject();
            Properties properties = this.proService.findById(goodsProp.getPropId());
            jsonObjectProp.put("propertiesId", properties.getId());
            jsonObjectProp.put("propertiesName", properties.getCnName());
            if(EntryMode.manual==properties.getEntryMode()){
                jsonObjectProp.put("propertiesValueName", goodsProp.getPropValue());
            }else {
                PropertiesValue propertiesValue = this.properValueService.findById(Integer.valueOf(goodsProp.getPropValue()));
                jsonObjectProp.put("propertiesValueId", propertiesValue.getId());
                jsonObjectProp.put("propertiesValueName", propertiesValue.getCnValue());
            }
            jsonArrayProp.add(jsonObjectProp);
        }
        one.setJsonArraySku(jsonArraySku);
        one.setJsonArrayProp(jsonArrayProp);
        return ValueUtil.toJson(HttpStatus.SC_OK, one);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> param) {//新增福袋
        try {
            ValueUtil.verify(param.get("goodsName"),"goodsName");
            ValueUtil.verify(param.get("randomNumber"),"randomNumber");
            ValueUtil.verify(param.get("skuIdString"),"skuIdString");
            ValueUtil.verify(param.get("propString"),"propString");
            ValueUtil.verify(param.get("salePrice"),"salePrice");
            ValueUtil.verify(param.get("price"),"price");
            ValueUtil.verify(param.get("imgIds"),"imgIds");
            ValueUtil.verify(param.get("goodsDetails"),"商品详情");
            ValueUtil.verify(param.get("categoryId"),"categoryId");
            String s = goodsService.addGoods(param);
            if("success".equals(s)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, s);
            }else {
                return ValueUtil.toError("500", s);
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/showone/itf",method = RequestMethod.GET)
    public String showone(Integer goodsId) {//展示商品详情(父文本)
       return ValueUtil.toJson(HttpStatus.SC_OK,goodsDao.findOne(goodsId));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer goodsId) {//删除福袋
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, goodsService.delete(goodsId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/applyStand",method = RequestMethod.POST)
    public String applyStand(@RequestParam Map<String, String> param
            ,HttpServletRequest request) {//申请上下架
        try {
            String userName = UserUtils.getUserName(request);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, goodsService.standDown(param,userName));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/checkStandDown",method = RequestMethod.POST)
    public String checkStandDown(@RequestParam Map<String, String> param,HttpServletRequest request) {//审核上下架
        try {
            String userName = UserUtils.getUserName(request);
            String s = goodsService.checkStandDown(param, userName);
            if("success".equals(s)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, s);
            }
            return ValueUtil.toError("500", s);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            return ValueUtil.toError("500",e.getMessage());
        }
    }


    @RequestMapping(value = "/updateGoods",method = RequestMethod.POST)
    public String updateGoods(@RequestParam Map<String, String> param,HttpServletRequest request) {//编辑商品去审核
        try {
            String userName = UserUtils.getUserName(request);
            ValueUtil.verify(param.get("id"),"id");
            ValueUtil.verify(param.get("imgIds"),"imgIds");
            ValueUtil.verify(param.get("salePrice"),"salePrice");
            String s = goodsService.updateGoods(param, userName);
            if("success".equals(s)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, s);
            }
            return ValueUtil.toError("500", s);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/checkGoods",method = RequestMethod.POST)
    public String checkGoods(@RequestParam Map<String, String> param,HttpServletRequest request) {//审核商品信息
        try {
            String userName = UserUtils.getUserName(request);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, goodsService.checkGoods(param,userName));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(@RequestParam Map<String,Object> map) {   //同步商品
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.synchronous(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/synchronousProp", method = RequestMethod.POST)
    public String synchronousProp(@RequestParam Map<String,Object> map) {   //同步商品属性是否显示
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.synchronousProp(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/synchronousGoods", method = RequestMethod.POST)
    public String synchronousGoods(@RequestParam Map<String,String> map) {   //同步商品普通属性
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.synchronousGoods(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/updateSales/itf", method = RequestMethod.POST)
    public String updateSales(String jsonString) {   //更新销量
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,goodsService.updateSales(jsonString));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/updateComment", method = RequestMethod.POST)
    public String updateComment() {   //更新商品评论数,好评率（每天凌晨定时更新）
        try {
            return ValueUtil.toJson(goodsService.updateComment());
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/itf/manyGoods", method = RequestMethod.GET)
    public String manyGoods(String goodsId) {//goodsI用,隔开
        List<Goods> list=new ArrayList<>();
        String[] goods = goodsId.split(",");
        for(int i=0;i<goods.length;i++){
            Goods g=goodsDao.findOne(Integer.parseInt(goods[i]));
            list.add(g);
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,list);
    }

    @RequestMapping(value = "/updateBookGoods/itf", method = RequestMethod.POST)
    public String updateBookGoods(Integer []goodsId,Integer []count,String[] price,String saleModel,String startTime,String endTime) {//设置抢购
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,goodsService.setBookGoods(goodsId, count, price,saleModel,startTime,endTime));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/updateBookNumber/itf", method = RequestMethod.POST)
    public String updateBookNumber(@RequestParam Map<String,String> param) {//设置预售剩余数量
            Integer goodsId=Integer.parseInt(param.get("goodsId"));
            Integer count=Integer.parseInt(param.get("count"));
            String code=param.get("code");
            Goods goods=goodsDao.findOne(goodsId);
            Integer remainBooknumber=goods.getRemainBooknumber();
            if(ValueUtil.isEmpity(code)){
                Integer number=remainBooknumber-count;
                goods.setRemainBooknumber(number);
                if(number==0||number<0){
                    goods.setPreStatus(1);
                }
            }else {
                Integer number=remainBooknumber+count;
                goods.setRemainBooknumber(number);
            }
           goodsDao.save(goods);
           return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(String goodsIds) {   //将商品信息同步到solr库中
        try {
            String[] split = goodsIds.split(",");
            for(String s:split) {
                Goods goods = this.goodsDao.findOne(Integer.valueOf(s));
                if(ValueUtil.isEmpity(goods)){
                    return ValueUtil.toError("500", "erro", "此 "+s+" 商品id不存在");
                }
                this.searchService.saveGoodsSearch(goods);
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500", "erro");
    }

    @RequestMapping(value = "/showDetail",method = RequestMethod.GET)
    public String showDetailback(Integer goodsId) {//展示商品详情(父文本)
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.showOne(goodsId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value = "/showMirrorDetail",method = RequestMethod.GET)
    public String showDetail(Integer goodsMirrorId) {//展示商品详情(父文本)
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.showMirrorOne(goodsMirrorId));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/categoryGoods",method = RequestMethod.GET)
    public String categoryGoods(Integer categoryId,Integer pageNo,Integer pageSize) {
        return ValueUtil.toJson(HttpStatus.SC_OK,goodsService.categoryGoods(categoryId,pageNo,pageSize));
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/9/16
    *@Param
    *@Description:全量重新创建索引
    */
    @RequestMapping(value = "/anewIndex",method = RequestMethod.GET)
    public String anewIndex() {
        Map<String, Object> params = new HashMap<>();
        params.put("goStatus",1);
        PageModel pageModel = new PageModel(1 ,100);
        pageModel.addCondition(params);
        pageModel = goodsService.findAll(pageModel);
        Integer totalPages = pageModel.getTotalPages();
        List<Goods> goodsList = pageModel.getContent();
        for(Goods goods: goodsList){
            try {
                Goods info = this.goodsDao.findOne(goods.getId());
                this.searchService.saveGoodsSearch(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (int i=1;i<totalPages;i++){
            Map<String, Object> params_2 = new HashMap<>();
            params.put("goStatus",1);
            pageModel = new PageModel(i+1 ,100);
            pageModel.addCondition(params_2);
            pageModel = goodsService.findAll(pageModel);
            List<Goods> goodsList_c = pageModel.getContent();
            for(Goods goods: goodsList_c){
                try {
                    Goods info = this.goodsDao.findOne(goods.getId());
                    this.searchService.saveGoodsSearch(info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ValueUtil.toJson(HttpStatus.SC_CREATED,"SUCCESS");
    }
}



