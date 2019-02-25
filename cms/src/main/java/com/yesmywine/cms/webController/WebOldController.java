
package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.OldService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/old")
public class WebOldController {

    @Autowired
    private OldService oldService;

    @RequestMapping(method = RequestMethod.GET)
    public String getShuffling() {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, oldService.getShuffling());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


}