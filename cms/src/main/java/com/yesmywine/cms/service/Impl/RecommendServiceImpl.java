package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yesmywine.cms.dao.RecommendFirstDao;
import com.yesmywine.cms.entity.RecommendFirst;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.RecommendService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.cms.dao.GoodsDao;
import com.yesmywine.cms.dao.RecommendSecentDao;
import com.yesmywine.cms.entity.ConstantData;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.entity.RecommendSecent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class RecommendServiceImpl implements RecommendService {

    @Autowired
    private RecommendFirstDao recommendFirstDao;
    @Autowired
    private RecommendSecentDao recommendSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer recommendFirstId) {
        RecommendFirst one = this.recommendFirstDao.findOne(recommendFirstId);
        if(ValueUtil.isEmpity(one)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getName());
        if(ValueUtil.notEmpity(one.getUserId())){
            jsonObject.put("userId", one.getUserId());

            HttpBean httpRequest = new HttpBean( Dictionary.MALL_HOST+"/userservice/userInfomation/show/itf" , RequestMethod.get);
            httpRequest.addParameter("userId",one.getUserId());
            httpRequest.run();
            String temp = httpRequest.getResponseContent();

//            Map<String,Object> map = new HashMap<>();
//            map.put("userId",one.getUserId());
//                    String temp = SynchronizeUtils.getResult(ConstantData.user,"/itf/web/userservice/userInfomation/show",RequestMethod.get);
            String userName=ValueUtil.getFromJson(temp,"data","userName");
            String nickName=ValueUtil.getFromJson(temp,"data","nickName");
            jsonObject.put("userName",userName);
            jsonObject.put("nickName",nickName);
            jsonObject.put("reasons",one.getReasons());
        }
        List<RecommendSecent> byRecommendFirstId = this.recommendSecentDao.findByRecommendFirstId(one.getId());

        for(RecommendSecent recommendSecent: byRecommendFirstId){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", recommendSecent.getId());
            Goods one2 = this.goodsService.findById(recommendSecent.getGoodsId());
            jsonObjectSecent.put("goodsId", recommendSecent.getGoodsId());
            jsonObjectSecent.put("name", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("goods", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<RecommendFirst> all = this.recommendFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        JsonParser jsonParser = new JsonParser();
        for(RecommendFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            if(ValueUtil.notEmpity(one.getUserId())){
                jsonObject.put("userId", one.getUserId());
            }
            if(one.getId()==2){//刚购买过
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/evaluation/comments/buy/itf", RequestMethod.get);
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String goods=ValueUtil.getFromJson(temp,"data");
                JsonArray goodsArray = jsonParser.parse(goods).getAsJsonArray();
                if(goodsArray.size()==0){
                    jsonObject.put("goods",new JSONArray());
                }
                for (int f = 0; f < goodsArray.size(); f++) {
                    String picture = goodsArray.get(f).getAsJsonObject().get("picture").getAsString();
//                    String enName = goodsArray.get(f).getAsJsonObject().get("enName").getAsString();
                    JsonElement jsonElementEn = goodsArray.get(f).getAsJsonObject().get("enName");
                    String enName = null;
                    if(ValueUtil.notEmpity(jsonElementEn) && !"null".equals(jsonElementEn.toString())) {
                        enName = jsonElementEn.getAsString();
                    }
                    String price = goodsArray.get(f).getAsJsonObject().get("price").getAsString();
                    String salePrice = goodsArray.get(f).getAsJsonObject().get("salePrice").getAsString();
                    JsonElement jsonElement = goodsArray.get(f).getAsJsonObject().get("content");
                    String content = null;
                    if(ValueUtil.notEmpity(jsonElement) && !"null".equals(jsonElement.toString())) {
                        content = jsonElement.getAsString();
                    }
                    String name = goodsArray.get(f).getAsJsonObject().get("name").getAsString();
                    String comment = goodsArray.get(f).getAsJsonObject().get("comment").getAsString();
                    JsonElement jsonElement1 = goodsArray.get(f).getAsJsonObject().get("sales");
                    String sales = null;
                    if(ValueUtil.notEmpity(jsonElement1) && !"null".equals(jsonElement1.toString())) {
                        sales = jsonElement1.getAsString();
                    }
                    String goodsId = goodsArray.get(f).getAsJsonObject().get("goodsId").getAsString();
                    JSONObject map1 = new JSONObject();
                    map1.put("picture", picture);
                    map1.put("content", content);
                    map1.put("name", name);
                    map1.put("goodsEnName", enName);
                    map1.put("comment", comment);
                    map1.put("sales", sales);
                    map1.put("goodsId", goodsId);
                    map1.put("price", price);
                    map1.put("salePrice", salePrice);
                    jsonArray.add(map1);
                    jsonObject.put("goods",jsonArray);
                }
            }else if(one.getId()==1){//刚刚被好评的
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/evaluation/comments/buyGoodComment/itf", RequestMethod.get);
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String goods=ValueUtil.getFromJson(temp,"data");
                JsonArray goodsArray = jsonParser.parse(goods).getAsJsonArray();
                if(goodsArray.size()==0){
                    jsonObject.put("goods",new JSONArray());
                }
                for (int f = 0; f < goodsArray.size(); f++) {
                    String picture = goodsArray.get(f).getAsJsonObject().get("picture").getAsString();
                    String price = goodsArray.get(f).getAsJsonObject().get("price").getAsString();
                    String salePrice = goodsArray.get(f).getAsJsonObject().get("salePrice").getAsString();
                    JsonElement jsonElementEn = goodsArray.get(f).getAsJsonObject().get("enName");
                    String enName = null;
                    if(ValueUtil.notEmpity(jsonElementEn) && !"null".equals(jsonElementEn.toString())) {
                        enName = jsonElementEn.getAsString();
                    }
//                    String enName = goodsArray.get(f).getAsJsonObject().get("enName").getAsString();
                    JsonElement jsonElement = goodsArray.get(f).getAsJsonObject().get("content");
                    String content = null;
                    if(ValueUtil.notEmpity(jsonElement) && !"null".equals(jsonElement.toString())) {
                        content = jsonElement.getAsString();
                    }
                    String name = goodsArray.get(f).getAsJsonObject().get("name").getAsString();
                    String comment = goodsArray.get(f).getAsJsonObject().get("comment").getAsString();
                    JsonElement jsonElement1 = goodsArray.get(f).getAsJsonObject().get("sales");
                    String sales = null;
                    if(ValueUtil.notEmpity(jsonElement1) && !"null".equals(jsonElement1.toString())) {
                        sales = jsonElement1.getAsString();
                    }
                    String goodsId = goodsArray.get(f).getAsJsonObject().get("goodsId").getAsString();
                    String evaluation = goodsArray.get(f).getAsJsonObject().get("evaluation").getAsString();
                    JSONObject map1 = new JSONObject();
                    map1.put("picture", picture);
                    map1.put("content", content);
                    map1.put("name", name);
                    map1.put("comment", comment);
                    map1.put("goodsEnName", enName);
                    map1.put("sales", sales);
                    map1.put("goodsId", goodsId);
                    map1.put("price", price);
                    map1.put("salePrice", salePrice);
                    map1.put("evaluation",evaluation);
                    jsonArray.add(map1);
                    jsonObject.put("goods",jsonArray);
                }
            }else if(one.getId()==3){//酒友品鉴
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/evaluation/comments/buyCondition/itf", RequestMethod.get);
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String goods=ValueUtil.getFromJson(temp,"data");
                JsonArray goodsArray = jsonParser.parse(goods).getAsJsonArray();
                if(goodsArray.size()==0){
                    jsonObject.put("goods",new JSONArray());
                }
                for (int f = 0; f < goodsArray.size(); f++) {
                    String picture = goodsArray.get(f).getAsJsonObject().get("picture").getAsString();
                    String price = goodsArray.get(f).getAsJsonObject().get("price").getAsString();
//                    String enName = goodsArray.get(f).getAsJsonObject().get("enName").getAsString();
                    JsonElement jsonElementEn = goodsArray.get(f).getAsJsonObject().get("enName");
                    String enName = null;
                    if(ValueUtil.notEmpity(jsonElementEn) && !"null".equals(jsonElementEn.toString())) {
                        enName = jsonElementEn.getAsString();
                    }
                    String salePrice = goodsArray.get(f).getAsJsonObject().get("salePrice").getAsString();
                    JsonElement jsonElement = goodsArray.get(f).getAsJsonObject().get("content");
                    String content = null;
                    if(ValueUtil.notEmpity(jsonElement) && !"null".equals(jsonElement.toString())) {
                        content = jsonElement.getAsString();
                    }
                    String name = goodsArray.get(f).getAsJsonObject().get("name").getAsString();
                    String comment = goodsArray.get(f).getAsJsonObject().get("comment").getAsString();
                    JsonElement jsonElement1 = goodsArray.get(f).getAsJsonObject().get("sales");
                    String sales = null;
                    if(ValueUtil.notEmpity(jsonElement1) && !"null".equals(jsonElement1.toString())) {
                        sales = jsonElement1.getAsString();
                    }
                    String goodsId = goodsArray.get(f).getAsJsonObject().get("goodsId").getAsString();
                    JSONObject map1 = new JSONObject();
                    map1.put("picture", picture);
                    map1.put("content", content);
                    map1.put("name", name);
                    map1.put("goodsEnName", enName);
                    map1.put("comment", comment);
                    map1.put("sales", sales);
                    map1.put("goodsId", goodsId);
                    map1.put("price", price);
                    map1.put("salePrice", salePrice);
                    jsonArray.add(map1);
                    jsonObject.put("goods",jsonArray);
                }
            }else{
                List<RecommendSecent> byRecommendFirstId = this.recommendSecentDao.findByRecommendFirstId(one.getId());

                for(RecommendSecent recommendSecent: byRecommendFirstId){
                    JSONObject jsonObjectSecent = new JSONObject();
                    jsonObjectSecent.put("id", recommendSecent.getId());
                    Goods one2 = this.goodsService.findById(recommendSecent.getGoodsId());
                    jsonObjectSecent.put("goodsId", recommendSecent.getGoodsId());
                    jsonObjectSecent.put("name", one2.getGoodsName());
                    jsonObjectSecent.put("goodsEnName", one2.getGoodsEnName());
                    jsonObjectSecent.put("picture", one2.getPicture());
                    jsonObjectSecent.put("price", one2.getPrice());
                    jsonObjectSecent.put("salePrice", one2.getSalePrice());
                    jsonObjectSecent.put("sales", one2.getSales());
                    jsonArray.add(jsonObjectSecent);
                }
                //通过userId获取一下信息
                if(one.getId()==6){
                    Integer userId=one.getUserId();
                    try {
                        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/userservice/userInfomation/show/itf", RequestMethod.get);
                        httpRequest.addParameter("userId", userId);
                        httpRequest.run();
                        String temp = httpRequest.getResponseContent();

//                    Map<String,Object> map = new HashMap<>();
//                    map.put("userId",userId);
//                    String temp = SynchronizeUtils.getResult(ConstantData.user,"/itf/web/userservice/userInfomation/show",RequestMethod.get);
                        String userName = ValueUtil.getFromJson(temp, "data", "userName");
                        String nickName = ValueUtil.getFromJson(temp, "data", "nickName");
                        String userImg = ValueUtil.getFromJson(temp, "data", "userImg");
                        jsonObject.put("userName", userName);
                        jsonObject.put("nickName", nickName);
                        jsonObject.put("image", userImg);
                        jsonObject.put("reasons", one.getReasons());
                    }catch (Exception e){
                        jsonObject.put("userName", "");
                        jsonObject.put("nickName", "");
                        jsonObject.put("image", "");
                        jsonObject.put("reasons", "");
                    }
                }
                jsonObject.put("goods", jsonArray);
            }
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(String name, Integer userId, String jsonString,String reasons) {
        try {
            RecommendFirst byName = this.recommendFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)){
                return "此栏目已存在";
            }
            RecommendFirst recommendFirst = new RecommendFirst();
            recommendFirst.setName(name);
            recommendFirst.setUserId(userId);
            recommendFirst.setReasons(reasons);
            this.recommendFirstDao.save(recommendFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    RecommendSecent recommendSecent = new RecommendSecent();
                    recommendSecent.setGoodsId(goodsId);
                    recommendSecent.setRecommendFirstId(recommendFirst.getId());
                    this.recommendSecentDao.save(recommendSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name, Integer userId, String jsonString,String reasons) {
        try {
            RecommendFirst byName = this.recommendFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)&& byName.getId()!=id){
                return "此栏目已存在";
            }
            RecommendFirst recommendFirst = new RecommendFirst();
            recommendFirst.setName(name);
            recommendFirst.setUserId(userId);
            recommendFirst.setReasons(reasons);
            recommendFirst.setId(id);
            this.recommendFirstDao.save(recommendFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    RecommendSecent recommendSecent = new RecommendSecent();
                    recommendSecent.setGoodsId(goodsId);
                    recommendSecent.setRecommendFirstId(recommendFirst.getId());
                    RecommendSecent byGoodsIdAndRecommendFirstId = this.recommendSecentDao.findByGoodsIdAndRecommendFirstId(goodsId, recommendFirst.getId());
                    if(ValueUtil.notEmpity(byGoodsIdAndRecommendFirstId)){
                        recommendSecent.setId(byGoodsIdAndRecommendFirstId.getId());
                    }
                    this.recommendSecentDao.save(recommendSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String deleteFirst(Integer id) {
        try{
            this.recommendFirstDao.delete(id);
            this.recommendSecentDao.deleteByRecommendFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.recommendSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
