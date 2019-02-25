
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.HomePagePanicBuyingService;
import com.yesmywine.cms.service.OldPanicBuyingService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 老酒馆抢购
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/oldPanicBuying")
public class OldPanicBuyingController {

    @Autowired
    private OldPanicBuyingService oldPanicBuyingService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(oldPanicBuyingService.findOne(id));
        }
        return ValueUtil.toJson(oldPanicBuyingService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(Integer activityId, Integer status, String jsonString) {   //新增
        try {
            ValueUtil.verify(activityId,"activityId");
            ValueUtil.verify(status,"status");
            ValueUtil.verify(jsonString,"jsonString");
            String insert = this.oldPanicBuyingService.insert(activityId, status, jsonString);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, Integer activityId, Integer status, String jsonString) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(activityId,"activityId");
            ValueUtil.verify(status,"status");
            ValueUtil.verify(jsonString,"jsonString");
            String update = this.oldPanicBuyingService.update(id, activityId, status, jsonString);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


//    @RequestMapping(method = RequestMethod.DELETE)
//    public String delete(Integer id) {
//        try {
//            ValueUtil.verify(id,"id");
//            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.oldPanicBuyingService.delete(id));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(),e.getMessage());
//        }
//    }


    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteGoods(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.oldPanicBuyingService.deleteGoods(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}