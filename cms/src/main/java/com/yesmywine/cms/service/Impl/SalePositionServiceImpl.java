package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.AdverPositionDao;
import com.yesmywine.cms.dao.SalePositionDao;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.entity.PositionEntity;
import com.yesmywine.cms.entity.SalePosition;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.SalePositionService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Service
public class SalePositionServiceImpl implements SalePositionService{
    @Autowired
    private SalePositionDao salePositionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;

    @Override
    public Object findAll() {
        List<SalePosition> all = this.salePositionDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(SalePosition one: all) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(one.getPositionId());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", one.getPositionId());
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
            jsonObject.put("position", jsonArrayPosition);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }


    @Override
    public String insert(Integer positionId) {
        try {
            List<SalePosition> all = this.salePositionDao.findAll();
            if(0!=all.size()){
                return "广告位已存在，只能修改";
            }else {
                SalePosition salePosition = new SalePosition();
                salePosition.setPositionId(positionId);
                this.salePositionDao.save(salePosition);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, Integer positionId) {
        try {
            SalePosition sale = this.salePositionDao.findOne(id);
            if(ValueUtil.isEmpity(sale)){
                return "没有此广告位";
            }else {
                SalePosition salePosition = new SalePosition();
                salePosition.setId(id);
                salePosition.setPositionId(positionId);
                this.salePositionDao.save(salePosition);
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
            this.salePositionDao.delete(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }
}

