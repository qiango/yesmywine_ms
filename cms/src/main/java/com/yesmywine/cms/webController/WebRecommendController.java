
package com.yesmywine.cms.webController;


import com.yesmywine.cms.service.RecommendService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 精品推荐
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/recommend")
public class WebRecommendController {

    @Autowired
    private RecommendService recommendService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(recommendService.findAll());
    }


}