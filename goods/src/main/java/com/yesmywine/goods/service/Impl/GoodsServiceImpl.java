package com.yesmywine.goods.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.goods.UploadFileThread;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.bean.Item;
import com.yesmywine.goods.dao.*;
import com.yesmywine.goods.entity.*;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.service.GoodsService;
import com.yesmywine.goods.service.SearchService;
import com.yesmywine.goods.service.SkuService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by hz on 12/8/16.
 * 异常,连接池
 */
@Service
@Transactional
public class GoodsServiceImpl extends BaseServiceImpl<Goods, Integer> implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private GoodsChannelDao goodsChannelDao;
    @Autowired
    private SkuService skuService;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private GoodsMirrorDao goodsMirrorDao;
    @Autowired
    private GoodsStandDownDao goodsStandDownDao;
    @Autowired
    private HistoryPriceDao historyPriceDao;
    @Autowired
    private GoodsPropDao goodsPropDao;
    @Autowired
    private GoodsSkudDao goodsSkudDao;
    @Autowired
    private PropMirrorDao propMirrorDao;
    @Autowired
    private GoodsDetailDao goodsDetailDao;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private SearchService searchService;
    @Autowired
    private CategoryPropertyDao categoryPropertyDao;
    @Autowired
    private GoodsMirrorDetailDao goodsMirrorDetailDao;


    public String addGoods(Map<String, String> param) throws YesmywineException{//新增福袋商品
        if(null!=goodsDao.findByGoodsName(param.get("goodsName"))){
            ValueUtil.isError("该福袋名已存在");
        }
        if(categoryDao.findOne(Integer.parseInt(param.get("categoryId"))).getLevel()!=3){
            ValueUtil.isError("分类只可为3级");
        }
        Goods goods=new Goods();
        goods.setGoodsName(param.get("goodsName"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddssHHmmss");
        String format = dateFormat.format(new Date());
        goods.setGoodsCode("G"+format);
        goods.setOperate(0);
        goods.setCategoryId(Integer.parseInt(param.get("categoryId")));
        goods.setRandomNumber(Integer.parseInt(param.get("randomNumber")));
        goods.setItem(Item.luckBage);
        goods.setGoStatus(0);
        goods.setSyncToStore(false);
        JsonParser jsonParser = new JsonParser();
        JsonArray arr1 = jsonParser.parse(param.get("skuIdString")).getAsJsonArray();//[{"skuId":"21","count":"2"}]
        List<GoodsSku> list=new ArrayList<>();
        for (int f = 0; f < arr1.size(); f++) {
            String skuId=arr1.get(f).getAsJsonObject().get("skuId").getAsString();
            String count=arr1.get(f).getAsJsonObject().get("count").getAsString();
            GoodsSku goodsSku=new GoodsSku();
            goodsSku.setCount(Integer.parseInt(count));
            goodsSku.setSkuId(Integer.parseInt(skuId));
            list.add(goodsSku);
        }
        goodsSkudDao.save(list);
        goods.setGoodsSku(list);
        goods.setSalePrice(param.get("salePrice"));
        goods.setPrice(param.get("price"));
        String propString = param.get("propString");//json
        List<GoodsProp> list1=new ArrayList<>();
        JSONObject obj = new JSONObject(propString);
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = obj.getString(key);
            GoodsProp goodsProp=new GoodsProp();
            goodsProp.setPropId(Integer.parseInt(key));
            goodsProp.setPropValue(value);
            list1.add(goodsProp);
        }
        goodsPropDao.save(list1);
        goods.setGoodsProp(list1);
        goods.setGoodsEnName(param.get("goodsEnName"));
        goods.setOperate(0);
        goods.setDiscount(1);
        goods.setStatus(0);
        goods.setLibrary(1);
        goods.setCashOnDelivery(1);
        goodsDao.save(goods);
        GoodsDetail goodsDetail=new GoodsDetail();
        goodsDetail.setGoodsId(goods.getId());
        goodsDetail.setCreateTime(new Date());
        goodsDetail.setGoodsDetail(param.get("goodsDetails"));
        goodsDetailDao.save(goodsDetail);
        String imgIds = param.get("imgIds");
        String[] imgArr = imgIds.split(";");
        Integer[] arr=new Integer[imgArr.length];
        for(int i=0;i<imgArr.length;i++){
            arr[i]=Integer.parseInt(imgArr[i]);
        }
        String goodImg = null;
        if(imgIds!=null&&!imgIds.equals("")){
            try {
                goodImg = saveGoodsImg(goods.getId(), arr);
            } catch (YesmywineException e) {
                e.printStackTrace();
            }
        }
        goods.setGoodsImageUrl(goodImg);
        goodsDao.save(goods);
        //同步到评论、cms
//        com.alibaba.fastjson.JSONObject jsonData= ValueUtil.toJsonObject(goods);
//        jsonData.put("synchronous",0);
////        String code=SynchronizeUtils.getCode("http://88.88.88.84:8085","/evaluation/goods/synchronous",jsonData.toJSONString(),RequestMethod.post);
//        String code=SynchronizeUtils.getCode("http://localhost:8443","/evaluation/goods/synchronous",jsonData.toJSONString(),RequestMethod.post);
//        if(!"200".equals(code)||ValueUtil.isEmpity(code)){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return "同步到评论失败";
//        }
////        String code1=SynchronizeUtils.getCode("http://88.88.88.84:8084","/cms/synchronous/goods",jsonData.toJSONString(),RequestMethod.post);
//        String code1=SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/cms/synchronous/goods",jsonData.toJSONString(),RequestMethod.post);
//        if(!"200".equals(code1)||ValueUtil.isEmpity(code1)){
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return "同步到cms失败";
//        }
       return "success";
    }
    public String standDown(Map<String,String> map,String userName)throws YesmywineException{//申请上下架
        Integer goodsId=Integer.parseInt(map.get("goodsId"));
        Goods goods=goodsDao.findOne(goodsId);
        GoodsDetail goodsDetail=goodsDetailDao.findByGoodsId(goodsId);
        ValueUtil.verifyGoods(goods.getPrice(),"price");
        ValueUtil.verifyGoods(goods.getSalePrice(),"salePrice");
        ValueUtil.verifyGoods(goods.getLibrary(),"library");
        ValueUtil.verifyGoods(goods.getGoodsImageUrl(),"image");
        ValueUtil.verifyGoods(goods.getCashOnDelivery(),"cashOnDelivery");
        ValueUtil.verifyGoods(goods.getDiscount(),"discount");
        ValueUtil.verifyGoods(goodsDetail,"goodsDetail");
        if(goods.getItem()==Item.fictitious&&ValueUtil.isEmpity(goods.getVirtualType())){

        }
        if(goods.getItem()==Item.single&&ValueUtil.isEmpity(goods.getSyncToStore())){
            ValueUtil.isError("是否同步到门店为空,不可上架");
        }
        Integer status=goods.getStatus();
        if(status==4||status==1||status==7){
            ValueUtil.isError("改商品处于编辑或上下架审核中,不可再申请上下架");
        }
        GoodsStandDown goodsStandDown=new GoodsStandDown();
        Integer newStatus=Integer.parseInt(map.get("newStatus"));//申请上下架id(0申请上架,1申请下架)
        if(newStatus==0){
            goods.setStatus(1);//上架审核
            goodsStandDown.setCheckType(0);
        }else {
            goods.setStatus(7);//下架审核
            goodsStandDown.setCheckType(1);
        }
        goodsStandDown.setApplyUser(userName);
        goodsStandDown.setCheckState(0);
        goodsStandDown.setApplyTime(new Date());
        goodsStandDown.setGoods(goods);
        goodsStandDownDao.save(goodsStandDown);
        goodsDao.save(goods);
       return "success";
    }

    public String checkStandDown(Map<String,String> map,String userName) throws Exception {//审核上下架
        Integer checkStatus=Integer.parseInt(map.get("checkStatus"));//0:审核通过,1:审核不通过
        Integer goodsStandId=Integer.parseInt(map.get("goodsStandDownId"));
        GoodsStandDown goodsStandDown=goodsStandDownDao.findOne(goodsStandId);
        Integer goodsId=goodsStandDown.getGoods().getId();
        Goods goods=goodsDao.findOne(goodsId);
        Integer status=goodsStandDown.getCheckType();
        if(status==0){//上架审核
            if(checkStatus==0){
                goods.setGoStatus(1);//已上架
                goods.setStatus(2);//上架审核通过
                goods.setListedTime(new Date());//上架时间
                goodsStandDown.setCheckState(1);
                //同步给评论、cms
                try {
                    this.searchService.saveGoodsSearch(goods);
                } catch (Exception e) {
                    ValueUtil.isError("添加到搜索库失败");
                }

                com.alibaba.fastjson.JSONObject jsonData1= ValueUtil.toJsonObject(goods);
                if (null == goods.getGoodsEnName()) {
                    jsonData1.put("goodsEnName", "");
                }
                jsonData1.put("synchronous",0);
                String code = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/evaluation/goods/synchronous", jsonData1.toJSONString(), RequestMethod.post);
//                String code = SynchronizeUtils.getCode("http://localhost:8086", "/evaluation/goods/synchronous", jsonData1.toJSONString(), RequestMethod.post);
                if(!"200".equals(code)||ValueUtil.isEmpity(code)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("同步到评论失败");
                }
//                String code1 = SynchronizeUtils.getCode("http://88.88.88.84:8084", "/cms/synchronous/goods", jsonData1.toJSONString(), RequestMethod.post);
                String code1 = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/cms/synchronous/goods", jsonData1.toJSONString(), RequestMethod.post);
                if(!"200".equals(code1)||ValueUtil.isEmpity(code1)){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("同步到cms失败");
                }
//                System.out.println(jsonData1.toJSONString());
//                String code1 = SynchronizeUtils.getCode("http://88.88.88.84:8084", "/cms/synchronous/goods", jsonData1.toJSONString(), RequestMethod.post);
                //同步给活动服务
                    com.alibaba.fastjson.JSONObject json= ValueUtil.toJsonObject(goods);
                if(goods.getItem()==Item.single){
                    Map<String,Object> maps=getBrand(goods);
                    if(maps.isEmpty()){
                        json.put("brandId", "");
                        json.put("brandValue","");
                    }else {
                        json.put("brandId", maps.get("propValueId"));
                        json.put("brandValue", maps.get("cnVaule"));
                    }
                }
                    String jsonData=json.toJSONString();
                    SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/activity/syn/receiveGoodsInfo",jsonData,RequestMethod.post);
//                    String aaa = SynchronizeUtils.getCode("http://localhost:8080","/active/syn/receiveGoodsInfo",jsonData,RequestMethod.post);
            }else {
                goods.setStatus(3);//上架审核失败
                goodsStandDown.setCheckState(2);
            }
            goodsStandDown.setCheckTime(new Date());
            goodsStandDown.setUserName(userName);//审核人
            goodsStandDown.setInstructions(map.get("instructions"));//审核说明
            goodsStandDownDao.save(goodsStandDown);
            goodsDao.save(goods);
            return "success";
        }else if(status==1) {
            if (checkStatus == 0) {
                goods.setGoStatus(2);//已下架
                goods.setStatus(8);//下架审核成功
                goodsStandDown.setCheckState(1);
                this.searchService.delete(goods.getId());
                //同步给活动服务
                com.alibaba.fastjson.JSONObject json= ValueUtil.toJsonObject(goods);

                String jsonData=json.toJSONString();
                SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/active/syn/receiveGoodsInfo",jsonData,RequestMethod.post);
                //同步到评论、cms
                com.alibaba.fastjson.JSONObject jsonData1= ValueUtil.toJsonObject(goods);
                jsonData1.put("synchronous",2);
                SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/evaluation/goods/synchronous",jsonData1.toJSONString(),RequestMethod.post);
                SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/cms/synchronous/goods",jsonData1.toJSONString(),RequestMethod.post);
            } else {
                goods.setStatus(9);//下架审核失败
                goodsStandDown.setCheckState(2);
            }
            goodsStandDown.setCheckTime(new Date());
            goodsStandDown.setUserName(userName);//审核人
            goodsStandDown.setInstructions(map.get("instructions"));//审核说明
            goodsStandDownDao.save(goodsStandDown);
            goodsDao.save(goods);
            return "success";
        }else
            return "该商品不在上下架审核中,不可审核";
    }
    public Map<String,Object> getBrand(Goods goods){//获取商品中的品牌
        Integer brandId =Integer.parseInt(Dictionary.getDicSingleValue("brand"));
        List<GoodsSku> goodsSku = goods.getGoodsSku();
        Integer skuId = goodsSku.get(0).getSkuId();
        List<SkuProp> skuProp = skuDao.findOne(skuId).getSkuProp();
        Integer propValueId=null;
        String cnValue=null;
        Map<String,Object> map=new HashMap<>();
        for (SkuProp s:skuProp){
            if(s.getPropId()==brandId){
                  propValueId = s.getPropValueId();
                  cnValue = properValueDao.findOne(propValueId).getCnValue();
                  map.put("propValueId",propValueId);
                  map.put("cnVaule",cnValue);
            }
        }
        return map;
    }
//    public String getGoodsInfo(Goods goods){
//        com.alibaba.fastjson.JSONObject jsonData = new com.alibaba.fastjson.JSONObject();
//        jsonData.put("goodsImageUrl", goods.getGoodsImageUrl());
//        jsonData.put("goodsName", goods.getGoodsName());
//        jsonData.put("goodsOriginalName", goods.getGoodsOriginalName());
//        jsonData.put("goodsEnName", goods.getGoodsEnName());
//        jsonData.put("goodsDetails", goodsDetailDao.findByGoodsId(goods.getId()).getGoodsDetail());
//        jsonData.put("price", goods.getPrice());
//        jsonData.put("salePrice", goods.getSalePrice());
//        jsonData.put("referencePrice", goods.getReferencePrice());
//        jsonData.put("categoryId", goods.getCategoryId());
//        jsonData.put("goStatus", goods.getGoStatus());
//        jsonData.put("id", goods.getId());
//        jsonData.put("library", goods.getLibrary());
//        jsonData.put("discount", goods.getDiscount());
//        jsonData.put("skuIds",goods.getGoodsSku());
//        jsonData.put("operate",goods.getOperate());
//        jsonData.put("item",goods.getItem());
//        if(goods.getItem()==Item.single){
//            HttpBean httpBean = new HttpBean(Dictionary.dicUrl+"/dic/sysCode", RequestMethod.get);
//            httpBean.addParameter("sysCode", "brand");
//            httpBean.run();
//            String temp = httpBean.getResponseContent();
//            String brand = ValueUtil.getFromJson(temp, "data","entityValue");
//            String propertiesValue=null;
//            List<GoodsProp> goodsProp=goods.getGoodsProp();
//            for(GoodsProp goodsProp1:goodsProp) {
//                Integer propId=goodsProp1.getPropId();
//                Properties properties=propertiesDao.findOne(propId);
//                String propValueId=goodsProp1.getPropValueId();
//                if(propId==Integer.parseInt(brand)){
//                    if (properties.getEntryMode() == EntryMode.manual) {
//                        propertiesValue = propValueId;
//                    } else {
//                        propertiesValue = this.properValueDao.findOne(Integer.parseInt(propValueId)).getCnValue();
//                    }
//                    jsonData.put("brandId", propertiesValue);
//                }
//
//            }
//        }
//        return jsonData.toJSONString();
//    }

    public String updateGoods(Map<String,String> map,String userName)throws YesmywineException{  //编辑商品信息去审核
        String imgIds = map.get("imgIds");
        Goods goods=goodsDao.findOne(Integer.parseInt(map.get("id")));
        Integer status=goods.getStatus();
        if(status==4||status==1||status==7){
            ValueUtil.isError("改商品处于编辑或上下架审核中,不可再编辑");
        }
        //添加到审核表中
        GoodsMirror goodsMirror=new GoodsMirror();
        Integer goodsId=Integer.parseInt(map.get("id"));
        Goods goodsName = goodsDao.findByGoodsName(map.get("goodsName"));
        if(ValueUtil.notEmpity(goodsName)&& (int)goodsName.getId()!=(int)goodsId){
            ValueUtil.isError("该商品名已存在");
        }
        goodsMirror.setGoodsName(map.get("goodsName"));
//        goodsMirror.setGoodsImageUrl(goodImg);//保存图片编码
        goodsMirror.setPrice(map.get("price"));//原始价格
        goodsMirror.setSalePrice(map.get("salePrice"));//销售价格
        goodsMirror.setGoodsEnName(map.get("goodsEnName"));//商品英文名
        if(ValueUtil.notEmpity(map.get("virtualType"))){
            goodsMirror.setVirtualType(map.get("virtualType"));
        }
//        GoodsMirrorDetail byGoodsMirrorId = this.goodsMirrorDetailDao.findByGoodsMirrorId(goodsMirror.getId());
//        if(ValueUtil.isEmpity(byGoodsMirrorId)){
//            byGoodsMirrorId = new GoodsMirrorDetail();
//            byGoodsMirrorId.setGoodsMirrorId(goodsMirror.getId());
//        }
//        goodsMirror.setGoodsDetails(map.get("goodsDetails"));
//        byGoodsMirrorId.setGoodsDetail(map.get("goodsDetails"));
//        byGoodsMirrorId.setCreateTime(new Date());
//        this.goodsMirrorDetailDao.save(byGoodsMirrorId);
        String propString=map.get("propString");//{"2":"3","3":"4","4":"5"}
        List<PropMirror> list1=new ArrayList<>();
        JSONObject obj = new JSONObject(propString);
        Iterator it = obj.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String value = obj.getString(key);
            PropMirror propMirror=new PropMirror();
            propMirror.setPropId(Integer.parseInt(key));
            propMirror.setPropValue(value);
            list1.add(propMirror);
        }
        goodsMirror.setPropMirrors(list1);
        propMirrorDao.save(list1);
        goodsMirror.setGoodsId(goodsId);
        goodsMirror.setChannelCode(goods.getChannelCode());
        goodsMirror.setChannelName(goods.getChannelName());
        goodsMirror.setGoodsCode(goods.getGoodsCode());
        goodsMirror.setCheckState(0);
        if(goods.getItem()!=Item.luckBage) {
            if (map.get("discount").equals("0")) {
                goodsMirror.setDiscount(0);
            } else {
                goodsMirror.setDiscount(1);
            }
            if (map.get("library").equals("0")) {
                goodsMirror.setLibrary(0);
            } else {
                goodsMirror.setLibrary(1);
            }
            if (map.get("cashOnDelivery").equals("0")) {
                goodsMirror.setCashOnDelivery(0);
            } else {
                goodsMirror.setCashOnDelivery(1);
            }
        }
        if(goods.getItem()==Item.single){
            if(map.get("ToStore").equals("0")){
                goodsMirror.setSyncToStore(true);
            }else {
                goodsMirror.setSyncToStore(false);
            }
        }
        goodsMirror.setItem(goods.getItem());
        goodsMirror.setCheckType(2);//商品信息审核
        goodsMirror.setApplyUser(userName);
        goodsMirror.setApplyTime(new Date());
        goodsMirror.setOperate(goods.getOperate());
        if(goods.getOperate()==1){//如果为预售商品,则是设定开始时间等等...
            goodsMirror.setStartTime(DateUtil.toDate(map.get("startTime"),"yyyy-MM-dd HH:mm:ss"));
            goodsMirror.setEndTime(DateUtil.toDate(map.get("endTime"),"yyyy-MM-dd HH:mm:ss"));
            goodsMirror.setBooknumber(Integer.parseInt(map.get("booknumber")));
//            goodsMirror.setRemainBooknumber(Integer.parseInt(map.get("remainBooknumber")));
            goodsMirror.setDelivery(DateUtil.toDate(map.get("delivery"),"yyyy-MM-dd HH:mm:ss"));//预计发货时间
        }
        goodsMirrorDao.save(goodsMirror);
        GoodsMirrorDetail  byGoodsMirrorId = new GoodsMirrorDetail();
        byGoodsMirrorId.setGoodsMirrorId(goodsMirror.getId());
        byGoodsMirrorId.setCreateTime(new Date());
        if(ValueUtil.notEmpity(map.get("goodsDetails"))){
            byGoodsMirrorId.setGoodsDetail(map.get("goodsDetails"));
        }else{
            GoodsDetail g=goodsDetailDao.findByGoodsId(Integer.parseInt(map.get("id")));
            byGoodsMirrorId.setGoodsDetail(g.getGoodsDetail());
        }
        goodsMirrorDetailDao.save(byGoodsMirrorId);
        if(ValueUtil.notEmpity(imgIds)){
            String[] imgArr = imgIds.split(";");
            Integer[] arr=new Integer[imgArr.length];
            for(int i=0;i<imgArr.length;i++){
                arr[i]=Integer.parseInt(imgArr[i]);
            }
            if(imgIds!=null&&!imgIds.equals("")){
//                try {
//
//                goodImg = saveGoodsImg(Integer.parseInt(map.get("id")), arr);
//                } catch (YesmywineException e) {
//                    e.printStackTrace();
//                }
                UploadFileThread uploadFileThread = new UploadFileThread(goodsMirrorDao,goodsId,goodsMirror.getId(), arr);//异步上传图片
                Thread thread = new Thread(uploadFileThread);
                thread.start();
            }
        }
        goods.setStatus(4);
        goodsDao.save(goods);
        return "success";
    }

    private String saveGoodsImg(Integer goodsId, Integer[] imgIds) throws YesmywineException {
        try{
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "goods");
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
                JSONArray maps = new JSONArray(imgIds.length);
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


    public String checkGoods(Map<String,String> map,String userName)throws YesmywineException{//审核商品信息
        Integer goodsMirrorId=Integer.parseInt(map.get("goodsMirrorId"));
        GoodsMirror goodsMirror=goodsMirrorDao.findOne(goodsMirrorId);
        Integer goodsId=goodsMirror.getGoodsId();
        Goods goods=goodsDao.findOne(goodsId);
        Integer status=goodsMirror.getCheckState();
        if(status!=0){//编辑审核中
            ValueUtil.isError("该商品不在编辑审核中");
        }
        Integer checkStatus=Integer.parseInt(map.get("checkStatus"));//0:审核通过,1:审核不通过
        goodsMirror.setUserName(userName);
        goodsMirror.setCheckTime(new Date());
        String instructions=null;
        if(map.get("instructions")!=null){
            instructions=map.get("instructions");
        }
        goodsMirror.setInstructions(instructions);
        if(checkStatus==0){
            goodsMirror.setCheckState(1);
            goods.setStatus(5);
        }else {
            goodsMirror.setCheckState(2);
            goods.setStatus(6);
        }
        goodsMirrorDao.save(goodsMirror);
        //审核完成同步到商品
        if(checkStatus==0) {
            goods.setGoodsName(goodsMirror.getGoodsName());
            goods.setGoodsImageUrl(goodsMirror.getGoodsImageUrl());
            goods.setPrice(goodsMirror.getPrice());//原始价格
            goods.setSalePrice(goodsMirror.getSalePrice());//销售价格
//            goods.setGoodsDetails(goodsMirror.getGoodsDetails());
            GoodsDetail goodsDetail = this.goodsDetailDao.findByGoodsId(goodsId);
            if (ValueUtil.isEmpity(goodsDetail)) {
                goodsDetail = new GoodsDetail();
                goodsDetail.setGoodsId(goodsId);
            }
            goodsDetail.setCreateTime(new Date());

            GoodsMirrorDetail byGoodsMirrorId = this.goodsMirrorDetailDao.findByGoodsMirrorId(goodsMirror.getId());
            if (ValueUtil.notEmpity(byGoodsMirrorId)) {
                goodsDetail.setGoodsDetail(byGoodsMirrorId.getGoodsDetail());
                goodsDetailDao.save(goodsDetail);
            }
            goods.setGoodsEnName(goodsMirror.getGoodsEnName());
            goods.setLibrary(goodsMirror.getLibrary());
            goods.setCashOnDelivery(goodsMirror.getCashOnDelivery());
            goods.setDiscount(goodsMirror.getDiscount());
            if (null != goodsMirror.getVirtualType()) {
                goods.setVirtualType(goodsMirror.getVirtualType());
            }
            List<GoodsProp> list1=new ArrayList<>();
            List<PropMirror> list = goodsMirror.getPropMirrors();//保存商品镜像表属性
            for (PropMirror p : list) {
                if(ValueUtil.isEmpity(p.getPropValue())){
                    break;
                }
                List<GoodsProp> list2 = goods.getGoodsProp();
                if(list2.size()==0){
                    GoodsProp goodsProp=new GoodsProp();
                    goodsProp.setPropId(p.getPropId());
                    goodsProp.setPropValue(p.getPropValue());
                    goodsProp.setUpdate(false);
                    goodsPropDao.save(goodsProp);
                    list1.add(goodsProp);
                }
                boolean a=false;
                for (GoodsProp g : list2) {
                    if (p.getPropId().equals(g.getPropId())){
                        g.setPropValue(p.getPropValue());
                        g.setUpdate(true);
                        goodsPropDao.save(g);
                        list1.add(g);
                        a=true;
                    }
                }
                if(!a){
                        GoodsProp goodsProp=new GoodsProp();
                        goodsProp.setPropId(p.getPropId());
                        goodsProp.setPropValue(p.getPropValue());
                        goodsProp.setUpdate(false);
                        goodsPropDao.save(goodsProp);
                        list1.add(goodsProp);
                }
            }
            goods.setGoodsProp(list1);
            if (goods.getOperate() == 1) {//预售
                goods.setStartTime(goodsMirror.getStartTime());
                goods.setEndTime(goodsMirror.getEndTime());
                goods.setBooknumber(goodsMirror.getBooknumber());
                goods.setDelivery(goodsMirror.getDelivery());
                goods.setRemainBooknumber(goodsMirror.getRemainBooknumber());
            }
            if(goods.getItem()==Item.single){
                goods.setSyncToStore(goodsMirror.getSyncToStore());
            }
            goodsDao.save(goods);//同步到活动
            if(goods.getGoStatus()==1){
                try {
                    this.searchService.saveGoodsSearch(goods);
                } catch (Exception e) {
                    ValueUtil.isError("添加到搜索库失败");
                }
            }
            com.alibaba.fastjson.JSONObject json = ValueUtil.toJsonObject(goods);
            Map<String,Object> maps=getBrand(goods);
            if(maps.isEmpty()){
                json.put("brandId", "");
                json.put("brandValue","");
            }else {
                json.put("brandId", maps.get("propValueId"));
                json.put("brandValue", maps.get("cnVaule"));
            }
            String jsonData = json.toJSONString();
            String code2 = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/activity/syn/receiveGoodsInfo", jsonData, RequestMethod.post);
            if (!"201".equals(code2) || ValueUtil.isEmpity(code2)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("同步到活动失败");
            }
            if(goods.getGoStatus()==1){
                //同步到cms,评论
                com.alibaba.fastjson.JSONObject jsonData1 = ValueUtil.toJsonObject(goods);
                if (null == goods.getComments()) {
                    jsonData1.put("comments", "");
                }
                if (null == goods.getSales()) {
                    jsonData1.put("sales", "");
                }
                if (null == goods.getGoodsEnName()) {
                    jsonData1.put("goodsEnName", "");
                }
                if (null == goods.getPraise()) {
                    jsonData1.put("praise", "");
                }
                jsonData1.put("synchronous", 1);
                jsonData1.toJSONString();
//        SynchronizeUtils.getCode("http://88.88.88.84:8084","/cms/synchronous/goods",jsonData.toJSONString(),RequestMethod.post);
                String code = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/cms/synchronous/goods", jsonData1.toJSONString(), RequestMethod.post);
                if (!"200".equals(code) || ValueUtil.isEmpity(code)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("同步到cms失败");
                }
                String code1 = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/evaluation/goods/synchronous", jsonData1.toJSONString(), RequestMethod.post);
                if (!"200".equals(code1) || ValueUtil.isEmpity(code1)) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError("同步到评论失败");
                }
            }

            Integer historyId = historyPriceDao.findId(goodsId);//保存历史价格
            if (null != historyId && goodsMirror.getSalePrice().equals(historyPriceDao.findOne(historyId).getSalePrice())) {
            } else {
                HistoryPrice historyPrice1 = new HistoryPrice();
                historyPrice1.setSalePrice(goodsMirror.getSalePrice());
                historyPrice1.setGoodsId(goodsId);
                historyPriceDao.save(historyPrice1);
            }
            return "审核完成";
        }else {
            return "审核完成";
        }
    }

    @Override
    public Object details(Integer goodsId) throws YesmywineException {
        Goods goods = goodsDao.findOne(goodsId);
        if(ValueUtil.isEmpity(goods)){
            ValueUtil.isError("无此商品");
        }
        com.alibaba.fastjson.JSONObject jsonObject =ValueUtil.toJsonObject(goods);
        jsonObject.remove("goodsProp");
        jsonObject.remove("goodsDetails");
        jsonObject.remove("goodsSku");
        jsonObject.remove("goodsOriginalName");
        jsonObject.remove("referencePrice");
        jsonObject.remove("categoryName");
        jsonObject.remove("categoryGroup");
        jsonObject.remove("channelId");
        jsonObject.remove("channelCode");
        jsonObject.remove("channelName");
        jsonObject.remove("status");
        jsonObject.remove("listedTime");
        jsonObject.remove("randomNumber");
        jsonObject.remove("passGoodsId");
        jsonObject.remove("jsonArraySku");
        jsonObject.remove("jsonArrayProp");
        Integer categoryIdThird=goodsDao.findOne(goodsId).getCategoryId();
        Category category=categoryDao.findOne(categoryIdThird);
        String categoryNameThird=category.getCategoryName();
        Category categorySecond=category.getParentName();
        String categoryNameSecond=categorySecond.getCategoryName();
        Integer categoryIdSecond=categorySecond.getId();
        Category categoryFirst=categorySecond.getParentName();
        String categoryNameFirst=categoryFirst.getCategoryName();
        Integer categoryIdFirst=categoryFirst.getId();
        jsonObject.put("categoryIdThird",categoryIdThird);
        jsonObject.put("categoryNameThird",categoryNameThird);
        jsonObject.put("categoryIdSecond",categoryIdSecond);
        jsonObject.put("categoryNameSecond",categoryNameSecond);
        jsonObject.put("categoryIdFirst",categoryIdFirst);
        jsonObject.put("categoryNameFirst",categoryNameFirst);

        if(!goods.getOperate().equals(0)){
            Date endTime = goods.getEndTime();
            Date startTime = new Date();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);
            Long endSecond = endCalendar.getTimeInMillis();
            Long startSecond = startCalendar.getTimeInMillis();
            Long balance = endSecond-startSecond;
            Integer s_day = 60*60*24*1000;
            Integer s_hour = 60*60*1000;
            Integer s_min = 60*1000;
            Long day = balance/s_day;//还剩多少天
            Long hour = (balance-day*s_day)/s_hour;//还剩多少小时
            Long min = (balance-day*s_day-hour*s_hour)/s_min;//还剩多少分钟
            Long sec = (balance-day*s_day-hour*s_hour-min*s_min)/1000;//还剩多少秒

            com.alibaba.fastjson.JSONObject dataJson = new com.alibaba.fastjson.JSONObject();
            dataJson.put("day",day);
            dataJson.put("hour",hour);
            dataJson.put("min",min);
            dataJson.put("sec",sec);
            jsonObject.put("countDown",dataJson);
        }

        return jsonObject;
    }

    @Override
    public Object combination(Integer goodsId, Integer size) throws YesmywineException {
        Goods goods = findOne(goodsId);
        if(ValueUtil.isEmpity(goods)){
            ValueUtil.isError("无此商品");
        }
//        String skuIdString = goods.getSkuIdString();
        List<GoodsSku> goodsSkuList=goods.getGoodsSku();
        JsonParser jsonParser = new JsonParser();
//        JsonArray arr = jsonParser.parse(skuIdString).getAsJsonArray();
        List<Integer> skuIdList = new ArrayList<>();
        for (GoodsSku goodsSku:goodsSkuList){
            skuIdList.add(goodsSku.getSkuId());
        }
        List<Goods> goodsList = goodsDao.findBySkuIdIn(skuIdList,size==null?6:size);
        goodsList.remove(goods);
        JSONArray array = new JSONArray();
        for (Goods goodsInfo:goodsList) {
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            jsonObject.put("goodsPicture",goodsInfo.getGoodsImageUrl());
            jsonObject.put("goodsName",goodsInfo.getGoodsName());
            jsonObject.put("salePrice",goodsInfo.getSalePrice());
            jsonObject.put("price",goodsInfo.getPrice());
            jsonObject.put("goodsId",goodsInfo.getId());
            array.add(jsonObject);
        }
        return array;
    }

    @Override
    public Object parameter(Integer goodsId) throws YesmywineException {
        Goods goods = findOne(goodsId);
        List<GoodsProp>list = goods.getGoodsProp();
        JSONArray jsonArray = new JSONArray();
        for (GoodsProp goodsProp:list) {
            com.alibaba.fastjson.JSONObject jsonObject1 = new com.alibaba.fastjson.JSONObject();
            Properties properties = propertiesDao.findOne(goodsProp.getPropId());
            String propName=properties.getCnName();
            String propValueId=goodsProp.getPropValue();
            String propertiesValue=null;
            if(properties.getEntryMode()== EntryMode.manual){
                propertiesValue=propValueId;
            }else {
                propertiesValue = this.properValueDao.findOne(Integer.parseInt(propValueId)).getCnValue();
            }
//            String propValue = properValueDao.findOne(goodsProp.getPropValueId()).getCnValue();
            jsonObject1.put(propName, propertiesValue);
            jsonArray.add(jsonObject1);
        }

        return jsonArray;
    }

    @Scheduled(cron = "0 00 00 ? * *")//"0/5 * *  * * ? "
    public void cancelOrders() {//每天凌晨执行一遍
        System.out.println("定时任务开始......");
        long begin = System.currentTimeMillis();
        System.out.println("系统当前时间");
        try {
            updateComment();
        } catch (YesmywineException e) {
            e.getMsg();
        }
        long end = System.currentTimeMillis();
        System.out.println("定时任务结束，共耗时：[" + (end - begin) / 1000 + "]秒");
    }

    @Override
    public String updateComment() throws YesmywineException {
        List<Goods> list=goodsDao.findAll();
//        List<Goods> list1=new ArrayList<>();
        for(Goods g:list){
            Integer goodsId=g.getId();
            try {
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/evaluation/comments/goodComments/itf", RequestMethod.get);
                httpRequest.addParameter("type", 1);
                httpRequest.addParameter("goodsId", goodsId);
                httpRequest.run();
                String result = httpRequest.getResponseContent();
                String comment = ValueUtil.getFromJson(result, "data", "comment");
                String praise = ValueUtil.getFromJson(result, "data", "praise");
                g.setComments(Integer.parseInt(comment));//评论数
                g.setPraise(Double.valueOf(praise));//好评率
                goodsDao.save(g);
                //同步到评论,cms
                if(g.getGoStatus()==1){
                    //同步到cms,评论
                    com.alibaba.fastjson.JSONObject jsonData1 = ValueUtil.toJsonObject(g);
                    if (null == g.getComments()) {
                        jsonData1.put("comments", "");
                    }
                    if (null == g.getSales()) {
                        jsonData1.put("sales", "");
                    }
                    if (ValueUtil.isEmpity(g.getPraise())) {
                        jsonData1.put("praise", "");
                    }
                    if (null == g.getGoodsEnName()) {
                        jsonData1.put("goodsEnName", "");
                    }
                    jsonData1.put("synchronous", 1);
//        SynchronizeUtils.getCode("http://88.88.88.84:8084","/cms/synchronous/goods",jsonData.toJSONString(),RequestMethod.post);
                    String code = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/cms/synchronous/goods", jsonData1.toJSONString(), RequestMethod.post);
                    if (!"200".equals(code) || ValueUtil.isEmpity(code)) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        ValueUtil.isError("同步到cms失败");
                    }
                    String code1 = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/evaluation/goods/synchronous", jsonData1.toJSONString(), RequestMethod.post);
                    if (!"200".equals(code1) || ValueUtil.isEmpity(code1)) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        ValueUtil.isError("同步到评论失败");
                    }
                }
//                com.alibaba.fastjson.JSONObject jsonData= ValueUtil.toJsonObject(g);
//                jsonData.put("synchronous",1);
//                SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/evaluation/goods/synchronous",jsonData.toJSONString(),RequestMethod.post);
//              list1.add(g);
            }catch (Exception e){
                return "连接被拒绝";
            }
        }
        return "success";
    }

    @Override
    public Object getSkuId(Integer goodsId) throws YesmywineException {
        Goods luckBage = goodsDao.findOne(goodsId);
        if (luckBage.getItem() != Item.luckBage) {
            ValueUtil.isError("该商品不是福袋");
        }
        Integer randNumber = luckBage.getRandomNumber();//随机数量
        Integer channelId = luckBage.getChannelId();
        List<Integer> set = new ArrayList<>();
        List<GoodsSku> list = luckBage.getGoodsSku();
        while (true) {
            Integer all=0;
            for (GoodsSku goodsSku : list) {
                Integer inventory = Integer.parseInt(http(goodsSku.getSkuId(), channelId));
                Integer sale = goodsSku.getSoldNumber();//已卖
                if(null==sale){
                    sale=0;
                }
                Integer count = goodsSku.getCount();//销售上限
                Integer canSale = inventory < count ? inventory : count;//实际可售数量
                if (canSale > sale) {
                    set.add(goodsSku.getSkuId());
                    all=canSale-sale+all;
                }
                if (set.size() == randNumber) {
                    break;
                }
            }
            if (set.size() == randNumber){
                break;
            }
            if(all<randNumber){
                ValueUtil.isError("福袋可售数量不足,不可出售"); ;
            }
        }
        for(Integer i:set) {
            for (GoodsSku goodsSku : list) {
                if(goodsSku.getSkuId()==i){
                    Integer sold=goodsSku.getSoldNumber();
                    if(null==sold){
                        sold=0;
                    }
                    goodsSku.setSoldNumber(sold+1);
                    goodsSkudDao.save(goodsSku);
                }
            }
        }
        com.alibaba.fastjson.JSONArray array=new com.alibaba.fastjson.JSONArray();
        Map<Integer,Integer> map=new HashMap<>();
        for(Integer i:set){
            if(map.containsKey(i)){
                map.put(i,map.get(i).intValue()+1);
            }else {
                map.put(i,1);
            }
        }
        Iterator iterator=map.entrySet().iterator();
        while (iterator.hasNext()){
            com.alibaba.fastjson.JSONObject j=new com.alibaba.fastjson.JSONObject();
            Map.Entry entry = (Map.Entry) iterator.next();
            Integer key = (Integer)entry.getKey();
            Integer value = (Integer)entry.getValue();
            String code=skuDao.findOne(key).getCode();
            j.put("skuId",key);
            j.put("count",value);
            j.put("code",code);
            array.add(j);
        }
        return array;
    }

    @Override
    public Object cancelSkuId(Integer goodsId,String jsonArray) throws YesmywineException {//取消福袋
        Goods goods=goodsDao.findOne(goodsId);
        List<GoodsSku> list1=goods.getGoodsSku();
        JsonParser jsonParser = new JsonParser();
        JsonArray arr = jsonParser.parse(jsonArray).getAsJsonArray();
        for (int f = 0; f < arr.size(); f++) {
            String skuId=arr.get(f).getAsJsonObject().get("skuId").getAsString();
            String count=arr.get(f).getAsJsonObject().get("count").getAsString();
            for (GoodsSku goodsSku : list1) {
                if(goodsSku.getSkuId()==Integer.parseInt(skuId)){
                    goodsSku.setSoldNumber(goodsSku.getSoldNumber()-Integer.parseInt(count));
                    goodsSkudDao.save(goodsSku);
                }
        }
        }
        return "取消成功";
    }

    @Override
    public String setBookGoods(Integer []goodsId,Integer []count,String[] price,String saleModel,String startTime,String endTime) throws YesmywineException {
        List<Goods> list=new ArrayList<>();
        for(int i=0;i<goodsId.length;i++){
            Goods goods=goodsDao.findOne(goodsId[i]);
            goods.setBooknumber(count[i]);
            goods.setRemainBooknumber(count[i]);
            goods.setSalePrice(price[i]);
            if(saleModel.equals("2")){
                goods.setOperate(2);
                goods.setStartTime(DateUtil.toDate(startTime,"yyyy-MM-dd HH:mm:ss"));
                goods.setEndTime(DateUtil.toDate(endTime,"yyyy-MM-dd HH:mm:ss"));
            }else {
                goods.setOperate(0);
            }
            list.add(goods);
                com.alibaba.fastjson.JSONObject json= ValueUtil.toJsonObject(goods);
                String jsonData=json.toJSONString();
                String code3=SynchronizeUtils.getCode(Dictionary.MALL_HOST,"/activity/syn/receiveGoodsInfo",jsonData,RequestMethod.post);
            if (!"201".equals(code3) || ValueUtil.isEmpity(code3)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("同步到活动失败");
            }
            if (null == goods.getComments()) {
                json.put("comments", "");
            }
            if (null == goods.getSales()) {
                json.put("sales", "");
            }
            if (ValueUtil.isEmpity(goods.getPraise())) {
                json.put("praise", "");
            }
            if (null == goods.getGoodsEnName()) {
                json.put("goodsEnName", "");
            }
            json.put("synchronous", 1);
//        SynchronizeUtils.getCode("http://88.88.88.84:8084","/cms/synchronous/goods",jsonData.toJSONString(),RequestMethod.post);
            String code = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/cms/synchronous/goods", json.toJSONString(), RequestMethod.post);
            if (!"200".equals(code) || ValueUtil.isEmpity(code)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("同步到cms失败");
            }
            String code1 = SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/evaluation/goods/synchronous", json.toJSONString(), RequestMethod.post);
            if (!"200".equals(code1) || ValueUtil.isEmpity(code1)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("同步到评论失败");
            }
            try {
                this.searchService.saveGoodsSearch(goods);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("同步到solr失败");
            }
        }
        goodsDao.save(list);
        return "success";
    }

    public String http ( Integer skuId,Integer channelId){//查库存
        HttpBean bean = new HttpBean(Dictionary.MALL_HOST + "/inventory/channelInventory/skuInventory/itf", RequestMethod.get);
        bean.addParameter("skuId",skuId);
//        bean.addParameter("channelId",channelId);
        bean.run();
        String json=bean.getResponseContent();
        String useCount="0";
        if(ValueUtil.notEmpity(ValueUtil.getFromJson(json,"data"))){
            useCount=ValueUtil.getFromJson(json,"data","useCount");
        }
        return useCount;
    }


    public String delete(Integer goodsId) throws YesmywineException {//删除福袋
            Goods goods=goodsDao.findOne(goodsId);
            if(goods.getItem()!=Item.luckBage){
                ValueUtil.isError("该商品不是福袋,不可删除");
            }else if(goods.getStatus()!=0){
                ValueUtil.isError("该商品不能删除！");
            }
            goodsDao.delete(goodsId);
            return "delete success";

        }

    public String synchronous(Map<String,Object> map){
        if(0==Integer.parseInt(String.valueOf(map.get("synchronous")))){
            Goods goods=new Goods();
            goods.setGoodsImageUrl(String.valueOf(map.get("goodsImageUrl")));
            goods.setGoodsOriginalName(String.valueOf(map.get("goodsName")));
            goods.setGoodsName(String.valueOf(map.get("goodsName")));
            goods.setGoodsCode(String.valueOf(map.get("goodsCode")));
            goods.setSyncToStore(false);
            if(map.get("item").equals("single")){
                goods.setItem(Item.single);
            }else if(map.get("item").equals("fictitious")) {
                goods.setItem(Item.fictitious);
            }else {
                goods.setItem(Item.plural);
            }
                goods.setReferencePrice(String.valueOf(map.get("price")));
//                goods.setSkuIdString(String.valueOf(map.get("skuIdString")));
            JsonParser jsonParser = new JsonParser();
            JsonArray arr1 = jsonParser.parse(String.valueOf(map.get("skuIdString"))).getAsJsonArray();//[{"skuId":"21","count":"2"}]
//            com.alibaba.fastjson.JSONArray arr1 = com.alibaba.fastjson.JSONArray.parseArray(String.valueOf(map.get("skuIdString")));
            List<GoodsSku> list=new ArrayList<>();
            for (int f = 0; f < arr1.size(); f++) {
//                arr1.get(f).toString()
                String skuId=arr1.get(f).getAsJsonObject().get("skuId").getAsString();
                String count=arr1.get(f).getAsJsonObject().get("count").getAsString();
                String code=arr1.get(f).getAsJsonObject().get("code").getAsString();
                GoodsSku goodsSku=new GoodsSku();
                goodsSku.setCount(Integer.parseInt(count));
                goodsSku.setSkuId(Integer.parseInt(skuId));
                goodsSku.setCode(code);
                list.add(goodsSku);
                }
                goods.setGoodsSku(list);
                goodsSkudDao.save(list);
                goods.setPassGoodsId(Integer.parseInt(String.valueOf(map.get("id"))));
                goods.setOperate(Integer.parseInt(String.valueOf(map.get("operate"))));
                goods.setChannelName(String.valueOf(map.get("channelName")));
                goods.setChannelCode(String.valueOf(map.get("channelCode")));
                goods.setChannelId(Integer.parseInt(String.valueOf(map.get("channelId"))));
                goods.setCategoryId(Integer.parseInt(String.valueOf(map.get("categoryId"))));
                goods.setCategoryGroup(String.valueOf(map.get("categoryGroup")));
                goods.setDiscount(1);
                goods.setStatus(0);
                goods.setGoStatus(0);
            goodsDao.save(goods);
            if(map.get("item").equals("single")) {
                JsonElement jsonElement = arr1.get(0).getAsJsonObject().get("image");
                JsonArray prop = arr1.get(0).getAsJsonObject().get("prop").getAsJsonArray();
                List<GoodsProp> skuCommonProps = new ArrayList<>();
                for(int i=0;i<prop.size();i++){
                    GoodsProp skuCommonProp = new GoodsProp();
                    skuCommonProp.setPropId(Integer.valueOf(prop.get(i).getAsJsonObject().get("propId").getAsString()));
                    JsonElement jsonElement2 = prop.get(i).getAsJsonObject().get("propValue");
                    if(ValueUtil.notEmpity(jsonElement2)) {
                        skuCommonProp.setPropValue(jsonElement2.getAsString());
                    }
                    JsonElement jsonElement1 = prop.get(i).getAsJsonObject().get("propType");
                    if(ValueUtil.notEmpity(jsonElement1)) {
                        skuCommonProp.setType(Integer.valueOf(jsonElement1.getAsString()));
                    }
                    skuCommonProp.setUpdate(false);
                    skuCommonProps.add(skuCommonProp);
                }
                this.goodsPropDao.save(skuCommonProps);

                goods.setGoodsProp(skuCommonProps);
                if(ValueUtil.notEmpity(jsonElement)) {
                    String imageString = jsonElement.toString();
                    com.alibaba.fastjson.JSONArray image = com.alibaba.fastjson.JSONArray.parseArray(imageString);
                    Integer[] imageIds = new Integer[image.size()];
                    for (int i = 0; i < image.size(); i++) {
                        String s = image.get(i).toString();
                        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(s);
                        imageIds[i] = Integer.valueOf(jsonObject.get("id").toString());
                    }
                    try {
                        String s = this.saveGoodsImg(goods.getId(), imageIds);
                        goods.setGoodsImageUrl(s);
                    } catch (YesmywineException e) {
                        return "erro";
                    }
                }
                this.goodsDao.save(goods);
            }
            return "add success";
        }else {
            Integer passId = Integer.parseInt(String.valueOf(map.get("id")));
            goodsDao.delete(goodsDao.findByPassGoodsId(passId).getId());
            return "delete success";
        }
    }


    public String synchronousProp(Map<String,Object> map){
        Integer categoryId = Integer.valueOf(map.get("categoryId").toString());
        Integer proptiesId = Integer.valueOf(map.get("proptiesId").toString());
        Integer type = Integer.valueOf(map.get("type").toString());

        List<CategoryProperty> byCategoryIdAndPropertyId = this.categoryPropertyDao.findByCategoryIdAndPropertyId(categoryId, proptiesId);
        for (CategoryProperty categoryProperty : byCategoryIdAndPropertyId) {
            categoryProperty.setType(type);
        }
        this.categoryPropertyDao.save(byCategoryIdAndPropertyId);
        return "success";
    }

    public String synchronousGoods(Map<String,String> map){
        String valueJson=map.get("valueJson");
        Integer skuId=Integer.parseInt(map.get("skuId"));
        String image=map.get("imgIds");
        List<Goods> list=goodsDao.findByItem(Item.single);
        for(int i=0;i<list.size();i++){
            List<GoodsSku> list1=list.get(i).getGoodsSku();
            if(list1.size()!=0) {
                Integer skuIds = list1.get(0).getSkuId();
                if (skuId == skuIds) {
                    List<GoodsProp> list3 = list.get(i).getGoodsProp();
                    JSONObject obj = new JSONObject(valueJson);
                    Iterator it = obj.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        String value = obj.getString(key);
                        for (GoodsProp g : list3) {
                            Integer propId = g.getPropId();
                            boolean isUpdate;
                            if (ValueUtil.isEmpity(g.isUpdate())) {
                                isUpdate = false;
                            } else {
                                isUpdate = g.isUpdate();
                            }
                            if (!isUpdate && propId == Integer.parseInt(key)) {//false未改动
                                g.setPropValue(value);
                                goodsPropDao.save(g);
                            }
                        }
                    }
                }
            }
        }
        return "success";
    }

    @Override
    public List<Goods> findByOperateAndPreStatusAndEndTimeGt(Integer saleModel, Integer preStatus) {
        Date now = new Date();
//        goodsDao.findByOperateAndPreStatusAndEndTimeGreaterThan(saleModel,preStatus,now)
        return goodsDao.findByOperateAndPreStatus(saleModel,preStatus);
    }

    @Override
    public com.alibaba.fastjson.JSONObject showOne(Integer goodsId) throws YesmywineException {
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        GoodsDetail goodsDetail=goodsDetailDao.findByGoodsId(goodsId);
        if(null!=goodsDetail){
            jsonObject.put("goodsDetail",goodsDetail.getGoodsDetail());
        }else
        {
            jsonObject.put("goodsDetail","");
        }
        return jsonObject;
    }

    @Override
    public com.alibaba.fastjson.JSONObject showMirrorOne(Integer goodsMirrorId) throws YesmywineException {
        com.alibaba.fastjson.JSONObject jsonObject=new com.alibaba.fastjson.JSONObject();
        GoodsMirrorDetail goodsDetail=goodsMirrorDetailDao.findByGoodsMirrorId(goodsMirrorId);
        if(null!=goodsDetail){
            jsonObject.put("goodsDetail",goodsDetail.getGoodsDetail());
        }else
        {
            jsonObject.put("goodsDetail","");
        }
        return jsonObject;
    }

    @Override
    public Object categoryGoods(Integer cagetoryId, Integer pageNo, Integer pageSize) {
        List<Category> categoryList = categoryDao.getAllChildren(cagetoryId);
        categoryList.add(categoryDao.findOne(cagetoryId));
        Integer [] categoryIds1 = new Integer[categoryList.size()];
        List<Integer> categoryIds = new ArrayList();
        int i=0;
        for(Category category:categoryList){
            categoryIds1[i] = category.getId();
            categoryIds.add(category.getId());
            i++;
        }
        Query query = entityManager.createQuery("select g from Goods g where g.categoryId in (:categoryIds)");
        query.setParameter("categoryIds",categoryIds);
        query.setFirstResult((pageNo==null?0:pageNo-1)*(pageSize==null?10:pageSize));
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();
        Integer totalCount = goodsDao.findByCategoryIdIn(categoryIds1);
        PageModel pageModel = new PageModel(pageNo==null?1:pageNo,pageSize==null?10:pageSize);
        pageModel.setTotalRows(Long.valueOf(totalCount));
        long tempTPd = pageModel.getTotalRows() % pageModel.getPageSize();
        Integer tempTp = Integer.valueOf((pageModel.getTotalRows() / pageModel.getPageSize()) + "");
        if (tempTPd == 0) {
            pageModel.setTotalPages(tempTp);
        } else {
            pageModel.setTotalPages(tempTp + 1);
        }
        pageModel.setContent(list);
        return pageModel;
    }

    @Override
    public PageModel findByGoods(Integer pageNo, Integer pageSize,String goodsName_l) {
        String sql="SELECT * FROM mall_goods.goods  WHERE item not in (\"luckBage\",\"fictitious\") and operate NOT in(\"1\") AND goods.goStatus=1";
        Integer totalCount = goodsDao.findByGoods();
        if(ValueUtil.notEmpity(goodsName_l)){
            sql="SELECT * FROM mall_goods.goods  WHERE item not in (\"luckBage\",\"fictitious\") and operate NOT in(\"1\") AND goods.goStatus=1 AND goods.goodsName LIKE "+"'%"+goodsName_l+"%'";
            totalCount = goodsDao.findByGoodsl(goodsName_l);
        }
        Query query = entityManager.createNativeQuery(sql,Goods.class);
            query.setFirstResult((pageNo==null?0:pageNo-1)*(pageSize==null?10:pageSize));
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();


        PageModel pageModel = new PageModel(pageNo==null?1:pageNo,pageSize==null?10:pageSize);
        pageModel.setTotalRows(Long.valueOf(totalCount));
        long tempTPd = pageModel.getTotalRows() % pageModel.getPageSize();
        Integer tempTp = Integer.valueOf((pageModel.getTotalRows() / pageModel.getPageSize()) + "");
        if (tempTPd == 0) {
            pageModel.setTotalPages(tempTp);
        } else {
            pageModel.setTotalPages(tempTp + 1);
        }
        pageModel.setContent(list);
        return pageModel;
    }

    @Override
    public String updateSales(String jsonString) throws YesmywineException {
        JsonParser jsonParser = new JsonParser();
        JsonArray arr = jsonParser.parse(jsonString).getAsJsonArray();
        List<Goods> list=new ArrayList<>();
        for (int f = 0; f < arr.size(); f++) {
            String goodsId = arr.get(f).getAsJsonObject().get("goodsId").getAsString();
            String count = arr.get(f).getAsJsonObject().get("count").getAsString();
            Goods goods = goodsDao.findOne(Integer.parseInt(goodsId));
            Integer sales=goods.getSales();
            if(null==sales){
                sales=0;
            }
            goods.setSales(sales+Integer.parseInt(count));
            list.add(goods);
        }
        goodsDao.save(list);
        return "success";
    }
}


