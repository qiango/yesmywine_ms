package com.yesmywine.push.service.Impl;


import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.*;
import com.yesmywine.push.service.CreateTagService;
import com.yesmywine.push.service.PushMsgToSingleDeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户定义的标签组信息。
 * Created by light on 2017/4/12.
 */
@Service
public class CreateTagServiceImpl implements CreateTagService {

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
            CreateTagRequest request = new CreateTagRequest().addTagName(
                    "xxxxx").addDeviceType(3);
            // 5. http request
            CreateTagResponse response = pushClient.createTag(request);
            System.out.println(String.format("tagName: %s, result: %d",
                    response.getTagName(), response.getResult()));
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



    public Object delete() throws PushClientException,PushServerException {
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
            DeleteTagRequest request = new DeleteTagRequest().addTagName(
                    "xxxxx").addDeviceType(new Integer(3));
            // 5. http request
            DeleteTagResponse response = pushClient.deleteTag(request);
            // Http请求返回值解析
            System.out.println(String.format("tagName: %s, result: %d",
                    response.getTagName(), response.getResult()));
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



    public Object addDevicesToTag () throws PushClientException,PushServerException {
        // 1. get apiKey and secretKey from developer console
        String apiKey = "xxxxxxxxxxxxxxxxxxxx";
        String secretKey = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        PushKeyPair pair = new PushKeyPair(apiKey, secretKey);

        // 2. build a BaidupushClient object to access released interfaces
        BaiduPushClient pushClient = new BaiduPushClient(pair,
                BaiduPushConstants.CHANNEL_REST_URL);

        // 3. register a YunLogHandler to get detail interacted information
        // in this request.
        pushClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            // 4. specify request arguments
            String[] channelIds = { "xxxxxxxxxxxxxxxxx" };
            AddDevicesToTagRequest request = new AddDevicesToTagRequest()
                    .addTagName("xxxxx").addChannelIds(channelIds)
                    .addDeviceType(3);
            // 5. http request
            AddDevicesToTagResponse response = pushClient
                    .addDevicesToTag(request);
            // Http请求返回值解析
            if (null != response) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("devicesInTag：{");
                List<?> devicesInfo = response.getDevicesInfoAfterAdded();
                for (int i = 0; i < devicesInfo.size(); i++) {
                    Object object = devicesInfo.get(i);
                    if (i != 0) {
                        strBuilder.append(",");
                    }
                    if (object instanceof DeviceInfo) {
                        DeviceInfo deviceInfo = (DeviceInfo) object;
                        strBuilder.append("{channelId:"
                                + deviceInfo.getChannelId() + ",result:"
                                + deviceInfo.getResult() + "}");
                    }
                }
                strBuilder.append("}");
                System.out.println(strBuilder.toString());
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



    public Object deleteDevicesFromTag () throws PushClientException,PushServerException {
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
            String[] channelIds = { "xxxxxxxxxxxxxxxxx" };
            DeleteDevicesFromTagRequest request = new DeleteDevicesFromTagRequest()
                    .addTagName("xxxxx").addChannelIds(channelIds)
                    .addDeviceType(3);
            // 5. http request
            DeleteDevicesFromTagResponse response = pushClient
                    .deleteDevicesFromTag(request);
            // Http请求返回值解析
            if (null != response) {
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("devicesInfoAfterDel:{");
                List<?> list = response.getDevicesInfoAfterDel();
                for (int i = 0; i < list.size(); i++) {
                    if (i != 0) {
                        strBuilder.append(",");
                    }
                    Object object = list.get(i);
                    if (object instanceof DeviceInfo) {
                        DeviceInfo deviceInfo = (DeviceInfo) object;
                        strBuilder.append("{channelId: "
                                + deviceInfo.getChannelId() + ", result: "
                                + deviceInfo.getResult() + "}");
                    }
                }
                strBuilder.append("}");
                System.out.println(strBuilder.toString());
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
