package com.yesmywine.activity.ifttt.trigger;

import com.yesmywine.activity.ifttt.Trigger;

import java.util.Map;

/**
 * Created by SJQ on 2017/5/19.
 */
public class SpecificTrigger implements Trigger {
    @Override
    public Map<String, Object> runnable(Map<String, Object> param) {
        String triggerValue = param.get("triggerValue").toString();
        return null;
    }
}
