package com.yesmywine.activity.ifttt.trigger;

import com.yesmywine.activity.ifttt.Trigger;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by SJQ on 2017/5/12.
 */
@Component
public class NullTrigger implements Trigger {
    @Override
    public Map<String, Object> runnable(Map<String, Object> param) {
        param.put("isMeet", true);
        return param;
    }
}
