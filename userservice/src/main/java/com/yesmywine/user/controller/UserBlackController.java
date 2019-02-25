package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.entity.UserBlack;
import com.yesmywine.user.service.UserBlackService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/5.
 */
@RestController
@RequestMapping("/userservice/userBlack")
public class UserBlackController {

    @Autowired
    private UserBlackService userBlackService;

    // 查询黑名单用户列表
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer userId) {
        MapUtil.cleanNull(params);
        if (ValueUtil.isEmpity(userId)) {
            if (null != params.get("all") && params.get("all").toString().equals("true")) {
                return ValueUtil.toJson(userBlackService.findAll());
            } else if (null != params.get("all")) {
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            for (String key : params.keySet()) {
                if (ValueUtil.isEmpity(params.get(key))) {
                    params.remove(key);
                }
            }
            pageModel.addCondition(params);
            pageModel = userBlackService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        } else {
            UserBlack isBlackUser = userBlackService.findByUserId(userId);
            return ValueUtil.toJson(HttpStatus.SC_OK, isBlackUser);
        }

    }

    //  *@Description 加入黑名单
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public String disable(Integer userId, String reason, HttpServletRequest request) {
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
            if (ValueUtil.isEmpity(userInfo)) {
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

        try {
            return userBlackService.disable(userId, reason);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //       *@Description 修改黑名单原因
    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer userId, String reason, HttpServletRequest request) {
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        if (ValueUtil.isEmpity(userInfo)) {
            return "未登录";
        }
        try {
            return userBlackService.update(userId, reason);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //    @Description 解除黑名单状态
    @RequestMapping(value = "/recover", method = RequestMethod.PUT)
    public String recover(String userIdList, HttpServletRequest request) {
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        if (ValueUtil.isEmpity(userInfo)) {
            return "未登录";
        }
        try {
            return userBlackService.recover(userIdList);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //   判断是否是黑名单用户
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String isBlack(HttpServletRequest request) {
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
        try {
            return userBlackService.isBlack(userId);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}
