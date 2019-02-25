package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.ActivityColumnDao;
import com.yesmywine.cms.dao.GoodsDao;
import com.yesmywine.cms.dao.HomePagePanicBuyingSecentDao;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.HomePagePanicBuyingService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.cms.dao.HomePagePanicBuyingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class HomePagePanicBuyingServiceImpl implements HomePagePanicBuyingService {

    @Autowired
    private HomePagePanicBuyingDao homePagePanicBuyingDao;
//    @Autowired
//    private ActivityFirstDao activityFirstDao;
    @Autowired
    private ActivityColumnDao activityColumnDao;
    @Autowired
    private HomePagePanicBuyingSecentDao homePagePanicBuyingSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer id) {
//        return homePagePanicBuyingDao.findOne(id);
        HomePagePanicBuying pan = this.homePagePanicBuyingDao.findOne(id);
        if(ValueUtil.isEmpity(pan)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", pan.getId());
//        jsonObject.put("name", pan.getName());
        jsonObject.put("activityId", pan.getActivityId());
        jsonObject.put("status", pan.getStatus());

        List<HomePagePanicBuyingSecent> panicBuyingSecents = this.homePagePanicBuyingSecentDao.findByHomePagePanicBuyingFirstId(id);

        for(HomePagePanicBuyingSecent homePagePanicBuyingSecent: panicBuyingSecents){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", homePagePanicBuyingSecent.getId());
            Goods one2 = this.goodsService.findById(homePagePanicBuyingSecent.getGoodsId());
            jsonObjectSecent.put("goodsId",one2.getId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("panicBuyingSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<HomePagePanicBuying> panList = this.homePagePanicBuyingDao.findAll();

        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for(HomePagePanicBuying pan: panList) {
            jsonObject.put("id", pan.getId());
//        jsonObject.put("name", pan.getName());
            jsonObject.put("activityId", pan.getActivityId());
            jsonObject.put("status", pan.getStatus());

            List<HomePagePanicBuyingSecent> panicBuyingSecents = this.homePagePanicBuyingSecentDao.findByHomePagePanicBuyingFirstId(pan.getId());

            for (HomePagePanicBuyingSecent homePagePanicBuyingSecent : panicBuyingSecents) {
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", homePagePanicBuyingSecent.getId());
                Goods one2 = this.goodsService.findById(homePagePanicBuyingSecent.getGoodsId());
                jsonObjectSecent.put("goodsId", one2.getId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonArray.add(jsonObjectSecent);
            }
            jsonObject.put("panicBuyingSecent", jsonArray);
        }
        return jsonObject;
    }

//    public Object getShuffling(){
//        List<HomePagePanicBuying> list = homePagePanicBuyingDao.findAll();
//        JSONObject jsonObject11 = new JSONObject();
//        JSONArray jsonArrayResult = new JSONArray();
//        for(HomePagePanicBuying one:list) {
//            JSONObject jsonObjectResult = new JSONObject();
//            Integer activityId = one.getActivityId();
//            HttpBean httpRequest = new HttpBean(ConstantData.activity + "/web/activity", RequestMethod.get);
//            httpRequest.addParameter("activityId", activityId);
//            httpRequest.addParameter("pageSize", "6");
//            httpRequest.run();
//            String temp = httpRequest.getResponseContent();
//            String cd = "";
//            try {
//                cd = ValueUtil.getFromJson(temp, "code");
//            } catch (ExceptionThread e) {
//                return "erro";
//            }
//            if (!"200".equals(cd) || ValueUtil.isEmpity(cd)) {
//                return "erro";
//            } else {
//                String result = ValueUtil.getFromJson(temp, "data", "goodsList", "content");
//                JSONArray jsonArray = JSONArray.parseArray(result);
//                JSONArray jsonArrayRe = new JSONArray();
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    JSONObject jsonObjectRe = new JSONObject();
//                    jsonObjectRe.put("id", jsonObject.get("goodsId"));
//                    jsonObjectRe.put("salePrice", jsonObject.get("salePrice"));
//                    jsonObjectRe.put("price", jsonObject.get("price"));
//                    jsonObjectRe.put("goodsName", jsonObject.get("goodsName"));
//                    jsonObjectRe.put("goodsImageUrl", jsonObject.get("goodsImageUrl"));
//                    jsonArrayRe.add(jsonObjectRe);
//                }
//
//                jsonObjectResult.put("goods", jsonArrayRe);
//                List<ActivityColumn> byActivityId = this.activityColumnDao.findByActivityId(activityId);
//                JSONArray jsonArrayAc = new JSONArray();
//
//                Map<Integer, Integer> map = new HashMap<>();
//                for(int i=0;i<byActivityId.size();i++){
//                    map.put(byActivityId.get(i).getActivityFirstId(), byActivityId.get(i).getActivityFirstId());
//                }
//
//                for(Integer activityFirstId:map.keySet()){
//                    JSONObject jsonObjectAc = new JSONObject();
//                    jsonObjectAc.put("id", activityFirstId);
//                    jsonArrayAc.add(jsonObjectAc);
//                }
//
//                jsonObjectResult.put("activityPage", jsonArrayAc);
//                jsonArrayResult.add(jsonObjectResult);
//
//                String resultDate = ValueUtil.getFromJson(temp, "data", "date");
//                JSONObject jsonObject1 = JSONObject.parseObject(resultDate);
//
//                jsonObject11.put("time", jsonObject1);
//                jsonObject11.put("goods", jsonArrayResult);
//            }
//        }
//        return jsonObject11;
//    }

    public JSONObject getShuffling(Integer pageNo, Integer pageSize) {
//        com.alibaba.fastjson.JSONObject first = new com.alibaba.fastjson.JSONObject();
////        com.alibaba.fastjson.JSONObject second = new com.alibaba.fastjson.JSONObject();
//        com.alibaba.fastjson.JSONArray firstArray = new com.alibaba.fastjson.JSONArray();
        List<HomePagePanicBuying> panicBuyingFirst=homePagePanicBuyingDao.findAll();//所有栏目
        for(HomePagePanicBuying p:panicBuyingFirst){
            if(p.getStatus()!=0){
                return new JSONObject();
            }
            com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
//            com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
//            String name=p.getName();
//            third.put("classifyName",name);
//            List<HomePagePanicBuyingSecent> list=homePagePanicBuyingSecentDao.findByHomePagePanicBuyingFirstId(p.getId());
//            for(HomePagePanicBuyingSecent j:list){
//                com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
//                Goods goods=goodsDao.findOne(j.getGoodsId());
//                fourth.put("goodPicture",goods.getPicture());
//                fourth.put("goodName",goods.getGoodsName());
//                fourth.put("goodsId",goods.getId());
//                fourth.put("goodsEnName",goods.getGoodsEnName());
//                fourth.put("price",goods.getPrice());
//                fourth.put("salePrice",goods.getSalePrice());
//                fourth.put("sold",goods.getSales());
////                HttpBean httpRequest = new HttpBean(ConstantData.evaluation + "/evaluation/comments/goodComments/itf", RequestMethod.get);
////                httpRequest.addParameter("type", 1);//type:1好评,2中评,3差评
////                httpRequest.addParameter("goodsId",goods.getId());
////                httpRequest.run();
////                String temp = httpRequest.getResponseContent();
////                String comment = ValueUtil.getFromJson(temp, "data", "comment");
////                String praise = ValueUtil.getFromJson(temp, "data", "praise");
//                fourth.put("comment",goods.getComments());//评论数量
//                fourth.put("praise",goods.getPraise());//好评率
//                scondArray.add(fourth);
//            }

            if(ValueUtil.isEmpity(pageNo)){
                pageNo = 1;
            }
            if(ValueUtil.isEmpity(pageSize)){
                pageSize = 6;
            }
            int page = pageNo.intValue();
            int size = pageSize.intValue();
//        Pageable pageable = (Pageable) new PageRequest(page, pageSize);
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            Pageable pageable = new PageRequest(page-1, size, sort);
//        return goodsDao.findAllOrderBySales(pageable);
            Page<HomePagePanicBuyingSecent> byLikeFirstId1 = homePagePanicBuyingSecentDao.findByHomePagePanicBuyingFirstId(p.getId(),pageable);
            JSONArray jsonArray1 = new JSONArray();
            for(HomePagePanicBuyingSecent homePagePanicBuyingSecent: byLikeFirstId1){
                JSONObject jsonObject1 = new JSONObject();
//                jsonObject1.put("id", homePagePanicBuyingSecent.getGoodsId());
                Goods goods = this.goodsService.findById(homePagePanicBuyingSecent.getGoodsId());
                jsonObject1.put("goodsImageUrl",goods.getPicture());
                jsonObject1.put("goodsName",goods.getGoodsName());
                jsonObject1.put("id",goods.getId());
                jsonObject1.put("goodsEnName",goods.getGoodsEnName());
                jsonObject1.put("price",goods.getPrice());
                jsonObject1.put("salePrice",goods.getSalePrice());
                jsonArray1.add(jsonObject1);
            }
            JSONObject jsonObjectRe = new JSONObject();
            jsonObjectRe.put("totalElements", byLikeFirstId1.getTotalElements());
            jsonObjectRe.put("totalPages", byLikeFirstId1.getTotalPages());
            jsonObjectRe.put("pageNo", pageNo);
            jsonObjectRe.put("pageSize", pageSize);
            jsonObjectRe.put("goods", jsonArray1);


            List<ActivityColumn> byActivityId = this.activityColumnDao.findByActivityId(p.getActivityId());
            JSONArray jsonArrayAc = new JSONArray();

            Map<Integer, Integer> map = new HashMap<>();
            for(int i=0;i<byActivityId.size();i++){
                map.put(byActivityId.get(i).getActivityFirstId(), byActivityId.get(i).getActivityFirstId());
            }

            for(Integer activityFirstId:map.keySet()){
                JSONObject jsonObjectAc = new JSONObject();
                jsonObjectAc.put("id", activityFirstId);
                jsonArrayAc.add(jsonObjectAc);
            }

            jsonObjectRe.put("activityPage", jsonArrayAc);


            third.put("goods",jsonObjectRe);

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

            JSONObject dataJson = new JSONObject();
            dataJson.put("day",day);
            dataJson.put("hour",hour);
            dataJson.put("min",min);
            dataJson.put("sec",sec);
            third.put("time", dataJson);

//            firstArray.add(third);
            return third;
        }
//        first.put("classify",firstArray);
        return new JSONObject();
    }

    @Override
    public String insert(Integer activityId, Integer status, String jsonString) {
        try{
            List<HomePagePanicBuying> all = this.homePagePanicBuyingDao.findAll();
            if(all.size()>0){
                return "抢购已有数据，只能编辑";
            }

            HomePagePanicBuying homePagePanicBuying = new HomePagePanicBuying();
            homePagePanicBuying.setActivityId(activityId);

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
                homePagePanicBuying.setStartTime(startDate);
                homePagePanicBuying.setEndTime(endDate);
            }else {
                return "没有此活动";
            }

            homePagePanicBuying.setStatus(status);
            this.homePagePanicBuyingDao.save(homePagePanicBuying);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    HomePagePanicBuyingSecent homePagePanicBuyingSecent = new HomePagePanicBuyingSecent();
                    homePagePanicBuyingSecent.setGoodsId(goodsId);
                    homePagePanicBuyingSecent.setHomePagePanicBuyingFirstId(homePagePanicBuying.getId());
                    List<HomePagePanicBuyingSecent> flashPurchaseSecent1 = this.homePagePanicBuyingSecentDao.findByGoodsIdAndHomePagePanicBuyingFirstId(goodsId, homePagePanicBuying.getId());
                    if(flashPurchaseSecent1.size()>0){
                        continue;
                    }else {
                        this.homePagePanicBuyingSecentDao.save(homePagePanicBuyingSecent);
                    }
                }
            }



        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String update(Integer id, Integer activityId, Integer status, String jsonString) {
        try{
            HomePagePanicBuying one = this.homePagePanicBuyingDao.findOne(id);
            if(activityId!=one.getActivityId()){
                this.homePagePanicBuyingSecentDao.deleteByHomePagePanicBuyingFirstId(id);

                one.setActivityId(activityId);

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
                    one.setStartTime(startDate);
                    one.setEndTime(endDate);
                }else {
                    return "没有此活动";
                }

                one.setStatus(status);
                this.homePagePanicBuyingDao.save(one);

                if(ValueUtil.notEmpity(jsonString)){
                    JSONArray jsonArray = JSON.parseArray(jsonString);
                    for(int i=0; i<jsonArray.size(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                        HomePagePanicBuyingSecent homePagePanicBuyingSecent = new HomePagePanicBuyingSecent();
                        homePagePanicBuyingSecent.setGoodsId(goodsId);
                        homePagePanicBuyingSecent.setHomePagePanicBuyingFirstId(id);
                        List<HomePagePanicBuyingSecent> flashPurchaseSecent1 = this.homePagePanicBuyingSecentDao.findByGoodsIdAndHomePagePanicBuyingFirstId(goodsId, id);
                        if(flashPurchaseSecent1.size()>0){
                            continue;
                        }else {
                            this.homePagePanicBuyingSecentDao.save(homePagePanicBuyingSecent);
                        }
                    }
                }

            }
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String delete(Integer id) {
        try{
            this.homePagePanicBuyingDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteGoods(Integer id) {
        try{
            this.homePagePanicBuyingSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
