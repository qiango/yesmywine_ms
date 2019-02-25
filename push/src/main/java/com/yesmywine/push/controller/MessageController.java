package com.yesmywine.push.controller;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.push.dao.UserChannelDao;
import com.yesmywine.push.entity.Message;
import com.yesmywine.push.entity.UserChannel;
import com.yesmywine.push.service.MessageService;
import com.yesmywine.push.service.PushMsgToAllService;
import com.yesmywine.push.service.PushMsgToSingleDeviceService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.catalina.User;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/push/message")
public class MessageController {

    @Autowired
    private PushMsgToAllService pushMsgToAllService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PushMsgToSingleDeviceService pushMsgToSingleDeviceService;
    @Autowired
    private UserChannelDao userChannelDao;


    @RequestMapping(method = RequestMethod.GET)
    public String page(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {   //查看
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }

        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(messageService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }

        if(ValueUtil.notEmpity(params.get("id"))){
            return ValueUtil.toJson(HttpStatus.SC_OK,messageService.findOne(Integer.valueOf(params.get("id").toString())));
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK,messageService.findAll(pageModel));
    }


//    @RequestMapping(method = RequestMethod.GET)
//    public String get(Integer id, Integer status) {
//        try {
//            if(ValueUtil.notEmpity(id)) {
//                return ValueUtil.toJson(HttpStatus.SC_OK,this.messageService.findOne(id));
//            }
//            return ValueUtil.toJson(HttpStatus.SC_OK, this.messageService.findAll(status));
//        } catch (ExceptionThread e) {
//            e.printStackTrace();
//        }
//        return ValueUtil.toError("500", "erro");
//    }


    @RequestMapping(method = RequestMethod.POST)
    public String create(Message message) {
        try {
            String create = this.messageService.create(message);
            if("success".equals(create)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, create);
            }
            return ValueUtil.toError("500", create);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500", "erro");
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String update(Message message) {
        try {
            String update = this.messageService.update(message);
            if("success".equals(update)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            }
            return ValueUtil.toError("500", update);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500", "erro");
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            String delete = this.messageService.delete(id);
            if("success".equals(delete)) {
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
            }
            return ValueUtil.toError("500", delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500", "erro");
    }



    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public String save(Integer id) {
        try {
            this.pushMsgToAllService.pushMsgToAllAnd(id);
            this.pushMsgToAllService.pushMsgToAllIOS(id);
        } catch (PushClientException e) {
            e.printStackTrace();
        } catch (PushServerException e) {
            e.printStackTrace();
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }


    @RequestMapping(value = "/push/itf", method = RequestMethod.POST)
    public String saveItf(Integer id) {
        try {
            this.pushMsgToAllService.pushMsgToAllAnd(id);
            this.pushMsgToAllService.pushMsgToAllIOS(id);
        } catch (PushClientException e) {
            e.printStackTrace();
        } catch (PushServerException e) {
            e.printStackTrace();
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }


    @RequestMapping(value = "/pushToSingle", method = RequestMethod.POST)
    public String pushToSingle(String userId, String title, Integer relevancyType) {
        try {
            List<UserChannel> byUserId = this.userChannelDao.findByUserId(userId);
            if(byUserId.size()==0){
                return ValueUtil.toError("500", "erro", "此用户没有绑定设备");
            }
            for(UserChannel userChannel: byUserId){
                this.pushMsgToSingleDeviceService.pushMsgToSingleDevice(userChannel, title, relevancyType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
    }

}
