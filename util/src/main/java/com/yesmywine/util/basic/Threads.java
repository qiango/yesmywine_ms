package com.yesmywine.util.basic;

/**
 * Created by hz on 11/14/17.
 */
public class Threads {

    public static void createExceptionFile(String module,String message){
        ExceptionThread exceptionThread=new ExceptionThread(module, message);
        Thread thread=new Thread(exceptionThread);
        thread.start();
    }
}
