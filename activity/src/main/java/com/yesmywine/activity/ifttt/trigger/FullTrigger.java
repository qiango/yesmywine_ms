package com.yesmywine.activity.ifttt.trigger;

import com.yesmywine.activity.ifttt.Trigger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by SJQ on 2017/5/12.
 */
@Component
public class FullTrigger implements Trigger {
    @Override
    public Map<String, Object> runnable(Map<String, Object> param) {
        double goodsTotalPrice = Double.valueOf(param.get("goodsTotalPrice").toString());//商品总价，eg：商品A，买了2个，总价=2*商品A
        double full = Double.valueOf(param.get("triggerValue").toString());//触发条件
        if (goodsTotalPrice >= full) {//如果总价满足，则继续走action
            param.put("isMeet", true);
            return param;
        } else {//否则直接返回
            param.put("isMeet", false);
            return param;
        }
    }
}
