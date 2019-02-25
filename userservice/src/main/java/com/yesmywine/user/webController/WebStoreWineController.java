package com.yesmywine.user.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.service.StoreWineFlowService;
import com.yesmywine.user.service.StoreWineService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/8/10.
 */
@RestController
@RequestMapping("/member/userservice/storeWine")
public class WebStoreWineController {
    @Autowired
    private StoreWineService storeWineService;
    @Autowired
    private StoreWineFlowService storeWineFlowService;

    //个人可提酒库
    @RequestMapping(value = "/myself/extract", method = RequestMethod.GET)
    public String myselfExtract(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        MapUtil.cleanNull(params);
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if (ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
//        Integer userId = 1;
        params.put("userId", userId);
        params.put("isOver", "0");
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = storeWineService.findAll(pageModel);
//        return ValueUtil.toJson(pageModel);
        try {
            return ValueUtil.toJson(storeWineService.page(pageModel));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //个已酒库
    @RequestMapping(value = "/myself/flow", method = RequestMethod.GET)
    public String myselfFlow(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, HttpServletRequest request) {
        MapUtil.cleanNull(params);
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if (ValueUtil.isEmpity(userId)) {
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
//        Integer userId = 1;
        params.put("userId", userId);
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = storeWineFlowService.findAll(pageModel);
//        return ValueUtil.toJson(pageModel);
        try {
            return ValueUtil.toJson(storeWineFlowService.page(pageModel));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //安卓网页显示提酒数据
    @RequestMapping(value = "/load", method = RequestMethod.GET)
    public String getLoadItf(String json, HttpServletRequest request) {
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        return storeWineService.show(userId, json);
    }


}
