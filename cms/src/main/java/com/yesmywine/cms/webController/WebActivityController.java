
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.ActivityColumnService;
import com.yesmywine.cms.service.ActivityService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 广告页
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/activity")
public class WebActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityColumnService activityColumnService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Integer id, String appPosition) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(activityService.findOne(id));
        }
        if(ValueUtil.notEmpity(appPosition)){
            return ValueUtil.toJson(activityService.findByAppPosition(appPosition));
        }
        return ValueUtil.toJson(activityService.findAll());
    }


    @RequestMapping(value = "/findMajor", method = RequestMethod.GET)
    public String indexMajor(Integer id, String appPosition) {   //查看
        if(ValueUtil.notEmpity(appPosition)){
            return ValueUtil.toJson(activityService.findByAppPositionMajor(appPosition));
        }
        return ValueUtil.toJson(activityService.findOneMajor(id));
    }


    @RequestMapping(value = "/findColumn", method = RequestMethod.GET)
    public String indexColumn(Integer id) {   //查看
        if(ValueUtil.notEmpity(id)){
            return ValueUtil.toJson(activityColumnService.findOne(id));
        }
        return ValueUtil.toJson(activityColumnService.findAll());
    }


    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public String indexApp(String appPosition) {   //查看
        if(ValueUtil.notEmpity(appPosition)){
            return ValueUtil.toJson(activityService.findAppByAppPosition(appPosition));
        }
        return ValueUtil.toJson(activityService.findAllApp());
    }


    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String indexApp(Integer id) {   //查看
        return ValueUtil.toJson(activityService.findTemplate(id));
    }


}