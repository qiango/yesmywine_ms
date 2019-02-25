
package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.CacheService;
import com.yesmywine.cms.service.PlateService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类板块111
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/cms/cache")
public class CacheController {

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/homePage/itf", method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(cacheService.getGoods());
    }

    @RequestMapping(value = "/rank/itf", method = RequestMethod.GET)
    public Object indexRank(Integer pageNo, Integer pageSize) {   //查看
        Object all = cacheService.getGoodsFront(pageNo, pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
//        return ValueUtil.toJson(cacheService.getGoodsFront(pageNo, pageSize));
    }


}