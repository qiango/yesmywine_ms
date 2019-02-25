package com.yesmywine.logistics.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.logistics.service.ShipperService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by wangdiandian on 2017/7/14.
 */
@RestController
@RequestMapping("/web/logistics/shippers")
public class WebShipperController {
    @Autowired
    private ShipperService shipperService;

    @RequestMapping(method = RequestMethod.GET)
    public String shippersName(String shipperCode) {//根据承运商code显示承运商
        try {
            ValueUtil.verify(shipperCode, "shipperCodeNull");
            return ValueUtil.toJson(HttpStatus.SC_OK, shipperService.shippersName(shipperCode));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
//    @RequestMapping( method = RequestMethod.GET)
//    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) throws YesmywineException {//查询供应商
//        MapUtil.cleanNull(params);
//        if(id!=null){
//            return ValueUtil.toJson(HttpStatus.SC_OK, shipperService.updateLoad(id));
//        }
//        params.put("deleteEnum", 0);
//        if(params.get("shipperType")!=null) {
//            if (ValueUtil.notEmpity(params.get("shipperType"))) {
//                String shipperType = params.get("shipperType").toString();
//                params.remove(params.remove("shipperType").toString());
//                params.put("shipperType", shipperType);
//            }
//        }
//        if(params.get("status")!=null) {
//            if (ValueUtil.notEmpity(params.get("status"))) {
//                String status = params.get("status").toString();
//                params.remove(params.remove("status").toString());
//                params.put("status", status);
//            }
//        }
//        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
//            return ValueUtil.toJson(shipperService.findAll());
//        }else if(null!=params.get("all")){
//            params.remove(params.remove("all").toString());
//        }
//        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
//        if (null != params.get("showFields")) {
//            pageModel.setFields(params.remove("showFields").toString());
//        }
//        if (pageNo != null) params.remove(params.remove("pageNo").toString());
//        if (pageSize != null) params.remove(params.remove("pageSize").toString());
//        pageModel.addCondition(params);
//        pageModel = shipperService.findAll(pageModel);
//        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
//    }
}
