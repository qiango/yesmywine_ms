package com.yesmywine.push.controller;

import com.yesmywine.push.service.UserChannelService;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/push/user")
public class UserChannelController {

    @Autowired
    private UserChannelService userChannelService;


    @RequestMapping(method = RequestMethod.POST)
    public String create(String userId, String channelId, Integer deviceType) {
        try {
            ValueUtil.verify(userId,"userId");
            ValueUtil.verify(channelId,"channelId");
            ValueUtil.verify(deviceType,"deviceType");
            String create = this.userChannelService.create(userId, channelId, deviceType);
            if("success".equals(create)) {
                return ValueUtil.toJson(HttpStatus.SC_CREATED, create);
            }
            return ValueUtil.toError("500", create);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500", "erro");
    }


    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(String userId, String channelId) {
        try {
            ValueUtil.verify(userId,"userId");
            ValueUtil.verify(channelId,"channelId");
            String delete = this.userChannelService.delete(userId, channelId);
            if("success".equals(delete)) {
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
            }
            return ValueUtil.toError("500", delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ValueUtil.toError("500", "erro");
    }

}
