package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.util.TestService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/testrecord")
public class TestController {

    @Autowired
    private TestService testBiz;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params) {
        return ValueUtil.toJson(testBiz.findAll(new PageModel(params)));
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params) {
        PageModel pageModel = new PageModel(0, 10);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        pageModel.addCondition(params);
        return ValueUtil.toJson(testBiz.findAll(pageModel));
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String tes() {
        File file = new File("");
        return "dddddd";
    }


}
