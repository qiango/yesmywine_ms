package com.yesmywine.push.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.yesmywine.push.entity.ConstantData;
import com.yesmywine.push.entity.UserChannel;
import com.yesmywine.push.service.PushMsgToSingleDeviceService;
import org.springframework.stereotype.Service;

/**
 * 向单个设备推送消息
 * Created by light on 2017/4/12.
 */
@Service
public class PushMsgToSingleDeviceServiceImpl implements PushMsgToSingleDeviceService {

    public Object pushMsgToSingleDeviceAnd(String channelId, String title, Integer relevancyType) throws PushClientException,PushServerException {
        /*1. 创建PushKeyPair
         *用于app的合法身份认证
         *apikey和secretKey可在应用详情中获取
         */
        String apiKey = ConstantData.Android_apiKey;
        String secretKey = ConstantData.Android_secretKey;
        PushKeyPair pair = new PushKeyPair(apiKey,secretKey);

        // 2. 创建BaiduPushClient，访问SDK接口
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. 注册YunLogHandler，获取本次请求的交互信息
        pushClient.setChannelLogHandler (new YunLogHandler() {
            @Override
            public void onHandle (YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("relevancyType", relevancyType);

        try {
            // 4. 设置请求参数，创建请求实例
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
                    addChannelId(channelId).
                    addMsgExpires(new Integer(3600)).   //设置消息的有效时间,单位秒,默认3600*5.
                    addMessageType(0).              //设置消息类型,0表示透传消息,1表示通知,默认为0.
                    addMessage(jsonObject.toString())
//                    add("{\"title\":\"TEST\",\"description\":\"Hello Baidu push!\"}").
                    .addDeviceType(3);      //设置设备类型，deviceType => 1 for web, 2 for pc,
            //3 for android, 4 for ios, 5 for wp.
            // 5. 执行Http请求
            PushMsgToSingleDeviceResponse response = pushClient.
                    pushMsgToSingleDevice(request);
            // 6. Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId()
                    + ",sendTime: " + response.getSendTime());
        } catch (PushClientException e) {
            //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
            //'true' 表示抛出, 'false' 表示捕获。
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


    public Object pushMsgToSingleDeviceIOS(String channelId, String title, Integer relevancyType) throws PushClientException,PushServerException {
        /*1. 创建PushKeyPair
         *用于app的合法身份认证
         *apikey和secretKey可在应用详情中获取
         */
        String apiKey = ConstantData.IOS_apiKey;
        String secretKey = ConstantData.IOS_secretKey;
        PushKeyPair pair = new PushKeyPair(apiKey,secretKey);

        // 2. 创建BaiduPushClient，访问SDK接口
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. 注册YunLogHandler，获取本次请求的交互信息
        pushClient.setChannelLogHandler (new YunLogHandler() {
            @Override
            public void onHandle (YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("relevancyType", relevancyType);

        try {
            // 4. 设置请求参数，创建请求实例
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
                    addChannelId(channelId).
                    addMsgExpires(new Integer(3600)).   //设置消息的有效时间,单位秒,默认3600*5.
                    addMessageType(0).              //设置消息类型,0表示透传消息,1表示通知,默认为0.
                    addMessage(jsonObject.toString())
//                    add("{\"title\":\"TEST\",\"description\":\"Hello Baidu push!\"}").
                    .addDeviceType(4);      //设置设备类型，deviceType => 1 for web, 2 for pc,
            //3 for android, 4 for ios, 5 for wp.
            // 5. 执行Http请求
            PushMsgToSingleDeviceResponse response = pushClient.
                    pushMsgToSingleDevice(request);
            // 6. Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId()
                    + ",sendTime: " + response.getSendTime());
        } catch (PushClientException e) {
            //ERROROPTTYPE 用于设置异常的处理方式 -- 抛出异常和捕获异常,
            //'true' 表示抛出, 'false' 表示捕获。
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
    public String pushMsgToSingleDevice(UserChannel userChannel, String title, Integer relevancyType) throws PushClientException, PushServerException {
        try {
            if (3 == userChannel.getDeviceType()) {
                this.pushMsgToSingleDeviceAnd(userChannel.getChannelId(), title, relevancyType);
            } else if (4 == userChannel.getDeviceType()) {
                this.pushMsgToSingleDeviceIOS(userChannel.getChannelId(), title, relevancyType);
            }else {
                return "没有此设备类型";
            }
        }catch (Exception e){
            return "erro";
        }
        return "success";
    }
}
