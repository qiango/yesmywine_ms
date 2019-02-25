package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.FlashPurchaseService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@Service
@Transactional
public class FlashPurchaseServiceImpl implements FlashPurchaseService {
    @Autowired
    private FlashPurchaseFirstDao flashPurchaseFirstDao;
    @Autowired
    private FlashPurchaseSecentDao flashPurchaseSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private FlashPurchasePositionDao flashPurchasePositionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer id) {
        FlashPurchaseFirst flash = this.flashPurchaseFirstDao.findOne(id);
        if(ValueUtil.isEmpity(flash)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", flash.getId());
        jsonObject.put("name", flash.getName());
        jsonObject.put("activityId", flash.getActivityId());

        List<FlashPurchaseSecent> flashPurchaseSecents = this.flashPurchaseSecentDao.findByFlashPurchaseFirstId(id);

        for(FlashPurchaseSecent flashPurchaseSecent: flashPurchaseSecents){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", flashPurchaseSecent.getId());
            Goods one2 = this.goodsService.findById(flashPurchaseSecent.getGoodsId());
            jsonObjectSecent.put("goodsId",one2.getId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("saleSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<FlashPurchaseFirst> all = this.flashPurchaseFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(FlashPurchaseFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            jsonObject.put("activityId", one.getActivityId());
//
            List<FlashPurchaseSecent> flashPurchaseSecents = this.flashPurchaseSecentDao.findByFlashPurchaseFirstId(one.getId());
            for(FlashPurchaseSecent flashPurchaseSecent: flashPurchaseSecents){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", flashPurchaseSecent.getId());
                Goods one2 = this.goodsService.findById(flashPurchaseSecent.getGoodsId());
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
    public Object findGoods(Integer pageNo, Integer pageSize) {
        if(ValueUtil.isEmpity(pageNo)){
            pageNo = 1;
        }
        if(ValueUtil.isEmpity(pageSize)){
            pageSize = 5;
        }
        int page = pageNo.intValue();
        int size = pageSize.intValue();
//        Pageable pageable = (Pageable) new PageRequest(page, pageSize);
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page-1, size, sort);
//        return goodsDao.findAllOrderBySales(pageable);
        Page<FlashPurchaseSecent> all = flashPurchaseSecentDao.findAll(pageable);
        JSONArray jsonArray = new JSONArray();
        for(FlashPurchaseSecent flashPurchaseSecent: all){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", flashPurchaseSecent.getGoodsId());
            Goods one = this.goodsService.findById(flashPurchaseSecent.getGoodsId());
            jsonObject.put("goodsName", one.getGoodsName());
            jsonObject.put("picture", one.getPicture());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObjectRe = new JSONObject();
        jsonObjectRe.put("totalRows", all.getTotalElements());
        jsonObjectRe.put("totalPages", all.getTotalPages());
        jsonObjectRe.put("pageNo", pageNo);
        jsonObjectRe.put("pageSize", pageSize);
        jsonObjectRe.put("goods", jsonArray);
        return jsonObjectRe;
    }

    @Override
    public String insert(String name,Integer activityId,  String jsonString) {
        try {
            FlashPurchaseFirst firstName = this.flashPurchaseFirstDao.findByName(name);
            if(ValueUtil.notEmpity(firstName)){
                return "此栏目已存在";
            }
            FlashPurchaseFirst flashPurchaseFirst = new FlashPurchaseFirst();
            flashPurchaseFirst.setActivityId(activityId);

            HttpBean httpRequest = new HttpBean(ConstantData.activity + "/activity/itf", RequestMethod.get);
            httpRequest.addParameter("id", activityId);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String activity=ValueUtil.getFromJson(temp,"data");
            JSONObject jsonObjectAc = JSONObject.parseObject(activity);
            if(ValueUtil.notEmpity(jsonObjectAc)){
                String startTime = jsonObjectAc.get("startTime").toString();
                String endTime = jsonObjectAc.get("endTime").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);
                Date startDate =sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                flashPurchaseFirst.setStartTime(startDate);
                flashPurchaseFirst.setEndTime(endDate);
            }else {
                return "没有此活动";
            }

            flashPurchaseFirst.setName(name);
            this.flashPurchaseFirstDao.save(flashPurchaseFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    FlashPurchaseSecent flashPurchaseSecent = new FlashPurchaseSecent();
                    flashPurchaseSecent.setGoodsId(goodsId);
                    flashPurchaseSecent.setFlashPurchaseFirstId(flashPurchaseFirst.getId());
                    List<FlashPurchaseSecent> flashPurchaseSecent1 = this.flashPurchaseSecentDao.findByGoodsIdAndFlashPurchaseFirstId(goodsId, flashPurchaseFirst.getId());
                    if(flashPurchaseSecent1.size()>0){
                        continue;
                    }else {
                        this.flashPurchaseSecentDao.save(flashPurchaseSecent);
                    }
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name,Integer activityId, String jsonString) {
        try {
            FlashPurchaseFirst byName = this.flashPurchaseFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)&&byName.getId()!=id){
                return "此栏目已存在";
            }
            FlashPurchaseFirst flashPurchaseFirst = new FlashPurchaseFirst();
            flashPurchaseFirst.setName(name);
            flashPurchaseFirst.setActivityId(activityId);
            flashPurchaseFirst.setId(id);
//            FlashPurchaseFirst one1 = this.flashPurchaseFirstDao.findOne(id);
//            flashPurchaseFirst.setStartTime(one1.getStartTime());
//            flashPurchaseFirst.setEndTime(one1.getEndTime());

            HttpBean httpRequest = new HttpBean(ConstantData.activity + "/activity/itf", RequestMethod.get);
            httpRequest.addParameter("id", activityId);
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String activity=ValueUtil.getFromJson(temp,"data");
            JSONObject jsonObjectAc = JSONObject.parseObject(activity);
            if(ValueUtil.notEmpity(jsonObjectAc)){
                String startTime = jsonObjectAc.get("startTime").toString();
                String endTime = jsonObjectAc.get("endTime").toString();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.US);
                Date startDate =sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                flashPurchaseFirst.setStartTime(startDate);
                flashPurchaseFirst.setEndTime(endDate);
            }else {
                return "没有此活动";
            }

            this.flashPurchaseFirstDao.save(flashPurchaseFirst);
            FlashPurchaseFirst one = this.flashPurchaseFirstDao.findOne(id);
            if(one.getActivityId()!=activityId){
                flashPurchaseSecentDao.deleteAll();
            }
            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    FlashPurchaseSecent flashPurchaseSecent = new FlashPurchaseSecent();
                    flashPurchaseSecent.setGoodsId(goodsId);
                    flashPurchaseSecent.setFlashPurchaseFirstId(flashPurchaseFirst.getId());
                    List<FlashPurchaseSecent> flashPurchaseSecent1 = this.flashPurchaseSecentDao.findByGoodsIdAndFlashPurchaseFirstId(goodsId, flashPurchaseFirst.getId());
                    if(flashPurchaseSecent1.size()>0){
                        flashPurchaseSecent.setId(flashPurchaseSecent1.get(0).getId());
                    }
                    this.flashPurchaseSecentDao.save(flashPurchaseSecent);
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
            this.flashPurchaseFirstDao.delete(id);
            this.flashPurchaseSecentDao.deleteByFlashPurchaseFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.flashPurchaseSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    public JSONObject getShuffling() {
        com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
//        com.alibaba.fastjson.JSONObject second = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONArray firstArray = new com.alibaba.fastjson.JSONArray();
        List<FlashPurchaseFirst> panicBuyingFirst=flashPurchaseFirstDao.findAll();//所有栏目
        List<FlashPurchasePosition> panicBuyingPosition=flashPurchasePositionDao.findAll();//轮播
        JSONArray jsonArrayPosition = new JSONArray();
        if(panicBuyingPosition.size()>0) {
            Integer postionId = panicBuyingPosition.get(0).getPositionId();
            PositionEntity positionEntity = new PositionEntity();
            positionEntity.setId(postionId);
            List<AdverPositionEntity> byPositionEntity = this.adverPositionDao.findByPositionEntity(positionEntity);
            for (AdverPositionEntity aP : byPositionEntity) {
                JSONObject jsonObjectPosition = new JSONObject();
                AdverEntity adverEntity = aP.getAdverEntity();
                PositionEntity positionEntity1 = aP.getPositionEntity();
                jsonObjectPosition.put("name", positionEntity1.getPositionName());
                jsonObjectPosition.put("id", postionId);
                jsonObjectPosition.put("image", adverEntity.getImage());
                jsonObjectPosition.put("adverId", adverEntity.getId());
                jsonObjectPosition.put("relevance", adverEntity.getRelevancy());
                jsonObjectPosition.put("relevancyType", adverEntity.getRelevancyType());
                Integer templ = null;
                if (1 == adverEntity.getRelevancyType()) {
                    templ = this.activityService.findTempl(Integer.valueOf(adverEntity.getRelevancy()));
                }
                jsonObjectPosition.put("template", templ);
                jsonArrayPosition.add(jsonObjectPosition);
            }
        }
        first.put("lunbo",jsonArrayPosition);
        for(FlashPurchaseFirst p:panicBuyingFirst){
            com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
            com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
            String name=p.getName();
            third.put("classifyName",name);


//            if(ValueUtil.isEmpity(pageNo)){
//                pageNo = 1;
//            }
//            if(ValueUtil.isEmpity(pageSize)){
//                pageSize = 6;
//            }
//            int page = pageNo.intValue();
//            int size = pageSize.intValue();
////        Pageable pageable = (Pageable) new PageRequest(page, pageSize);
//            Sort sort = new Sort(Sort.Direction.ASC, "id");
//            Pageable pageable = new PageRequest(page-1, size, sort);
////        return goodsDao.findAllOrderBySales(pageable);
//            Page<FlashPurchaseSecent> flashPurchaseSecentList = flashPurchaseSecentDao.findByFlashPurchaseFirstId(p.getId(),pageable);
//            JSONArray jsonArray1 = new JSONArray();
//            for(FlashPurchaseSecent flashPurchaseSecent: flashPurchaseSecentList){
//                JSONObject jsonObject1 = new JSONObject();
////                jsonObject1.put("id", flashPurchaseSecent.getGoodsId());
//                Goods goods = this.goodsDao.findOne(flashPurchaseSecent.getGoodsId());
////                jsonObject1.put("name", goods.getGoodsName());
////                jsonObject1.put("picture", goods.getPicture());
//
//                jsonObject1.put("goodPicture",goods.getPicture());
//                jsonObject1.put("goodName",goods.getGoodsName());
//                jsonObject1.put("goodsId",goods.getId());
//                jsonObject1.put("goodsEnName",goods.getGoodsEnName());
//                jsonObject1.put("price",goods.getPrice());
//                jsonObject1.put("salePrice",goods.getSalePrice());
//                jsonObject1.put("sold",goods.getSales());
////                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/goodComments/itf", RequestMethod.get);
////                httpRequest.addParameter("type", 1);//type:1好评,2中评,3差评
////                httpRequest.addParameter("goodsId",goods.getId());
////                httpRequest.run();
////                String temp = httpRequest.getResponseContent();
////                String comment = ValueUtil.getFromJson(temp, "data", "comment");
////                String praise = ValueUtil.getFromJson(temp, "data", "praise");
//                jsonObject1.put("comment",goods.getComments());//评论数量
//                jsonObject1.put("praise",goods.getPraise());//好评率
//
//                jsonArray1.add(jsonObject1);
//            }
////            JSONObject jsonObjectRe = new JSONObject();
//            third.put("totalElements", flashPurchaseSecentList.getTotalElements());
//            third.put("totalPages", flashPurchaseSecentList.getTotalPages());
//            third.put("pageNo", pageNo);
//            third.put("pageSize", pageSize);
//            third.put("goods", jsonArray1);


            List<FlashPurchaseSecent> list=flashPurchaseSecentDao.findByFlashPurchaseFirstId(p.getId());
            for(FlashPurchaseSecent j:list){
                com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                Goods goods=goodsService.findById(j.getGoodsId());
                fourth.put("goodPicture",goods.getPicture());
                fourth.put("goodName",goods.getGoodsName());
                fourth.put("goodsId",goods.getId());
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
                fourth.put("praise",goods.getPraise());//好评率
                scondArray.add(fourth);
            }
            third.put("goodsList",scondArray);

            Date startTime = new Date();
            Date endTime = p.getEndTime();
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startTime);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endTime);
            Long endSecond = endCalendar.getTimeInMillis();
            Long startSecond = startCalendar.getTimeInMillis();
            Long balance = endSecond-startSecond;
            Integer s_day = 60*60*24*1000;
            Integer s_hour = 60*60*1000;
            Integer s_min = 60*1000;
            Long day = balance/s_day;//还剩多少天
            Long hour = (balance-day*s_day)/s_hour;//还剩多少小时
            Long min = (balance-day*s_day-hour*s_hour)/s_min;//还剩多少分钟
            Long sec = (balance-day*s_day-hour*s_hour-min*s_min)/1000;//还剩多少秒

//            JSONObject dataJson = new JSONObject();
            third.put("day",day);
            third.put("hour",hour);
            third.put("min",min);
            third.put("sec",sec);

            firstArray.add(third);
        }
        first.put("classify",firstArray);
        return first;
    }
}

