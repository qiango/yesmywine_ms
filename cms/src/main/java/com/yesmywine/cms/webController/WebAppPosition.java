package com.yesmywine.cms.webController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.AdverPositionDao;
import com.yesmywine.cms.dao.AppAdvertisingDao;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.entity.AppAdvertising;
import com.yesmywine.cms.entity.PositionEntity;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hz on 7/5/17.
 */
@RestController
@RequestMapping("/web/cms/appPosition")
public class WebAppPosition {

    @Autowired
    private AppAdvertisingDao appAdvertisingDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        if(0==appAdvertisingDao.findAll().size()){
            return ValueUtil.toJson(HttpStatus.SC_OK,null);
        }
        AppAdvertising appAdvertising=appAdvertisingDao.findAll().get(0);
        JSONObject jsonObject = new JSONObject();
        PositionEntity positionEntity1 = new PositionEntity();
        PositionEntity positionEntity2 = new PositionEntity();
        PositionEntity positionEntity3 = new PositionEntity();
        positionEntity1.setId(appAdvertising.getTopId());
        positionEntity2.setId(appAdvertising.getImportantId());
        positionEntity3.setId(appAdvertising.getCollaborateId());
        List<AdverPositionEntity> byPositionEntity1 = this.adverPositionDao.findByPositionEntity(positionEntity1);
        List<AdverPositionEntity> byPositionEntity2 = this.adverPositionDao.findByPositionEntity(positionEntity2);
        List<AdverPositionEntity> byPositionEntity3 = this.adverPositionDao.findByPositionEntity(positionEntity3);
        JSONArray jsonArray1 = new JSONArray();
        JSONArray jsonArray2 = new JSONArray();
        JSONArray jsonArray3 = new JSONArray();
        if(byPositionEntity1.size()>0) {
            for (AdverPositionEntity adverPositionEntity : byPositionEntity1) {
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", appAdvertising.getTopId());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevanceType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray1.add(jsonOb);
            }
        }else {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("id", appAdvertising.getTopId());
            jsonOb.put("name", positionEntity1.getPositionName());
            jsonArray1.add(jsonOb);
        }
        if(byPositionEntity2.size()>0) {
            for (AdverPositionEntity adverPositionEntity : byPositionEntity2) {
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", appAdvertising.getImportantId());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevanceType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray2.add(jsonOb);
            }
        }else {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("id", appAdvertising.getTopId());
            jsonOb.put("name", positionEntity2.getPositionName());
            jsonArray1.add(jsonOb);
        }
        if(byPositionEntity3.size()>0) {
            for (AdverPositionEntity adverPositionEntity : byPositionEntity3) {
                JSONObject jsonOb = new JSONObject();
                AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                PositionEntity positionEntity = adverPositionEntity.getPositionEntity();
                jsonOb.put("id", appAdvertising.getCollaborateId());
                jsonOb.put("name", positionEntity.getPositionName());
                jsonOb.put("adverId", adverEntity.getId());
                jsonOb.put("image", adverEntity.getImage());
                jsonOb.put("relevance", adverEntity.getRelevancy());
                jsonOb.put("relevanceType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonOb.put("template", templ);
                jsonArray3.add(jsonOb);
            }
        }else {
            JSONObject jsonOb = new JSONObject();
            jsonOb.put("id", appAdvertising.getTopId());
            jsonOb.put("name", positionEntity3.getPositionName());
            jsonArray1.add(jsonOb);
        }
        jsonObject.put("id",appAdvertising.getId());
        jsonObject.put("topPosition", jsonArray1);
        jsonObject.put("importanPositilon", jsonArray2);
        jsonObject.put("collaboratePosition", jsonArray3);

        return ValueUtil.toJson(HttpStatus.SC_OK,jsonObject);
    }

}
