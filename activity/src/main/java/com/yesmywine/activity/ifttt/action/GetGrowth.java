package com.yesmywine.activity.ifttt.action;

import com.yesmywine.activity.ifttt.Action;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by SJQ on 2017/5/15.
 * <p>
 * 赠成长值
 */
@Component
public class GetGrowth implements Action {

    @Override
    public Map<String, Object> run(Map<String, Object> param) {
        param.put("action", String.valueOf(param.get("actionValue")));
        return param;
    }
}
