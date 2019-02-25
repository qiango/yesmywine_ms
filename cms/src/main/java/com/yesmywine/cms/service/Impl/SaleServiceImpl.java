package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.SaleService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.cms.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Service
@Transactional
public class SaleServiceImpl implements SaleService {
    @Autowired
    private SaleFirstDao saleFirstDao;
    @Autowired
    private SaleSecentDao saleSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private SalePositionDao salePositionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;


    @Override
    public Object findOne(Integer id) {
        SaleFirst sale = this.saleFirstDao.findOne(id);
        if(ValueUtil.isEmpity(sale)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", sale.getId());
        jsonObject.put("name", sale.getName());
        jsonObject.put("title",sale.getTitle());
        if(ValueUtil.notEmpity(sale.getPositionId())){
            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(sale.getPositionId());
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", sale.getPositionId());
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
        }

        List<SaleSecent> saleSecents = this.saleSecentDao.findBySaleFirstId(id);

        for(SaleSecent saleSecent: saleSecents){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", saleSecent.getId());
            Goods one2 = this.goodsService.findById(saleSecent.getGoodsId());
            jsonObjectSecent.put("goodsId",one2.getId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("saleSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<SaleFirst> all = this.saleFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(SaleFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            jsonObject.put("title",one.getTitle());

            if(ValueUtil.notEmpity(one.getPositionId())){
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
            }

//            PositionEntity one1 = this.positionDao.findOne(one.getPositionId());
//            if(ValueUtil.notEmpity(one1)){
//                jsonObject.put("positionName", one1.getPositionName());
//            }
            List<SaleSecent> saleSecents = this.saleSecentDao.findBySaleFirstId(one.getId());
            for(SaleSecent saleSecent: saleSecents){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", saleSecent.getId());
                Goods one2 = this.goodsService.findById(saleSecent.getGoodsId());
                jsonObjectSecent.put("goodsId",one2.getId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonArray.add(jsonObjectSecent);
            }
            jsonObject.put("saleSecent", jsonArray);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(String name, Integer positionId, String title , String jsonString) {
        try {
            SaleFirst firstName = this.saleFirstDao.findByName(name);
            if(ValueUtil.notEmpity(firstName)){
                return "此栏目已存在";
            }
            SaleFirst saleFirst = new SaleFirst();
            saleFirst.setName(name);
            saleFirst.setTitle(title);
            if(ValueUtil.notEmpity(positionId)){
                saleFirst.setPositionId(positionId);
            }
            this.saleFirstDao.save(saleFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    SaleSecent saleSecent = new SaleSecent();
                    saleSecent.setGoodsId(goodsId);
                    saleSecent.setSaleFirstId(saleFirst.getId());
                    this.saleSecentDao.save(saleSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name, Integer positionId, String title, String jsonString) {
        try {
            SaleFirst byName = this.saleFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)&&byName.getId()!=id){
                return "此栏目已存在";
            }
            SaleFirst saleFirst = new SaleFirst();
            saleFirst.setName(name);
            saleFirst.setTitle(title);
//            if(ValueUtil.notEmpity(positionId)){
                saleFirst.setPositionId(positionId);
//            }
            saleFirst.setId(id);
            this.saleFirstDao.save(saleFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    SaleSecent saleSecent = new SaleSecent();
                    saleSecent.setGoodsId(goodsId);
                    saleSecent.setSaleFirstId(saleFirst.getId());
                    SaleSecent saleSecent1 = this.saleSecentDao.findByGoodsIdAndSaleFirstId(goodsId, saleFirst.getId());
                    if(ValueUtil.notEmpity(saleSecent1)){
                        saleSecent.setId(saleSecent1.getId());
                    }
                    this.saleSecentDao.save(saleSecent);
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
            this.saleFirstDao.delete(id);
            this.saleSecentDao.deleteBySaleFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.saleSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public com.alibaba.fastjson.JSONObject getShuffling() {
        com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject second = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONArray firstArray = new com.alibaba.fastjson.JSONArray();
        List<SaleFirst> panicBuyingFirst=saleFirstDao.findAll();//所有栏目
        List<SalePosition> panicBuyingPosition=salePositionDao.findAll();//轮播

        if(panicBuyingPosition.size()>0){

            Integer postionId=panicBuyingPosition.get(0).getPositionId();

            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(postionId);
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            JSONArray jsonArrayPosition = new JSONArray();
            for(AdverPositionEntity aP: byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", postionId);
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
            second.put("position",jsonArrayPosition);
        }else {
            second.put("position", new JSONArray());
        }

        first.put("lunbo",second);
        for(SaleFirst p:panicBuyingFirst){
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
            third.put("title",p.getTitle());
            third.put("position",jsonArrayPositionOld);
            List<SaleSecent> list=saleSecentDao.findBySaleFirstId(p.getId());
            for(SaleSecent j:list){
                com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                Goods goods=goodsService.findById(j.getGoodsId());
                fourth.put("goodPicture",goods.getPicture());
                fourth.put("goodsId",goods.getId());
                fourth.put("goodName",goods.getGoodsName());
                fourth.put("goodsEnName",goods.getGoodsEnName());
                fourth.put("price",goods.getPrice());
                fourth.put("salePrice",goods.getSalePrice());
                fourth.put("sold",goods.getSales());
//                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/goodComments/itf", RequestMethod.get);
//                httpRequest.addParameter("type", 1);//type:1好评,2中评,3差评
//                httpRequest.addParameter("goodsId",goods.getId());
//                httpRequest.run();
//                String temp = httpRequest.getResponseContent();
//                String comment = ValueUtil.getFromJson(temp, "data", "comment");
//                String praise = ValueUtil.getFromJson(temp, "data", "praise");
                fourth.put("comment",goods.getComments());//评论数量
                fourth.put("Praise",goods.getPraise());//好评率
                scondArray.add(fourth);
            }
            third.put("goodsList",scondArray);
            firstArray.add(third);
        }
        first.put("classify",firstArray);
        return first;
    }
}

