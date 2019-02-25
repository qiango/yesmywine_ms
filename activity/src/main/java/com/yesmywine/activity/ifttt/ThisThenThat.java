package com.yesmywine.activity.ifttt;

import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.Activity;
import com.yesmywine.activity.ifttt.entity.IftttEntity;
import com.yesmywine.activity.ifttt.entity.IftttEnum;
import com.yesmywine.activity.entity.IftttRegulation;
import com.yesmywine.activity.service.ActivityService;
import com.yesmywine.activity.ifttt.service.IftttConfigService;
import com.yesmywine.activity.ifttt.service.IftttService;
import com.yesmywine.activity.service.RegulationService;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Service
@Transactional
public class ThisThenThat {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private IftttService iftttService;
    @Autowired
    private IftttConfigService iftttConfigService;
    @Autowired
    private RegulationService regulationService;
    @Autowired
    private IfTTTfactory ifTTTfactory;

    private Object run(Trigger trigger, Action action, Map<String, Object> params, IftttConfigBiz iftttConfigBiz) {
        Map<String, Object> triggerMap = params;
        Map<String, Object> actionMap = params;
        triggerMap.putAll(iftttConfigBiz.getTriggerParams());
        Map<String, Object> triggerResult = trigger.runnable(triggerMap);
        Boolean isMeet = (Boolean) triggerResult.get("isMeet");
        if (isMeet) {
            actionMap.putAll(iftttConfigBiz.getActionParams());
            actionMap.putAll(triggerResult);
            return action.run(triggerResult);
        } else {
            if(triggerResult.get("regulationType").equals("reductionA")||triggerResult.get("regulationType").equals("discountA")){
                params.put("action", triggerMap.get("goodsTotalPrice"));
            }else{
                action.run(triggerResult);
            }
            return params;
        }
    }

//    public Object getRegulation(Integer regulationId, Map<String, Object> params) {
//        IftttEntity iftttEntity = iftttService.findById(regulation.getActionId());
//        Date endDate = activity.getEndTime();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String endTime = sdf.format(endDate);
//        params.put("regulationName", regulation.getName());
//        params.put("regulationId", regulation.getId());
//        params.put("regulationType", iftttEntity.getCode());
//        params.put("endTime", endTime);
//
//        return params;
//    }

    public Object runGoods(IftttRegulation regulation, Map<String, Object> params,Integer shareId) throws YesmywineException {
        List<Integer> TAIds = regulation.getTTTIds();
        List<IftttEntity> ttts = iftttService.findBytaIds(TAIds);
        IftttEntity iftttEntity = iftttService.findById(regulation.getActionId());
        params.put("regulationType", iftttEntity.getCode());
        if(shareId!=null){
            params.put("shareId", shareId);
        }
        Map<IftttEnum, String> tttMap = new HashMap<>();
        for (int i = 0; i < ttts.size(); i++) {
            if (ttts.get(i).getType().equals(IftttEnum.trigger)) {
                tttMap.put(IftttEnum.trigger, ttts.get(i).getPackagePath());
            }
            if (ttts.get(i).getType().equals(IftttEnum.action)) {
                tttMap.put(IftttEnum.action, ttts.get(i).getPackagePath());
            }
        }

        Trigger trigger = ifTTTfactory.getTrigger(tttMap.get(IftttEnum.trigger));
        Action action = ifTTTfactory.getAction(tttMap.get(IftttEnum.action));
        return run(trigger, action, params, new IftttConfigBiz(iftttConfigService.findByDiscountId(regulation.getId())));

    }


    public List<Map<String, Object>> runOrder(Double totalPrice) {
        List<IftttRegulation> regulationList = regulationService.findByTypeAndIsDeleteAndStatus("order", DeleteEnum.NOT_DELETE, ActivityStatus.current);

//        JSONArray array = new JSONArray();
        List<Map<String, Object>> array = new ArrayList();
        regulationList.forEach(discount -> {
            Map<String, Object> params = new HashMap<>();
            Activity activity = activityService.findOne(discount.getActivityId());
            Integer discountId = discount.getId();
            Date endDate = activity.getEndTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTime = sdf.format(endDate);
            params.put("goodsTotalPrice", totalPrice);
            params.put("triggerValue", discount.getTriggerValue());
            params.put("discountId", discountId);
            params.put("regulationName", discount.getName());
            params.put("regulationId", discount.getId());
            params.put("actionValue", discount.getActionValue());
            params.put("actionType", discount.getActionId());
            params.put("endTime", endTime);

            List<Integer> TAIds = discount.getTTTIds();
            List<IftttEntity> ttts = iftttService.findAll(TAIds);
            Map<IftttEnum, String> tttMap = new HashMap<>();
            for (int i = 0; i < ttts.size(); i++) {
                if (ttts.get(i).getType().equals(IftttEnum.trigger)) {
                    tttMap.put(IftttEnum.trigger, ttts.get(i).getPackagePath());
                }
                if (ttts.get(i).getType().equals(IftttEnum.action)) {
                    tttMap.put(IftttEnum.action, ttts.get(i).getPackagePath());
                }
            }
            Trigger trigger = ifTTTfactory.getTrigger(tttMap.get(IftttEnum.trigger));
            Action action = ifTTTfactory.getAction(tttMap.get(IftttEnum.action));

            run(trigger, action, params, new IftttConfigBiz(iftttConfigService.findByDiscountId(discountId)));
            array.add(params);
        });
        return array;
        //        IftttDiscount discount = regulationService.findByIdAndType("order");
    }

}
