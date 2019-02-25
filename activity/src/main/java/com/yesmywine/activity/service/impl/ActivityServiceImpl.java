package com.yesmywine.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.entity.*;
import com.yesmywine.activity.ifttt.dao.ActivityDao;
import com.yesmywine.activity.ifttt.dao.ActivityTypeDao;
import com.yesmywine.activity.ifttt.service.*;
import com.yesmywine.activity.service.*;
import com.yesmywine.activity.thread.ChangeGoosInfoThread;
import com.yesmywine.activity.thread.RestoreInventoryThread;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.date.DateUtil;
import com.yesmywine.util.error.YesmywineException;
import net.sf.ehcache.CacheManager;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by SJQ on 2017/2/14.
 */
@Service
@Transactional
public class ActivityServiceImpl extends BaseServiceImpl<Activity, Integer> implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private RegulationService regulationService;
    @Autowired
    private IftttConfigService iftttConfigService;
    @Autowired
    private RegulationGoodsService regulationGoodsService;
    @Autowired
    private ActivityGoodsService activityGoodsService;
    @Autowired
    private ActivityTypeDao activityTypeDao;
    @Autowired
    private UseService useService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private EntityManager entityManager;

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";


    @Override
    public String createActivity(Map<String, String> param, HttpServletRequest request) throws YesmywineException {//增加活动
        ValueUtil.verify(param, new String[]{"name", "startTime", "endTime", "priority",
                "activityType", "typeName", "goodsType", "goodsJson"});
        String username = UserUtils.getUserName(request);
        Activity activity = new Activity();
        activity.setName(param.get("name"));
        activity.setComment(param.get("comment"));
        activity.setStatus(ActivityStatus.notCurrent);
        String st = param.get("startTime");
        Date dateStart = DateUtil.toDate(st, dateFormat);
        if (new Date().getTime() > dateStart.getTime()) {
            ValueUtil.isError("开始时间不能小于当前时间");
        }
        activity.setStartTime(dateStart);
        String et = param.get("endTime");
        Date datEnd = DateUtil.toDate(et, dateFormat);
        if (dateStart.getTime() > datEnd.getTime()) {
            ValueUtil.isError("结束时间不能小于开始时间");
        }
        activity.setEndTime(datEnd);
        activity.setIsDelete(DeleteEnum.NOT_DELETE);
        String type = param.get("activityType");
        ActivityType activityType = activityTypeDao.findByCode(type);
        String typeName = param.get("typeName");
        activity.setTypeName(typeName);
        activity.setTypeAlias(activityType.getAlias());
        activity.setType(type);
        String[] arr = type.split("-");
        String triggerCode = arr[0];
        activity.setTriggerCode(triggerCode);
        String actionCode = arr[1];
        activity.setActionCode(actionCode);
        activity.setMember(Boolean.valueOf(param.get("isMember")));
        if (actionCode.split("_").length > 1) {
            activity.setShare(true);
        } else {
            activity.setShare(false);
        }
        activity.setPriority(Integer.valueOf(param.get("priority")));
        activity.setAuditStatus(3);//草稿
        activity.setCreator(username);
        activityDao.save(activity);
        String goodsType = param.get("goodsType");
        if (goodsType.equals("category")) {
            return saveCategoryGoods(activity, param);
        } else if (goodsType.equals("brand")) {
            return saveBrandGoods(activity, param);
        } else {
            return saveSingleGoods(activity, param);
        }
    }

    private String checkGoodsIsJoinShareActivity(Activity activity, String[] goodsIds, List<ActivityGoods> AGList, List<RegulationGoods> regulationGoodsList) throws YesmywineException {
        List<Activity> activityList = activityDao.findByStatusNot(ActivityStatus.overdue);//找出所有未过时的活动
        List<Activity> startGtEndActivityList = getStartTimeGtEndTime(activity.getEndTime());
        List<Activity> endGtStartActivityList = getEndTimeLtStartTime(activity.getStartTime());
        List<Activity> collectActivityList = new ArrayList<>();
        collectActivityList.addAll(startGtEndActivityList);
        collectActivityList.addAll(endGtStartActivityList);
        List<Activity> overlapActivity = new ArrayList<>();
        List<Integer> shareActivityIds = new ArrayList<>();

        for (Activity activitySingle : activityList) {
            if (!collectActivityList.contains(activitySingle)) {
                overlapActivity.add(activitySingle);
                if (activitySingle.getShare()) {
                    shareActivityIds.add(activitySingle.getId());
                }
            }
        }

        if (shareActivityIds.size() > 0) {//有时间重叠的活动，判断商品是否已参加该活动
            List<String> goodsIdList = activityGoodsService.checkGoodsIsJoinShareActivity(shareActivityIds, goodsIds);//是否参加共享活动
            return failReason(activity, goodsIdList, AGList,regulationGoodsList);
        }

        return "SUCCESS";
    }

    public String checkGoodsIsJoinOtherActivity(Activity activity, String[] goodsIds, List<ActivityGoods> AGList, List<RegulationGoods> regulationGoodsList) throws YesmywineException {
        List<Activity> activityList = activityDao.findByStatusNot(ActivityStatus.overdue);//找出所有未过时的活动
        List<Activity> startGtEndActivityList = getStartTimeGtEndTime(activity.getEndTime());
        List<Activity> endGtStartActivityList = getEndTimeLtStartTime(activity.getStartTime());
        List<Activity> collectActivityList = new ArrayList<>();
        collectActivityList.addAll(startGtEndActivityList);
        collectActivityList.addAll(endGtStartActivityList);
        List<Activity> overlapActivity = new ArrayList<>();
        List<Integer> otherActivityIds = new ArrayList<>();

        for (Activity activitySingle : activityList) {
            if (!collectActivityList.contains(activitySingle)) {
                overlapActivity.add(activitySingle);
                otherActivityIds.add(activitySingle.getId());
            }
        }

        if (otherActivityIds.size() > 0) {
            List<String> goodsIdList = activityGoodsService.checkGoodsIsJoinOtherActivity(otherActivityIds, goodsIds);
            return failReason(activity, goodsIdList, AGList, regulationGoodsList);
        }
        return "SUCCESS";
    }

    private String failReason(Activity activity, List<String> goodsIdList, List<ActivityGoods> AGList, List<RegulationGoods> regulationGoodsList) throws YesmywineException {
        if (goodsIdList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            List<GoodsMirroring> goodsList = goodsService.findByGoodsIdIn(goodsIdList);
            sb.append("以下商品已参加其他活动，被自动过滤掉：\\n");
            for (GoodsMirroring goods : goodsList) {
                ActivityGoods activityGoods = new ActivityGoods();
                activityGoods.setActivityId(activity.getId());
                activityGoods.setGoods(goods);
                activityGoods.setCreateTime(null);

                for(int i=0;i<AGList.size();i++){
                    ActivityGoods AGGoods = AGList.get(i);
                    if(AGGoods.getGoods().equals(goods)&&AGGoods.getActivityId().equals(activity.getId())){

                        for(int m=0;m<regulationGoodsList.size();m++ ){
                            RegulationGoods regulationGoods = regulationGoodsList.get(m);
                            if(activityGoods.getGoods().getGoodsId().equals(regulationGoods.getTargetId().toString())){
                                regulationGoodsList.remove(regulationGoods);
                            }
                        }
                        AGList.remove(AGGoods);
                    }
                }

//                if (AGList.contains(activityGoods)) {
//                    for(RegulationGoods regulationGoods:regulationGoodsList){
//                        if(activityGoods.getGoods().getGoodsId().equals(regulationGoods.getTargetId().toString())){
//                            regulationGoodsList.remove(regulationGoods);
//                        }
//                    }
//                    AGList.remove(activityGoods);
//                }
                sb.append(goods.getGoodsName() + "\\n");
            }
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            ValueUtil.isError(sb.toString());
            return sb.toString();
        }
        return "SUCCESS";
    }

    private String saveSingleGoods(Activity activity, Map<String, String> param) throws YesmywineException {
        JSONArray goodsArray = JSON.parseArray(param.get("goodsJson"));
        List<RegulationGoods> regulationGoodsList = new ArrayList<>();
        List<GoodsMirroring> activityGoodsList = new ArrayList<>();
        List<String> goodsIdList = new ArrayList<>();
        for (int i = 0; i < goodsArray.size(); i++) {
            RegulationGoods regulationGoods = new RegulationGoods();
            JSONObject goodsObject = (JSONObject) goodsArray.get(i);
            String goodsId = String.valueOf(goodsObject.get("targetId"));
            String targetName = String.valueOf(goodsObject.get("targetName"));
            GoodsMirroring goods = goodsService.findById(goodsId);
            if (goods == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError("活动服务无名称为：" + targetName + " 的商品");
            }
            Double originPrice = goodsObject.getString("originPrice") == null ? null : Double.valueOf(goodsObject.getString("originPrice"));
            Double activityPrice = goodsObject.getString("activityPrice") == null ? null : Double.valueOf(goodsObject.getString("activityPrice"));
            Integer rushCount = goodsObject.getString("count") == null ? null : Integer.valueOf(goodsObject.getString("count"));
            if(activity.getActionCode().equals("rushPurA")){
                ValueUtil.verify(originPrice,"originPrice");
                ValueUtil.verify(activityPrice,"activityPrice");
                ValueUtil.verify(rushCount,"count");
            }

            regulationGoods.setIsDelete(DeleteEnum.NOT_DELETE);
            regulationGoods.setTargetId(Integer.valueOf(goodsId));
            regulationGoods.setWare(WareEnum.Main);
            regulationGoods.setActivityId(activity.getId());
            regulationGoods.setStatus(ActivityStatus.notCurrent);
            regulationGoods.setOriginPrice(originPrice);
            regulationGoods.setActivityPrice(activityPrice);
            regulationGoods.setAllLimitCount(rushCount);
            if (activity.getType().equals("nullT-rushPurA")) {//判断该商品在同一时间段内是否已参加抢购
                goodsIdList.add(goodsId);
            }
            regulationGoodsList.add(regulationGoods);
            activityGoodsList.add(goods);
        }
        if(goodsIdList.size()>0){
            isJoinOtherRushPur(activity,goodsIdList);
        }
        List<ActivityGoods> existedGoodsList = activityGoodsService.findByActivityId(activity.getId());
        List<ActivityGoods> AGList = new ArrayList<>();
        for (GoodsMirroring goods : activityGoodsList) {
            ActivityGoods activityGoods = new ActivityGoods();
            activityGoods.setActivityId(activity.getId());
            activityGoods.setGoods(goods);
            if (!existedGoodsList.contains(activityGoods)) {
                AGList.add(activityGoods);
            }
        }
        if(AGList.size()>0&&regulationGoodsList.size()>0){
            String result = checkGoodsActivityRestraint(activity, AGList,regulationGoodsList);
            activityGoodsService.save(AGList);
            regulationGoodsService.save(regulationGoodsList);
            return result;
        }
        return "SUCCESS";
    }

    private String saveBrandGoods(Activity activity, Map<String, String> param) throws YesmywineException {
        JSONArray brandArray = JSON.parseArray(param.get("goodsJson"));
        List<RegulationGoods> regulationGoodsList = new ArrayList<>();
        List<ActivityGoods> AGList = new ArrayList<>();
        for (int i = 0; i < brandArray.size(); i++) {
            JSONObject brandObject = (JSONObject) brandArray.get(i);
            String brandId = brandObject.getString("targetId");
            String targetName = brandObject.getString("targetName");
            List<GoodsMirroring> goodsList = goodsService.findByBrandIdAndGoStatusAndSaleModelAndGoodsTypeIn(brandId);
            if (goodsList.size() < 1) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ValueUtil.isError(" 品牌：" + targetName + " 下无商品数据");
            }
            List<ActivityGoods> existedGoodsList = activityGoodsService.findByActivityId(activity.getId());
            addRegulationGoodsAndActivityGoods(activity, goodsList, existedGoodsList, regulationGoodsList, AGList);
        }
        //活动商品约束校验
        if(AGList.size()>0&&regulationGoodsList.size()>0){
            String result = checkGoodsActivityRestraint(activity, AGList,regulationGoodsList);
            activityGoodsService.save(AGList);
            regulationGoodsService.save(regulationGoodsList);
            return result;
        }
        return "SUCCESS";
    }

    private void addRegulationGoodsAndActivityGoods(Activity activity, List<GoodsMirroring> goodsList, List<ActivityGoods> existedGoodsList, List<RegulationGoods> regulationGoodsList, List<ActivityGoods> AGList) {
        for (GoodsMirroring goods : goodsList) {
            RegulationGoods regulationGoods = new RegulationGoods();
            regulationGoods.setIsDelete(DeleteEnum.NOT_DELETE);
            regulationGoods.setTargetId(Integer.valueOf(goods.getGoodsId()));
            regulationGoods.setWare(WareEnum.Main);
            regulationGoods.setActivityId(activity.getId());
            regulationGoods.setStatus(ActivityStatus.notCurrent);
            regulationGoodsList.add(regulationGoods);

            ActivityGoods activityGoods = new ActivityGoods();
            activityGoods.setActivityId(activity.getId());
            activityGoods.setGoods(goods);
            if (!existedGoodsList.contains(activityGoods)) {
                AGList.add(activityGoods);
            }
        }
    }

    private String checkGoodsActivityRestraint(Activity activity, List<ActivityGoods> AGList, List<RegulationGoods> regulationGoodsList) throws YesmywineException {
        //如果创建的是共享活动，判断同时间段内商品是否已参加活动
        if (activity.getShare()) {
            String[] goodsIds = new String[AGList.size()];
            for (int k = 0; k < AGList.size(); k++) {
                ActivityGoods goodsObject = AGList.get(k);
                String goodsId = goodsObject.getGoods().getGoodsId();
                goodsIds[k] = goodsId;
            }
            return checkGoodsIsJoinOtherActivity(activity, goodsIds, AGList,regulationGoodsList);//判断商品相同时间段内是否参加了互斥活动
        } else {//若不是，判断相同时间内是否参加共享活动
            String[] goodsIds = new String[AGList.size()];
            for (int k = 0; k < AGList.size(); k++) {
                RegulationGoods regulationGoods = new RegulationGoods();
                ActivityGoods goodsObject = AGList.get(k);
                String goodsId = goodsObject.getGoods().getGoodsId();
                goodsIds[k] = goodsId;
            }
            return checkGoodsIsJoinShareActivity(activity, goodsIds, AGList,regulationGoodsList);//判断商品相同时间段内是否参加了共享活动
        }
    }

    private String saveCategoryGoods(Activity activity, Map<String, String> param) throws YesmywineException {
        JSONArray categorArray = JSON.parseArray(param.get("goodsJson"));
        List<ActivityGoods> AGList = new ArrayList<>();
        List<RegulationGoods> regulationGoodsList = new ArrayList<>();
        for (int i = 0; i < categorArray.size(); i++) {
            JSONObject categoryObject = (JSONObject) categorArray.get(i);
            Integer categoryParentId = Integer.valueOf(categoryObject.getString("targetId"));
            String targetName = categoryObject.getString("targetName");
            Map<String, Object> map = new HashMap<>();
            map.put("parentId", categoryParentId);
            String result = SynchronizeUtils.getResult(Dictionary.PAAS_HOST, "/goods/categories/getChildren/itf", RequestMethod.get, map, null);
            String categoryListStr = ValueUtil.getFromJson(result, "data");
            JSONArray array = JSON.parseArray(categoryListStr);
            for (int m = 0; m < array.size(); m++) {
                JSONObject category = (JSONObject) array.get(m);
                String categoryId = category.getString("id");
                List<GoodsMirroring> goodsList = goodsService.findByCategoryIdAndGoStatusAndSaleModelAndGoodsTypeIn(categoryId);
                if (goodsList.size() < 1) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ValueUtil.isError(" 分类：" + targetName + " 下无商品数据");
                }
                List<ActivityGoods> existedGoodsList = activityGoodsService.findByActivityId(activity.getId());
                addRegulationGoodsAndActivityGoods(activity, goodsList, existedGoodsList, regulationGoodsList, AGList);
            }
        }
        if(AGList.size()>0&&regulationGoodsList.size()>0){
            String result = checkGoodsActivityRestraint(activity, AGList,regulationGoodsList);
            activityGoodsService.save(AGList);
            regulationGoodsService.save(regulationGoodsList);
            return result;
        }
        return "SUCCESS";
    }

    private Boolean isJoinOtherRushPur(Activity activity, List<String> goodsIdList) throws YesmywineException {
        List<Activity> activityList = activityDao.findByStatusNot(ActivityStatus.overdue);//找出所有未过时的活动
        List<Activity> startGtEndActivityList = getStartTimeGtEndTime(activity.getEndTime());
        List<Activity> endGtStartActivityList = getEndTimeLtStartTime(activity.getStartTime());
        List<Activity> collectActivityList = new ArrayList<>();
        collectActivityList.addAll(startGtEndActivityList);
        collectActivityList.addAll(endGtStartActivityList);
        List<Activity> overlapActivity = new ArrayList<>();
        List<Integer> otherActivityIds = new ArrayList<>();

        for (Activity activitySingle : activityList) {
            if (!collectActivityList.contains(activitySingle)) {
                overlapActivity.add(activitySingle);
                otherActivityIds.add(activitySingle.getId());
            }
        }

        String [] goodsIds = new String[goodsIdList.size()];
        for(int i = 0;i<goodsIdList.size();i++){
            goodsIds[i] = goodsIdList.get(i);
        }

        if (otherActivityIds.size() > 0) {
            List<String> isJoinGoodsIdList = activityGoodsService.checkGoodsIsJoinOtherActivity(otherActivityIds, goodsIds);
            if (isJoinGoodsIdList.size() > 0) {
                StringBuffer sb = new StringBuffer();
                List<GoodsMirroring> goodsList = goodsService.findByGoodsIdIn(isJoinGoodsIdList);
                sb.append("以下商品已参加其他活动，无法参加抢购：");
                sb.append("\n");
                for (GoodsMirroring goods : goodsList) {
                    sb.append(goods.getGoodsName());
                    sb.append("\n");
                }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError(sb.toString());
            }
        }
        return null;
    }


    @Cacheable(value = CacheStatement.ACTIVITY_VALUE, key = "'activity_'+#id")
    @Override
    public Activity findById(Integer id) {
        System.err.println("没有走缓存！" + id);
        return activityDao.findOne(id);
    }

    @Override
    public String startActivity(Activity activity) {
        //启动活动
        activityDao.save(activity);
        List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByActivityId(activity.getId());
        regulationGoodsList.forEach(regulationGoods -> {
            regulationGoods.setStatus(ActivityStatus.current);
        });
        regulationGoodsService.save(regulationGoodsList);
        return "SUCCESS";
    }

    @Override
    public String endActivity(Activity activity) {
        //结束活动
        activityDao.save(activity);
        List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByActivityId(activity.getId());
        List<GoodsMirroring> goodsMirroringList = new ArrayList<>();
        regulationGoodsList.forEach(r -> {
            r.setStatus(ActivityStatus.overdue);
            if(activity.getActionCode().equals("reushPurA")){//如果是抢购活动，还原商品售价
                GoodsMirroring goods = goodsService.findById(r.getTargetId().toString());
                goods.setSalePrice(r.getActivityPrice().toString());
                goodsMirroringList.add(goods);
            }
        });
        regulationGoodsService.save(regulationGoodsList);
        activityGoodsService.deleteByActivityId(activity.getId());
        if(activity.getActionCode().equals("reushPurA")){//如果是抢购活动，还原商品售价
            goodsService.save(goodsMirroringList);
        }
        return "SUCCESS";
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE, key = "'ActivityServiceImpl_'+#regulationId")
    public Activity findByRegulationId(Integer regulationId) {
        return activityDao.findByRegulationId(regulationId);
    }

    @Override
    public String cancelActivity(Integer activityId) throws YesmywineException {
        Activity activity = activityDao.findOne(activityId);
        if (activity.getAuditStatus() != 1) {
            ValueUtil.isError("非审核通过的数据，无法撤销活动");
        }
        List<IftttRegulation> regulationList = regulationService.findByActivityId(activityId);
        activity.setAuditStatus(2);//撤销
        activity.setStatus(ActivityStatus.overdue);
        activityDao.save(activity);
        activityGoodsService.deleteByActivityId(activityId);
        regulationGoodsService.updateActivityStatusByActivityId(activityId, ActivityStatus.overdue);

        cacheManager.clearAllStartingWith(CacheStatement.ACTIVITY_VALUE);//清除活动所有缓存

        if(activity.getType().equals("nullT-rushPurA")){
            List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByActivityId(activity.getId());
            String goodsIds = "";
            String priceArr = "";
            String countArr = "";
            for(RegulationGoods goods:regulationGoodsList){
                String goodsId = String.valueOf(goods.getTargetId());
                String price = String.valueOf(goods.getOriginPrice());
                String count = String.valueOf(goods.getAllLimitCount());
                goodsIds +=goodsId +",";
                priceArr +=price +",";
                countArr += count +",";
            }
            goodsIds = goodsIds.substring(0,goodsIds.length()-1);
            priceArr = priceArr.substring(0,priceArr.length()-1);
            countArr = countArr.substring(0,countArr.length()-1);
            //还原冻结库存  并通知cms删除活动商品
            RestoreInventoryThread restoreThread = new RestoreInventoryThread(goodsIds,activity);
            Thread restore_Thread = new Thread(restoreThread);
            restore_Thread.start();

            ChangeGoosInfoThread runnable = new ChangeGoosInfoThread(goodsIds,priceArr,countArr,"0",activity);
            Thread thread = new Thread(runnable);
            thread.start();

            List<GoodsMirroring> goodsMirroringList = new ArrayList<>();
            regulationGoodsList.forEach(r -> {
                r.setStatus(ActivityStatus.overdue);
                if(activity.getActionCode().equals("reushPurA")){//如果是抢购活动，还原商品售价
                    GoodsMirroring goods = goodsService.findById(r.getTargetId().toString());
                    goods.setSalePrice(r.getActivityPrice().toString());
                    goodsMirroringList.add(goods);
                }
            });
            regulationGoodsService.save(regulationGoodsList);
            activityGoodsService.deleteByActivityId(activity.getId());
            goodsService.save(goodsMirroringList);
        }

        return "SUCCESS";
    }

    @Override
    public List<ActivityType> findByType(String type) {
        return activityTypeDao.findAll();
    }

    @Override
    public String reject(Integer activityId, HttpServletRequest request) throws YesmywineException {
        Activity activity = activityDao.findOne(activityId);
        if (activity == null) {
            ValueUtil.isError("无此活动！");
        }
        if (activity.getAuditStatus() != 0) {
            ValueUtil.isError("非待审核的数据，无法驳回！");
        }
        List<IftttRegulation> regulationList = regulationService.findByActivityId(activityId);
        if (regulationList.size() < 1 && !activity.getActionCode().equals("rushPurA")) {
            ValueUtil.isError("活动下未创建规则，无法驳回");
        }
        activity.setAuditor(UserUtils.getUserName(request));
        activity.setAuditTime(new Date());
        activity.setAuditStatus(-1);
        activityDao.save(activity);
        return "SUCCESS";
    }

    @Override
    public List<Activity> getStartTimeGtEndTime(Date endTime) {
        return activityDao.findByStatusNotAndStartTimeGreaterThanEqual(ActivityStatus.overdue, endTime);
    }

    @Override
    public List<Activity> getEndTimeLtStartTime(Date startTime) {
        return activityDao.findByStatusNotAndEndTimeLessThanEqual(ActivityStatus.overdue, startTime);
    }

    @Override
    public Object submitAudit(Integer activityId) throws YesmywineException {
        Activity activity = activityDao.findOne(activityId);
        List<IftttRegulation> regulationList = regulationService.findByActivityId(activityId);
        if (regulationList.size() < 1 && !activity.getActionCode().equals("rushPurA")) {
            ValueUtil.isError("该活动下尚无相应的规则，无法提交审核");
        }
        List<RegulationGoods> goodsList = regulationGoodsService.findByActivityId(activityId);
        if(goodsList.size()<1){
            ValueUtil.isError("该活动下未添加商品，无法提交审核");
        }
        activity.setAuditStatus(0);
        activityDao.save(activity);
        return "SUCCESS";
    }

    @Override
    public List<Activity> findByIsDeleteAndStatusAndAuditStatus(DeleteEnum isDelete, ActivityStatus status, Integer auditStatus) {
        return activityDao.findByIsDeleteAndStatusAndAuditStatus(isDelete, status, auditStatus);
    }

    @Override
    public Object addGoods(Integer activityId, String goodsJson) throws YesmywineException {
        JSONArray goodsArray = JSON.parseArray(goodsJson);
        List<ActivityGoods> agList = new ArrayList<>();
        List<RegulationGoods> regulationGoodsList = new ArrayList<>();
        Activity activity = activityDao.findOne(activityId);
        List<String> goodsNameList = new ArrayList<>();
        for (int i=0;i<goodsArray.size();i++ ){
            JSONObject goodsObject = (JSONObject) goodsArray.get(i);
            String goodsId = String.valueOf(goodsObject.get("goodsId"));
            String goodsName = String.valueOf(goodsObject.get("goodsName"));
            List<RegulationGoods > isExist = regulationGoodsService.findByActivityIdAndTargetIdAndRegulationIdIsNot(activityId,Integer.valueOf(goodsId));
            if(isExist!=null&&isExist.size()>0){
                goodsNameList.add(goodsName);
            }
            Double originPrice = goodsObject.getString("originPrice") == null ? null : Double.valueOf(goodsObject.getString("originPrice"));
            Double activityPrice = goodsObject.getString("activityPrice") == null ? null : Double.valueOf(goodsObject.getString("activityPrice"));
            Integer rushCount = goodsObject.getString("count") == null ? null : Integer.valueOf(goodsObject.getString("count"));
            if(activity.getActionCode().equals("rushPurA")){
                ValueUtil.verify(originPrice,"originPrice");
                ValueUtil.verify(activityPrice,"activityPrice");
                ValueUtil.verify(rushCount,"count");
            }

            ActivityGoods activityGoods = new ActivityGoods();
            activityGoods.setActivityId(activityId);
            activityGoods.setGoods(goodsService.findById(goodsId));
            activityGoods.setCreateTime(null);
            agList.add(activityGoods);

            RegulationGoods regulationGoods = new RegulationGoods();
            regulationGoods.setIsDelete(DeleteEnum.NOT_DELETE);
            regulationGoods.setTargetId(Integer.valueOf(goodsId));
            regulationGoods.setWare(WareEnum.Main);
            regulationGoods.setActivityId(activityId);
            regulationGoods.setStatus(ActivityStatus.notCurrent);
            regulationGoods.setOriginPrice(originPrice);
            regulationGoods.setActivityPrice(activityPrice);
            regulationGoods.setAllLimitCount(rushCount);
            regulationGoodsList.add(regulationGoods);
        }

        if(goodsNameList.size()>0){
            StringBuffer sb = new StringBuffer();
            sb.append("以下商品已参加该活动：");
            sb.append("\n");
            for(String goodsName:goodsNameList){
                sb.append(goodsName);
                sb.append("\n");
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError(sb.toString());
        }
        String result = "SUCCESS";
        if(!activity.getActionCode().equals("couponsA")){//优惠券不做控制
            result = checkGoodsActivityRestraint(activityDao.findOne(activityId), agList, regulationGoodsList);
        }


        if(!result.equals("SUCCESS")){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ValueUtil.isError(result);
        }
        if(agList.size()>0){
            activityGoodsService.save(agList);
        }
        if(regulationGoodsList.size()>0){
            regulationGoodsService.save(regulationGoodsList);
        }
        return result;
    }

    @Override
    public Object delGoods(Integer id) {
        RegulationGoods regulationGoods = regulationGoodsService.findOne(id);
        activityGoodsService.deleteByActivityIdAndGoods(regulationGoods.getActivityId(),regulationGoods.getTargetId());
        List<RegulationGoods> regulationGoodsList = new ArrayList<>();
        regulationGoodsList.add(regulationGoods);
        regulationGoodsService.deleteEntity(regulationGoodsList);
        return "SUCCESS";
    }

    @Override
    public PageModel getGoods(Integer activityId, String goodsName, Integer pageNo, Integer pageSize) {
        String sql = "select irg.*,g.goodsName as targetName from regulationGoods irg left join goodsMirroring g on irg.targetId=g.goodsId where irg.activityId="+activityId+" and irg.ware="+WareEnum.Main.getValue()+" ";
        String countSql = "select count(*) from regulationGoods irg left join goodsMirroring g on irg.targetId=g.goodsId where irg.activityId="+activityId+" and irg.ware="+WareEnum.Main.getValue()+"  ";
        if(goodsName!=null&&!goodsName.equals("")){
            sql += " and g.goodsName like '%"+goodsName+"%'";
            countSql += " and g.goodsName like '%"+goodsName+"%'";
        }
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setFirstResult((pageNo==null?0:pageNo-1)*(pageSize==null?10:pageSize));
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();
        Query countQuery = entityManager.createNativeQuery(countSql);
        Object obj = countQuery.getSingleResult();
        PageModel pageModel = new PageModel(pageNo==null?1:pageNo,pageSize==null?10:pageSize);
        pageModel.setTotalRows(Long.valueOf(obj.toString()));
        long tempTPd = pageModel.getTotalRows() % pageModel.getPageSize();
        Integer tempTp = Integer.valueOf((pageModel.getTotalRows() / pageModel.getPageSize()) + "");
        if (tempTPd == 0) {
            pageModel.setTotalPages(tempTp);
        } else {
            pageModel.setTotalPages(tempTp + 1);
        }
        pageModel.setContent(list);
        return pageModel;
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'ActivityServiceImplfindByIds_'+#activityIdList")
    public List<Activity> findByIds(String activityIdList) {
        activityIdList = activityIdList.substring(0,activityIdList.length()-1);
        String activityIds[] = activityIdList.split(";");
        List<Integer> ids = new ArrayList<>();
        for(String id:activityIds){
            ids.add(Integer.parseInt(id));
        }
        return activityDao.findAll(ids);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'ActivityServiceImplfindByIdList_'+#activityIdList")
    public List<Activity> findByIdList(List<Integer> activityIdList) {
        return activityDao.findByIdInOrderByPriorityDesc(activityIdList);
    }

    @Cacheable(value = CacheStatement.ACTIVITY_VALUE, key = "'ActivityServiceImplfindByIdAndIsDeleteAndStatus_'+#activityId+#isDelete+#status")
    private List<Activity> findByIdAndIsDeleteAndStatus(Integer activityId, DeleteEnum isDelete, ActivityStatus status) {
        return activityDao.findByIdAndIsDeleteAndStatus(activityId, isDelete, status);
    }

    @Override
    public String deleteActivity(Integer activityId) throws YesmywineException {//删除活动
        Activity activity = activityDao.findOne(activityId);
        if (activity.getStatus().equals(ActivityStatus.current) || !activity.getAuditStatus().equals(3)) {
            ValueUtil.isError("活动尚未结束，无法删除！");
        }

        activity.setIsDelete(DeleteEnum.DELETED);
        activity.setStatus(ActivityStatus.overdue);
        activityDao.save(activity);
        List<IftttRegulation> regulationList = regulationService.findByActivityId(activityId);
        for (int i = 0; i < regulationList.size(); i++) {
            List<RegulationGoods> discountGoodss = regulationGoodsService.findByActivityId(activityId);
            discountGoodss.forEach(r -> r.setIsDelete(DeleteEnum.DELETED));
            regulationGoodsService.save(discountGoodss);
        }
        regulationList.forEach(r -> r.setIsDelete(DeleteEnum.DELETED));
        regulationService.save(regulationList);
        activityGoodsService.deleteByActivityId(activityId);
        return "SUCCESS";

    }

    @Override
    public String updateActivity(Map<String, String> param, HttpServletRequest request) throws YesmywineException {//修改活动
        ValueUtil.verify(param, new String[]{"id", "name", "startTime", "endTime", "priority",
                "activityType", "typeName"});
        String username = UserUtils.getUserName(request);
        Integer activityId = Integer.parseInt(param.get("id"));
        Activity activity = activityDao.findOne(activityId);
        if (!activity.getAuditStatus().equals(3)) {
            ValueUtil.isError("活动已审核，无法修改！");
        }
        List<IftttRegulation> regulationList = regulationService.findByActivityIdAndIsDelete(activityId,DeleteEnum.NOT_DELETE);
        if(regulationList.size()<1){
            String type = param.get("activityType");
            ActivityType activityType = activityTypeDao.findByCode(type);
            String typeName = param.get("typeName");
            activity.setTypeName(typeName);
            activity.setTypeAlias(activityType.getAlias());
            activity.setType(type);
            String[] arr = type.split("-");
            String triggerCode = arr[0];
            activity.setTriggerCode(triggerCode);
            String actionCode = arr[1];
            activity.setActionCode(actionCode);
        }
        activity.setName(param.get("name"));
        activity.setComment(param.get("comment"));
        String st = param.get("startTime");
        Date datStar = DateUtil.toDate(st, dateFormat);
        if (new Date().getTime() > datStar.getTime()) {
            ValueUtil.isError("开始时间不能小于当前时间");
        }
        activity.setStartTime(datStar);
        String et = param.get("endTime");
        Date datEnd = DateUtil.toDate(et, dateFormat);
        activity.setEndTime(datEnd);
        activity.setPriority(Integer.parseInt(param.get("priority")));
        activity.setModifyTime(new Date());
        activity.setMember(Boolean.valueOf(param.get("isMember")));
        activityDao.save(activity);
        String goodsType = param.get("goodsType");//商品json
        if (goodsType != null && !goodsType.equals("/choose/")) {
            activityGoodsService.deleteByActivityId(activityId);
            List<RegulationGoods> delRegulationGoodsList = regulationGoodsService.findByActivityId(activityId);
            regulationGoodsService.deleteEntity(delRegulationGoodsList);
            if (goodsType.equals("category")) {
                return saveCategoryGoods(activity, param);
            } else if (goodsType.equals("brand")) {
                return saveBrandGoods(activity, param);
            } else {
                return saveSingleGoods(activity, param);
            }
        }

        return null;
    }

    @Override
    public String audit(Integer activityId, HttpServletRequest request) throws YesmywineException {
        Activity activity = activityDao.findOne(activityId);
        if (activity == null) {
            ValueUtil.isError("无此活动！");
        }
        if (activity.getAuditStatus() != 0) {
            ValueUtil.isError("非待审核的数据，无法审核！");
        }
        activity.setAuditor(UserUtils.getUserName(request));
        activity.setAuditTime(new Date());
        activity.setAuditStatus(1);
        List<IftttRegulation> regulationList = regulationService.findByActivityId(activityId);
        if (regulationList.size() < 1 && !activity.getActionCode().equals("rushPurA")) {
            ValueUtil.isError("该活动下尚无相应的规则，无法审核");
        }
        activityDao.save(activity);
        //抢购活动审核通过，冻结库存
        if (activity.getType().equals("nullT-rushPurA")) {
            freezeInventory(activity);
        }
        return "SUCCESS";
    }

    private void freezeInventory(Activity activity) {
        String skuIds = "";
        String counts = "";
        List<GoodsMirroring> goodsList = useService.getActivityGoods(activity.getId(), 1, 100000).getContent();
        for (GoodsMirroring goods : goodsList) {
            Set<GoodsSku> skuSet = goods.getSkuInfo();
            List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByActivityIdAndTargetId(activity.getId(), Integer.valueOf(goods.getGoodsId()));
            Integer goodsCount = regulationGoodsList.get(0).getAllLimitCount();
            for (GoodsSku skuInfo : skuSet) {
                String skuCount = String.valueOf(skuInfo.getCount() * goodsCount);
                String skuId = String.valueOf(skuInfo.getSkuId());
                skuIds += skuId + ",";
                counts += skuCount + ",";
            }
        }
        skuIds = skuIds.substring(0, skuIds.length() - 1);
        counts = counts.substring(0, counts.length() - 1);
        //冻结库存
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("skuIds", skuIds);
        paramMap.put("counts", counts);
        SynchronizeUtils.getCode(Dictionary.MALL_HOST, "/inventory/channelInventory/itf/freeze", RequestMethod.post, paramMap, null);
    }

    @Override
    public List<Activity> findByIsDeleteAndStatus(DeleteEnum isDelete, ActivityStatus status) {
        return activityDao.findByIsDeleteAndStatus(isDelete, status);
    }
}
