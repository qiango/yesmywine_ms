
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.OldPositionService;
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
@RequestMapping("/cms/oldPosition")
public class OldPositionController {

    @Autowired
    private OldPositionService oldPositionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(oldPositionService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(Integer firstPositionId, Integer secentPositionId) {   //新增
        try {

            ValueUtil.verify(firstPositionId,"firstPositionId");
            ValueUtil.verify(secentPositionId,"secentPositionId");
            String insert = this.oldPositionService.insert(firstPositionId, secentPositionId);
            if(!"success".equals(insert)){
                return ValueUtil.toError("500", insert);
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, Integer firstPositionId, Integer secentPositionId) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(firstPositionId,"firstPositionId");
            ValueUtil.verify(secentPositionId,"secentPositionId");
            String update = this.oldPositionService.update(id, firstPositionId, secentPositionId);
            if(!"success".equals(update)){
                return ValueUtil.toError("500", update);
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping( method = RequestMethod.DELETE)
    public String deleteFirst(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            String delete = this.oldPositionService.delete(id);
            if(!"success".equals(delete)){
                return ValueUtil.toError("500", delete);
            }
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
}