package com.yesmywine.cms.controller;

import com.yesmywine.cms.service.SalePositionService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangdiandian on 2017/5/26.
 */
@RestController
@RequestMapping("/cms/salePosition")
public class SalePositionController {
    @Autowired
    private SalePositionService salePositionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index() {   //查看
        return ValueUtil.toJson(salePositionService.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(Integer positionId) {   //新增
        try {
            ValueUtil.verify(positionId,"positionId");
            String insert = this.salePositionService.insert(positionId);
            if(!"success".equals(insert)){
                return ValueUtil.toError("500", insert);
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, insert);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


        @RequestMapping(method = RequestMethod.PUT)
        public String update(Integer id, Integer positionId) {   //修改
            try {
                ValueUtil.verify(id,"id");
                ValueUtil.verify(positionId,"positionId");
                String update = this.salePositionService.update(id, positionId);
                if(!"success".equals(update)){
                    return ValueUtil.toError("500", update);
                }
                return ValueUtil.toJson(HttpStatus.SC_CREATED, update);
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(),e.getMessage());
            }
        }

        @RequestMapping( method = RequestMethod.DELETE)
        public String deleteFirst(Integer id) {
            try {
                ValueUtil.verify(id,"id");
                String delete = this.salePositionService.delete(id);
                if(!"success".equals(delete)){
                    return ValueUtil.toError("500", delete);
                }
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, delete);
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(),e.getMessage());
            }
        }
    }

