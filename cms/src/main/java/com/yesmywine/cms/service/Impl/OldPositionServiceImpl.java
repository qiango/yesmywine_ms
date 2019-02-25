package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.OldPositionService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.cms.dao.AdverPositionDao;
import com.yesmywine.cms.dao.OldPositionDao;
import com.yesmywine.cms.dao.PositionEntityDao;
import com.yesmywine.cms.entity.OldPosition;
import com.yesmywine.cms.entity.PositionEntity;
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
public class OldPositionServiceImpl implements OldPositionService {

    @Autowired
    private OldPositionDao oldPositionDao;
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;

    @Override
    public Object findAll() {
        List<OldPosition> all = this.oldPositionDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(OldPosition one: all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(one.getFirstPositionId());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArray1 = new JSONArray();
            if(byPositionEntity.size()>0) {
                for (AdverPositionEntity adverPositionEntity : byPositionEntity) {
                    JSONObject jsonOb = new JSONObject();
                    AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                    PositionEntity positionEntity1 = adverPositionEntity.getPositionEntity();
                    jsonOb.put("id", one.getFirstPositionId());
                    jsonOb.put("name", positionEntity1.getPositionName());
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
                JSONObject jsonOb = new JSONObject();
                PositionEntity one1 = this.positionDao.findOne(one.getFirstPositionId());
                jsonOb.put("id", one.getFirstPositionId());
                jsonOb.put("name", one1.getPositionName());
                jsonArray1.add(jsonOb);
            }
            jsonObject.put("firstPosition", jsonArray1);
            PositionEntity positionEntity3 = new PositionEntity();
            positionEntity3.setId(one.getSecentPositionId());
            List<AdverPositionEntity> byPositionEntity2 = this.adverPositionDao.findByPositionEntity(positionEntity3);
            JSONArray jsonArray2 = new JSONArray();
            if(byPositionEntity2.size()>0) {
                for (AdverPositionEntity adverPositionEntity : byPositionEntity2) {
                    JSONObject jsonOb = new JSONObject();
                    AdverEntity adverEntity = adverPositionEntity.getAdverEntity();
                    PositionEntity positionEntity4 = adverPositionEntity.getPositionEntity();
                    jsonOb.put("id", one.getSecentPositionId());
                    jsonOb.put("name", positionEntity4.getPositionName());
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
                JSONObject jsonOb = new JSONObject();
                PositionEntity one1 = this.positionDao.findOne(one.getSecentPositionId());
                jsonOb.put("id", one.getSecentPositionId());
                jsonOb.put("name", one1.getPositionName());
                jsonArray2.add(jsonOb);
            }
            jsonObject.put("secentPosition", jsonArray2);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }


    @Override
    public String insert(Integer firstPositionId, Integer secentPositionId) {
        try {
            List<OldPosition> all = this.oldPositionDao.findAll();
            if(0!=all.size()){
                return "广告位已存在，只能修改";
            }else {
                OldPosition oldPosition = new OldPosition();
                oldPosition.setFirstPositionId(firstPositionId);
                oldPosition.setSecentPositionId(secentPositionId);
                this.oldPositionDao.save(oldPosition);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, Integer firstPositionId, Integer secentPositionId) {
        try {
            OldPosition one = this.oldPositionDao.findOne(id);
            if(ValueUtil.isEmpity(one)){
                return "没有此广告位";
            }else {
                OldPosition oldPosition = new OldPosition();
                oldPosition.setId(id);
                oldPosition.setFirstPositionId(firstPositionId);
                oldPosition.setSecentPositionId(secentPositionId);
                this.oldPositionDao.save(oldPosition);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String delete(Integer id) {
        try{
            this.oldPositionDao.delete(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }
}
