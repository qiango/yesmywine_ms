package com.yesmywine.util.basic;


/**
 * Created by hz on 11/14/17.
 */
public class ExceptionThread implements Runnable{

    private String module;
    private String message;

    public ExceptionThread(String module,String message){
        this.module=module;
        this.message=message;
    }
    public void run() {
        OperateLogger.except(module,message);
    }
}
