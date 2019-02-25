package com.yesmywine.push.service.Impl;


import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.*;
import com.yesmywine.push.service.PushMsgToSingleDeviceService;
import com.yesmywine.push.service.QueryMsgStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 查询消息推送状态，包括成功、失败、待发送、发送中4种状态。
 * Created by light on 2017/4/12.
 */
@Service
public class QueryMsgStatusServiceImpl implements QueryMsgStatusService {

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
            String[] msgIds = { "xxxxxxxxxxxxxxxxxx" };
            QueryMsgStatusRequest request = new QueryMsgStatusRequest()
                    .addMsgIds(msgIds)
                    .addDeviceType(3);
            // 5. http request
            QueryMsgStatusResponse response = pushClient
                    .queryMsgStatus(request);
            // Http请求返回值解析
            System.out.println("totalNum: " + response.getTotalNum() + "\n"
                    + "result:");
            if (null != response) {
                List<?> list = response.getMsgSendInfos();
                for (int i = 0; i < list.size(); i++) {
                    Object object = list.get(i);
                    if (object instanceof MsgSendInfo) {
                        MsgSendInfo msgSendInfo = (MsgSendInfo) object;
                        StringBuilder strBuilder = new StringBuilder();
                        strBuilder.append("List[" + i + "]: {" + "msgId = "
                                + msgSendInfo.getMsgId() + ",status = "
                                + msgSendInfo.getMsgStatus() + ",sendTime = "
                                + msgSendInfo.getSendTime() + ",successCount = "
                                + msgSendInfo.getSuccessCount());
                        strBuilder.append("}\n");
                        System.out.println(strBuilder.toString());
                    }
                }
            }
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
