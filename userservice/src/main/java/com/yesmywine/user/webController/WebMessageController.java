package com.yesmywine.user.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.dao.MessageDao;
import com.yesmywine.user.entity.Notices;
import com.yesmywine.user.service.NoticesService;
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
 * Created by ${shuang} on 2017/6/30.
 */

@RestController
@RequestMapping("member/userservice/message")
public class WebMessageController {

     @Autowired
     private NoticesService noticesService;
     @Autowired
     private MessageDao messageDao;


    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
               ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

        MapUtil.cleanNull(params);
        params.put("userId",userId);
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(noticesService.findAll());
            }else  if(null!=params.get("all")){
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            for (String key :params.keySet()) {
                if(ValueUtil.isEmpity(params.get(key))){
                    params.remove(key);
                }
            }
            pageModel.addCondition(params);
            pageModel = noticesService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String index(Integer messageId,HttpServletRequest request) {
        Integer userId = null;
        try {
            userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
              ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

        Notices notices = messageDao.findByUserIdAndId(userId,messageId);
        if(ValueUtil.isEmpity(notices)){
            return ValueUtil.toJson(HttpStatus.SC_INTERNAL_SERVER_ERROR,"没有此物流消息");
        }
        notices.setStatus(1);
        messageDao.save(notices);
        return ValueUtil.toJson(HttpStatus.SC_OK,"success");
    }

}
