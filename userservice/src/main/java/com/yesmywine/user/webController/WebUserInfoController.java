
package com.yesmywine.user.webController;

import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.dao.UserInformationDao;
import com.yesmywine.user.entity.UserInformation;
import com.yesmywine.user.service.UserInformationService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.Threads;
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


@RestController
@RequestMapping("/member/userservice/userInfomation")
public class WebUserInfoController {
    @Autowired
    private UserInformationService userInformationService;
    @Autowired
    private UserInformationDao userInformationDao;


    //  @Description   查询yonghu
    @RequestMapping(method = RequestMethod.GET)
    public String index(HttpServletRequest request) {
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
        UserInformation userInformation = userInformationDao.findOne(userId);
//        JSONObject jsonObject =ValueUtil.toJsonObject(userInformation);
//        List<ReceivingAddress> list = addressDao.findByUserId(userId);
//        jsonObject.put("receivingAddress",list);
          return ValueUtil.toJson(HttpStatus.SC_OK, userInformation);

    }

    @RequestMapping(value = "/upImg", method = RequestMethod.PUT)
    public String updateimg(HttpServletRequest request) {
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
        UserInformation userInformation = userInformationDao.findOne(userId);
        userInformation.setUserImg(null);

        String usercode = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,
                "/user/userInfo/syn",
                ValueUtil.toJson(userInformation), com.yesmywine.httpclient.bean.RequestMethod.post);
        if (ValueUtil.notEmpity(usercode) && usercode.equals("201")) {
            userInformation.setSynStatus(1);
        } else {
            userInformation.setSynStatus(0);
        }
        userInformationDao.save(userInformation);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String update(@RequestParam Map<String, String> params, HttpServletRequest request) {

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
        String nickName = params.get("nickName");
        if (ValueUtil.notEmpity(nickName)) {
            UserInformation userInformation1 = userInformationDao.findByNickName(nickName);
            if (ValueUtil.notEmpity(userInformation1)) {
                if (userInformation1.getId() != userId) {
                    return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR, "昵称已被使用", "erro");
                }
            }
        }

        UserInformation userInformation = userInformationDao.findOne(Integer.valueOf(params.get("id")));
        String imgIds = params.get("imgIds");
        String userImg = null;
        if (ValueUtil.notEmpity(imgIds)) {
            String[] imgArr = imgIds.split(";");
            Integer[] arr = new Integer[imgArr.length];
            for (int i = 0; i < imgArr.length; i++) {
                arr[i] = Integer.parseInt(imgArr[i]);
            }
            if (imgIds != null && !imgIds.equals("")) {
                try {
                    userImg = userInformationService.saveUserImg(Integer.parseInt(params.get("id")), arr);
                    userInformation.setUserImg(userImg);
                } catch (YesmywineException e) {
                    Threads.createExceptionFile("userservice",e.getMessage());
                    return ValueUtil.toError(e.getCode(),e.getMessage());
                }
            }
        }
        userInformation.setNickName(params.get("nickName"));
        userInformation.setRealName(params.get("realName"));
        userInformation.setBirthday(params.get("birthday"));
        userInformation.setFixedPhone(params.get("fixedPhone"));
        userInformation.setSex(params.get("sex"));
        String usercode = SynchronizeUtils.getCode(Dictionary.PAAS_HOST,
                "/user/userInfo/syn",
                ValueUtil.toJson(userInformation), com.yesmywine.httpclient.bean.RequestMethod.post);
        if (ValueUtil.notEmpity(usercode) && usercode.equals("201")) {
            userInformation.setSynStatus(1);
        } else {
            userInformation.setSynStatus(0);
        }
        userInformationDao.save(userInformation);
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

}

