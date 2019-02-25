
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.OldMenuService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/oldMenu")
public class WebOldMenuController {

    @Autowired
    private OldMenuService oldMenuService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer menuFirstId) {   //查看
        if(ValueUtil.notEmpity(menuFirstId)){
            return ValueUtil.toJson(oldMenuService.findOne(menuFirstId));
        }
        return ValueUtil.toJson(oldMenuService.findAll());
    }

}