package com.yesmywine.push.service.Impl;


import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToTagRequest;
import com.baidu.yun.push.model.PushMsgToTagResponse;
import com.yesmywine.push.service.PushMsgToSingleDeviceService;
import com.yesmywine.push.service.PushMsgToTagService;
import org.springframework.stereotype.Service;

/**
 * 向绑定到tag中的用户推送消息，即普通组播
 * Created by light on 2017/4/12.
 */
@Service
public class PushMsgToTagServiceImpl implements PushMsgToTagService {

    public Object pushMsgToSingleDevice() throws PushClientException,PushServerException {
        // 1. get apiKey and secretKey from developer console
        String apiKey = "xxxxxxxxxxxxxxxxxxxx";
        String secretKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
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
            // 4. specify request arguments
            // pushTagTpye = 1 for common tag pushing
            PushMsgToTagRequest request = new PushMsgToTagRequest()
                    .addTagName("xxxxx")
                    .addMsgExpires(new Integer(3600))
                    .addMessageType(1)
                    // .addSendTime(System.currentTimeMillis() / 1000 + 70).
                    .addMessage("{\"title\":\"TEST\",\"description\":\"Hello Baidu push!\"}")
                    .addDeviceType(3);
            // 5. http request
            PushMsgToTagResponse response = pushClient.pushMsgToTag(request);
            // Http请求返回值解析
            System.out.println("msgId: " + response.getMsgId() + ",sendTime: "
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());
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
