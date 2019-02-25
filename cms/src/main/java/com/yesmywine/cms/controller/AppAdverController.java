package com.yesmywine.cms.controller;

import com.yesmywine.cms.dao.AppAdvertisingDao;
import com.yesmywine.cms.entity.AppAdvertising;
import com.yesmywine.cms.service.AppAdverService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 7/5/17.
 */
@RestController
@RequestMapping("/cms/appAdver")
public class AppAdverController {

    @Autowired
    private AppAdverService appAdverService;
    @Autowired
    private AppAdvertisingDao appAdvertisingDao;

    //新增修改
    @RequestMapping(method = RequestMethod.POST)
    public String create(AppAdvertising appAdvertising){
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,appAdverService.save(appAdvertising));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id){
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,appAdverService.delete(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return ValueUtil.toJson(HttpStatus.SC_OK,appAdvertisingDao.findAll());
    }
}
