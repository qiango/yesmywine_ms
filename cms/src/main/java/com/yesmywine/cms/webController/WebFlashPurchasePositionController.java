package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.FlashPurchasePositionService;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/web/cms/flashPurchasePosition")
public class WebFlashPurchasePositionController {
    @Autowired
    private FlashPurchasePositionService flashPurchasePositionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(flashPurchasePositionService.findAll());
    }
}
