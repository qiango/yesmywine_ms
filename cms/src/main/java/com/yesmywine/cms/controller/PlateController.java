
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.PlateService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类板块111
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/plate")
public class PlateController {

    @Autowired
    private PlateService plateService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer plateFirstId, Integer isShow) {   //查看
        if(ValueUtil.notEmpity(plateFirstId)){
            return ValueUtil.toJson(plateService.findOne(plateFirstId));
        }
        if(ValueUtil.notEmpity(isShow)){
            return ValueUtil.toJson(plateService.findByIsShow(isShow));
        }
        return ValueUtil.toJson(plateService.findAll());
    }

    @RequestMapping(value = "/front",method = RequestMethod.GET)
    public String frontIndex() {   //查看

        return ValueUtil.toJson(plateService.frontFindAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(Integer firstCategoryId, Integer firstIndex, String goodsJsonString,
                         Integer firstPositionId, Integer secentPositionId, Integer thirdPositionId, Integer fourthPositionId,
                         Integer appPositionId, String labelJsonString, String rankJsonString, Integer isShow) {   //新增
        try {
            ValueUtil.verify(firstCategoryId,"firstCategoryId");
            ValueUtil.verify(firstIndex,"firstIndex");
            String insert = this.plateService.insert(firstCategoryId, firstIndex, goodsJsonString,
                    firstPositionId, secentPositionId, thirdPositionId, fourthPositionId, appPositionId,
                    labelJsonString, rankJsonString, isShow);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, Integer firstCategoryId, Integer firstIndex, String goodsJsonString,
                         Integer firstPositionId, Integer secentPositionId, Integer thirdPositionId, Integer fourthPositionId,
                         Integer appPositionId, String labelJsonString, String rankJsonString, Integer isShow) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(firstCategoryId,"firstCategoryId");
            ValueUtil.verify(firstIndex,"firstIndex");
            String update = this.plateService.update(id, firstCategoryId, firstIndex, goodsJsonString,
                    firstPositionId, secentPositionId, thirdPositionId, fourthPositionId,
                    appPositionId, labelJsonString, rankJsonString, isShow);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/first", method = RequestMethod.DELETE)
    public String deleteFirst(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.plateService.deleteFirst(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(value = "/secentGoods", method = RequestMethod.DELETE)
    public String deleteSecent(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.plateService.deleteSecentGoods(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/secentLabel", method = RequestMethod.DELETE)
    public String deleteSecentLabel(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.plateService.deleteSecentLabel(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/secentRank", method = RequestMethod.DELETE)
    public String deleteSecentRank(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, this.plateService.deleteSecentRank(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}