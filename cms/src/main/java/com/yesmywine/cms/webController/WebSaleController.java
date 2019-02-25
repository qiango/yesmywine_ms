package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.SaleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 名庄特卖
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/web/cms/sale")
public class WebSaleController {
    @Autowired
    private SaleService saleService;

    @RequestMapping(method = RequestMethod.GET)
    public String getShuffling() {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, saleService.getShuffling());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
