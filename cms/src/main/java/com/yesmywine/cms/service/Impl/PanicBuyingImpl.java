package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.PanicBuyingService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.cms.dao.*;
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
 * Created by wangdiandian on 2017/5/25.
 */
@Service
@Transactional
public class PanicBuyingImpl implements PanicBuyingService {

    @Autowired
    private PanicBuyingFirstDao panicBuyingFirstDao;
    @Autowired
    private PanicBuyingSecentDao panicBuyingSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PositionEntityDao positionDao;
    @Autowired
    private PanicBuyingPositionDao panicBuyingPositionDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer id) {
        PanicBuyingFirst pan = this.panicBuyingFirstDao.findOne(id);
        if(ValueUtil.isEmpity(pan)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", pan.getId());
        jsonObject.put("name", pan.getName());
        jsonObject.put("activityId", pan.getActicityId());

        List<PanicBuyingSecent> panicBuyingSecents = this.panicBuyingSecentDao.findByPanicBuyingFirstId(id);

        for(PanicBuyingSecent panicBuyingSecent: panicBuyingSecents){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", panicBuyingSecent.getId());
            Goods one2 = this.goodsService.findById(panicBuyingSecent.getGoodsId());
            jsonObjectSecent.put("goodsId",one2.getId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("saleSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<PanicBuyingFirst> all = this.panicBuyingFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(PanicBuyingFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            jsonObject.put("activityId", one.getActicityId());
            List<PanicBuyingSecent> panicBuyingSecents = this.panicBuyingSecentDao.findByPanicBuyingFirstId(one.getId());
            for(PanicBuyingSecent panicBuyingSecent: panicBuyingSecents){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", panicBuyingSecent.getId());
                Goods one2 = this.goodsService.findById(panicBuyingSecent.getGoodsId());
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
        Page<PanicBuyingSecent> all = panicBuyingSecentDao.findAll(pageable);
        JSONArray jsonArray = new JSONArray();
        for(PanicBuyingSecent panicBuyingSecent: all){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", panicBuyingSecent.getGoodsId());
            Goods one = this.goodsService.findById(panicBuyingSecent.getGoodsId());
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
    public String insert(String name, Integer activityId,String jsonString) {
        try {
            PanicBuyingFirst firstName = this.panicBuyingFirstDao.findByName(name);
            if(ValueUtil.notEmpity(firstName)){
                return "此栏目已存在";
            }
            PanicBuyingFirst panicBuyingFirst = new PanicBuyingFirst();
            panicBuyingFirst.setActicityId(activityId);

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
                panicBuyingFirst.setStartTime(startDate);
                panicBuyingFirst.setEndTime(endDate);
            }else {
                return "没有此活动";
            }

            panicBuyingFirst.setName(name);
            this.panicBuyingFirstDao.save(panicBuyingFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    PanicBuyingSecent panicBuyingSecent = new PanicBuyingSecent();
                    panicBuyingSecent.setGoodsId(goodsId);
                    panicBuyingSecent.setPanicBuyingFirstId(panicBuyingFirst.getId());
                    this.panicBuyingSecentDao.save(panicBuyingSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name, Integer activityId,String jsonString) {
        try {
            PanicBuyingFirst byName = this.panicBuyingFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)&&byName.getId()!=id){
                return "此栏目已存在";
            }
            PanicBuyingFirst panicBuyingFirst = new PanicBuyingFirst();
            panicBuyingFirst.setName(name);
            panicBuyingFirst.setId(id);
//            PanicBuyingFirst one = this.panicBuyingFirstDao.findOne(id);
            panicBuyingFirst.setActicityId(activityId);


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
                panicBuyingFirst.setStartTime(startDate);
                panicBuyingFirst.setEndTime(endDate);
            }else {
                return "没有此活动";
            }

//            panicBuyingFirst.setStartTime(one.getStartTime());
//            panicBuyingFirst.setEndTime(one.getEndTime());
            this.panicBuyingFirstDao.save(panicBuyingFirst);
            if(ValueUtil.notEmpity(byName)&& byName.getActicityId()!=activityId){
                panicBuyingSecentDao.deleteAll();
            }
            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    PanicBuyingSecent panicBuyingSecent = new PanicBuyingSecent();
                    panicBuyingSecent.setGoodsId(goodsId);
                    panicBuyingSecent.setPanicBuyingFirstId(panicBuyingFirst.getId());
                    List<PanicBuyingSecent> panicBuyingSecent1 = this.panicBuyingSecentDao.findByGoodsIdAndPanicBuyingFirstId(goodsId, panicBuyingFirst.getId());
                    if(panicBuyingSecent1.size()>0){
                        panicBuyingSecent.setId(panicBuyingSecent1.get(0).getId());
                    }
                    this.panicBuyingSecentDao.save(panicBuyingSecent);
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
            this.panicBuyingFirstDao.delete(id);
            this.panicBuyingSecentDao.deleteByPanicBuyingFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.panicBuyingSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public JSONObject getShuffling() {
        com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONObject second = new com.alibaba.fastjson.JSONObject();
        com.alibaba.fastjson.JSONArray firstArray = new com.alibaba.fastjson.JSONArray();
        List<PanicBuyingFirst> panicBuyingFirst=panicBuyingFirstDao.findAll();//所有栏目
        List<PanicBuyingPosition> panicBuyingPosition=panicBuyingPositionDao.findAll();//轮播

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
        }

        first.put("lunbo",jsonArrayPosition);
        for(PanicBuyingFirst p:panicBuyingFirst){
            com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
            com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
            String name=p.getName();
            third.put("classifyName",name);
            List<PanicBuyingSecent> list=panicBuyingSecentDao.findByPanicBuyingFirstId(p.getId());
            for(PanicBuyingSecent j:list){
                com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                Goods goods=goodsService.findById(j.getGoodsId());
                fourth.put("goodPicture",goods.getPicture());
                fourth.put("goodName",goods.getGoodsName());
                fourth.put("goodsEnName",goods.getGoodsEnName());
                fourth.put("price",goods.getPrice());
                fourth.put("salePrice",goods.getSalePrice());
                fourth.put("goodsId",goods.getId());
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


