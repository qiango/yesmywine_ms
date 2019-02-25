package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityFirstDao activityFirstDao;
    @Autowired
    private ActivitySecentDao activitySecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private AdverDao adverDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityColumnDao activityColumnDao;
    @Autowired
    private PositionEntityDao positionEntityDao;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer id) {
        ActivityFirst one = this.activityFirstDao.findOne(id);
        if(ValueUtil.isEmpity(one)){
            return "没有此活动";
        }
//        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getName());
        jsonObject.put("templateId", one.getTemplateId());
        jsonObject.put("imageBack", one.getImageBack());
        jsonObject.put("label", one.getLabel());
        jsonObject.put("subtitle", one.getSubtitle());
        jsonObject.put("appPosition", one.getAppPosition());
        jsonObject.put("appImage", one.getAppImage());
        jsonObject.put("appTitle", one.getAppTitle());
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setId(one.getPositionIdBanner());
        List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
        JSONArray jsonArrayPosition = new JSONArray();
        if(byPositionEntity.size()>0) {
            for (AdverPositionEntity aP : byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", one.getPositionIdBanner());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                jsonObjectPosition.put("positionType", positionEntity1.getPositionType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }
        }else {
            JSONObject jsonObjectPosition = new JSONObject();
            PositionEntity one1 = this.positionEntityDao.findOne(one.getPositionIdBanner());
            jsonObjectPosition.put("name", one1.getPositionName());
            jsonObjectPosition.put("id", one.getPositionIdBanner());
            jsonObjectPosition.put("positionType", one1.getPositionType());
            jsonArrayPosition.add(jsonObjectPosition);
        }
        jsonObject.put("positionBanner", jsonArrayPosition);
        List<ActivityColumn> activityColumnList = this.activityColumnDao.findByActivityFirstId(one.getId());
        JSONArray jsonArrayColumn = new JSONArray();
        for(ActivityColumn activityColumn:activityColumnList){
            JSONObject jsonObjectColumn = new JSONObject();
            jsonObjectColumn.put("id", activityColumn.getId());
            jsonObjectColumn.put("name", activityColumn.getName());
            jsonObjectColumn.put("image", activityColumn.getImage());
            jsonObjectColumn.put("activityId", activityColumn.getActivityId());
            String result = SynchronizeUtils.getResult(Dictionary.MALL_HOST, "/activity/itf",RequestMethod.get,"id",activityColumn.getActivityId().toString());
            String name=ValueUtil.getFromJson(result,"data","name");
            jsonObjectColumn.put("activityName",name);
            PositionEntity positionEntityColumn = new PositionEntity();
            positionEntityColumn.setId(activityColumn.getPositionId());
            List<AdverPositionEntity> byPositionEntityColumn = this.adverPositionDao.findByPositionEntity(positionEntityColumn);
            JSONArray jsonArrayPositionColumn = new JSONArray();
            if(byPositionEntityColumn.size()>0) {
                for (AdverPositionEntity aPC : byPositionEntityColumn) {
                    JSONObject jsonObjectPositionC = new JSONObject();
                    AdverEntity adverEntityC = aPC.getAdverEntity();
                    PositionEntity positionEntityC = aPC.getPositionEntity();
                    jsonObjectPositionC.put("name", positionEntityC.getPositionName());
                    jsonObjectPositionC.put("id", activityColumn.getPositionId());
                    jsonObjectPositionC.put("adverId", adverEntityC.getId());
                    jsonObjectPositionC.put("image", adverEntityC.getImage());
                    jsonObjectPositionC.put("relevance", adverEntityC.getRelevancy());
                    jsonObjectPositionC.put("relevancyType", adverEntityC.getRelevancyType());
                    Integer templ = null;
                    if(1==adverEntityC.getRelevancyType()){
                        templ = this.findTempl(Integer.valueOf(adverEntityC.getRelevancy()));
                    }
                    jsonObjectPositionC.put("template", templ);
                    jsonArrayPositionColumn.add(jsonObjectPositionC);
                }
            }else {
                JSONObject jsonObjectPositionC = new JSONObject();
                PositionEntity oneC = this.positionEntityDao.findOne(activityColumn.getPositionId());
                jsonObjectPositionC.put("name", oneC.getPositionName());
                jsonObjectPositionC.put("id", activityColumn.getPositionId());
                jsonArrayPositionColumn.add(jsonObjectPositionC);
            }
            jsonObjectColumn.put("ColumnPosition", jsonArrayPositionColumn);

            List<ActivitySecent> goodsList = this.activitySecentDao.findByColumnId(activityColumn.getId());
            JSONArray jsonArrayGoods = new JSONArray();
            for(ActivitySecent activitySecent:goodsList){
                JSONObject jsonObjectGoods = new JSONObject();
                jsonObjectGoods.put("id", activitySecent.getColumnId());
                Integer goodsId = activitySecent.getGoodsId();
                Goods goods = this.goodsDao.findOne(goodsId);
                jsonObjectGoods.put("goodsId", goods.getId());
                jsonObjectGoods.put("goodsName", goods.getGoodsName());
                jsonObjectGoods.put("image", goods.getPicture());
                jsonObjectGoods.put("salePrice", goods.getSalePrice());
                jsonObjectGoods.put("price", goods.getPrice());
                jsonObjectGoods.put("sales", goods.getSales());
                jsonArrayGoods.add(jsonObjectGoods);
            }

            jsonObjectColumn.put("goods", jsonArrayGoods);
            jsonArrayColumn.add(jsonObjectColumn);
        }
        jsonObject.put("activityColumn", jsonArrayColumn);
        return jsonObject;
    }

    @Override
    public Object findOneMajor(Integer id) {
        ActivityFirst one = this.activityFirstDao.findOne(id);
        if(ValueUtil.isEmpity(one)){
            return "没有此活动";
        }
//        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getName());
        jsonObject.put("templateId", one.getTemplateId());
        jsonObject.put("imageBack", one.getImageBack());
        jsonObject.put("label", one.getLabel());
        jsonObject.put("subtitle", one.getSubtitle());
        jsonObject.put("appPosition", one.getAppPosition());
        jsonObject.put("appImage", one.getAppImage());
        jsonObject.put("appTitle", one.getAppTitle());
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setId(one.getPositionIdBanner());
        List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
        JSONArray jsonArrayPosition = new JSONArray();
        if(byPositionEntity.size()>0) {
            for (AdverPositionEntity aP : byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", one.getPositionIdBanner());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }
        }else {
            JSONObject jsonObjectPosition = new JSONObject();
            PositionEntity one1 = this.positionEntityDao.findOne(one.getPositionIdBanner());
            jsonObjectPosition.put("name", one1.getPositionName());
            jsonObjectPosition.put("id", one.getPositionIdBanner());
            jsonArrayPosition.add(jsonObjectPosition);
        }
        jsonObject.put("positionBanner", jsonArrayPosition);
        List<ActivityColumn> activityColumnList = this.activityColumnDao.findByActivityFirstId(one.getId());
        JSONArray jsonArrayColumn = new JSONArray();
        for(ActivityColumn activityColumn:activityColumnList){
            JSONObject jsonObjectColumn = new JSONObject();
            jsonObjectColumn.put("id", activityColumn.getId());
            jsonObjectColumn.put("name", activityColumn.getName());
            jsonObjectColumn.put("image", activityColumn.getImage());
            jsonObjectColumn.put("activityId", activityColumn.getActivityId());
            PositionEntity positionEntityColumn = new PositionEntity();
            positionEntityColumn.setId(activityColumn.getPositionId());
            List<AdverPositionEntity> byPositionEntityColumn = this.adverPositionDao.findByPositionEntity(positionEntityColumn);
            JSONArray jsonArrayPositionColumn = new JSONArray();
            if(byPositionEntityColumn.size()>0) {
                for (AdverPositionEntity aPC : byPositionEntityColumn) {
                    JSONObject jsonObjectPositionC = new JSONObject();
                    AdverEntity adverEntityC = aPC.getAdverEntity();
                    PositionEntity positionEntityC = aPC.getPositionEntity();
                    jsonObjectPositionC.put("name", positionEntityC.getPositionName());
                    jsonObjectPositionC.put("id", activityColumn.getPositionId());
                    jsonObjectPositionC.put("adverId", adverEntityC.getId());
                    jsonObjectPositionC.put("image", adverEntityC.getImage());
                    jsonObjectPositionC.put("relevance", adverEntityC.getRelevancy());
                    jsonObjectPositionC.put("relevancyType", adverEntityC.getRelevancyType());
                    Integer templ = null;
                    if(1==adverEntityC.getRelevancyType()){
                        templ = this.findTempl(Integer.valueOf(adverEntityC.getRelevancy()));
                    }
                    jsonObjectPositionC.put("template", templ);
                    jsonArrayPositionColumn.add(jsonObjectPositionC);
                }
            }else {
                JSONObject jsonObjectPositionC = new JSONObject();
                PositionEntity oneC = this.positionEntityDao.findOne(activityColumn.getPositionId());
                jsonObjectPositionC.put("name", oneC.getPositionName());
                jsonObjectPositionC.put("id", activityColumn.getPositionId());
                jsonArrayPositionColumn.add(jsonObjectPositionC);
            }
            jsonObjectColumn.put("ColumnPosition", jsonArrayPositionColumn);
            jsonArrayColumn.add(jsonObjectColumn);
        }
        jsonObject.put("activityColumn", jsonArrayColumn);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<ActivityFirst> all = this.activityFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(ActivityFirst one: all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            jsonObject.put("templateId", one.getTemplateId());
            jsonObject.put("imageBack", one.getImageBack());
            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(one.getPositionIdBanner());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            if(byPositionEntity.size()>0) {
                for(AdverPositionEntity aP: byPositionEntity) {
                    JSONObject jsonObjectPosition = new JSONObject();
                    AdverEntity adverEntity = aP.getAdverEntity();
                    PositionEntity positionEntity1 = aP.getPositionEntity();
                    jsonObjectPosition.put("name", positionEntity1.getPositionName());
                    jsonObjectPosition.put("id", one.getPositionIdBanner());
                    Integer templ = null;
                    if(1==adverEntity.getRelevancyType()){
                        templ = this.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                    }
                    jsonObjectPosition.put("template", templ);
                    jsonArrayPosition.add(jsonObjectPosition);
                }
            }else {
                JSONObject jsonObjectPosition = new JSONObject();
                PositionEntity one1 = this.positionEntityDao.findOne(one.getPositionIdBanner());
                jsonObjectPosition.put("name", one1.getPositionName());
                jsonObjectPosition.put("id", one.getPositionIdBanner());
                jsonArrayPosition.add(jsonObjectPosition);
            }
            jsonObject.put("positionBanner", jsonArrayPosition);
            List<ActivityColumn> activityColumnList = this.activityColumnDao.findByActivityFirstId(one.getId());
            JSONArray jsonArrayColumn = new JSONArray();
            for(ActivityColumn activityColumn:activityColumnList){
                JSONObject jsonObjectColumn = new JSONObject();
                jsonObjectColumn.put("id", activityColumn.getId());
                jsonObjectColumn.put("name", activityColumn.getName());
                PositionEntity positionEntityColumn = new PositionEntity();
                positionEntityColumn.setId(activityColumn.getPositionId());
                List<AdverPositionEntity> byPositionEntityColumn = this.adverPositionDao.findByPositionEntity(positionEntityColumn);
                JSONArray jsonArrayPositionColumn = new JSONArray();
                if(byPositionEntityColumn.size()>0) {
                    for (AdverPositionEntity aPC : byPositionEntityColumn) {
                        JSONObject jsonObjectPositionC = new JSONObject();
                        PositionEntity positionEntityC = aPC.getPositionEntity();
                        jsonObjectPositionC.put("name", positionEntityC.getPositionName());
                        jsonArrayPositionColumn.add(jsonObjectPositionC);
                    }
                }else {
                    JSONObject jsonObjectPositionC = new JSONObject();
                    PositionEntity oneC = this.positionEntityDao.findOne(activityColumn.getPositionId());
                    jsonObjectPositionC.put("name", oneC.getPositionName());
                    jsonObjectPositionC.put("id", activityColumn.getPositionId());
                    jsonArrayPositionColumn.add(jsonObjectPositionC);
                }
                jsonObjectColumn.put("ColumnPosition", jsonArrayPositionColumn);
                jsonArrayColumn.add(jsonObjectColumn);
            }
            jsonObject.put("activityColumn", jsonArrayColumn);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }


    @Override
    public Object findAllApp() {
        List<ActivityFirst> all = this.activityFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(ActivityFirst one: all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("appPosition", one.getAppPosition());
            jsonObject.put("appImage", one.getAppImage());
            jsonObject.put("appTitle", one.getAppTitle());
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }


    public Object findTemplate(Integer id) {
        ActivityFirst activityFirst = this.activityFirstDao.findOne(id);
        JSONObject jsonObject = new JSONObject();
        if(ValueUtil.notEmpity(activityFirst)) {
            jsonObject.put("id", activityFirst.getId());
            jsonObject.put("name", activityFirst.getName());
            jsonObject.put("templateId", activityFirst.getTemplateId());
        }
        return jsonObject;
    }

    public Integer findTempl(Integer id) {
        ActivityFirst activityFirst = this.activityFirstDao.findOne(id);
//        JSONObject jsonObject = new JSONObject();
        if(ValueUtil.notEmpity(activityFirst)) {
//            jsonObject.put("id", activityFirst.getId());
//            jsonObject.put("name", activityFirst.getName());
//            jsonObject.put("templateId", activityFirst.getTemplateId());
            return activityFirst.getTemplateId();
        }
        return null;
    }

    @Override
    public Object findByAppPosition(String appPosition) {
        appPosition = "%"+appPosition+"%";
        List<ActivityFirst> all = this.activityFirstDao.findByAppPositionLikeOrderByAppPosition(appPosition);
        JSONArray jsonArrayRe = new JSONArray();
        for(ActivityFirst one: all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            jsonObject.put("templateId", one.getTemplateId());
            jsonObject.put("imageBack", one.getImageBack());
            jsonObject.put("label", one.getLabel());
            jsonObject.put("subtitle", one.getSubtitle());
            jsonObject.put("appPosition", one.getAppPosition());
            jsonObject.put("appImage", one.getAppImage());
            jsonObject.put("appTitle", one.getAppTitle());

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(one.getPositionIdBanner());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", one.getPositionIdBanner());
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }
            jsonObject.put("positionBanner", jsonArrayPosition);

            List<ActivityColumn> activityColumnList = this.activityColumnDao.findByActivityFirstId(one.getId());
            JSONArray jsonArrayColumn = new JSONArray();
            for(ActivityColumn activityColumn:activityColumnList){
                JSONObject jsonObjectColumn = new JSONObject();
                jsonObjectColumn.put("id", activityColumn.getId());
                jsonObjectColumn.put("name", activityColumn.getName());
                jsonObjectColumn.put("image", activityColumn.getImage());
                jsonObjectColumn.put("activityId", activityColumn.getActivityId());
                PositionEntity positionEntityColumn = new PositionEntity();
                positionEntityColumn.setId(activityColumn.getPositionId());
                List<AdverPositionEntity> byPositionEntityColumn = this.adverPositionDao.findByPositionEntity(positionEntityColumn);
                JSONArray jsonArrayPositionColumn = new JSONArray();
                if(byPositionEntityColumn.size()>0) {
                    for (AdverPositionEntity aPC : byPositionEntityColumn) {
                        JSONObject jsonObjectPositionC = new JSONObject();
                        AdverEntity adverEntityC = aPC.getAdverEntity();
                        PositionEntity positionEntityC = aPC.getPositionEntity();
                        jsonObjectPositionC.put("name", positionEntityC.getPositionName());
                        jsonObjectPositionC.put("id", activityColumn.getPositionId());
                        jsonObjectPositionC.put("adverId", adverEntityC.getId());
                        jsonObjectPositionC.put("image", adverEntityC.getImage());
                        jsonObjectPositionC.put("relevance", adverEntityC.getRelevancy());
                        jsonObjectPositionC.put("relevancyType", adverEntityC.getRelevancyType());
                        Integer templ = null;
                        if(1==adverEntityC.getRelevancyType()){
                            templ = this.findTempl(Integer.valueOf(adverEntityC.getRelevancy()));
                        }
                        jsonObjectPositionC.put("template", templ);
                        jsonArrayPositionColumn.add(jsonObjectPositionC);
                    }
                }else {
                    JSONObject jsonObjectPositionC = new JSONObject();
                    PositionEntity oneC = this.positionEntityDao.findOne(activityColumn.getPositionId());
                    jsonObjectPositionC.put("name", oneC.getPositionName());
                    jsonObjectPositionC.put("id", activityColumn.getPositionId());
                    jsonArrayPositionColumn.add(jsonObjectPositionC);
                }
                jsonObjectColumn.put("ColumnPosition", jsonArrayPositionColumn);

                List<ActivitySecent> goodsList = this.activitySecentDao.findByColumnId(activityColumn.getId());
                JSONArray jsonArrayGoods = new JSONArray();
                for(ActivitySecent activitySecent:goodsList){
                    JSONObject jsonObjectGoods = new JSONObject();
                    jsonObjectGoods.put("id", activitySecent.getColumnId());
                    Integer goodsId = activitySecent.getGoodsId();
                    Goods goods = this.goodsService.findById(goodsId);
                    jsonObjectGoods.put("goodsId", goods.getId());
                    jsonObjectGoods.put("goodsName", goods.getGoodsName());
                    jsonObjectGoods.put("image", goods.getPicture());
                    jsonObjectGoods.put("salePrice", goods.getSalePrice());
                    jsonObjectGoods.put("price", goods.getPrice());
                    jsonObjectGoods.put("sales", goods.getSales());
                    jsonArrayGoods.add(jsonObjectGoods);
                }

                jsonObjectColumn.put("goods", jsonArrayGoods);
                jsonArrayColumn.add(jsonObjectColumn);
            }
            jsonObject.put("activityColumn", jsonArrayColumn);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }


    @Override
    public Object findAppByAppPosition(String appPosition) {
        appPosition = "%"+appPosition+"%";
        List<ActivityFirst> all = this.activityFirstDao.findByAppPositionLikeOrderByAppPosition(appPosition);
        JSONArray jsonArrayRe = new JSONArray();
        for(ActivityFirst one: all) {
            if(one.getAppPosition().equals("A10")){
                    continue;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("appPosition", one.getAppPosition());
            jsonObject.put("appImage", one.getAppImage());
            jsonObject.put("appLabel", one.getLabel());
            jsonObject.put("appTitle", one.getAppTitle());
//            jsonObject.put("",one.get)
            jsonArrayRe.add(jsonObject);
        }
        if(all.size()>9){
            ActivityFirst one = activityFirstDao.findByAppPosition("A10").get(0);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("appPosition", one.getAppPosition());
            jsonObject.put("appImage", one.getAppImage());
            jsonObject.put("appLabel", one.getLabel());
            jsonObject.put("appTitle", one.getAppTitle());
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public Object findByAppPositionMajor(String appPosition) {
        appPosition = "%"+appPosition+"%";
        List<ActivityFirst> all = this.activityFirstDao.findByAppPositionLikeOrderByAppPosition(appPosition);
        JSONArray jsonArrayRe = new JSONArray();
        for(ActivityFirst one: all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            jsonObject.put("templateId", one.getTemplateId());
            jsonObject.put("imageBack", one.getImageBack());
            jsonObject.put("label", one.getLabel());
            jsonObject.put("subtitle", one.getSubtitle());
            jsonObject.put("appPosition", one.getAppPosition());
            jsonObject.put("appImage", one.getAppImage());
            jsonObject.put("appTitle", one.getAppTitle());

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(one.getPositionIdBanner());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", one.getPositionIdBanner());
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }
            jsonObject.put("positionBanner", jsonArrayPosition);

            List<ActivityColumn> activityColumnList = this.activityColumnDao.findByActivityFirstId(one.getId());
            JSONArray jsonArrayColumn = new JSONArray();
            for(ActivityColumn activityColumn:activityColumnList){
                JSONObject jsonObjectColumn = new JSONObject();
                jsonObjectColumn.put("id", activityColumn.getId());
                jsonObjectColumn.put("name", activityColumn.getName());
                jsonObjectColumn.put("image", activityColumn.getImage());
                jsonObjectColumn.put("activityId", activityColumn.getActivityId());
                PositionEntity positionEntityColumn = new PositionEntity();
                positionEntityColumn.setId(activityColumn.getPositionId());
                List<AdverPositionEntity> byPositionEntityColumn = this.adverPositionDao.findByPositionEntity(positionEntityColumn);
                JSONArray jsonArrayPositionColumn = new JSONArray();
                if(byPositionEntityColumn.size()>0) {
                    for (AdverPositionEntity aPC : byPositionEntityColumn) {
                        JSONObject jsonObjectPositionC = new JSONObject();
                        AdverEntity adverEntityC = aPC.getAdverEntity();
                        PositionEntity positionEntityC = aPC.getPositionEntity();
                        jsonObjectPositionC.put("name", positionEntityC.getPositionName());
                        jsonObjectPositionC.put("id", activityColumn.getPositionId());
                        jsonObjectPositionC.put("adverId", adverEntityC.getId());
                        jsonObjectPositionC.put("image", adverEntityC.getImage());
                        jsonObjectPositionC.put("relevance", adverEntityC.getRelevancy());
                        jsonObjectPositionC.put("relevancyType", adverEntityC.getRelevancyType());
                        Integer templ = null;
                        if(1==adverEntityC.getRelevancyType()){
                            templ = this.findTempl(Integer.valueOf(adverEntityC.getRelevancy()));
                        }
                        jsonObjectPositionC.put("template", templ);
                        jsonArrayPositionColumn.add(jsonObjectPositionC);
                    }
                }else {
                    JSONObject jsonObjectPositionC = new JSONObject();
                    PositionEntity oneC = this.positionEntityDao.findOne(activityColumn.getPositionId());
                    jsonObjectPositionC.put("name", oneC.getPositionName());
                    jsonObjectPositionC.put("id", activityColumn.getPositionId());
                    jsonArrayPositionColumn.add(jsonObjectPositionC);
                }
                jsonObjectColumn.put("ColumnPosition", jsonArrayPositionColumn);
                jsonArrayColumn.add(jsonObjectColumn);
            }
            jsonObject.put("activityColumn", jsonArrayColumn);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(String pageJsonString, String appJsonString, Integer id, String columnJsonString
            , Integer columnId, String goodsJsonString) {

            ActivityFirst activityFirst = new ActivityFirst();
            if(ValueUtil.notEmpity(pageJsonString)) {
                try {
                    JSONObject jsonObject = JSONObject.parseObject(pageJsonString);
                    String name = jsonObject.get("name").toString();
                    String imageBackIds = jsonObject.get("imageBackIds").toString();
                    Integer positionIdBanner = Integer.valueOf(jsonObject.get("positionIdBanner").toString());
                    Integer templateId = Integer.valueOf(jsonObject.get("templateId").toString());
                    String label = jsonObject.get("label").toString();
                    String subtitle = jsonObject.get("subtitle").toString();
                    activityFirst.setName(name);
                    activityFirst.setPositionIdBanner(positionIdBanner);
//                    String[] idsPage = imageBackIds.split(",");
                    activityFirst.setTemplateId(templateId);
                    activityFirst.setLabel(label);
                    activityFirst.setSubtitle(subtitle);

                    if (ValueUtil.notEmpity(appJsonString)) {
                        JSONObject jsonObjectApp = JSONObject.parseObject(appJsonString);
                        String appPosition = jsonObjectApp.get("appPosition").toString();
                        if (!"XX".equals(appPosition)) {
                            List<ActivityFirst> byAppPosition = this.activityFirstDao.findByAppPosition(appPosition);
                            if (0 != byAppPosition.size()) {
                                return "此App类型已存在";
                            }
                        }
                        String appImage = jsonObjectApp.get("appImage").toString();
                        String appTitle = jsonObjectApp.get("appTitle").toString();
                        activityFirst.setAppPosition(appPosition);
                        activityFirst.setAppTitle(appTitle);
//                        String[] idsApp = appImage.split(",");
                        this.activityFirstDao.save(activityFirst);
                        String appImg = this.saveGoodsImg(activityFirst.getId(), appImage);
                        activityFirst.setAppImage(appImg);
                    } else {
                        this.activityFirstDao.save(activityFirst);
                    }
                    String backImg = this.saveGoodsImg(activityFirst.getId(), imageBackIds);
                    activityFirst.setImageBack(backImg);
                    this.activityFirstDao.save(activityFirst);
                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return "字段错误";
                }
            }

            try{
                    ActivityColumn activityColumn = new ActivityColumn();
                    if(ValueUtil.notEmpity(id)){
                        activityColumn.setActivityFirstId(id);
                    }else {
                        activityColumn.setActivityFirstId(activityFirst.getId());
                    }
                    if(ValueUtil.notEmpity(columnJsonString)){
                        JSONObject jsonObjectColumn = JSONObject.parseObject(columnJsonString);
                        String columnName = jsonObjectColumn.get("name").toString();
                        ActivityColumn byActivityFirstIdAndName = activityColumnDao.findByActivityFirstIdAndName(id, columnName);
                        if(null!=byActivityFirstIdAndName){
                            return "该栏目名已存在";
                        }
                        Integer positionId = Integer.valueOf(jsonObjectColumn.get("positionId").toString());
                        Integer activityId = Integer.valueOf(jsonObjectColumn.get("activityId").toString());
                        String image = jsonObjectColumn.get("image").toString();
                        activityColumn.setPositionId(positionId);
//                        activityColumn.setActivityFirstId(activityFirst.getId());
                        activityColumn.setName(columnName);
                        activityColumn.setActivityId(activityId);
                        this.activityColumnDao.save(activityColumn);
                        String s = this.saveGoodsImg(activityColumn.getId(), image);
                        activityColumn.setImage(s);
                        this.activityColumnDao.save(activityColumn);
                    }

                    if(ValueUtil.notEmpity(goodsJsonString)){
                        JSONArray jsonArrayGoods = JSONArray.parseArray(goodsJsonString);
                        for (int i = 0; i < jsonArrayGoods.size(); i++) {
                            ActivitySecent activitySecent = new ActivitySecent();
                            if(ValueUtil.notEmpity(columnId)){
                                activitySecent.setColumnId(columnId);
                            }else {
                                activitySecent.setColumnId(activityColumn.getId());
                            }
                            JSONObject jsonObjectGoods = jsonArrayGoods.getJSONObject(i);
                            Integer goodsId = Integer.valueOf(jsonObjectGoods.get("goodsId").toString());
                            activitySecent.setGoodsId(goodsId);
                            this.activitySecentDao.save(activitySecent);
                        }
                    }


                }catch (Exception e){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return "图片服务器出现问题";
                }


        return "success";
    }

    @Override
    public String update(Integer id, String pageJsonString, String appJsonString, Integer columnId, String columnJsonString
            ,Integer activitySecentId, String goodsJsonString) {
        ActivityFirst activityFirst = new ActivityFirst();
        try {
        if(ValueUtil.notEmpity(pageJsonString) && ValueUtil.notEmpity(id)) {

            JSONObject jsonObject = JSONObject.parseObject(pageJsonString);
            String name = jsonObject.get("name").toString();
            String imageBackIds = jsonObject.get("imageBackIds").toString();
            Integer positionIdBanner = Integer.valueOf(jsonObject.get("positionIdBanner").toString());
            Integer templateId = Integer.valueOf(jsonObject.get("templateId").toString());
            String label = jsonObject.get("label").toString();
            String subtitle = jsonObject.get("subtitle").toString();
            activityFirst.setName(name);
            activityFirst.setPositionIdBanner(positionIdBanner);
//                    String[] idsPage = imageBackIds.split(",");
            activityFirst.setTemplateId(templateId);
            activityFirst.setLabel(label);
            activityFirst.setSubtitle(subtitle);
            activityFirst.setId(id);

            if (ValueUtil.notEmpity(appJsonString)) {
                JSONObject jsonObjectApp = JSONObject.parseObject(appJsonString);
                String appPosition = jsonObjectApp.get("appPosition").toString();
                if (!"XX".equals(appPosition)) {
                    List<ActivityFirst> byAppPosition = this.activityFirstDao.findByAppPosition(appPosition);
                    if (0 != byAppPosition.size() && byAppPosition.get(0).getId() != id) {
                        return "此appPosition已存在";
                    }
                }
                String appImage = jsonObjectApp.get("appImage").toString();
                String appTitle = jsonObjectApp.get("appTitle").toString();
                activityFirst.setAppPosition(appPosition);
                activityFirst.setAppTitle(appTitle);
//                        String[] idsApp = appImage.split(",");
                this.activityFirstDao.save(activityFirst);
                String appImg = this.saveGoodsImg(activityFirst.getId(), appImage);
                activityFirst.setAppImage(appImg);
            } else {
                this.activityFirstDao.save(activityFirst);
            }
            String backImg = this.saveGoodsImg(activityFirst.getId(), imageBackIds);
            activityFirst.setImageBack(backImg);
            this.activityFirstDao.save(activityFirst);
        }
                ActivityColumn activityColumn = new ActivityColumn();
                if(ValueUtil.notEmpity(columnJsonString) && ValueUtil.notEmpity(columnId)){
                    JSONObject jsonObjectColumn = JSONObject.parseObject(columnJsonString);
                    ActivityColumn one = this.activityColumnDao.findOne(columnId);
                    String columnName = jsonObjectColumn.get("name").toString();
                    ActivityColumn byActivityFirstIdAndName = activityColumnDao.findByActivityFirstIdAndName(id, columnName);
                    if(null!=byActivityFirstIdAndName&&byActivityFirstIdAndName.getId()!=columnId){
                        return "该栏目名已存在";
                    }
                    Integer positionId = Integer.valueOf(jsonObjectColumn.get("positionId").toString());
                    Integer activityId = Integer.valueOf(jsonObjectColumn.get("activityId").toString());
                    if(activityId != one.getActivityId()){
                        this.activitySecentDao.deleteByColumnId(columnId);
                    }
                    String image = jsonObjectColumn.get("image").toString();
                    activityColumn.setId(columnId);
                    activityColumn.setActivityFirstId(id);
                    activityColumn.setPositionId(positionId);
                    activityColumn.setName(columnName);
                    activityColumn.setActivityId(activityId);
                    this.activityColumnDao.save(activityColumn);
                    String s = this.saveGoodsImg(activityColumn.getId(), image);
                    activityColumn.setImage(s);
                    this.activityColumnDao.save(activityColumn);
                }

                if(ValueUtil.notEmpity(goodsJsonString)){
                    if(ValueUtil.notEmpity(columnId)){
                        this.activitySecentDao.deleteByColumnId(columnId);
                    }else {
                        this.activitySecentDao.deleteByColumnId(activityColumn.getId());
                    }
                    JSONArray jsonArrayGoods = JSONArray.parseArray(goodsJsonString);
                    for (int i = 0; i < jsonArrayGoods.size(); i++) {
                        ActivitySecent activitySecent = new ActivitySecent();
                        if(ValueUtil.notEmpity(columnId)){
                            activitySecent.setColumnId(columnId);
                        }else {
                            activitySecent.setColumnId(activityColumn.getId());
                        }
                        JSONObject jsonObjectGoods = jsonArrayGoods.getJSONObject(i);
                        Integer goodsId = Integer.valueOf(jsonObjectGoods.get("goodsId").toString());
                        activitySecent.setGoodsId(goodsId);
                        this.activitySecentDao.save(activitySecent);
                    }
                }

            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return "字段错误";
            }


        return "success";
    }

    @Override
    public String deleteFirst(Integer id) {
        try{
            if(null!=adverDao.findByRelevancyTypeAndRelevancy(1,id.toString())){
                ValueUtil.isError("该活动页已被使用,不可删除");
            }
            this.activityFirstDao.delete(id);
            List<ActivityColumn> byActivityFirstId = this.activityColumnDao.findByActivityFirstId(id);
            this.activityColumnDao.deleteByActivityFirstId(id);
            for(ActivityColumn activityColumn:byActivityFirstId) {
                Integer columnId = activityColumn.getId();
                this.activitySecentDao.deleteByColumnId(columnId);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteColumn(String ids) {
        try{
            String[] split = ids.split(",");
            for(String id:split){
                this.activityColumnDao.delete(Integer.valueOf(id));
                this.activitySecentDao.deleteByColumnId(Integer.valueOf(id));
            }
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(String ids) {
        try{
            String[] split = ids.split(",");
            List<ActivitySecent> idList = new ArrayList<>();
            for(String s:split){
                ActivitySecent activitySecent = new ActivitySecent();
                activitySecent.setId(Integer.valueOf(s));
                idList.add(activitySecent);
            }
            this.activitySecentDao.delete(idList);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    private String saveGoodsImg(Integer goodsId, String imgIds) throws YesmywineException {
        try{
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "cms");
            httpRequest.addParameter("mId", goodsId);
            httpRequest.addParameter("type", "1");
//            String ids = "";
//            for (int i = 0; i < imgIds.length; i++) {
//                if (i == 0) {
//                    ids = ids + imgIds[i];
////                    imageIds=imageIds+imageId[i];
//                } else {
//                    ids = ids + "," + imgIds[i];
////                    imageIds=imageIds+":"+imageId[i];
//                }
//                category.setImageId(imageIds);
                httpRequest.addParameter("id", imgIds);
//            }
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String cd = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(cd) || ValueUtil.isEmpity(cd)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("图片上传失败");
            } else {
                JSONArray maps = new JSONArray();
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
}
