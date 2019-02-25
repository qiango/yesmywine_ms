package com.yesmywine.activity.ifttt;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@Component
public class IfTTTfactory {

    @Autowired
    private BeanFactory beanFactory;


    public Trigger getTrigger(String packagePath) {
        Trigger trigger = null;
        try {
            trigger = (Trigger) beanFactory.getBean(Class.forName(packagePath));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return trigger;
    }

    public Action getAction(String packagePath) {
        Action action = null;
        try {
            action = (Action) beanFactory.getBean(Class.forName(packagePath));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return action;
    }

}
