
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.OldMenuService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/oldMenu")
public class OldMenuController {

    @Autowired
    private OldMenuService oldMenuService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer menuFirstId) {   //查看
        if(ValueUtil.notEmpity(menuFirstId)){
            return ValueUtil.toJson(oldMenuService.findOne(menuFirstId));
        }
        return ValueUtil.toJson(oldMenuService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(Integer firstCategoryId, Integer firstIndex, String jsonString) {   //新增
        try {
            ValueUtil.verify(firstCategoryId,"firstCategoryId");
            ValueUtil.verify(firstIndex,"firstIndex");
            String insert = this.oldMenuService.insert(firstCategoryId, firstIndex, jsonString);
            if("success".equals(insert)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }else {
                return ValueUtil.toError("500", insert);
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, Integer firstCategoryId, Integer firstIndex, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(firstCategoryId,"firstCategoryId");
            ValueUtil.verify(firstIndex,"firstIndex");
            String update = this.oldMenuService.update(id, firstCategoryId, firstIndex, jsonString);
            if("success".equals(update)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }else {
                return ValueUtil.toError("500", update);
            }
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/first", method = RequestMethod.DELETE)
    public String deleteFirst(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.oldMenuService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secent", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.oldMenuService.deleteSecent(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}