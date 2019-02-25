package com.yesmywine.activity.controller;

import com.sdicons.json.mapper.MapperException;
import com.yesmywine.activity.bean.ActivityStatus;
import com.yesmywine.activity.entity.DeleteEnum;
import com.yesmywine.activity.entity.Activity;
import com.yesmywine.activity.service.ActivityService;
import com.yesmywine.activity.ifttt.service.IftttService;
import com.yesmywine.activity.service.UseService;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/1/10.
 */
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private IftttService iftttService;
    @Autowired
    private UseService useService;

    @RequestMapping(method = RequestMethod.POST)
    public String createActivity(@RequestParam Map<String, String> param, HttpServletRequest request) {//创建活动
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, activityService.createActivity(param,request),null);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer activityId) {//删除活动
        try {
            ValueUtil.verify(activityId);
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, activityService.deleteActivity(activityId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/cancel",method = RequestMethod.PUT)
    public String cancelActivity(Integer activityId) {//取消活动
        try {
            ValueUtil.verify(activityId);
            return ValueUtil.toJson(HttpStatus.SC_CREATED, activityService.cancelActivity(activityId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/sbmAudit",method = RequestMethod.PUT)
    public String submitAudit(Integer activityId) {//提交审核
        try {
            ValueUtil.verify(activityId,"activityId");
            return ValueUtil.toJson(HttpStatus.SC_CREATED, activityService.submitAudit(activityId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/audit", method = RequestMethod.PUT)
    public String audit(Integer activityId,HttpServletRequest request) {//审核活动
        try {
            ValueUtil.verify(activityId, "activityId");
            return ValueUtil.toJson(HttpStatus.SC_CREATED, activityService.audit(activityId,request));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
    @RequestMapping(value = "/reject", method = RequestMethod.PUT)
    public String reject(Integer activityId,HttpServletRequest request) {//驳回活动
        try {
            ValueUtil.verify(activityId, "activityId");
            return ValueUtil.toJson(HttpStatus.SC_CREATED, activityService.reject(activityId,request));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestParam Map<String, String> param,HttpServletRequest request) {//修改保存活动
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,activityService.updateActivity(param,request),null);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/10
    *@Param
    *@Description:获取活动类型
    */
    @RequestMapping(value = "/type",method = RequestMethod.GET)
    public String type(String type) {
        return ValueUtil.toJson(activityService.findByType(type));
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/16
    *@Param
    *@Description:添加活动商品
    */
    @RequestMapping(value = "/addGoods",method = RequestMethod.POST)
    public String addGoods(Integer activityId,String goodsJson) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,activityService.addGoods(activityId,goodsJson));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/delGoods",method = RequestMethod.DELETE)
    public String delGoods(Integer id) {
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,activityService.delGoods(id));
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/16
    *@Param
    *@Description:根据活动id 及商品名查看活动下的商品列表
    */
    @RequestMapping(value = "/getGoods",method = RequestMethod.GET)
    public String getGoods(Integer activityId,String goodsName,Integer pageNo,Integer pageSize) {
        return ValueUtil.toJson(activityService.getGoods(activityId,goodsName,pageNo,pageSize));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) throws MapperException {//查询分页活动
        MapUtil.cleanNull(params);
        if (id != null) {
            Activity activity = activityService.findOne(id);
//                ValueUtil.verifyNotExist(activity, "该活动不存在！");
//                JSONObject object = JSON.parseObject(ValueUtil.getFromJson(ValueUtil.toJson(activity),"data"));
//                object.put("activityGoods",useService.getActivityGoods(activity.getId(),pageNo,pageSize));
//                object.toJSONString();
            return ValueUtil.toJson(activity);
        }
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(activityService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }

        if(null != params.get("status")&&!params.get("status").toString().equals("")){
            ActivityStatus.getValue(params.get("status").toString());
            params.put("status",ActivityStatus.getValue(params.get("status").toString()));
        }

        params.put("isDelete", DeleteEnum.NOT_DELETE);

        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK, activityService.findAll(pageModel));

    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/8/16
    *@Param
    *@Description:根据活动Id查看活动信息
    */
    @RequestMapping(value = "/itf",method = RequestMethod.GET)
    public String findOne(Integer id) throws MapperException {
        try {
            ValueUtil.verify(id,"id");
            Activity activity = activityService.findOne(id);
            return ValueUtil.toJson(activity);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}
