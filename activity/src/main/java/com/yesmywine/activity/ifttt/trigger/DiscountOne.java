package com.yesmywine.activity.ifttt.trigger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yesmywine.activity.ifttt.Trigger;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Component
public class DiscountOne implements Trigger {

    @Override
    public Map<String, Object> runnable(Map<String, Object> param) {
        if (null != param.get("price")) {
            double prise = Double.valueOf(param.get("price").toString());
            String json = param.get("full").toString();
            JsonParser jsonParser = new JsonParser();
            JsonElement origin = jsonParser.parse(json);
            JsonArray objects = origin.getAsJsonArray();
            for (int i = 0; i < objects.size(); i++) {
//				[{"price":100,"percent":0.9},{"price":200,"percent":0.8}]
                JsonObject object = objects.get(i).getAsJsonObject();
                Double full = object.get("price").getAsDouble();
                String percent = object.get("percent").getAsString();
                if (prise > full) {
                    param.put("percent", percent);
                    break;
                }
            }
            if (null == param.get("percent")) {
                return null;
            } else {
                return param;
            }
        } else {
            return null;
        }
    }
}
