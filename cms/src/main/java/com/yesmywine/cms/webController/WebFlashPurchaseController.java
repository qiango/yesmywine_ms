package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.FlashPurchaseService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 闪购
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/web/cms/flashPurchase")
public class WebFlashPurchaseController {
    @Autowired
    private FlashPurchaseService flashPurchaseService;

    @RequestMapping(method = RequestMethod.GET)
    public String getShuffling() {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, flashPurchaseService.getShuffling());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public Object frontIndex(Integer pageNo, Integer pageSize) {   //查看
//        Object all = flashPurchaseService.getShuffling(pageNo,pageSize);
//        RestJson restJson = new RestJson();
//        restJson.setCode("200");
//        restJson.setMsg("success");
//        restJson.setData(all);
//        return restJson;
//    }

}

