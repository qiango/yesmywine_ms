
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.LocationService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定位
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/location")
public class WebLocationController {

    @Autowired
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.GET)
    public String showArea() {   //查看
        return ValueUtil.toJson(locationService.findAreaAll());
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String showUserArea(Integer userId) {   //查看
        try {
            ValueUtil.verify(userId,"userId");
            return ValueUtil.toJson(locationService.findUserAreaOne(userId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String insert(Integer userId, Integer areaId) {   //新增
        return ValueUtil.toJson(locationService.insert(userId, areaId));
    }


}