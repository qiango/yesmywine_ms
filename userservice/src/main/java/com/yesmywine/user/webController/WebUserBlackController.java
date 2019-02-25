package com.yesmywine.user.webController;

import com.yesmywine.user.service.UserBlackService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ${shuang} on 2017/4/5.
 */
@RestController
@RequestMapping("/member/userservice/userBlack")
public class WebUserBlackController {

    @Autowired
    private UserBlackService userBlackService;


//   判断是否是黑名单用户
    @RequestMapping(value = "/check",method = RequestMethod.GET)
    public String isBlack(HttpServletRequest request ) {
        Integer userId =null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
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
