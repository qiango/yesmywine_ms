package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.OldService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
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
public class OldServiceImpl implements OldService {

    @Autowired
    private OldFirstDao oldFirstDao;
    @Autowired
    private OldSecentDao oldSecentDao;
    @Autowired
    private OldPositionDao positionDaos;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer id) {
        OldFirst one = this.oldFirstDao.findOne(id);
        if(ValueUtil.isEmpity(one)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getName());

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
            jsonArrayPosition.add(jsonObjectPosition);
        }
        jsonObject.put("position", jsonArrayPosition);

        List<OldSecent> byOldFirstId = this.oldSecentDao.findByOldFirstId(one.getId());

        for(OldSecent oldSecent: byOldFirstId){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", oldSecent.getId());
            Goods one2 = this.goodsService.findById(oldSecent.getGoodsId());
            jsonObjectSecent.put("secentGoodsId", oldSecent.getGoodsId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("oldSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<OldFirst> all = this.oldFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(OldFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());

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
                jsonArrayPosition.add(jsonObjectPosition);
            }
            jsonObject.put("position", jsonArrayPosition);

            List<OldSecent> byOldFirstId = this.oldSecentDao.findByOldFirstId(one.getId());

            for(OldSecent oldSecent: byOldFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", oldSecent.getId());
                Goods one2 = this.goodsService.findById(oldSecent.getGoodsId());
                jsonObjectSecent.put("secentGoodsId", oldSecent.getGoodsId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonArray.add(jsonObjectSecent);
            }
            jsonObject.put("oldSecent", jsonArray);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(String name, Integer positionId, String jsonString) {
        try {
            OldFirst byName = this.oldFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)){
                return "此栏目已存在";
            }
            OldFirst oldFirst = new OldFirst();
            oldFirst.setName(name);
            oldFirst.setPositionId(positionId);
            this.oldFirstDao.save(oldFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    OldSecent oldSecent = new OldSecent();
                    oldSecent.setGoodsId(goodsId);
                    oldSecent.setOldFirstId(oldFirst.getId());
                    this.oldSecentDao.save(oldSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name, Integer positionId, String jsonString) {
        try {
            OldFirst byName = this.oldFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)&& byName.getId() != id){
                return "此栏目已存在";
            }
            OldFirst oldFirst = new OldFirst();
            oldFirst.setName(name);
            oldFirst.setPositionId(positionId);
            oldFirst.setId(id);
            this.oldFirstDao.save(oldFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    OldSecent oldSecent = new OldSecent();
                    oldSecent.setGoodsId(goodsId);
                    oldSecent.setOldFirstId(oldFirst.getId());
                    OldSecent byGoodsIdAndOldFirstId = this.oldSecentDao.findByGoodsIdAndOldFirstId(goodsId, oldFirst.getId());
                    if(ValueUtil.notEmpity(byGoodsIdAndOldFirstId)){
                        oldSecent.setId(byGoodsIdAndOldFirstId.getId());
                    }
                    this.oldSecentDao.save(oldSecent);
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
            this.oldFirstDao.delete(id);
            this.oldSecentDao.deleteByOldFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.oldSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public JSON getShuffling() throws YesmywineException {
        com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject second = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONArray firstArray = new com.alibaba.fastjson.JSONArray();
        List<OldFirst> panicBuyingFirst=oldFirstDao.findAll();//所有栏目
        List<OldPosition> panicBuyingPosition=positionDaos.findAll();//轮播

        if(panicBuyingPosition.size()>0) {
            Integer firstPostionId = panicBuyingPosition.get(0).getFirstPositionId();
            Integer secentPostionId = panicBuyingPosition.get(0).getSecentPositionId();

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(firstPostionId);
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for (AdverPositionEntity aP : byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", firstPostionId);
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if (1 == adverEntity.getRelevancyType()) {
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }

            PositionEntity positionEntity2 = new PositionEntity();
            positionEntity2.setId(secentPostionId);
            List<AdverPositionEntity> byPositionEntity2 = this.adverPositionDao.findByPositionEntity(positionEntity2);
            JSONArray jsonArrayPosition2 = new JSONArray();
            for (AdverPositionEntity aP : byPositionEntity2) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", secentPostionId);
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if (1 == adverEntity.getRelevancyType()) {
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition2.add(jsonObjectPosition);
            }
            second.put("firstPostion", jsonArrayPosition);
            second.put("secentPostion", jsonArrayPosition2);
        }else {
            second.put("firstPostion", new JSONArray());
            second.put("secentPostion", new JSONArray());
        }
        first.put("lunbo",second);
        for(OldFirst p:panicBuyingFirst){
            com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
            com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
            String name=p.getName();
            Integer positionId = p.getPositionId();

            PositionEntity positionEntityOld = new PositionEntity();
            positionEntityOld.setId(positionId);
            List<AdverPositionEntity> byPositionEntityOld = this.adverPositionDao.findByPositionEntity(positionEntityOld);
            JSONArray jsonArrayPositionOld = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntityOld) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", positionId);
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if(1==adverEntity.getRelevancyType()){
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPositionOld.add(jsonObjectPosition);
            }
            third.put("classifyName",name);
            third.put("position",jsonArrayPositionOld);
            List<OldSecent> list=oldSecentDao.findByOldFirstId(p.getId());
            for(OldSecent j:list){
                com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                Goods goods=goodsService.findById(j.getGoodsId());
                fourth.put("goodPicture",goods.getPicture());
                fourth.put("goodName",goods.getGoodsName());
                fourth.put("goodsEnName",goods.getGoodsEnName());
                fourth.put("price",goods.getPrice());
                fourth.put("salePrice",goods.getSalePrice());
                fourth.put("sold",goods.getSales());
                fourth.put("goodsId",goods.getId());
//                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/goodComments", RequestMethod.get);
//                httpRequest.addParameter("type", 1);//type:1好评,2中评,3差评
//                httpRequest.addParameter("goodsId",goods.getId());
//                httpRequest.run();
//                String temp = httpRequest.getResponseContent();
//                String comment = ValueUtil.getFromJson(temp, "data", "comment");
//                String praise = ValueUtil.getFromJson(temp, "data", "praise");
//                fourth.put("comment",comment);//评论数量
//                fourth.put("Praise",praise);//好评率
                scondArray.add(fourth);
            }
            third.put("goodsList",scondArray);
            firstArray.add(third);
        }
        first.put("classify",firstArray);
        return first;

    }
}
