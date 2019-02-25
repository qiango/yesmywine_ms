package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.PlateService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class PlateServiceImpl implements PlateService {

    @Autowired
    private PlateFirstDao plateFirstDao;
    @Autowired
    private PlateSecentCategoryLabelDao plateSecentCategoryLabelDao;
    @Autowired
    private PlateSecentCategoryRankDao plateSecentCategoryRankDao;
    @Autowired
    private PlateSecentGoodsDao plateSecentGoodsDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;

    @Override
    public Object findOne(Integer plateFirstId) {
        PlateFirst one = this.plateFirstDao.findOne(plateFirstId);
        if(ValueUtil.isEmpity(one)){
            return "没有此分类板块";
        }
        JSONArray jsonArrayGoods= new JSONArray();
        JSONArray jsonArrayLabel= new JSONArray();
        JSONArray jsonArrayRank= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("firstIndex", one.getFirstIndex());
        jsonObject.put("isShow", one.getIsShow());
        Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
        jsonObject.put("firstCategoryId", one.getFirstCategoryId());
        jsonObject.put("firstCategoryName", one1.getCategoryName());
        PositionEntity positionEntity1 = this.positionDao.findOne(one.getFirstPositionId());
        jsonObject.put("firstPositionId", one.getFirstPositionId());
        jsonObject.put("firstPositionName", positionEntity1.getPositionName());
        PositionEntity positionEntity2 = this.positionDao.findOne(one.getSecentPositionId());
        jsonObject.put("secentPositionId", one.getSecentPositionId());
        jsonObject.put("secentPositionName", positionEntity2.getPositionName());
        PositionEntity positionEntity3 = this.positionDao.findOne(one.getThirdPositionId());
        jsonObject.put("thirdPositionId", one.getThirdPositionId());
        jsonObject.put("thirdPositionName", positionEntity3.getPositionName());
        PositionEntity positionEntity4 = this.positionDao.findOne(one.getFourthPositionId());
        jsonObject.put("fourthPositionId", one.getFourthPositionId());
        jsonObject.put("fourthPositionName", positionEntity4.getPositionName());
        PositionEntity positionEntity5 = this.positionDao.findOne(one.getAppPositionId());
        jsonObject.put("appPositionId", one.getAppPositionId());
        jsonObject.put("appPositionName", positionEntity5.getPositionName());


        List<PlateSecentGoods> byPlateFirstId = this.plateSecentGoodsDao.findByPlateFirstId(plateFirstId);
        for(PlateSecentGoods plateSecentGoods: byPlateFirstId){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", plateSecentGoods.getId());
            Goods one2 = this.goodsService.findById(plateSecentGoods.getGoodsId());
            jsonObjectSecent.put("secentGoodsId", plateSecentGoods.getGoodsId());
            jsonObjectSecent.put("secentGoodsAlias", plateSecentGoods.getAlias());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArrayGoods.add(jsonObjectSecent);
        }
        jsonObject.put("secentGoods", jsonArrayGoods);

        List<PlateSecentCategoryLabel> byPlateFirstIdLable = this.plateSecentCategoryLabelDao.findByPlateFirstId(plateFirstId);
        for(PlateSecentCategoryLabel plateSecentCategoryLabel: byPlateFirstIdLable){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", plateSecentCategoryLabel.getId());
            Category one2 = this.categoryDao.findOne(plateSecentCategoryLabel.getCategoryId());
            jsonObjectSecent.put("secentCategoryLabelId", plateSecentCategoryLabel.getCategoryId());
            jsonObjectSecent.put("secentCategoryLabelName", one2.getCategoryName());
            jsonArrayLabel.add(jsonObjectSecent);
        }
        jsonObject.put("secentCategoryLabel", jsonArrayLabel);

        List<PlateSecentCategoryRank> byPlateFirstIdRank = this.plateSecentCategoryRankDao.findByPlateFirstId(plateFirstId);
        for(PlateSecentCategoryRank plateSecentCategoryRank: byPlateFirstIdRank){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", plateSecentCategoryRank.getId());
            Category one2 = this.categoryDao.findOne(plateSecentCategoryRank.getCategoryId());
            jsonObjectSecent.put("secentCategoryRankId", plateSecentCategoryRank.getCategoryId());
            jsonObjectSecent.put("secentCategoryRankName", one2.getCategoryName());
            jsonArrayRank.add(jsonObjectSecent);
        }
        jsonObject.put("secentCategoryRank", jsonArrayRank);
        return jsonObject;
    }

    @Override
    public Object findByIsShow(Integer isShow) {
        List<PlateFirst> all = this.plateFirstDao.findByIsShow(isShow);
        JSONArray jsonArrayRe = new JSONArray();
        for(PlateFirst one: all) {
            JSONArray jsonArrayGoods= new JSONArray();
            JSONArray jsonArrayLabel= new JSONArray();
            JSONArray jsonArrayRank= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("firstIndex", one.getFirstIndex());
            jsonObject.put("isShow", one.getIsShow());

            Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
            jsonObject.put("firstCategoryId", one.getFirstCategoryId());
            jsonObject.put("firstCategoryName", one1.getCategoryName());

            PositionEntity positionEntity1 = this.positionDao.findOne(one.getFirstPositionId());
            jsonObject.put("firstPositionId", one.getFirstPositionId());
            jsonObject.put("firstPositionName", positionEntity1.getPositionName());
            PositionEntity positionEntity2 = this.positionDao.findOne(one.getSecentPositionId());
            jsonObject.put("secentPositionId", one.getSecentPositionId());
            jsonObject.put("secentPositionName", positionEntity2.getPositionName());
            PositionEntity positionEntity3 = this.positionDao.findOne(one.getThirdPositionId());
            jsonObject.put("thirdPositionId", one.getThirdPositionId());
            jsonObject.put("thirdPositionName", positionEntity3.getPositionName());
            PositionEntity positionEntity4 = this.positionDao.findOne(one.getFourthPositionId());
            jsonObject.put("fourthPositionId", one.getFourthPositionId());
            jsonObject.put("fourthPositionName", positionEntity4.getPositionName());
            PositionEntity positionEntity5 = this.positionDao.findOne(one.getAppPositionId());
            jsonObject.put("appPositionId", one.getAppPositionId());
            jsonObject.put("appPositionName", positionEntity5.getPositionName());

            List<PlateSecentGoods> byPlateFirstId = this.plateSecentGoodsDao.findByPlateFirstId(one.getId());
            for(PlateSecentGoods plateSecentGoods: byPlateFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentGoods.getId());
                Goods one2 = this.goodsService.findById(plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("secentGoodsId", plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonObjectSecent.put("secentGoodsAlias", plateSecentGoods.getAlias());
                jsonArrayGoods.add(jsonObjectSecent);
            }
            jsonObject.put("secentGoods", jsonArrayGoods);

            List<PlateSecentCategoryLabel> byPlateFirstIdLable = this.plateSecentCategoryLabelDao.findByPlateFirstId(one.getId());
            for(PlateSecentCategoryLabel plateSecentCategoryLabel: byPlateFirstIdLable){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentCategoryLabel.getId());
                Category one2 = this.categoryDao.findOne(plateSecentCategoryLabel.getCategoryId());
                jsonObjectSecent.put("secentCategoryLabelId", plateSecentCategoryLabel.getCategoryId());
                jsonObjectSecent.put("secentCategoryLabelName", one2.getCategoryName());
                jsonArrayLabel.add(jsonObjectSecent);
            }
            jsonObject.put("secentCategoryLabel", jsonArrayLabel);

            List<PlateSecentCategoryRank> byPlateFirstIdRank = this.plateSecentCategoryRankDao.findByPlateFirstId(one.getId());
            for(PlateSecentCategoryRank plateSecentCategoryRank: byPlateFirstIdRank){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentCategoryRank.getId());
                Category one2 = this.categoryDao.findOne(plateSecentCategoryRank.getCategoryId());
                jsonObjectSecent.put("secentCategoryRankId", plateSecentCategoryRank.getCategoryId());
                jsonObjectSecent.put("secentCategoryRankName", one2.getCategoryName());
                jsonArrayRank.add(jsonObjectSecent);
            }
            jsonObject.put("secentCategoryRank", jsonArrayRank);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public Object findAll() {
        List<PlateFirst> all = this.plateFirstDao.findByFirst();
        JSONArray jsonArrayRe = new JSONArray();
        for(PlateFirst one: all) {
            JSONArray jsonArrayGoods= new JSONArray();
            JSONArray jsonArrayLabel= new JSONArray();
            JSONArray jsonArrayRank= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("firstIndex", one.getFirstIndex());
            jsonObject.put("isShow", one.getIsShow());

            Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
            jsonObject.put("firstCategoryId", one.getFirstCategoryId());
            jsonObject.put("firstCategoryName", one1.getCategoryName());

            PositionEntity positionEntity1 = this.positionDao.findOne(one.getFirstPositionId());
            jsonObject.put("firstPositionId", one.getFirstPositionId());
            jsonObject.put("firstPositionName", positionEntity1.getPositionName());
            PositionEntity positionEntity2 = this.positionDao.findOne(one.getSecentPositionId());
            jsonObject.put("secentPositionId", one.getSecentPositionId());
            jsonObject.put("secentPositionName", positionEntity2.getPositionName());
            PositionEntity positionEntity3 = this.positionDao.findOne(one.getThirdPositionId());
            jsonObject.put("thirdPositionId", one.getThirdPositionId());
            jsonObject.put("thirdPositionName", positionEntity3.getPositionName());
            PositionEntity positionEntity4 = this.positionDao.findOne(one.getFourthPositionId());
            jsonObject.put("fourthPositionId", one.getFourthPositionId());
            jsonObject.put("fourthPositionName", positionEntity4.getPositionName());
            PositionEntity positionEntity5 = this.positionDao.findOne(one.getAppPositionId());
            jsonObject.put("appPositionId", one.getAppPositionId());
            jsonObject.put("appPositionName", positionEntity5.getPositionName());

            List<PlateSecentGoods> byPlateFirstId = this.plateSecentGoodsDao.findByPlateFirstId(one.getId());
            for(PlateSecentGoods plateSecentGoods: byPlateFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentGoods.getId());
                Goods one2 = this.goodsService.findById(plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("secentGoodsId", plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonObjectSecent.put("secentGoodsAlias", plateSecentGoods.getAlias());
                jsonArrayGoods.add(jsonObjectSecent);
            }
            jsonObject.put("secentGoods", jsonArrayGoods);

            List<PlateSecentCategoryLabel> byPlateFirstIdLable = this.plateSecentCategoryLabelDao.findByPlateFirstId(one.getId());
            for(PlateSecentCategoryLabel plateSecentCategoryLabel: byPlateFirstIdLable){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentCategoryLabel.getId());
                Category one2 = this.categoryDao.findOne(plateSecentCategoryLabel.getCategoryId());
                jsonObjectSecent.put("secentCategoryLabelId", plateSecentCategoryLabel.getCategoryId());
                jsonObjectSecent.put("secentCategoryLabelName", one2.getCategoryName());
                jsonArrayLabel.add(jsonObjectSecent);
            }
            jsonObject.put("secentCategoryLabel", jsonArrayLabel);

            List<PlateSecentCategoryRank> byPlateFirstIdRank = this.plateSecentCategoryRankDao.findByPlateFirstId(one.getId());
            for(PlateSecentCategoryRank plateSecentCategoryRank: byPlateFirstIdRank){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentCategoryRank.getId());
                Category one2 = this.categoryDao.findOne(plateSecentCategoryRank.getCategoryId());
                jsonObjectSecent.put("secentCategoryRankId", plateSecentCategoryRank.getCategoryId());
                jsonObjectSecent.put("secentCategoryRankName", one2.getCategoryName());
                jsonArrayRank.add(jsonObjectSecent);
            }
            jsonObject.put("secentCategoryRank", jsonArrayRank);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public Object frontFindAll() {
//        List<PlateFirst> all = this.plateFirstDao.findAll();
        List<PlateFirst> all = this.plateFirstDao.findByIsShow(0);
        JSONArray jsonArrayRe = new JSONArray();
        for(PlateFirst one: all) {
            JSONArray jsonArrayLeft= new JSONArray();
            JSONArray jsonArrayGoods= new JSONArray();
            JSONArray jsonArrayLabel= new JSONArray();
            JSONArray jsonArrayRank= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObjectLeft = new JSONObject();

            jsonObject.put("weights", one.getFirstIndex());

            Category one1 = this.categoryDao.findOne(one.getFirstCategoryId());
            jsonObjectLeft.put("id", one.getId());
            jsonObjectLeft.put("title", one1.getCategoryName());
            jsonObject.put("left", jsonObjectLeft);

            List<PlateSecentCategoryLabel> byPlateFirstIdLable = this.plateSecentCategoryLabelDao.findByPlateFirstId(one.getId());
            for(PlateSecentCategoryLabel plateSecentCategoryLabel: byPlateFirstIdLable){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentCategoryLabel.getId());
                Category one2 = this.categoryDao.findOne(plateSecentCategoryLabel.getCategoryId());
                jsonObjectSecent.put("name", one2.getCategoryName());
                jsonArrayLabel.add(jsonObjectSecent);
            }
            jsonObject.put("leftClassifies", jsonArrayLabel);

            List<PlateSecentGoods> byPlateFirstId = this.plateSecentGoodsDao.findByPlateFirstId(one.getId());
            for(PlateSecentGoods plateSecentGoods: byPlateFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentGoods.getId());
                Goods one2 = this.goodsService.findById(plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("GoodsId", plateSecentGoods.getGoodsId());
                jsonObjectSecent.put("GoodsAlias", plateSecentGoods.getAlias());
                jsonObjectSecent.put("GoodsName", one2.getGoodsName());
                jsonObjectSecent.put("GoodsPicture", one2.getPicture());
                jsonObjectSecent.put("price", one2.getPrice());
                jsonObjectSecent.put("salePrice", one2.getSalePrice());
                jsonArrayGoods.add(jsonObjectSecent);
            }
            jsonObject.put("leftGoodsColumns", jsonArrayGoods);

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(one.getAppPositionId());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", one.getAppPositionId());
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }

            jsonObject.put("appAdvertisement", jsonArrayPosition);

            JSONObject jsonObjectPo = new JSONObject();

            PositionEntity positionEntity1 = new PositionEntity();
            PositionEntity positionEntity2 = new PositionEntity();
            PositionEntity positionEntity3 = new PositionEntity();
            PositionEntity positionEntity4 = new PositionEntity();
            positionEntity1.setId(one.getFirstPositionId());
            positionEntity2.setId(one.getSecentPositionId());
            positionEntity3.setId(one.getThirdPositionId());
            positionEntity4.setId(one.getFourthPositionId());
            List<AdverPositionEntity> byPositionEntity1 = this.adverPositionDao.findByPositionEntity(positionEntity1);
            List<AdverPositionEntity> byPositionEntity2 = this.adverPositionDao.findByPositionEntity(positionEntity2);
            List<AdverPositionEntity> byPositionEntity3 = this.adverPositionDao.findByPositionEntity(positionEntity3);
            List<AdverPositionEntity> byPositionEntity4 = this.adverPositionDao.findByPositionEntity(positionEntity4);
            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();
            JSONArray jsonArray3 = new JSONArray();
            JSONArray jsonArray4 = new JSONArray();
            for(AdverPositionEntity adverPositionEntity: byPositionEntity1){
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntityPo = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", one.getFirstPositionId());
                jsonOb.put("name", positionEntityPo.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray1.add(jsonOb);
            }
            for(AdverPositionEntity adverPositionEntity: byPositionEntity2){
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntityPo = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", one.getSecentPositionId());
                jsonOb.put("name", positionEntityPo.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray2.add(jsonOb);
            }
            for(AdverPositionEntity adverPositionEntity: byPositionEntity3){
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntityPo = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", one.getThirdPositionId());
                jsonOb.put("name", positionEntityPo.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray3.add(jsonOb);
            }
            for(AdverPositionEntity adverPositionEntity: byPositionEntity4){
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntityPo = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", one.getFourthPositionId());
                jsonOb.put("name", positionEntityPo.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray4.add(jsonOb);
            }
            jsonObjectPo.put("firstPositionId", jsonArray1);
            jsonObjectPo.put("secentPositionId", jsonArray2);
            jsonObjectPo.put("thirdPositionId", jsonArray3);
            jsonObjectPo.put("fourthPositionId", jsonArray4);
            jsonArrayLeft.add(jsonObjectPo);
            jsonObject.put("leftAdvertisements", jsonArrayLeft);


            List<PlateSecentCategoryRank> byPlateFirstIdRank = this.plateSecentCategoryRankDao.findByPlateFirstId(one.getId());
            for(PlateSecentCategoryRank plateSecentCategoryRank: byPlateFirstIdRank){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", plateSecentCategoryRank.getId());
                Integer categoryId=plateSecentCategoryRank.getCategoryId();
                Category one2 = this.categoryDao.findOne(categoryId);
                jsonObjectSecent.put("name", one2.getCategoryName());
                jsonArrayRank.add(jsonObjectSecent);
                List<Goods> goodsList=goodsDao.findByCategoryIdOrderBySalesDesc(categoryId);
                JSONArray jsonArray= new JSONArray();
                for(int i=0;i<6;i++){
                    if(i==goodsList.size()){
                        break;
                    }
                    Goods goods=goodsList.get(i);
                    JSONObject third=new JSONObject();
                    third.put("goodsId",goods.getId());
                    third.put("goodsName",goods.getGoodsName());
                    third.put("picture",goods.getPicture());
                    jsonArray.add(third);
                }
                jsonObjectSecent.put("goodsList",jsonArray);
            }
            jsonObject.put("rightClassifies", jsonArrayRank);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(Integer firstCategoryId, Integer firstIndex, String goodsJsonString,
                         Integer firstPositionId, Integer secentPositionId, Integer thirdPositionId, Integer fourthPositionId,
                         Integer appPositionId, String labelJsonString, String rankJsonString, Integer isShow) {
        try {
            PlateFirst byIndex = this.plateFirstDao.findByFirstIndex(firstIndex);
            if(ValueUtil.notEmpity(byIndex)){
                return "此权重已存在";
            }
            PlateFirst plateFirst = new PlateFirst();
            plateFirst.setFirstCategoryId(firstCategoryId);
            plateFirst.setFirstIndex(firstIndex);
            plateFirst.setFirstPositionId(firstPositionId);
            plateFirst.setSecentPositionId(secentPositionId);
            plateFirst.setThirdPositionId(thirdPositionId);
            plateFirst.setFourthPositionId(fourthPositionId);
            plateFirst.setAppPositionId(appPositionId);
            plateFirst.setIsShow(isShow);

            PlateFirst byFirstCategoryId = this.plateFirstDao.findByFirstCategoryId(firstCategoryId);
            if(ValueUtil.isEmpity(byFirstCategoryId)){
                this.plateFirstDao.save(plateFirst);
            }else {
                return "此分类板块已存在";
            }

            if(ValueUtil.notEmpity(goodsJsonString)){
                JSONArray jsonArray = JSON.parseArray(goodsJsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    String alias = jsonObject.get("alias").toString();
                    PlateSecentGoods plateSecentGoods = new PlateSecentGoods();
                    plateSecentGoods.setGoodsId(goodsId);
                    plateSecentGoods.setAlias(alias);
                    plateSecentGoods.setPlateFirstId(plateFirst.getId());
                    this.plateSecentGoodsDao.save(plateSecentGoods);
                }
            }

            if(ValueUtil.notEmpity(labelJsonString)){
                JSONArray jsonArray = JSON.parseArray(labelJsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer categoryId = Integer.valueOf(jsonObject.get("categoryId").toString());
                    PlateSecentCategoryLabel plateSecentCategoryLabel = new PlateSecentCategoryLabel();
                    plateSecentCategoryLabel.setCategoryId(categoryId);
                    plateSecentCategoryLabel.setPlateFirstId(plateFirst.getId());
                    this.plateSecentCategoryLabelDao.save(plateSecentCategoryLabel);
                }
            }

            if(ValueUtil.notEmpity(rankJsonString)){
                JSONArray jsonArray = JSON.parseArray(rankJsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer categoryId = Integer.valueOf(jsonObject.get("categoryId").toString());
                    PlateSecentCategoryRank plateSecentCategoryRank = new PlateSecentCategoryRank();
                    plateSecentCategoryRank.setCategoryId(categoryId);
                    plateSecentCategoryRank.setPlateFirstId(plateFirst.getId());
                    this.plateSecentCategoryRankDao.save(plateSecentCategoryRank);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, Integer firstCategoryId, Integer firstIndex, String goodsJsonString,
                         Integer firstPositionId, Integer secentPositionId, Integer thirdPositionId, Integer fourthPositionId,
                         Integer appPositionId, String labelJsonString, String rankJsonString, Integer isShow) {
        try {
            PlateFirst byIndex = this.plateFirstDao.findByFirstIndex(firstIndex);
            if(ValueUtil.notEmpity(byIndex)&& byIndex.getId() != id){
                return "此权重已存在";
            }
            if(byIndex.getFirstCategoryId()!=firstCategoryId){
                plateSecentGoodsDao.deleteAll();
            }
            PlateFirst plateFirst = new PlateFirst();
            plateFirst.setFirstCategoryId(firstCategoryId);
            plateFirst.setFirstIndex(firstIndex);
            plateFirst.setFirstPositionId(firstPositionId);
            plateFirst.setSecentPositionId(secentPositionId);
            plateFirst.setThirdPositionId(thirdPositionId);
            plateFirst.setFourthPositionId(fourthPositionId);
            plateFirst.setAppPositionId(appPositionId);
            plateFirst.setIsShow(isShow);
            plateFirst.setId(id);

            PlateFirst byFirstCategoryId = this.plateFirstDao.findByFirstCategoryId(firstCategoryId);
            if(ValueUtil.isEmpity(byFirstCategoryId) || byFirstCategoryId.getId()==id){
                this.plateFirstDao.save(plateFirst);
            }else {
                return "此分类板块已存在";
            }

            if(ValueUtil.notEmpity(goodsJsonString)){
                JSONArray jsonArray = JSON.parseArray(goodsJsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    String alias = jsonObject.get("alias").toString();
                    PlateSecentGoods plateSecentGoods = new PlateSecentGoods();
                    plateSecentGoods.setGoodsId(goodsId);
                    plateSecentGoods.setAlias(alias);
                    plateSecentGoods.setPlateFirstId(plateFirst.getId());
                    this.plateSecentGoodsDao.save(plateSecentGoods);
                }
            }

            if(ValueUtil.notEmpity(labelJsonString)){
                JSONArray jsonArray = JSON.parseArray(labelJsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer categoryId = Integer.valueOf(jsonObject.get("categoryId").toString());
                    PlateSecentCategoryLabel plateSecentCategoryLabel = new PlateSecentCategoryLabel();
                    plateSecentCategoryLabel.setCategoryId(categoryId);
                    plateSecentCategoryLabel.setPlateFirstId(plateFirst.getId());
                    this.plateSecentCategoryLabelDao.save(plateSecentCategoryLabel);
                }
            }

            if(ValueUtil.notEmpity(rankJsonString)){
                JSONArray jsonArray = JSON.parseArray(rankJsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer categoryId = Integer.valueOf(jsonObject.get("categoryId").toString());
                    PlateSecentCategoryRank plateSecentCategoryRank = new PlateSecentCategoryRank();
                    plateSecentCategoryRank.setCategoryId(categoryId);
                    plateSecentCategoryRank.setPlateFirstId(plateFirst.getId());
                    this.plateSecentCategoryRankDao.save(plateSecentCategoryRank);
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
            this.plateFirstDao.delete(id);
            this.plateSecentCategoryLabelDao.deleteByPlateFirstId(id);
            this.plateSecentGoodsDao.deleteByPlateFirstId(id);
            this.plateSecentCategoryRankDao.deleteByPlateFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecentGoods(Integer id) {
        try{
            this.plateSecentGoodsDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecentLabel(Integer id) {
        try{
            this.plateSecentCategoryLabelDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecentRank(Integer id) {
        try{
            this.plateSecentCategoryRankDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
