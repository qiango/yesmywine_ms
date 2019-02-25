
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.HomePagePositionService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页广告位
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/homePagePosition")
public class HomePagePositionController {

    @Autowired
    private HomePagePositionService pagePositionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(pagePositionService.findAll());
    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(Integer bannerPositionId, Integer positionIdOne, Integer positionIdTwo, Integer positionIdThree) {   //新增
        try {
            ValueUtil.verify(bannerPositionId,"bannerPositionId");
            ValueUtil.verify(positionIdOne,"positionIdOne");
            ValueUtil.verify(positionIdTwo,"positionIdTwo");
            ValueUtil.verify(positionIdThree,"positionIdThree");
            String insert = this.pagePositionService.insert(bannerPositionId, positionIdOne, positionIdTwo, positionIdThree);
            if("success".equals(insert)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
            }
            return ValueUtil.toError("500", insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id, Integer bannerPositionId, Integer positionIdOne, Integer positionIdTwo, Integer positionIdThree) {   //修改
        try {
            ValueUtil.verify(id,"id");
            ValueUtil.verify(bannerPositionId,"bannerPositionId");
            ValueUtil.verify(positionIdOne,"positionIdOne");
            ValueUtil.verify(positionIdTwo,"positionIdTwo");
            ValueUtil.verify(positionIdThree,"positionIdThree");
            String update = this.pagePositionService.update(id, bannerPositionId, positionIdOne, positionIdTwo, positionIdThree);
            if("success".equals(update)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            String delete = this.pagePositionService.delete(id);
            if("success".equals(delete)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
            }
            return ValueUtil.toError("500", delete);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}