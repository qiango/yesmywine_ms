package com.yesmywine.goods.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.bean.DeleteEnum;
import com.yesmywine.goods.bean.SupplierTypeEnum;
import com.yesmywine.goods.dao.SupplierDao;
import com.yesmywine.goods.service.SupplierService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by wangdiandian on 2017/3/16.
 */
@RestController
@RequestMapping("/goods/suppliers")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private SupplierDao supplierDao;



    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params,Integer pageNo,Integer pageSize,Integer id) throws  Exception{//分页查询供应商

        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK, supplierService.updateLoad(id));
        }
        if(params.get("deleteEnum")!=null) {
            params.remove(params.remove("deleteEnum").toString());
            params.put("deleteEnum", DeleteEnum.NOT_DELETE);
        }
        if(params.get("supplierType")!=null) {
            if (ValueUtil.notEmpity(params.get("supplierType"))) {
                String supplierType = params.get("supplierType").toString();
                params.remove(params.remove("supplierType").toString());
                switch (supplierType) {
                    case "distribution":
                        params.put("supplierType", SupplierTypeEnum.distribution);
                        break;
                    case "consignment":
                        params.put("supplierType", SupplierTypeEnum.consignment);
                        break;
                    case "seaAmoy":
                        params.put("supplierType", SupplierTypeEnum.seaAmoy);
                        break;
                }
            }
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(supplierService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = supplierService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }

    @RequestMapping(value = "/notDelete/show", method = RequestMethod.GET)
    public String list() {   //查看未被删除
        return ValueUtil.toJson(HttpStatus.SC_OK,supplierDao.findByDeleteEnum(DeleteEnum.NOT_DELETE));
    }

    @RequestMapping(value = "/synchronous", method = RequestMethod.POST)
    public String synchronous(@RequestParam Map<String,String> map){
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,supplierService.synchronous(map));
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(HttpStatus.SC_NO_CONTENT,"Erro");
        }
    }
}

