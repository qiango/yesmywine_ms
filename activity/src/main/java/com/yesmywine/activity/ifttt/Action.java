package com.yesmywine.activity.ifttt;

import java.util.Map;

/**
 * Created by WANG, RUIQING on 1/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public interface Action {

    Map<String, Object> run(Map<String, Object> param);

}
