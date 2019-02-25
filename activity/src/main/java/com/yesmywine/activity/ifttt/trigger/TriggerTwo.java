package com.yesmywine.activity.ifttt.trigger;

import com.yesmywine.activity.ifttt.Trigger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Component
public class TriggerTwo implements Trigger {
    @Override
    public Map<String, Object> runnable(Map<String, Object> param) {
        double skuPrice = Double.valueOf(param.get("price").toString());
        double full = Double.valueOf(param.get("targetPrice").toString());
        if (skuPrice > full) {
            return param;
        } else {
            return null;
        }
    }
}
