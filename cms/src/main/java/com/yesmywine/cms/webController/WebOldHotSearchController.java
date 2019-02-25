
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.OldHotSearchService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/oldHotSearch")
public class WebOldHotSearchController {

    @Autowired
    private OldHotSearchService oldHotSearchService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(oldHotSearchService.findOne(id));
        }
        return ValueUtil.toJson(oldHotSearchService.findAll());
    }

}