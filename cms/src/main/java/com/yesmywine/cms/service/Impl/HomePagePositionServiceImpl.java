package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.AdverPositionDao;
import com.yesmywine.cms.dao.HomePagePositionDao;
import com.yesmywine.cms.dao.PositionEntityDao;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.entity.HomePagePosition;
import com.yesmywine.cms.entity.PositionEntity;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.HomePagePositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class HomePagePositionServiceImpl implements HomePagePositionService {

    @Autowired
    private HomePagePositionDao homePagePositionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private PositionEntityDao positionEntityDao;
    @Autowired
    private ActivityService activityService;


    @Override
    public Object findAll() {
        List<HomePagePosition> all = this.homePagePositionDao.findAll();
        JSONArray jsonArray= new JSONArray();
        for(HomePagePosition one: all) {
            JSONObject jsonObject = new JSONObject();

            PositionEntity positionEntity1 = new PositionEntity();
            PositionEntity positionEntity2 = new PositionEntity();
            PositionEntity positionEntity3 = new PositionEntity();
            PositionEntity positionEntity4 = new PositionEntity();
            positionEntity1.setId(one.getBannerPositionId());
            positionEntity2.setId(one.getPositionIdOne());
            positionEntity3.setId(one.getPositionIdTwo());
            positionEntity4.setId(one.getPositionIdThree());
            List<AdverPositionEntity> byPositionEntity1 = this.adverPositionDao.findByPositionEntity(positionEntity1);
            List<AdverPositionEntity> byPositionEntity2 = this.adverPositionDao.findByPositionEntity(positionEntity2);
            List<AdverPositionEntity> byPositionEntity3 = this.adverPositionDao.findByPositionEntity(positionEntity3);
            List<AdverPositionEntity> byPositionEntity4 = this.adverPositionDao.findByPositionEntity(positionEntity4);
            JSONArray jsonArray1 = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();
            JSONArray jsonArray3 = new JSONArray();
            JSONArray jsonArray4 = new JSONArray();
            if(byPositionEntity1.size()>0) {
                for (AdverPositionEntity adverPositionEntity : byPositionEntity1) {
                    JSONObject jsonOb = new JSONObject();
                    AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                    PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                    jsonOb.put("id", one.getBannerPositionId());
                    jsonOb.put("name", positionEntity.getPositionName());
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
            }else {
                PositionEntity positionEntity = this.positionEntityDao.findOne(one.getBannerPositionId());
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("id", one.getBannerPositionId());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonArray1.add(jsonOb);
            }

            if(byPositionEntity2.size()>0) {
                for (AdverPositionEntity adverPositionEntity : byPositionEntity2) {
                    JSONObject jsonOb = new JSONObject();
                    AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                    PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                    jsonOb.put("id", one.getPositionIdOne());
                    jsonOb.put("name", positionEntity.getPositionName());
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
            }else {
                PositionEntity positionEntity = this.positionEntityDao.findOne(one.getPositionIdOne());
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("id", one.getPositionIdOne());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonArray2.add(jsonOb);
            }

            if(byPositionEntity3.size()>0) {
                for (AdverPositionEntity adverPositionEntity : byPositionEntity3) {
                    JSONObject jsonOb = new JSONObject();
                    AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                    PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                    jsonOb.put("id", one.getPositionIdTwo());
                    jsonOb.put("name", positionEntity.getPositionName());
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
            }else {
                PositionEntity positionEntity = this.positionEntityDao.findOne(one.getPositionIdTwo());
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("id", one.getPositionIdTwo());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonArray3.add(jsonOb);
            }

            if(byPositionEntity4.size()>0) {
                for (AdverPositionEntity adverPositionEntity : byPositionEntity4) {
                    JSONObject jsonOb = new JSONObject();
                    AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                    PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                    jsonOb.put("id", one.getPositionIdThree());
                    jsonOb.put("name", positionEntity.getPositionName());
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
            }else {
                PositionEntity positionEntity = this.positionEntityDao.findOne(one.getPositionIdThree());
                JSONObject jsonOb = new JSONObject();
                jsonOb.put("id", one.getPositionIdThree());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonArray4.add(jsonOb);
            }

            jsonObject.put("bannerPosition", jsonArray1);
            jsonObject.put("positionOne", jsonArray2);
            jsonObject.put("positionTwo", jsonArray3);
            jsonObject.put("positionThree", jsonArray4);
            jsonObject.put("id", all.get(0).getId());
            return jsonObject;
        }
        return null;
    }

    @Override
    public String insert(Integer bannerPositionId, Integer positionIdOne, Integer positionIdTwo, Integer positionIdThree) {
        try{
            HomePagePosition homePagePosition = new HomePagePosition();
            homePagePosition.setBannerPositionId(bannerPositionId);
            homePagePosition.setPositionIdOne(positionIdOne);
            homePagePosition.setPositionIdTwo(positionIdTwo);
            homePagePosition.setPositionIdThree(positionIdThree);
            this.homePagePositionDao.save(homePagePosition);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String update(Integer id, Integer bannerPositionId, Integer positionIdOne, Integer positionIdTwo, Integer positionIdThree) {
        try{
            HomePagePosition homePagePosition = new HomePagePosition();
            homePagePosition.setId(id);
            homePagePosition.setBannerPositionId(bannerPositionId);
            homePagePosition.setPositionIdOne(positionIdOne);
            homePagePosition.setPositionIdTwo(positionIdTwo);
            homePagePosition.setPositionIdThree(positionIdThree);
            this.homePagePositionDao.save(homePagePosition);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String delete(Integer id) {
        try{
            this.homePagePositionDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
