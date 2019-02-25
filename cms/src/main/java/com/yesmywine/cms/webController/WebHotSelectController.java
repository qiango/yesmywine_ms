
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.HotSelectService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页热搜
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/hotSelect")
public class WebHotSelectController {

    @Autowired
    private HotSelectService hotSelectService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(hotSelectService.findAll());
    }


}