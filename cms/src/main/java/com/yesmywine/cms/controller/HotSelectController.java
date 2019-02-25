
package com.yesmywine.cms.controller;

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
@RequestMapping("/cms/hotSelect")
public class HotSelectController {

    @Autowired
    private HotSelectService hotSelectService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(hotSelectService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(String name) {   //新增
        try {
            ValueUtil.verify(name,"name");
            String insert = this.hotSelectService.insert(name);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping( method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            String delete = this.hotSelectService.delete(id);
            if("success".equals(delete)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
            }
            return ValueUtil.toError("500", delete);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}