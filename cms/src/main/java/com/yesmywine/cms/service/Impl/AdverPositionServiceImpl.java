package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.AdverPositionDao;
import com.yesmywine.cms.dao.PositionEntityDao;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.entity.PositionEntity;
import com.yesmywine.cms.service.AdverPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by yly on 2017/2/10.
 */
@Service
@Transactional
public class AdverPositionServiceImpl extends BaseServiceImpl<AdverPositionEntity, Integer> implements AdverPositionService {
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private PositionEntityDao positionEntityDao;


    @Override
    public String saveAP(AdverPositionEntity adverPosition) throws Exception{
        try{
            PositionEntity positionEntity = adverPosition.getPositionEntity();
            if(0==positionEntity.getPositionType()){
                List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
                if(0 != byPositionEntity.size()){
                    return "此广告位是已有资源";
                }
            }
            this.adverPositionDao.save(adverPosition);

        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
//            return "erro";
        }
        return "success";
    }

    @Override
    public Object findByAdverEntity(Integer adverId) {
        AdverEntity adverEntity = new AdverEntity();
        adverEntity.setId(adverId);
        return this.adverPositionDao.findByAdverEntity(adverEntity);
    }

    @Override
    public Object findByPositionEntity(Integer positionId) {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setId(positionId);
        List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
        return byPositionEntity;
    }

    @Override
    public Object findByPositionEntityShowAdver(Integer positionId) {
        PositionEntity positionEntity = new PositionEntity();
        positionEntity.setId(positionId);
        List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
        JSONObject jsonObject = new JSONObject();
        if(byPositionEntity.size()==0){
            jsonObject.put("position", this.positionEntityDao.findOne(positionId));
            return jsonObject;
        }

        jsonObject.put("position", byPositionEntity.get(0).getPositionEntity());
        JSONArray jsonArray = new JSONArray();
        for(AdverPositionEntity adverPositionEntity: byPositionEntity){
            JSONObject jsonObjectAd = new JSONObject();
            jsonObjectAd.put("adver", adverPositionEntity.getAdverEntity());
            jsonArray.add(jsonObjectAd);
        }
        jsonObject.put("adver", jsonArray);
        return jsonObject;
    }

    @Override
    public String deleteAP(Integer adverId, Integer positionId) throws Exception{
        try {
            AdverEntity adverEntity = new AdverEntity();
            adverEntity.setId(adverId);
            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(positionId);
            this.adverPositionDao.deleteByAdverEntityAndPositionEntity(adverEntity, positionEntity);
        }catch (Exception e){
            throw e;
        }
        return "success";
    }
}
