package com.yesmywine.user.webController;

import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.SynchronizeUtils;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: HS_JAVA
 * @description:
 * @Date: Created in 14:33 2017/11/17
 * Modified :
 */
@RestController
@RequestMapping("userservice/test1/itf")
public class Test1Cotroller {
    @RequestMapping( method = RequestMethod.GET)
    public String getnumber(){
        HttpBean httpRequest = new HttpBean("http://localhost:8080/userservice/test/itf", com.yesmywine.httpclient.bean.RequestMethod.get);
        httpRequest.addParameter("parentId", null);
        httpRequest.setStatusCode(100);
        try {
            httpRequest.run();
        }catch (Exception e){
            Threads.createExceptionFile("userservice",e.getMessage());
           return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
        String temp = httpRequest.getResponseContent();

       return temp;
    }
}