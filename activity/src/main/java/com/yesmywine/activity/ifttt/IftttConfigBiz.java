package com.yesmywine.activity.ifttt;

import com.yesmywine.activity.ifttt.entity.IftttConfig;
import com.yesmywine.activity.ifttt.entity.IftttEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class IftttConfigBiz {

    private Map<String, Object> triggerParams;
    private Map<String, Object> actionParams;

    public IftttConfigBiz() {
        this.triggerParams = new HashMap<>();
        this.actionParams = new HashMap<>();
    }

    public IftttConfigBiz(List<IftttConfig> configs) {
        this.triggerParams = new HashMap<>();
        this.actionParams = new HashMap<>();

        for (int i = 0; i < configs.size(); i++) {
            if (configs.get(i).getType().equals(IftttEnum.trigger)) {
                triggerParams.put(configs.get(i).getConfigKey(), configs.get(i).getConfigValue());
            }
            if (configs.get(i).getType().equals(IftttEnum.action)) {
                actionParams.put(configs.get(i).getConfigKey(), configs.get(i).getConfigValue());
            }
        }

    }

    public Map<String, Object> getTriggerParams() {
        return triggerParams;
    }

    public void setTriggerParams(Map<String, Object> triggerParams) {
        this.triggerParams = triggerParams;
    }

    public Map<String, Object> getActionParams() {
        return actionParams;
    }

    public void setActionParams(Map<String, Object> actionParams) {
        this.actionParams = actionParams;
    }
}
