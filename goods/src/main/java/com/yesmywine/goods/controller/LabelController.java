
package com.yesmywine.goods.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.dao.GoodsDao;
import com.yesmywine.goods.dao.GoodsLabelDao;
import com.yesmywine.goods.dao.LabelDao;
import com.yesmywine.goods.entity.Goods;
import com.yesmywine.goods.entity.GoodsLabel;
import com.yesmywine.goods.service.GoodsLabelService;
import com.yesmywine.goods.service.LabelService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/goods/label")
public class LabelController {
    @Autowired
    private LabelService tagService;
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private GoodsLabelService goodsLabelService;
    @Autowired
    private GoodsLabelDao goodsLabelDao;
    @Autowired
    private GoodsDao goodsDao;

    @RequestMapping(method = RequestMethod.GET)//单查多查(后台)
    public String backIndex(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) {
        MapUtil.cleanNull(params);
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(tagService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }
        if(ValueUtil.notEmpity(params.get("id"))){
            return ValueUtil.toJson(HttpStatus.SC_OK,tagService.findOne(Integer.parseInt(params.get("id").toString())));
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(HttpStatus.SC_OK,tagService.findAll(pageModel));

    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> params) {   //新增标签
        try {
            ValueUtil.verify(params.get("name"),"name");
            String result = tagService.create(params);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
            }else {
                return ValueUtil.toError("500",  result);
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/goodsLabel",method = RequestMethod.POST)
    public String goodsLabel(Integer labelId,String goodsIds) {   //新增标签商品
        try {
            ValueUtil.verify(labelId,"labelId");
            ValueUtil.verify(goodsIds,"goodsIds");
            String result = goodsLabelService.create(labelId,goodsIds);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
            }else {
                return ValueUtil.toError("500",  result);
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping(method = RequestMethod.PUT)
    public String put(@RequestParam Map<String, Object> params) {   //修改
        try {
            ValueUtil.verify(params.get("id"),"id");
            ValueUtil.verify(params.get("name"),"name");
            String result = tagService.put(params);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_CREATED, "success");
            }else {
                return ValueUtil.toError("500",  result);
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping( value = "/getGoodsId",method = RequestMethod.GET)
    public String getGoodsId(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer labelId,String goodsName) {
        MapUtil.cleanNull(params);
        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(goodsLabelService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }
        if(ValueUtil.notEmpity(labelId)&&ValueUtil.isEmpity(goodsName)){
            int size=0;
            List list=new ArrayList();
            JSONObject jsonNew = new JSONObject();
            List<GoodsLabel> byLabelId = goodsLabelDao.findByLabelId(labelId);
            for(GoodsLabel goodsLabel:byLabelId){
                goodsLabel.setGoodsName(goodsDao.findOne(goodsLabel.getGoodsId()).getGoodsName());
            }
            size=byLabelId.size()+size;
            if (null == pageSize) {
                pageSize = 10;
            }
            if (null == pageNo) {
                pageNo = 1;
            }
            for (int i = (pageNo - 1) * pageSize; i < pageNo * pageSize; i++) {
                if (i > byLabelId.size() || i == byLabelId.size()) {
                    break;
                }
                list.add(byLabelId.get(i));
            }
        jsonNew.put("content",list);
        jsonNew.put("totalRows",size);
        jsonNew.put("page",pageNo);
        jsonNew.put("pageSize",pageSize);
        int totalPages;
        if(size<pageSize){
            totalPages=1;
        }else {
            totalPages = size%pageSize==0?size/pageSize:size/pageSize+1;
        }
        jsonNew.put("totalPages",totalPages);
        return ValueUtil.toJson(jsonNew);
        }else if(ValueUtil.notEmpity(goodsName)){
            List<Goods> goods=goodsDao.findByGoodsNameContaining(goodsName);
            List<GoodsLabel> list = new ArrayList();
            JSONObject jsonNew = new JSONObject();
            if(goods.size()!=0) {
                for(Goods d:goods) {
                    Integer goodsId = d.getId();
                    GoodsLabel g = goodsLabelDao.findByLabelIdAndGoodsId(labelId, goodsId);
                    if (null!= g) {
                        g.setGoodsName(d.getGoodsName());
                        list.add(g);
                    }
                }
            }else {
                list.add(null);
            }
            jsonNew.put("content", list);
            return ValueUtil.toJson(HttpStatus.SC_OK, jsonNew);
        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = goodsLabelService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }

    @RequestMapping(value = "/deleteGoods",method = RequestMethod.DELETE)
    public String deleteGoods(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            String result = tagService.deleteGoods(id);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "success");
            }else {
                return ValueUtil.toError("500",  result);
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.DELETE)
    public String deleteLabel(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            String result = tagService.deleteLabel(id);
            if("success".equals(result)){
                return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT, "success");
            }else {
                return ValueUtil.toError("500",  result);
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}