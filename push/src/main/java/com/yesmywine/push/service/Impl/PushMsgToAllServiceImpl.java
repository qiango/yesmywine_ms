package com.yesmywine.push.service.Impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.push.dao.MessageDao;
import com.yesmywine.push.entity.ConstantData;
import com.yesmywine.push.entity.Message;
import com.yesmywine.push.service.MessageService;
import com.yesmywine.push.service.PushMsgToAllService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 推送消息给所有设备，即广播推送。
 * Created by light on 2017/4/12.
 */
@Service
public class PushMsgToAllServiceImpl implements PushMsgToAllService {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MessageDao messageDao;

    public Object pushMsgToAllAnd(Integer id) throws PushClientException,PushServerException {
        // 1. get apiKey and secretKey from developer console
        String apiKey = ConstantData.Android_apiKey;
        String secretKey = ConstantData.Android_secretKey;
        PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacting information
        // in this request.
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            Message one = this.messageService.findOne(id);
            if(ValueUtil.isEmpity(one)){
                return "没有此消息";
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", one.getTitle());
            jsonObject.put("relevance", one.getRelevance());
            jsonObject.put("relevancyType", one.getRelevancyType());

            String template = null;
            if(1==one.getRelevancyType()){
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/cms/activity/template", RequestMethod.get);
                httpRequest.addParameter("id", one.getRelevance());
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String cd = "";
                try {
                    cd = ValueUtil.getFromJson(temp, "code");
                } catch (Exception e) {
                    return "erro";
                }
                if (!"200".equals(cd) || ValueUtil.isEmpity(cd)) {
                    return "erro";
                } else {
                    template = ValueUtil.getFromJson(temp, "data", "templateId");

                }
            }

            jsonObject.put("template", template);
            // 4. specify request arguments
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                    .addMsgExpires(new Integer(3600)).addMessageType(0)
                    .addMessage(jsonObject.toString())
                    // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
                    .addSendTime(System.currentTimeMillis() / 1000 + 70).
                            addDeviceType(3);
            // 5. http request
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            // Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());

            one.setStatus(1);
            this.messageDao.save(one);
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMsg: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
        return "success";
    }

    @Override
    public Object pushMsgToAllIOS(Integer id) throws PushClientException, PushServerException {
        // 1. get apiKey and secretKey from developer console
        String apiKey = ConstantData.IOS_apiKey;
        String secretKey = ConstantData.IOS_secretKey;
        PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacting information
        // in this request.
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            Message one = this.messageService.findOne(id);
            if(ValueUtil.isEmpity(one)){
                return "没有此消息";
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", one.getTitle());
            jsonObject.put("relevance", one.getRelevance());
            jsonObject.put("relevancyType", one.getRelevancyType());

            String template = null;
            if(1==one.getRelevancyType()){
                HttpBean httpRequest = new HttpBean(Dictionary.MALL_HOST + "/web/cms/activity/template", RequestMethod.get);
                httpRequest.addParameter("id", one.getRelevance());
                httpRequest.run();
                String temp = httpRequest.getResponseContent();
                String cd = "";
                try {
                    cd = ValueUtil.getFromJson(temp, "code");
                } catch (Exception e) {
                    return "erro";
                }
                if (!"200".equals(cd) || ValueUtil.isEmpity(cd)) {
                    return "erro";
                } else {
                    template = ValueUtil.getFromJson(temp, "data", "templateId");

                }
            }

            jsonObject.put("template", template);
            // 4. specify request arguments
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                    .addMsgExpires(new Integer(3600)).addMessageType(0)
                    .addMessage(jsonObject.toString())
                    // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例70秒后推送
                    .addSendTime(System.currentTimeMillis() / 1000 + 70).
                            addDeviceType(4);
            // 5. http request
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            // Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());

            one.setStatus(1);
            this.messageDao.save(one);
        } catch (PushClientException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                e.printStackTrace();
            }
        } catch (PushServerException e) {
            if (BaiduPushConstants.ERROROPTTYPE) {
                throw e;
            } else {
                System.out.println(String.format(
                        "requestId: %d, errorCode: %d, errorMsg: %s",
                        e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
            }
        }
        return "success";
    }

}
