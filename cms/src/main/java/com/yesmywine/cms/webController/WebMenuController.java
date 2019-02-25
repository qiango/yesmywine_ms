
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.MenuService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *分类导航
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/menu")
public class WebMenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer menuFirstId) {   //查看
        if(ValueUtil.notEmpity(menuFirstId)){
            return ValueUtil.toJson(menuService.findOne(menuFirstId));
        }
        return ValueUtil.toJson(menuService.findAll());
    }
}