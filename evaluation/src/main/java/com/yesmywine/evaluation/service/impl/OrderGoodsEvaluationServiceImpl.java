package com.yesmywine.evaluation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.evaluation.service.OrderGoodsEvaluationService;
import com.yesmywine.evaluation.service.UserBehaviorRecordService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.evaluation.bean.*;
import com.yesmywine.evaluation.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 评论
 * Created by light on 2017/2/10.
 */
@Service
@Transactional
public class OrderGoodsEvaluationServiceImpl extends BaseServiceImpl<OrderGoodsEvaluation, Integer> implements OrderGoodsEvaluationService {

    @Autowired
    private OrderGoodsEvaluationDao orderGoodsEvaluationRep;
    @Autowired
    private UserBehaviorRecordService userBehaviorRecordService;
//    @Autowired
//    private UserInformationDao userInformationDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsLableDao goodsLableDao;
    @Autowired
    private LableDao lableDao;
    @Autowired
    private ReplyDao replyDao;


    //插入
//    public String saveEvaluation(OrderGoodsEvaluation orderGoodsEvaluation,Integer goodsId,JSONObject userInfo)throws YesmywineException {
//        String userId=ValueUtil.getFromJson(userInfo.toJSONString(),"userId");
//        String userName=ValueUtil.getFromJson(userInfo.toJSONString(),"userName");
//        String userUrl=ValueUtil.getFromJson(userInfo.toJSONString(),"userUrl");
//        orderGoodsEvaluation.setUserId(Integer.parseInt(userId));
//        orderGoodsEvaluation.setUserName(userName);
//        orderGoodsEvaluation.setUserImage(userUrl);
//            orderGoodsEvaluation.setCreateTime(new Date());
//            orderGoodsEvaluation.setStatus(0);
//            orderGoodsEvaluation.setUseful(0);
//            orderGoodsEvaluation.setUseless(0);
//        orderGoodsEvaluation.setGoods(goodsDao.findOne(goodsId));
//        this.orderGoodsEvaluationRep.save(orderGoodsEvaluation);
//
//        if (null != orderGoodsEvaluation.getUseful() && 0 != orderGoodsEvaluation.getUseful()) {
//            UserBehaviorRecord userBehaviorRecord = new UserBehaviorRecord();
//            userBehaviorRecord.setUserId(Integer.valueOf(userId));
//            userBehaviorRecord.setAdviceId(orderGoodsEvaluation.getId());
//            userBehaviorRecord.setStatus(0);
//            this.userBehaviorRecordService.saveUserBehaviorRecord(userBehaviorRecord);
//        }
//
//        if (null != orderGoodsEvaluation.getUseless() && 0 != orderGoodsEvaluation.getUseless()) {
//            UserBehaviorRecord userBehaviorRecord = new UserBehaviorRecord();
//            userBehaviorRecord.setUserId(Integer.valueOf(userId));
//            userBehaviorRecord.setAdviceId(orderGoodsEvaluation.getId());
//            userBehaviorRecord.setStatus(1);
//            this.userBehaviorRecordService.saveUserBehaviorRecord(userBehaviorRecord);
//        }
//
////        if(null== goodsLableDao.findByGoodsIdAndLable(goodsId,lableDao.findOne(lableId))){
////            EvaluationLable evaluationLable=new EvaluationLable();
////            evaluationLable.setCount(1);
////            evaluationLable.setGoodsId(goodsId);
////            evaluationLable.setLable(lableDao.findOne(lableId));
////            goodsLableDao.save(evaluationLable);
////        }else {
////            EvaluationLable evaluationLable=goodsLableDao.findByGoodsIdAndLable(goodsId,lableDao.findOne(lableId));
////            evaluationLable.setCount(evaluationLable.getCount()+1);
////            goodsLableDao.save(evaluationLable);
////        }
//        String imgIds = orderGoodsEvaluation.getImage();
//        String goodImg = null;
//        if(ValueUtil.notEmpity(imgIds)){
//            String[] imgArr = imgIds.split(";");
//            Integer[] arr=new Integer[imgArr.length];
//            for(int i=0;i<imgArr.length;i++){
//                arr[i]=Integer.parseInt(imgArr[i]);
//            }
//            if(imgIds!=null&&!imgIds.equals("")){
//                try {
//                    goodImg = saveGoodsImg(orderGoodsEvaluation.getId(), arr);
//                } catch (YesmywineException e) {
//                    e.printStackTrace();
//                }
//            }
//            orderGoodsEvaluation.setImage(goodImg);
//            orderGoodsEvaluationRep.save(orderGoodsEvaluation);
//        }
//        return "success";
//    }
    //插入
    public String create(String jsonArray,JSONObject userInfo) throws YesmywineException {
        String userId = userInfo.get("id").toString();
        String userName = userInfo.getString("userName");
        String userImage = userInfo.getString("userImg");
        JsonParser jsonParser = new JsonParser();
        JsonArray arr = jsonParser.parse(jsonArray).getAsJsonArray();
        String orderNo=null;
        for (int f = 0; f < arr.size(); f++) {
            orderNo = arr.get(f).getAsJsonObject().get("orderNo").getAsString();
            String goodsId = arr.get(f).getAsJsonObject().get("goodsId").getAsString();
            String goodScore = arr.get(f).getAsJsonObject().get("goodScore").getAsString();
            String imgIds = arr.get(f).getAsJsonObject().get("image").getAsString();
            String evaluation = arr.get(f).getAsJsonObject().get("evaluation").getAsString();
            OrderGoodsEvaluation orderGoodsEvaluation = new OrderGoodsEvaluation();
            orderGoodsEvaluation.setUserId(Integer.parseInt(userId));
            orderGoodsEvaluation.setUserName(userName.substring(0,1)+"********"+userName.substring(userName.length()-1));
            orderGoodsEvaluation.setUserRealName(userName);
            orderGoodsEvaluation.setUserImage(userImage);
            orderGoodsEvaluation.setCreateTime(new Date());
            orderGoodsEvaluation.setStatus(0);
            orderGoodsEvaluation.setUseful(0);
            orderGoodsEvaluation.setUseless(0);
            orderGoodsEvaluation.setOrderNo(Long.parseLong(orderNo));
            Goods byGoodsId = goodsDao.findByGoodsId(Integer.parseInt(goodsId));
            orderGoodsEvaluation.setGoods(byGoodsId);
            orderGoodsEvaluation.setGoodsId(Integer.parseInt(goodsId));
            orderGoodsEvaluation.setGoodsName(byGoodsId.getName());
            orderGoodsEvaluation.setGoodScores(Integer.parseInt(goodScore));
            if(4<=Integer.parseInt(goodScore)){
                orderGoodsEvaluation.setAppType(0);
            }else if(2<=Integer.parseInt(goodScore)&&4>=Integer.parseInt(goodScore)){
                orderGoodsEvaluation.setAppType(1);
            }else if(2>Integer.parseInt(goodScore)){
                orderGoodsEvaluation.setAppType(2);
            }
            if(ValueUtil.notEmpity(imgIds)){
                orderGoodsEvaluation.setAppType(3);
            }
            orderGoodsEvaluation.setEvaluation(evaluation);
            orderGoodsEvaluationRep.save(orderGoodsEvaluation);
            if (null != orderGoodsEvaluation.getUseful() && 0 != orderGoodsEvaluation.getUseful()) {
                UserBehaviorRecord userBehaviorRecord = new UserBehaviorRecord();
                userBehaviorRecord.setUserId(Integer.valueOf(userId));
                userBehaviorRecord.setAdviceId(orderGoodsEvaluation.getId());
                userBehaviorRecord.setStatus(0);
                this.userBehaviorRecordService.saveUserBehaviorRecord(userBehaviorRecord);
            }

            if (null != orderGoodsEvaluation.getUseless() && 0 != orderGoodsEvaluation.getUseless()) {
                UserBehaviorRecord userBehaviorRecord = new UserBehaviorRecord();
                userBehaviorRecord.setUserId(Integer.valueOf(userId));
                userBehaviorRecord.setAdviceId(orderGoodsEvaluation.getId());
                userBehaviorRecord.setStatus(1);
                this.userBehaviorRecordService.saveUserBehaviorRecord(userBehaviorRecord);
            }
            String goodImg = null;
            if(ValueUtil.notEmpity(imgIds)){
                String[] imgArr = imgIds.split(";");
                Integer[] arr1=new Integer[imgArr.length];
                for(int i=0;i<imgArr.length;i++){
                    arr1[i]=Integer.parseInt(imgArr[i]);
                }
                if(imgIds!=null&&!imgIds.equals("")){
                    try {
                        goodImg = saveGoodsImg(orderGoodsEvaluation.getId(), arr1);
                    } catch (YesmywineException e) {
                        e.printStackTrace();
                    }
                }
                orderGoodsEvaluation.setImage(goodImg);
                orderGoodsEvaluationRep.save(orderGoodsEvaluation);
            }

        }
        String result= SynchronizeUtils.getResult(Dictionary.MALL_HOST,"/orders/goods/evaluate/itf", RequestMethod.post,"orderNo",orderNo);
        String code = JSON.parseObject(result).getString("code");
        if(!"201".equals(code)|| ValueUtil.isEmpity(code)){
            String msg=JSON.parseObject(result).getString("msg");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError(msg);
        }
        return "success";
    }

    private String saveGoodsImg(Integer goodsId, Integer[] imgIds) throws YesmywineException {
        try{
            HttpBean httpRequest = new HttpBean(com.yesmywine.util.basic.Dictionary.MALL_HOST+ "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "evaluation");
            httpRequest.addParameter("mId", goodsId);
            httpRequest.addParameter("type", "1");
            String ids = "";
            String imageIds = "";
            for (int i = 0; i < imgIds.length; i++) {
                if (i == 0) {
                    ids = ids + imgIds[i];
//                    imageIds=imageIds+imageId[i];
                } else {
                    ids = ids + "," + imgIds[i];
//                    imageIds=imageIds+":"+imageId[i];
                }
//                category.setImageId(imageIds);
                httpRequest.addParameter("id", ids);
            }
            httpRequest.run();
            String temp = httpRequest.getResponseContent();
            String cd = ValueUtil.getFromJson(temp, "code");
            if (!"201".equals(cd) || ValueUtil.isEmpity(cd)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("图片上传失败");
            } else {
                JSONArray maps = new JSONArray(imgIds.length);
                String result = ValueUtil.getFromJson(temp, "data");
                JsonParser jsonParser = new JsonParser();
                JsonArray image = jsonParser.parse(result).getAsJsonArray();
                for (int f = 0; f < image.size(); f++) {
                    String id = image.get(f).getAsJsonObject().get("id").getAsString();
                    String name = image.get(f).getAsJsonObject().get("name").getAsString();
                    com.alibaba.fastjson.JSONObject map1 = new com.alibaba.fastjson.JSONObject();
                    map1.put("id", id);
                    map1.put("name", name);
                    maps.add(map1);
                }

                String result1 =   maps.toJSONString().replaceAll( "\"", "\'");
                return result1;
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError("图片服务出现问题！");
        }
        return null;
    }
//修改
    public String updateEvaluation(String idList,Integer status) {
        String[] split = idList.split(",");
        List<OrderGoodsEvaluation> list = new ArrayList<>();
        for(String id:split){
            OrderGoodsEvaluation one = this.orderGoodsEvaluationRep.findOne(Integer.valueOf(id));
            one.setStatus(status);
            list.add(one);
        }
        this.orderGoodsEvaluationRep.save(list);
        return "success";
    }

    //删除
    public String delelteById(String[] ids) {
        for (String id : ids) {
            this.orderGoodsEvaluationRep.delete(Integer.parseInt(id));
        }
        return "success";
    }

    public String saveReply(Reply reply, Integer evaluationId){
        try {
            this.replyDao.save(reply);
//            List<Reply> replyList = new ArrayList<>();
//            Reply reply1 = new Reply();
//            reply1 = reply;
//            replyList.add(reply1);
            OrderGoodsEvaluation one = this.orderGoodsEvaluationRep.findOne(evaluationId);
            List<Reply> reply2 = one.getReply();
            reply2.add(reply);
            one.setReply(reply2);
            this.orderGoodsEvaluationRep.save(one);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "erro";
        }
        return "success";
    }


    public JSONObject getShuff(Integer type,Integer goodsId){
        Goods goods=goodsDao.findByGoodsId(goodsId);
        List<OrderGoodsEvaluation> max=orderGoodsEvaluationRep.findByGoodsAndStatus(goods,1);
        List<OrderGoodsEvaluation> first = null;
        switch (type){
            case 1: first=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(4,5,goods,1);break;
            case 2: first=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(2,3,goods,1);break;
            case 3: first=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(0,1,goods,1);break;
        }
        double shuff = 0;
        if(0!=max.size()) {
            shuff = (double) first.size() / max.size();
        }
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String s = df.format(shuff);//返回的是String类型
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("praise",s);
        jsonObject.put("comment",max.size());
        return jsonObject;
    }


    public JSONObject getShuff2(Integer goodsId){
        Goods goods=goodsDao.findByGoodsId(goodsId);
        List<OrderGoodsEvaluation> max=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(1,5,goods,1);
//        List<OrderGoodsEvaluation> first = null;

        List<OrderGoodsEvaluation> goodEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(4,5,goods,1);
        List<OrderGoodsEvaluation> generalEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(2,3,goods,1);
        List<OrderGoodsEvaluation> badEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(0,1,goods,1);

        double shuff1 = 0;
        double shuff2 = 0;
        double shuff3 = 0;
        if(0!=max.size()) {
            shuff1 = (double) goodEvaluation.size() / max.size();
            shuff2 = (double) generalEvaluation.size() / max.size();
            shuff3 = (double) badEvaluation.size() / max.size();
        }
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String s1 = df.format(shuff1);//返回的是String类型
        String s2 = df.format(shuff2);
        String s3 = df.format(shuff3);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("goodEvaluation",s1);
        jsonObject.put("generalEvaluation",s2);
        jsonObject.put("badEvaluation",s3);
        return jsonObject;
    }


    public JSONObject getShuffNum(Integer goodsId){
        Goods goods=goodsDao.findByGoodsId(goodsId);
//        List<OrderGoodsEvaluation> max=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoods(1,5,goods);
//        List<OrderGoodsEvaluation> first = null;
//        switch (type){
        List<OrderGoodsEvaluation> goodEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(4,5,goods,1);
        List<OrderGoodsEvaluation> generalEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(2,3,goods,1);
        List<OrderGoodsEvaluation> badEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(0,1,goods,1);
//        List<OrderGoodsEvaluation> allEvaluation=orderGoodsEvaluationRep.findByGoodScoresBetweenAndGoodsAndStatus(1,5,goods,1);
        List<OrderGoodsEvaluation> pictrueEvaluation=orderGoodsEvaluationRep.findByAppTypeAndGoodsIdAndStatus(3, goodsId,1);
//        }
//        double shuff=(double) first.size()/max.size();
//        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
//        String s = df.format(shuff);//返回的是String类型
        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("praise",s);
        jsonObject.put("goodEvaluation",goodEvaluation.size());
        jsonObject.put("generalEvaluation",generalEvaluation.size());
        jsonObject.put("badEvaluation",badEvaluation.size());
        jsonObject.put("allEvaluation",goodEvaluation.size()+generalEvaluation.size()+badEvaluation.size());
        jsonObject.put("pictrueEvaluation",pictrueEvaluation.size());
        return jsonObject;
    }


    public com.alibaba.fastjson.JSONArray getGoodByBuy(){  //刚刚购买过
        HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/orders/justBought", RequestMethod.get);
        httpRequest.run();
        String result = httpRequest.getResponseContent();
        String data = ValueUtil.getFromJson(result, "data");
        if(ValueUtil.isEmpity(data)){
            return new JSONArray();
        }
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        String []arr=data.split(",");
        for(int i=0;i<arr.length;i++){
            Goods goods=goodsDao.findByGoodsId(Integer.parseInt(arr[i]));
            List<OrderGoodsEvaluation> list=orderGoodsEvaluationRep.findByGoods(goods);
            goods.setComment(list.size());
            jsonArray.add(goods);
        }
        return jsonArray;
    }


    public com.alibaba.fastjson.JSONArray conditionAssessment(){//酒友品鉴
        List<Goods> list=orderGoodsEvaluationRep.findLength();
        Set<Goods> set=new HashSet<>();
//        Set<Integer> setList=new HashSet<>();
//        for(Integer goodsId:list){
//            setList.add(goodsId);
//        }
        if(list.size()>=3){
            for(Goods goods:list){
//                Goods goods1 = new Goods();
//                goods1.setId(goodsId);
                List<OrderGoodsEvaluation> listGoods=orderGoodsEvaluationRep.findByGoods(goods);
                if(listGoods.size()>=30){//评论条数大于几
                    set.add(goods);
                }
                if(set.size()==3){
                    break;
                }
            }
        }else {
            for (Goods goods : list) {
//                Goods goods1 = new Goods();
//                goods1.setId(goodsId);
                List<OrderGoodsEvaluation> listGoods = orderGoodsEvaluationRep.findByGoods(goods);
                if (listGoods.size() >=30) {//评论条数大于几
                    set.add(goods);
                }
            }
        }
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        for(Goods goods : set){
//            Goods goods1 = new Goods();
//            goods1.setId(str);
            List<OrderGoodsEvaluation> lists=orderGoodsEvaluationRep.findByGoods(goods);
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            Goods goods1=goodsDao.findByGoodsId(goods.getGoodsId());
            goods1.setComment(lists.size());
//            jsonObject.put("goods",goods);
            jsonArray.add(goods1);
        }

        return jsonArray;
    }


    public com.alibaba.fastjson.JSONArray getGoodComment(){//刚刚被好评
        List<Integer> list=orderGoodsEvaluationRep.findGoodComment();
        Set<Goods> set=new HashSet<>();
        List<String> list2=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            Goods goodsIds=orderGoodsEvaluationRep.findOne(list.get(i)).getGoods();
            set.add(goodsIds);
            String ev=orderGoodsEvaluationRep.findOne(list.get(i)).getEvaluation();
            list2.add(ev);
            if(set.size()==3){
                break;
            }
        }
        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        int i=0;
        for(Goods str:set){
            List<OrderGoodsEvaluation> list1=orderGoodsEvaluationRep.findByGoods(str);
            com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
            Goods goods=goodsDao.findByGoodsId(str.getGoodsId());
            goods.setComment(list1.size());
            goods.setEvaluation(list2.get(i));
            i=i+1;
//            jsonObject.put("goods",goods);
            jsonArray.add(goods);
        }
        return jsonArray;
    }
}
