
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.PlateService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类板块
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/plate")
public class WebPlateController {

    @Autowired
    private PlateService plateService;

    @RequestMapping(method = RequestMethod.GET)
    public String frontIndex() {   //查看

        return ValueUtil.toJson(plateService.frontFindAll());
    }

}