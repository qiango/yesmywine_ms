package com.yesmywine.activity.ifttt.action;

import com.yesmywine.activity.ifttt.Action;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 * 满减、立减
 */
@Component
public class GetReduction implements Action {
    @Override
    public Map<String, Object> run(Map<String, Object> param) {
        BigDecimal price = BigDecimal.valueOf(Double.valueOf(param.get("goodsTotalPrice").toString()));
        param.put("action",String.valueOf(price.subtract(BigDecimal.valueOf(Double.valueOf(param.get("actionValue").toString())))));
        return param;
    }
}
