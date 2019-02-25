package com.yesmywine.cms.webController;

import com.yesmywine.cms.service.BoutiqueService;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页精品闪购
 * Created by wangdiandian on 2017/6/1.
 */
@RestController
@RequestMapping("/web/cms/boutique")
public class WebBoutiqueController {

    @Autowired
    private BoutiqueService boutiqueService;

    @RequestMapping(method = RequestMethod.GET)
    public Object frontIndex(Integer pageNo, Integer pageSize) {   //查看
        Object all = boutiqueService.FrontfindAll(pageNo,pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }

}
