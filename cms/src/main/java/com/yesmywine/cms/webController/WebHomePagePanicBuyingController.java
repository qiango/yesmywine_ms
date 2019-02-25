
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.HomePagePanicBuyingService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页抢购
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/homePagePanicBuying")
public class WebHomePagePanicBuyingController {

    @Autowired
    private HomePagePanicBuyingService homePagePanicBuyingService;

//    @RequestMapping(method = RequestMethod.GET)
//    public String index() {   //查看
//        Object shuffling = homePagePanicBuyingService.getShuffling();
//        if("erro".equals(shuffling.toString())){
//            return ValueUtil.toError("500", "erro");
//        }
//        return ValueUtil.toJson(shuffling);
//    }


    @RequestMapping(method = RequestMethod.GET)
    public Object frontIndex(Integer pageNo, Integer pageSize) {   //查看
        Object all = homePagePanicBuyingService.getShuffling(pageNo,pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }


}