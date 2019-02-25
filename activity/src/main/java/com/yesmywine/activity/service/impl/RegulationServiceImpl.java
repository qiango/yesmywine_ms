package com.yesmywine.activity.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.bean.WareEnum;
import com.yesmywine.activity.entity.*;
import com.yesmywine.activity.ifttt.dao.*;
import com.yesmywine.activity.ifttt.entity.*;
import com.yesmywine.activity.service.ActivityService;
import com.yesmywine.activity.ifttt.service.GoodsService;
import com.yesmywine.activity.service.RegulationGoodsService;
import com.yesmywine.activity.service.RegulationService;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.ehcache.CacheStatement;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.*;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by SJQ on 2017/5/10.
 */
@Service
@Transactional
public class RegulationServiceImpl extends BaseServiceImpl<IftttRegulation, Integer> implements RegulationService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private IftttConfigDao iftttConfigDao;
    @Autowired
    private RegulationGoodsDao regulationGoodsDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ActivityGoodsDao activityGoodsDao;
    @Autowired
    private IftttDao ifttDao;
    @Autowired
    private RegulationDao regulationDao;
    @Autowired
    private RegulationGoodsService regulationGoodsService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private GoodsService goodsService;


    private static final String dateFormat = "yyyyMMdd";

    @Override
    public String create(Map<String, String> param) throws YesmywineException {
            ValueUtil.verify(param, new String[]{"name","activityId", "priority","actionValue"});
            //保存活动条件规则
            Integer activityId = Integer.valueOf(param.get("activityId"));
            Activity activity = activityDao.findOne(activityId);
            ValueUtil.verifyNotExist(activity, "该活动不存在");
            if(activity.getAuditStatus()!=3){
                ValueUtil.isError("非草稿状态的活动,无法创建规则");
            }

            IftttRegulation iftttRegulation = new IftttRegulation();
            iftttRegulation.setActivityId(activityId);
            iftttRegulation.setName(param.get("name"));
            iftttRegulation.setTriggerValue(param.get("triggerValue"));
            iftttRegulation.setPriority(Integer.valueOf(param.get("priority")));
            String triggerCode = activity.getTriggerCode();
            String actionCode = activity.getActionCode();
            iftttRegulation.setActionCode(actionCode);
            iftttRegulation.setTriggerCode(triggerCode);
            if(actionCode.split("_").length>1){//共享活动
                IftttEntity trigger = ifttDao.findByCode(activity.getTriggerCode());
                iftttRegulation.setTriggerId(trigger.getId());//触发条件
                iftttRegulation.setIsDelete(DeleteEnum.NOT_DELETE);
                iftttRegulation.setCreateTime(new Date());
                iftttRegulation.setActionCode(activity.getActionCode());
                regulationDao.save(iftttRegulation);
                String[] childrenActionCode = actionCode.split("_");
                for(int i = 0;i<childrenActionCode.length;i++){
                    JSONObject actionJson = JSON.parseObject(param.get("actionValue"));
                    String reductionValue = actionJson.getString("reductionValue");
                    String tradeInValue = actionJson.getString("tradeInValue");
                    String giftJson = actionJson.getString("giftJson");
                    String tradeInJson = actionJson.getString("tradeInJson");
                    String childActionCode = childrenActionCode[i];
                    IftttRegulation childRegulation = new IftttRegulation();
                    IftttEntity action = ifttDao.findByCode(childActionCode);
                    childRegulation.setTriggerId(trigger.getId());//触发条件
                    childRegulation.setTriggerValue(iftttRegulation.getTriggerValue());
                    childRegulation.setActionId(action.getId());//触发结果
                    childRegulation.setActionCode(action.getCode());
                    childRegulation.setIsDelete(DeleteEnum.NOT_DELETE);
                    if(childActionCode.equals("reductionA")){
                        childRegulation.setActionValue(reductionValue);
                    }else if (childActionCode.equals("tradeInA")){
                        childRegulation.setActionValue(tradeInValue);
                    }
                    childRegulation.setCreateTime(new Date());
                    iftttRegulation.addRegulation(childRegulation);
                    regulationDao.save(iftttRegulation);
                    //将赠品或换购商品与共享子规则关联
                    if(childActionCode.equals("giftA")){
                        for(IftttRegulation regulation:iftttRegulation.getChildren()){
                            if(regulation.getActionCode().equals("giftA")){
                                saveActivityGoods(giftJson, regulation.getId(), activityId, activity);
                            }
                        }
                    }else if(childActionCode.equals("tradeInA")){
                        for(IftttRegulation regulation:iftttRegulation.getChildren()){
                            if(regulation.getActionCode().equals("tradeInA")){
                                saveActivityGoods(tradeInJson, regulation.getId(), activityId, activity);
                            }
                        }
                    }
                }

            }else {//互斥活动
                if (actionCode.equals("giftA") || actionCode.equals("couponsA")) {
                    JSONObject actionJson = JSON.parseObject(param.get("actionValue"));
                    String premiumsJson = actionJson.getString("premiumsJson");
                    IftttEntity trigger = ifttDao.findByCode(activity.getTriggerCode());
                    IftttEntity action = ifttDao.findByCode(activity.getActionCode());
                    iftttRegulation.setTriggerId(trigger.getId());//触发条件
                    iftttRegulation.setActionId(action.getId());//触发结果
                    iftttRegulation.setIsDelete(DeleteEnum.NOT_DELETE);
                    iftttRegulation.setCreateTime(new Date());
                    regulationDao.save(iftttRegulation);

                    Integer regulationId = iftttRegulation.getId();
                    IftttConfig iftttConfig = new IftttConfig();
                    iftttConfig.setDiscountId(regulationId);
                    iftttConfig.setConfigValue("default");
                    iftttConfig.setType(IftttEnum.trigger);
                    iftttConfig.setIsDelete(DeleteEnum.NOT_DELETE);
                    iftttConfig.setConfigKey("default");
                    iftttConfig.setCreateTime(new Date());
                    iftttConfig.setStatus(ActivityStatus.notCurrent);
                    iftttConfigDao.save(iftttConfig);

                    saveActivityGoods(premiumsJson, regulationId, activityId, activity);

                } else if (actionCode.equals("tradeInA")) {
                    JSONObject actionJson = JSON.parseObject(param.get("actionValue"));
                    String value = actionJson.getString("value");
                    String premiumsJson = actionJson.getString("premiumsJson");
                    IftttEntity trigger = ifttDao.findByCode(activity.getTriggerCode());
                    IftttEntity action = ifttDao.findByCode(activity.getActionCode());
                    iftttRegulation.setTriggerId(trigger.getId());//触发条件
                    iftttRegulation.setActionId(action.getId());//触发结果
                    iftttRegulation.setActionValue(value);
                    iftttRegulation.setIsDelete(DeleteEnum.NOT_DELETE);
                    iftttRegulation.setCreateTime(new Date());
                    regulationDao.save(iftttRegulation);

                    Integer regulationId = iftttRegulation.getId();

                    IftttConfig iftttConfig = new IftttConfig();
                    iftttConfig.setDiscountId(regulationId);
                    iftttConfig.setConfigValue("default");
                    iftttConfig.setType(IftttEnum.trigger);
                    iftttConfig.setIsDelete(DeleteEnum.NOT_DELETE);
                    iftttConfig.setConfigKey("default");
                    iftttConfig.setCreateTime(new Date());
                    iftttConfig.setStatus(ActivityStatus.notCurrent);
                    iftttConfigDao.save(iftttConfig);

                    saveActivityGoods(premiumsJson, regulationId, activityId, activity);

                } else {
                    iftttRegulation.setActionValue(param.get("actionValue"));
                    IftttEntity trigger = ifttDao.findByCode(activity.getTriggerCode());
                    IftttEntity action = ifttDao.findByCode(activity.getActionCode());
                    iftttRegulation.setTriggerId(trigger.getId());//触发条件
                    iftttRegulation.setActionId(action.getId());//触发结果

                    iftttRegulation.setIsDelete(DeleteEnum.NOT_DELETE);
                    iftttRegulation.setCreateTime(new Date());
                    regulationDao.save(iftttRegulation);

                    Integer regulationId = iftttRegulation.getId();

                    IftttConfig iftttConfig = new IftttConfig();
                    iftttConfig.setDiscountId(regulationId);
                    iftttConfig.setConfigValue("default");
                    iftttConfig.setType(IftttEnum.trigger);
                    iftttConfig.setIsDelete(DeleteEnum.NOT_DELETE);
                    iftttConfig.setConfigKey("default");
                    iftttConfig.setCreateTime(new Date());
                    iftttConfig.setStatus(ActivityStatus.notCurrent);
                    iftttConfigDao.save(iftttConfig);
                }


            }
            return "SUCCESS";
    }

    @Override
    public String update(Map<String, String> param) throws YesmywineException {
        ValueUtil.verify(param, new String[]{"id","activityId", "name","activityId", "priority","actionValue"});
        IftttRegulation iftttRegulation = regulationDao.findOne(Integer.valueOf(param.get("id")));
        Integer activityId = iftttRegulation.getActivityId();
        //保存活动条件规则
        Activity activity = activityDao.findOne(activityId);
        ValueUtil.verifyNotExist(activity, "该活动不存在");
        if(activity.getAuditStatus()!=3){
            ValueUtil.isError("非草稿状态的活动，该规则无法修改");
        }

        iftttRegulation.setName(param.get("name"));
        iftttRegulation.setTriggerValue(param.get("triggerValue"));
        iftttRegulation.setPriority(Integer.valueOf(param.get("priority")));
        String triggerCode = activity.getTriggerCode();
        String actionCode = activity.getActionCode();
        if(actionCode.split("_").length>1){//共享活动
//            regulationDao.save(iftttRegulation);
            String[] childrenActionCode = actionCode.split("_");
            for(int i = 0;i<childrenActionCode.length;i++){
                String childActionCode = childrenActionCode[i];
                JSONObject actionJson = JSON.parseObject(param.get("actionValue"));
                String reductionValue = actionJson.getString("reductionValue");
                String tradeInValue = actionJson.getString("tradeInValue");
                String giftJson = actionJson.getString("giftJson");
                String tradeInJson = actionJson.getString("tradeInJson");
                if(childActionCode.equals("giftA")){
                    for(IftttRegulation regulation:iftttRegulation.getChildren()){
                        if(regulation.getActionCode().equals("giftA")){
                            regulationGoodsDao.deleteByRegulationId(regulation.getId());
                            regulation.setTriggerValue(iftttRegulation.getTriggerValue());
                            saveActivityGoods(giftJson, regulation.getId(), activityId, activity);
                        }
                    }
                }else if(childActionCode.equals("tradeInA")){
                    for(IftttRegulation regulation:iftttRegulation.getChildren()){
                        if(regulation.getActionCode().equals("tradeInA")){
                            regulationGoodsDao.deleteByRegulationId(regulation.getId());
                            regulation.setActionValue(tradeInValue);
                            regulation.setTriggerValue(iftttRegulation.getTriggerValue());
                            saveActivityGoods(tradeInJson, regulation.getId(), activityId, activity);
                        }
                    }
                }else if(childActionCode.equals("reductionA")){
                    for(IftttRegulation regulation:iftttRegulation.getChildren()){
                        if(regulation.getActionCode().equals("reductionA")){
                            regulation.setActionValue(reductionValue);
                            regulation.setTriggerValue(iftttRegulation.getTriggerValue());
                        }
                    }

                }
            }
            regulationDao.save(iftttRegulation);


        }else {//互斥活动
            if (actionCode.equals("giftA") || actionCode.equals("couponsA")) {
                JSONObject actionJson = JSON.parseObject(param.get("actionValue"));
                String premiumsJson = actionJson.getString("premiumsJson");
                regulationDao.save(iftttRegulation);

                Integer regulationId = iftttRegulation.getId();
                regulationGoodsDao.deleteByRegulationId(regulationId);
                saveActivityGoods(premiumsJson, regulationId, activityId, activity);

            } else if (actionCode.equals("tradeInA")) {
                JSONObject actionJson = JSON.parseObject(param.get("actionValue"));
                String value = actionJson.getString("value");
                String premiumsJson = actionJson.getString("premiumsJson");
                regulationDao.save(iftttRegulation);

                Integer regulationId = iftttRegulation.getId();
                regulationGoodsDao.deleteByRegulationId(regulationId);

                saveActivityGoods(premiumsJson, regulationId, activityId, activity);

            } else {
                iftttRegulation.setActionValue(param.get("actionValue"));
                regulationDao.save(iftttRegulation);
            }
        }
        return "SUCCESS";
    }

    private void saveActivityGoods(String premiumsJson, Integer regulationId, Integer activityId, Activity activity) throws YesmywineException {
        List<RegulationGoods> regulationGoodsList = new ArrayList<>();
        if(premiumsJson!=null&&!premiumsJson.equals("")){
            JSONArray arr = JSON.parseArray(premiumsJson);
            for (int i = 0; i < arr.size(); i++) {
                RegulationGoods regulationGoods = new RegulationGoods();
                regulationGoods.setActivityId(activityId);
                regulationGoods.setRegulationId(regulationId);
                JSONObject obj = (JSONObject) arr.get(i);
                Integer targetId = obj.getInteger("targetId");
                String ware = obj.getString("ware");
                ValueUtil.verify(targetId, "targetId");
                ValueUtil.verify(ware, "ware");
                regulationGoods.setTargetId(targetId);
                regulationGoods.setStatus(ActivityStatus.notCurrent);
                regulationGoods.setIsDelete(DeleteEnum.NOT_DELETE);
                switch (ware) {
                    case "coupon":
                        regulationGoods.setWare(WareEnum.Coupon);
                        break;
                    case "gift":
                        regulationGoods.setWare(WareEnum.Gift);
                        break;
                    case "tradeIn":
                        regulationGoods.setWare(WareEnum.TradeIn);
                        break;
                }
                regulationGoodsList.add(regulationGoods);
            }
            regulationGoodsDao.save(regulationGoodsList);
        }
    }

    @Override
    public String delete(Integer id) throws YesmywineException {
        IftttRegulation regulation = regulationDao.findOne(id);
        Activity activity = activityDao.findOne(regulation.getActivityId());
        if (!activity.getAuditStatus().equals(3)) {
            ValueUtil.isError("非草稿状态的活动，无法删除规则！");
        }
        regulation.setIsDelete(DeleteEnum.DELETED);
        regulationDao.save(regulation);
//        IftttConfig iftttConfig = iftttConfigDao.findByDiscountIdAndIsDelete(regulation.getId(), DeleteEnum.NOT_DELETE);
//        iftttConfig.setIsDelete(DeleteEnum.DELETED);
//        iftttConfigDao.save(iftttConfig);
        List<RegulationGoods> discountGoodss = regulationGoodsDao.findByRegulationId(regulation.getId());
        discountGoodss.forEach(r -> r.setIsDelete(DeleteEnum.DELETED));
        regulationGoodsDao.save(discountGoodss);
        activityGoodsDao.deleteByRegulationId(id);
        return "SUCCESS";
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttRegulation_'+#activityId+#isDelete")
    public List<IftttRegulation> findByActivityIdAndIsDeleteAndStatus(Integer activityId, DeleteEnum isDelete, ActivityStatus status) {
        return regulationDao.findByActivityId(activityId);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttRegulation_'+#goods+#isDelete+#status")
    public List<IftttRegulation> findByTypeAndIsDeleteAndStatus(String goods, DeleteEnum isDelete, ActivityStatus status) {
//        return regulationDao.findByIsDelete(goods,isDelete,status);
        return null;
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttRegulation_'+#actionId+#isDelete+#status")
    public List<IftttRegulation> findByActionIdAndIsDeleteAndStatus(Integer actionId, DeleteEnum isDelete, ActivityStatus status) {
        return regulationDao.findByActionIdAndIsDelete(actionId,isDelete);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'IftttRegulation_'+#regulationId")
    public IftttRegulation findById(Integer regulationId) {
        return regulationDao.findById(regulationId);
    }

    @Override
    @Cacheable(value = CacheStatement.ACTIVITY_VALUE,key = "'RegulationfindByActivityId'+#activityId")
    public List<IftttRegulation> findByActivityId(Integer activityId) {
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        System.out.println("没走缓存");
        return regulationDao.findByActivityIdOrderByPriorityDesc(activityId);
    }

    @Override
    public Object getOne(Integer regulationId) {
        JSONObject regulationObj = new JSONObject();
        IftttRegulation regulation = regulationDao.findOne(regulationId);
        regulationObj.put("regulationInfo",regulation);
        Activity activity = activityService.findOne(regulation.getActivityId());
        String actionCode = activity.getActionCode();
        if(activity.getShare()){
            regulationObj.put("triggerValue",regulation.getTriggerValue());
            Set<IftttRegulation> shareRegulationList = regulation.getChildren();
            for(IftttRegulation regulationChild:shareRegulationList){
                if(regulationChild.getActionCode().equals("giftA")){
                    List<RegulationGoods> giftRegulationGoodsList = regulationGoodsService.findByRegulationIdAndWareNoCache(regulationChild.getId(), WareEnum.Gift);
                    List<String> giftGoodsIdList = new ArrayList<>();
                    for(RegulationGoods regulationGoods:giftRegulationGoodsList){
                        giftGoodsIdList.add(regulationGoods.getTargetId().toString());
                    }
                    List<GoodsMirroring> giftGoodsList = goodsService.findByGoodsIdIn(giftGoodsIdList);
                    regulationObj.put("giftList",giftGoodsList);
                }else if(regulationChild.getActionCode().equals("tradeInA")){
                    List<RegulationGoods> tradeInRegulationGoodsList = regulationGoodsService.findByRegulationIdAndWareNoCache(regulationChild.getId(), WareEnum.TradeIn);
                    List<String> tradeInGoosIdList = new ArrayList<>();
                    for(RegulationGoods regulationGoods:tradeInRegulationGoodsList){
                        tradeInGoosIdList.add(regulationGoods.getTargetId().toString());
                    }
                    List<GoodsMirroring> tradeInGoodsList = goodsService.findByGoodsIdIn(tradeInGoosIdList);
                    regulationObj.put("tradeInList",tradeInGoodsList);
                    regulationObj.put("tradeInValue",regulationChild.getActionValue());
                }else  if(regulationChild.getActionCode().equals("reductionA")){
                    regulationObj.put("reductionValue",regulationChild.getActionValue());
                }
            }
        }else{
            if(actionCode.equals("giftA")){
                List<RegulationGoods> giftRegulationGoodsList = regulationGoodsService.findByRegulationIdAndWareNoCache(regulation.getId(), WareEnum.Gift);
                List<String> giftGoodsIdList = new ArrayList<>();
                for(RegulationGoods regulationGoods:giftRegulationGoodsList){
                    giftGoodsIdList.add(regulationGoods.getTargetId().toString());
                }
                List<GoodsMirroring> giftGoodsList = goodsService.findByGoodsIdIn(giftGoodsIdList);
                regulationObj.put("giftList",giftGoodsList);
            }else if(actionCode.equals("tradeInA")){
                regulationObj.put("triggerValue",regulation.getTriggerValue());
                List<RegulationGoods> tradeInRegulationGoodsList = regulationGoodsService.findByRegulationIdAndWareNoCache(regulation.getId(), WareEnum.TradeIn);
                List<String> tradeInGoosIdList = new ArrayList<>();
                for(RegulationGoods regulationGoods:tradeInRegulationGoodsList){
                    tradeInGoosIdList.add(regulationGoods.getTargetId().toString());
                }
                List<GoodsMirroring> tradeInGoodsList = goodsService.findByGoodsIdIn(tradeInGoosIdList);
                regulationObj.put("tradeInList",tradeInGoodsList);
                regulationObj.put("tradeInList",tradeInGoodsList);
            }else if(actionCode.equals("couponsA")){
                regulationObj.put("triggerValue",regulation.getTriggerValue());
                List<RegulationGoods> regulationGoodsList = regulationGoodsService.findByRegulationIdAndWareNoCache(regulationId,  WareEnum.Coupon);
                String action = "";
                for (int i = 0; i < regulationGoodsList.size(); i++) {
                    RegulationGoods dg = regulationGoodsList.get(i);
                    String targetId = String.valueOf(dg.getTargetId());
                    if (i == regulationGoodsList.size() - 1) {
                        action += targetId;
                    } else {
                        action += targetId + ";";
                    }
                }
                //获取可用的优惠券
                Map<String,Object> params = new HashMap<>();
                params.put("couponIds",action.replace(";",","));
                params.put("status","all");
                String result = SynchronizeUtils.getResult(com.yesmywine.util.basic.Dictionary.MALL_HOST,"/userservice/coupon/filter/itf", RequestMethod.get,params,null);
                if(result!=null&&!result.equals("")){
                    regulationObj.put("couponList", JSON.parseArray(ValueUtil.getFromJson(result,"data")));
                }
            }else{//满减
                regulationObj.put("triggerValue",regulation.getTriggerValue());
                regulationObj.put("actionValue",regulation.getActionValue());
            }
        }

        return regulationObj;
    }

    @Override
    public List<IftttRegulation> findByActivityIdNoCache(Integer activityId) {
        return regulationDao.findByActivityIdAndIsDelete(activityId,DeleteEnum.NOT_DELETE);
    }

    @Override
    public List<IftttRegulation> findByActivityIdAndIsDelete(Integer activityId, DeleteEnum notDelete) {
        return regulationDao.findByActivityIdAndIsDelete(activityId,notDelete);
    }

}
