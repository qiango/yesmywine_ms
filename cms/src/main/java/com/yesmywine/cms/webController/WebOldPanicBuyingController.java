
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.HomePagePanicBuyingService;
import com.yesmywine.cms.service.OldPanicBuyingService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 老酒馆抢购
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/oldPanicBuying")
public class WebOldPanicBuyingController {

    @Autowired
    private OldPanicBuyingService oldPanicBuyingService;

//    @RequestMapping(method = RequestMethod.GET)
//    public String index() {   //查看
//        return ValueUtil.toJson(oldPanicBuyingService.getShuffling());
//    }


    @RequestMapping(method = RequestMethod.GET)
    public Object frontIndex(Integer pageNo, Integer pageSize) {   //查看
        Object all = oldPanicBuyingService.getShuffling(pageNo,pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }

}