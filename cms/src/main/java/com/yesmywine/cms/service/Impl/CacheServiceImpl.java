package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.*;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class CacheServiceImpl implements CacheService {

    @Autowired
    private HomePagePanicBuyingDao homePagePanicBuyingDao;
    @Autowired
    private LikeFirstDao likeFirstDao;
    @Autowired
    private LikeSecentDao likeSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private BoutiqueFirstDao boutiqueFirstDao;
    @Autowired
    private BoutiqueSecentDao boutiqueSecentDao;
    @Autowired
    private RecommendFirstDao recommendFirstDao;
    @Autowired
    private RecommendSecentDao recommendSecentDao;
    @Autowired
    private PlateFirstDao plateFirstDao;
    @Autowired
    private PlateSecentGoodsDao plateSecentGoodsDao;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object getGoods() {

        List<HomePagePanicBuying> list = homePagePanicBuyingDao.findAll();
        JSONArray jsonArrayResult = new JSONArray();
        for(HomePagePanicBuying one:list) {
//            JSONObject jsonObjectResult = new JSONObject();
            Integer activityId = one.getActivityId();
            HttpBean httpRequest = new HttpBean(ConstantData.activity + "/web/activity/activityGoods", RequestMethod.post);
            httpRequest.addParameter("activityId", activityId);
            httpRequest.addParameter("pageSize", "6");
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String cd = "";
            try {
                cd = ValueUtil.getFromJson(temp, "code");
            } catch (Exception e) {
                return "erro";
            }
            if (!"200".equals(cd) || ValueUtil.isEmpity(cd)) {
                return "erro";
            } else {
                String result = ValueUtil.getFromJson(temp, "data", "content");
                JSONArray jsonArray = JSONArray.parseArray(result);
//                JSONArray jsonArrayRe = new JSONArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject jsonObjectRe = new JSONObject();
                    jsonObjectRe.put("goodsId", jsonObject.get("id"));
                    jsonObjectRe.put("price", jsonObject.get("price"));
                    jsonObjectRe.put("goodsName", jsonObject.get("goodsName"));
                    jsonObjectRe.put("goodsImageUrl", jsonObject.get("goodsImageUrl"));
                    jsonArrayResult.add(jsonObjectRe);
                }

//                jsonObjectResult.put("goods", jsonArrayRe);
//                jsonArrayResult.add(jsonObjectResult);
            }
        }


        List<LikeFirst> all = this.likeFirstDao.findAll();
//        JSONArray jsonArrayRe = new JSONArray();
        for(LikeFirst one: all) {
//            JSONArray jsonArray= new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", one.getId());
//            jsonObject.put("name", one.getName());
            List<LikeSecent> byLikeFirstId = this.likeSecentDao.findByLikeFirstId(one.getId());

            for(LikeSecent likeSecent: byLikeFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
//                jsonObjectSecent.put("id", likeSecent.getId());
                Goods one2 = this.goodsService.findById(likeSecent.getGoodsId());
                jsonObjectSecent.put("goodsId", likeSecent.getGoodsId());
                jsonObjectSecent.put("goodsName", one2.getGoodsName());
                jsonArrayResult.add(jsonObjectSecent);
            }
//            jsonObject.put("likeSecent", jsonArray);
//            jsonArrayRe.add(jsonObject);
        }


        List<BoutiqueFirst> allBoutiqueFirst = this.boutiqueFirstDao.findAll();
//        JSONArray jsonArrayRe = new JSONArray();
        for(BoutiqueFirst one: allBoutiqueFirst) {
//            JSONArray jsonArray= new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            if(one.getId()==4||one.getId()==5){
//                jsonObject.put("fixed",1);
//            }
//            jsonObject.put("id", one.getId());
//            jsonObject.put("name", one.getName());
            List<BoutiqueSecent> boutiqueSecents = this.boutiqueSecentDao.findByBoutiqueFirstId(one.getId());
            for(BoutiqueSecent boutiqueSecent: boutiqueSecents){
                JSONObject jsonObjectSecent = new JSONObject();
//                jsonObjectSecent.put("id", boutiqueSecent.getId());
                Goods one2 = this.goodsService.findById(boutiqueSecent.getGoodsId());
                jsonObjectSecent.put("goodsName", one2.getGoodsName());
                jsonObjectSecent.put("goodsId",one2.getId());
                jsonArrayResult.add(jsonObjectSecent);
            }

//            jsonObject.put("boutiqueSecent", jsonArray);
//            jsonArrayRe.add(jsonObject);
        }


        List<RecommendFirst> allRecommendFirst = this.recommendFirstDao.findAll();
//        JSONArray jsonArrayRe = new JSONArray();
        JsonParser jsonParser = new JsonParser();
        for(RecommendFirst one: allRecommendFirst) {
//            JSONArray jsonArray= new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", one.getId());
//            jsonObject.put("name", one.getName());
//            if(ValueUtil.notEmpity(one.getUserId())){
//                jsonObject.put("userId", one.getUserId());
//            }
            if(one.getId()==2){//刚购买过
                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/buy/itf", RequestMethod.get);
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String goods=ValueUtil.getFromJson(temp,"data");
                JsonArray goodsArray = jsonParser.parse(goods).getAsJsonArray();
                for (int f = 0; f < goodsArray.size(); f++) {
                    String picture = goodsArray.get(f).getAsJsonObject().get("picture").getAsString();
                    String price = goodsArray.get(f).getAsJsonObject().get("price").getAsString();
                    String content = goodsArray.get(f).getAsJsonObject().get("content").getAsString();
                    String name = goodsArray.get(f).getAsJsonObject().get("name").getAsString();
                    String comment = goodsArray.get(f).getAsJsonObject().get("comment").getAsString();
                    String sales = goodsArray.get(f).getAsJsonObject().get("sales").getAsString();
                    String goodsId = goodsArray.get(f).getAsJsonObject().get("goodsId").getAsString();
                    JSONObject map1 = new JSONObject();
                    map1.put("picture", picture);
                    map1.put("content", content);
                    map1.put("name", name);
                    map1.put("comment", comment);
                    map1.put("sales", sales);
                    map1.put("goodsId", goodsId);
                    map1.put("price", price);
                    jsonArrayResult.add(map1);
//                    jsonObject.put("goods",jsonArray);
                }
            }else if(one.getId()==1){//刚刚被好评的
                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/buyGoodComment/itf", RequestMethod.get);
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String goods=ValueUtil.getFromJson(temp,"data");
                JsonArray goodsArray = jsonParser.parse(goods).getAsJsonArray();
                for (int f = 0; f < goodsArray.size(); f++) {
                    String picture = goodsArray.get(f).getAsJsonObject().get("picture").getAsString();
                    String price = goodsArray.get(f).getAsJsonObject().get("price").getAsString();
                    String content = goodsArray.get(f).getAsJsonObject().get("content").getAsString();
                    String name = goodsArray.get(f).getAsJsonObject().get("name").getAsString();
                    String comment = goodsArray.get(f).getAsJsonObject().get("comment").getAsString();
                    String sales = goodsArray.get(f).getAsJsonObject().get("sales").getAsString();
                    String goodsId = goodsArray.get(f).getAsJsonObject().get("goodsId").getAsString();
                    String evaluation = goodsArray.get(f).getAsJsonObject().get("evaluation").getAsString();
                    JSONObject map1 = new JSONObject();
                    map1.put("picture", picture);
                    map1.put("content", content);
                    map1.put("name", name);
                    map1.put("comment", comment);
                    map1.put("sales", sales);
                    map1.put("goodsId", goodsId);
                    map1.put("price", price);
                    map1.put("evaluation",evaluation);
                    jsonArrayResult.add(map1);
//                    jsonObject.put("goods",jsonArray);
                }
            }else if(one.getId()==3){//酒友品鉴
                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/buyCondition/itf", RequestMethod.get);
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String goods=ValueUtil.getFromJson(temp,"data");
                JsonArray goodsArray = jsonParser.parse(goods).getAsJsonArray();
                for (int f = 0; f < goodsArray.size(); f++) {
                    String picture = goodsArray.get(f).getAsJsonObject().get("picture").getAsString();
                    String price = goodsArray.get(f).getAsJsonObject().get("price").getAsString();
                    String content = goodsArray.get(f).getAsJsonObject().get("content").getAsString();
                    String name = goodsArray.get(f).getAsJsonObject().get("name").getAsString();
                    String comment = goodsArray.get(f).getAsJsonObject().get("comment").getAsString();
                    String sales = goodsArray.get(f).getAsJsonObject().get("sales").getAsString();
                    String goodsId = goodsArray.get(f).getAsJsonObject().get("goodsId").getAsString();
                    JSONObject map1 = new JSONObject();
                    map1.put("picture", picture);
                    map1.put("content", content);
                    map1.put("name", name);
                    map1.put("comment", comment);
                    map1.put("sales", sales);
                    map1.put("goodsId", goodsId);
                    map1.put("price", price);
                    jsonArrayResult.add(map1);
//                    jsonObject.put("goods",jsonArray);
                }
            }else{
                List<RecommendSecent> byRecommendFirstId = this.recommendSecentDao.findByRecommendFirstId(one.getId());

                for(RecommendSecent recommendSecent: byRecommendFirstId){
                    JSONObject jsonObjectSecent = new JSONObject();
                    jsonObjectSecent.put("id", recommendSecent.getId());
                    Goods one2 = this.goodsService.findById(recommendSecent.getGoodsId());
                    jsonObjectSecent.put("goodsId", recommendSecent.getGoodsId());
                    jsonObjectSecent.put("name", one2.getGoodsName());
                    jsonObjectSecent.put("picture", one2.getPicture());
                    jsonObjectSecent.put("price", one2.getPrice());
                    jsonObjectSecent.put("sales", one2.getSales());
                    jsonArrayResult.add(jsonObjectSecent);
                }
//                //通过userId获取一下信息
//                if(one.getId()==6){
//                    Integer userId=one.getUserId();
//                    HttpBean httpRequest = new HttpBean( ConstantData.user+"/itf/userservice/userInfomation/show" , RequestMethod.get);
//                    httpRequest.addParameter("userId",userId);
//                    httpRequest.run();
//                    String temp = httpRequest.getResponseContent();

//                    Map<String,Object> map = new HashMap<>();
//                    map.put("userId",userId);
//                    String temp = SynchronizeUtils.getResult(ConstantData.user,"/itf/web/userservice/userInfomation/show",RequestMethod.get);
//                    String userName=ValueUtil.getFromJson(temp,"data","userName");
//                    String nickName=ValueUtil.getFromJson(temp,"data","nickName");
//                    String userImg=ValueUtil.getFromJson(temp,"data","userImg");
//                    jsonObject.put("userName",userName);
//                    jsonObject.put("nickName",nickName);
//                    jsonObject.put("image",userImg);
//                    jsonObject.put("reasons",one.getReasons());
//                }
//                jsonObject.put("goods", jsonArray);
            }
//            jsonArrayResult.add(jsonObject);
        }


        List<PlateFirst> allPlateFirst = this.plateFirstDao.findAll();
//        JSONArray jsonArrayRe = new JSONArray();
        for(PlateFirst one: allPlateFirst) {
//            JSONArray jsonArrayGoods= new JSONArray();
//            JSONArray jsonArrayLabel= new JSONArray();
//            JSONArray jsonArrayRank= new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", one.getId());
//            jsonObject.put("firstIndex", one.getFirstIndex());
//            jsonObject.put("isShow", one.getIsShow());

//            Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
//            jsonObject.put("firstCategoryId", one.getFirstCategoryId());
//            jsonObject.put("firstCategoryName", one1.getCategoryName());

//            PositionEntity positionEntity1 = this.positionDao.findOne(one.getFirstPositionId());
//            jsonObject.put("firstPositionId", one.getFirstPositionId());
//            jsonObject.put("firstPositionName", positionEntity1.getPositionName());
//            PositionEntity positionEntity2 = this.positionDao.findOne(one.getSecentPositionId());
//            jsonObject.put("secentPositionId", one.getSecentPositionId());
//            jsonObject.put("secentPositionName", positionEntity2.getPositionName());
//            PositionEntity positionEntity3 = this.positionDao.findOne(one.getThirdPositionId());
//            jsonObject.put("thirdPositionId", one.getThirdPositionId());
//            jsonObject.put("thirdPositionName", positionEntity3.getPositionName());
//            PositionEntity positionEntity4 = this.positionDao.findOne(one.getFourthPositionId());
//            jsonObject.put("fourthPositionId", one.getFourthPositionId());
//            jsonObject.put("fourthPositionName", positionEntity4.getPositionName());
//            PositionEntity positionEntity5 = this.positionDao.findOne(one.getAppPositionId());
//            jsonObject.put("appPositionId", one.getAppPositionId());
//            jsonObject.put("appPositionName", positionEntity5.getPositionName());

            List<PlateSecentGoods> byPlateFirstId = this.plateSecentGoodsDao.findByPlateFirstId(one.getId());
            for(PlateSecentGoods plateSecentGoods: byPlateFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
//                jsonObjectSecent.put("id", plateSecentGoods.getId());
                Goods one2 = this.goodsService.findById(plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("goodsId", plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("goodsName", one2.getGoodsName());
                jsonObjectSecent.put("goodsAlias", plateSecentGoods.getAlias());
                jsonArrayResult.add(jsonObjectSecent);
            }
//            jsonObject.put("secentGoods", jsonArrayGoods);

//            List<PlateSecentCategoryLabel> byPlateFirstIdLable = this.plateSecentCategoryLabelDao.findByPlateFirstId(one.getId());
//            for(PlateSecentCategoryLabel plateSecentCategoryLabel: byPlateFirstIdLable){
//                JSONObject jsonObjectSecent = new JSONObject();
//                jsonObjectSecent.put("id", plateSecentCategoryLabel.getId());
//                Category one2 = this.categoryDao.findOne(plateSecentCategoryLabel.getCategoryId());
//                jsonObjectSecent.put("secentCategoryLabelId", plateSecentCategoryLabel.getCategoryId());
//                jsonObjectSecent.put("secentCategoryLabelName", one2.getCategoryName());
//                jsonArrayLabel.add(jsonObjectSecent);
//            }
//            jsonObject.put("secentCategoryLabel", jsonArrayLabel);

//            List<PlateSecentCategoryRank> byPlateFirstIdRank = this.plateSecentCategoryRankDao.findByPlateFirstId(one.getId());
//            for(PlateSecentCategoryRank plateSecentCategoryRank: byPlateFirstIdRank){
//                JSONObject jsonObjectSecent = new JSONObject();
//                jsonObjectSecent.put("id", plateSecentCategoryRank.getId());
//                Category one2 = this.categoryDao.findOne(plateSecentCategoryRank.getCategoryId());
//                jsonObjectSecent.put("secentCategoryRankId", plateSecentCategoryRank.getCategoryId());
//                jsonObjectSecent.put("secentCategoryRankName", one2.getCategoryName());
//                jsonArrayRank.add(jsonObjectSecent);
//            }
//            jsonObject.put("secentCategoryRank", jsonArrayRank);
//            jsonArrayRe.add(jsonObject);
        }

        return jsonArrayResult;
    }

    @Override
    public Object getGoodsFront(Integer pageNo, Integer pageSize) {
        if(ValueUtil.isEmpity(pageNo)){
            pageNo = 1;
        }
        if(ValueUtil.isEmpity(pageSize)){
            pageSize = 500;
        }
        int page = pageNo.intValue();
        int size = pageSize.intValue();
//        Pageable pageable = (Pageable) new PageRequest(page, pageSize);
        Sort sort = new Sort(Sort.Direction.DESC, "Sales");
        Pageable pageable = new PageRequest(page-1, size, sort);
//        return goodsDao.findAllOrderBySales(pageable);
        Page<Goods> all = goodsDao.findAll(pageable);
        return all;
    }


}
