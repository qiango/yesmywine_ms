
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.ChartsService;
import com.yesmywine.util.basic.RestJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页排行榜
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/charts")
public class WebChartsController {

    @Autowired
    private ChartsService chartsService;

    @RequestMapping(method = RequestMethod.GET)
    public Object index() {   //查看
        Object all = chartsService.findAll();
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }
}