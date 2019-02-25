package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.AdverEntity;
import com.yesmywine.cms.entity.AdverPositionEntity;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.service.AdverService;
import com.yesmywine.cms.service.PositionService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yly on 2017/2/10.
 */
@Service
@Transactional
public class AdverServiceImpl extends BaseServiceImpl<AdverEntity, Integer> implements AdverService {
    @Autowired
    private AdverDao adverDao;
    @Autowired
    private AdverPositionDao adverPositionDao;
    @Autowired
    private PositionService positionService;
    @Autowired
    private ActivityFirstDao activityFirstDao;
    @Autowired
    private  GoodsDao goodsDao;
    @Autowired
    private CategoryDao categoryDao;



    private String saveImg(Integer mId, Integer[] imgIds) throws YesmywineException {
        try{
            HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/fileUpload/tempToFormal/itf", RequestMethod.post);
            httpRequest.addParameter("module", "adver");
            httpRequest.addParameter("mId", mId);
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


    /**
     * 新增广告素材
     *
//     * @param adverVo
     * @return
     */
    @Override
    public String saveAdver(Map<String, String> params) {


        //保存广告素材表
        AdverEntity adverEntity = new AdverEntity();
        adverEntity.setAdverName(params.get("adverName"));
        adverEntity.setRemark(params.get("remark"));
        adverEntity.setWidth(Integer.valueOf(params.get("width")));
        adverEntity.setHeight(Integer.valueOf(params.get("height")));
        adverEntity.setInOrOut(Integer.valueOf(params.get("inOrOut")));
        adverEntity.setRelevancy(params.get("relevancy"));
        String startTime = params.get("startTime");
        Date dateStartTime = DateUtil.toDate(startTime, "yyyy-mm-dd hh:mm:ss");
        adverEntity.setStartTime(dateStartTime);
        String endTime = params.get("endTime");
        Date dateEndTime = DateUtil.toDate(endTime, "yyyy-mm-dd hh:mm:ss");
        adverEntity.setEndTime(dateEndTime);
        //判断名称
        AdverEntity adverEntity1 = adverDao.findByAdverName(params.get("adverName"));
        if(ValueUtil.notEmpity(adverEntity1)){
            return "名称已被使用";
        }
        if(ValueUtil.notEmpity(params.get("relevancyType"))){
            adverEntity.setRelevancyType(Integer.valueOf(params.get("relevancyType")));
        }
        adverDao.save(adverEntity);
        if(ValueUtil.notEmpity(params.get("imgIds"))){
            try{
                String imgIds = params.get("imgIds");
                String[] imgArr = imgIds.split(";");
                Integer[] arr=new Integer[imgArr.length];
                for(int i=0;i<imgArr.length;i++){
                    arr[i]=Integer.parseInt(imgArr[i]);
                }
                String img = null;
                if(imgIds!=null&&!imgIds.equals("")){
                    try {
                        img = saveImg(adverEntity.getId(), arr);
                    } catch (YesmywineException e) {
                        e.printStackTrace();
                    }
                }
                adverEntity.setImage(img);
                adverDao.save(adverEntity);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return "erro";
            }
        }
            return "success";

    }


    /**
     * 根据广告素材ID删除广告素材
     *
//     * @param adverVo
     */
    @Override
    public String delete(Integer adverId) throws Exception{
        try{
            AdverEntity adverEntity = new AdverEntity();
            adverEntity.setId(adverId);
            List<AdverPositionEntity> byAdverEntity = this.adverPositionDao.findByAdverEntity(adverEntity);
            Boolean flag = true;
            for(AdverPositionEntity adverPositionEntity: byAdverEntity){
                Integer id = adverPositionEntity.getPositionEntity().getId();
                flag = this.positionService.used(id);
            }
            if(!flag){
                return "已被使用，不能删除";
            }
            this.adverDao.delete(adverId);
            for(AdverPositionEntity adverPositionEntity: byAdverEntity){
                this.adverPositionDao.delete(adverPositionEntity);
            }
        }catch (Exception e){
            throw e;
        }
        return "success";

    }


    @Override
    public String update(Map<String, String> params) {
        //保存广告素材表
        AdverEntity adverEntity = new AdverEntity();
        adverEntity.setAdverName(params.get("adverName"));
        adverEntity.setRemark(params.get("remark"));
        adverEntity.setWidth(Integer.valueOf(params.get("width")));
        adverEntity.setHeight(Integer.valueOf(params.get("height")));
        adverEntity.setInOrOut(Integer.valueOf(params.get("inOrOut")));
        adverEntity.setRelevancy(params.get("relevancy"));
        String startTime = params.get("startTime");
        Date dateStartTime = DateUtil.toDate(startTime, "yyyy-mm-dd hh:mm:ss");
        adverEntity.setStartTime(dateStartTime);
        String endTime = params.get("endTime");
        Date dateEndTime = DateUtil.toDate(endTime, "yyyy-mm-dd hh:mm:ss");
        adverEntity.setEndTime(dateEndTime);
        adverEntity.setId(Integer.valueOf(params.get("id")));
        Integer id = Integer.valueOf(params.get("id"));
        AdverEntity adverEntity1 = adverDao.findByAdverName(params.get("adverName"));
        if(ValueUtil.notEmpity(adverEntity1)){
            if(adverEntity1.getId()!=id){
                return "名称已被使用";
            }
        }
        if(ValueUtil.notEmpity(params.get("relevancyType"))){
            adverEntity.setRelevancyType(Integer.valueOf(params.get("relevancyType")));
        }
        if(ValueUtil.notEmpity(params.get("imgIds"))){
            try{
                String imgIds = params.get("imgIds");
                String[] imgArr = imgIds.split(";");
                Integer[] arr=new Integer[imgArr.length];
                for(int i=0;i<imgArr.length;i++){
                    arr[i]=Integer.parseInt(imgArr[i]);
                }
                String img = null;
                if(imgIds!=null&&!imgIds.equals("")){
                    try {
                        img = saveImg(adverEntity.getId(), arr);
                    } catch (YesmywineException e) {
                        e.printStackTrace();
                    }
                }
                adverEntity.setImage(img);
                adverDao.save(adverEntity);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return "erro";
            }
        }
        return "success";
    }

    @Override
    public Object page(String adverId) throws YesmywineException {
        AdverEntity adver = adverDao.findOne(Integer.valueOf(adverId));
        String relevancy = adver.getRelevancy();
        Integer relevancyType = adver.getRelevancyType();
        JSONObject jsonObject =ValueUtil.toJsonObject(adver);
        String name=null;
        if(relevancyType==1){//1是活动页id
           name = activityFirstDao.findOne(Integer.valueOf(relevancy)).getName();
        }else if(relevancyType==2){//2是商品id
            Goods goods = goodsDao.findOne(Integer.valueOf(relevancy));
            name =goods.getGoodsName();
            jsonObject.put("goods",goods);
        }else if(relevancyType==3){//3是分类id
            String [] ls= relevancy.split(",");
            String cataId = ls[ls.length-1];
            name = categoryDao.findOne(Integer.valueOf(cataId)).getCategoryName();
        }else if(relevancyType==0){
            name="url地址";
        }
        jsonObject.put("name",name);
        return jsonObject;
    }


}
