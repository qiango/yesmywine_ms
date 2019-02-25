package com.yesmywine.user.webController;

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
@RequestMapping("userservice/test/itf")
public class Test2Controller {
    @RequestMapping( method = RequestMethod.GET)
    public String getnumber(){
        Integer a =0;
        for (int i = 0; i <1000000000 ; i++) {
            a+=i;
        }
        return a.toString();
    }
}
