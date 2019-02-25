package com.yesmywine.jwt.thread;

import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.OperateLogger;
import com.yesmywine.util.basic.SynchronizeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by by on 2017/7/3.
 */
public class LogThread implements Runnable{

    private String username;

    private String operation;

    public LogThread(String username,String operation){
        this.username = username;
        this.operation = operation;
    }

    public void run() {
        OperateLogger.doLog(username,operation);
    }
}
