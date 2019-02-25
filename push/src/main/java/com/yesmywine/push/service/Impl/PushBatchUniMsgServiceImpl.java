package com.yesmywine.push.service.Impl;


import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushBatchUniMsgRequest;
import com.baidu.yun.push.model.PushBatchUniMsgResponse;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.yesmywine.push.service.PushBatchUniMsgService;
import com.yesmywine.push.service.PushMsgToSingleDeviceService;
import org.springframework.stereotype.Service;

/**
 * 推送消息给批量设备（批量单播）。
 * Created by light on 2017/4/12.
 */
@Service
public class PushBatchUniMsgServiceImpl implements PushBatchUniMsgService {

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
            String[] channelIds = { "xxxxxxxxxxxxxxxx", "xxxxxxxxxxxxxxxxx" };
            PushBatchUniMsgRequest request = new PushBatchUniMsgRequest()
                    .addChannelIds(channelIds)
                    .addMsgExpires(new Integer(3600))
                    .addMessageType(1)
                    .addMessage("{\"title\":\"TEST\",\"description\":\"Hello Baidu push!\"}")
                    .addDeviceType(3).addTopicId("BaiduPush");// 设置类别主题
            // 5. http request
            PushBatchUniMsgResponse response = pushClient
                    .pushBatchUniMsg(request);
            // Http请求返回值解析
            System.out.println(String.format("msgId: %s, sendTime: %d",
                    response.getMsgId(), response.getSendTime()));
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
