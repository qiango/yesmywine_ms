package com.yesmywine.evaluation.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.evaluation.service.ServiceEvaService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by hz on 6/25/17.
 */
@RestController
@RequestMapping("/web/evaluation/service")
public class WebServiceController {

    @Autowired
    private ServiceEvaService serviceEvaService;


    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) {
        MapUtil.cleanNull(params);
        if (id != null) {
              return ValueUtil.toJson(HttpStatus.SC_OK,serviceEvaService.findOne(id));
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(serviceEvaService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(null == pageNo ? 1 : pageNo, null == pageSize ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (null != pageNo) params.remove(params.remove("pageNo").toString());
        if (null != pageSize) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK,this.serviceEvaService.findAll(pageModel));
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> params, HttpServletRequest request) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.serviceEvaService.create(params, request));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
}
