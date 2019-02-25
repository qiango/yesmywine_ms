
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.HomePagePositionService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页广告位
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/homePagePosition")
public class WebHomePagePositionController {

    @Autowired
    private HomePagePositionService pagePositionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(pagePositionService.findAll());
    }


}