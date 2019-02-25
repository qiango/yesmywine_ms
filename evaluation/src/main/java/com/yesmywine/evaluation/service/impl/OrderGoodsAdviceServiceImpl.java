package com.yesmywine.evaluation.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.evaluation.bean.Goods;
import com.yesmywine.evaluation.bean.OrderGoodsAdvice;
import com.yesmywine.evaluation.bean.Reply;
import com.yesmywine.evaluation.bean.UserBehaviorRecord;
import com.yesmywine.evaluation.dao.GoodsDao;
import com.yesmywine.evaluation.dao.OrderGoodsAdviceDao;
import com.yesmywine.evaluation.dao.ReplyDao;
import com.yesmywine.evaluation.service.OrderGoodsAdviceService;
import com.yesmywine.evaluation.service.UserBehaviorRecordService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.hzbuvi.evaluation.dao.UserInformationDao;

/**
 * 咨询
 * Created by light on 2017/2/10.
 */
@Service
public class OrderGoodsAdviceServiceImpl extends BaseServiceImpl<OrderGoodsAdvice, Integer> implements OrderGoodsAdviceService {

    @Autowired
    private OrderGoodsAdviceDao orderGoodsAdviceRep;
    @Autowired
    private UserBehaviorRecordService userBehaviorRecordService;
//    @Autowired
//    private UserInformationDao userInformationDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ReplyDao replyDao;

    //插入和修改
    public String saveAdvice(OrderGoodsAdvice orderGoodsAdvice, Integer goodsId,JSONObject userInfo) {
//        String userId=ValueUtil.getFromJson(userInfo.toJSONString(),"id");
//        String userName=ValueUtil.getFromJson(userInfo.toJSONString(),"userName");
        String userId = userInfo.get("id").toString();
        String userName = userInfo.get("userName").toString();
//        String userUrl=ValueUtil.getFromJson(userInfo.toJSONString(),"userUrl");
        orderGoodsAdvice.setUserName(userName.substring(0,1)+"********"+userName.substring(userName.length()-1));
//        orderGoodsAdvice.setUserName(userName);
        orderGoodsAdvice.setUserId(Integer.parseInt(userId));
//        orderGoodsAdvice.setUserImage(userUrl);
        if (null == orderGoodsAdvice.getId()) {
            orderGoodsAdvice.setCreateTime(new Date());
            orderGoodsAdvice.setStatus(0);
            orderGoodsAdvice.setSatisfaction(0);
            orderGoodsAdvice.setDiscontent(0);
        }

        if (null != orderGoodsAdvice.getSatisfaction() && 0 != orderGoodsAdvice.getSatisfaction()) {
            UserBehaviorRecord userBehaviorRecord = new UserBehaviorRecord();
            userBehaviorRecord.setUserId(Integer.valueOf(userId));
            userBehaviorRecord.setAdviceId(orderGoodsAdvice.getId());
            userBehaviorRecord.setStatus(0);
            this.userBehaviorRecordService.saveUserBehaviorRecord(userBehaviorRecord);
        }

        if (null != orderGoodsAdvice.getDiscontent() && 0 != orderGoodsAdvice.getDiscontent()) {
            UserBehaviorRecord userBehaviorRecord = new UserBehaviorRecord();
            userBehaviorRecord.setUserId(Integer.valueOf(userId));
            userBehaviorRecord.setAdviceId(orderGoodsAdvice.getId());
            userBehaviorRecord.setStatus(1);
            this.userBehaviorRecordService.saveUserBehaviorRecord(userBehaviorRecord);
        }
        Goods one = goodsDao.findByGoodsId(goodsId);
        orderGoodsAdvice.setGoods(one);
        orderGoodsAdvice.setGoodsId(one.getGoodsId());
        orderGoodsAdvice.setGoodsName(one.getName());
        this.orderGoodsAdviceRep.save(orderGoodsAdvice);
        String imgIds = orderGoodsAdvice.getImage();
        String goodImg = null;
        if(ValueUtil.notEmpity(imgIds)){
            String[] imgArr = imgIds.split(";");
            Integer[] arr=new Integer[imgArr.length];
            for(int i=0;i<imgArr.length;i++){
                arr[i]=Integer.parseInt(imgArr[i]);
            }
            if(imgIds!=null&&!imgIds.equals("")){
                try {
                    goodImg = saveGoodsImg(orderGoodsAdvice.getId(), arr);
                } catch (YesmywineException e) {
                    e.printStackTrace();
                }
            }
            orderGoodsAdvice.setImage(goodImg);
            orderGoodsAdviceRep.save(orderGoodsAdvice);
        }
        return "success";
    }

    private String saveGoodsImg(Integer orderGoodsAdviceId, Integer[] imgIds) throws YesmywineException {
        try{
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "evaluation");
            httpRequest.addParameter("mId", orderGoodsAdviceId);
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

//                com.alibaba.fastjson.JSONObject map = new com.alibaba.fastjson.JSONObject();
//                for (int i = 0; i < maps.size(); i++) {
//                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) maps.get(i);
//                    map.put("id" + i, jsonObject.getString("id"));
//                    map.put("name" + i, jsonObject.getString("name"));
//                }
//                map.put("num", String.valueOf(maps.size()));
                return result1;
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError("图片服务出现问题！");
        }
        return null;
    }

    @Override
    public String updateAdvice(String idList,Integer status) {
        String[] split = idList.split(",");
        List<OrderGoodsAdvice> list = new ArrayList<>();
        for(String id:split) {
            OrderGoodsAdvice one = this.orderGoodsAdviceRep.findOne(Integer.valueOf(id));
            one.setStatus(status);
            list.add(one);
        }
        this.orderGoodsAdviceRep.save(list);
        return "success";
    }

    //删除
    public String delelteById(String[] ids) {
        for (String id : ids) {
            this.orderGoodsAdviceRep.delete(Integer.parseInt(id));
        }
        return "success";
    }


    public String saveReply(Reply reply, Integer adviceId){
        try {
            this.replyDao.save(reply);
//            List<Reply> replyList = new ArrayList<>();
//            Reply reply1 = new Reply();
//            reply1 = reply;
//            replyList.add(reply1);
            OrderGoodsAdvice one = this.orderGoodsAdviceRep.findOne(adviceId);
            List<Reply> reply2 = one.getReply();
            reply2.add(reply);
            one.setReply(reply2);
            this.orderGoodsAdviceRep.save(one);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "erro";
        }
        return "success";
    }

}
