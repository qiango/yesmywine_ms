package com.yesmywine.cms.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.dao.ArticleDao;
import com.yesmywine.cms.dao.ColumnDao;
import com.yesmywine.cms.entity.ArticleEntity;
import com.yesmywine.cms.entity.ColumnsEntity;
import com.yesmywine.cms.service.ArticleService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liqingqing on 2017/1/6.
 */
@RestController
@RequestMapping("/cms/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ColumnDao columnDao;
    @Autowired
    private ArticleDao articleDao;
    /**
     * 文章增加接口
     *
     * @param parm
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestParam Map<String, String> parm) {
        try {
            ValueUtil.verify(parm.get("columnsId"),"栏目不能为空");
            ValueUtil.verify(parm.get("title"),"标题不能为空");
            ValueUtil.verify(parm.get("articleContent"),"文章不能为空");
            return ValueUtil.toJson(HttpStatus.SC_CREATED,articleService.create(parm));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /**
     * 文章修改接口
     *
     * @param id
     * @param parm
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public String update( Integer id, @RequestParam Map<String, String> parm) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED,articleService.update(id, parm));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    /**
     * 文章删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,articleService.deletes(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
    /**
     * 文章查询接口
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id,String columnsName) {
        MapUtil.cleanNull(params);
        if(id!=null){
            return ValueUtil.toJson(HttpStatus.SC_OK,articleService.findOne(id));
        }
//        if(ValueUtil.notEmpity(columnsName)){
//            List<ColumnsEntity> columnsEntity=columnDao.findByColumnsNameContaining(columnsName);
//            List list=new ArrayList();
//            JSONObject jsonNew = new JSONObject();
//            int size=0;
//            for(ColumnsEntity c:columnsEntity) {
//                List<ArticleEntity> articleEntity = articleDao.findByColumnsEntity(c);
//                size=articleEntity.size()+size;
//                if (null == pageSize) {
//                    pageSize = 10;
//                }
//                if (null == pageNo) {
//                    pageNo = 1;
//                }
//                for (int i = (pageNo - 1) * pageSize; i < pageNo * pageSize; i++) {
//                    if (i > articleEntity.size() || i == articleEntity.size()) {
//                        break;
//                    }
//                    list.add(articleEntity.get(i));
//                }
//            }
//            jsonNew.put("content",list);
//            jsonNew.put("totalRows",size);
//            jsonNew.put("page",pageNo);
//            jsonNew.put("pageSize",pageSize);
//            int totalPages;
//            if(size<pageSize){
//                totalPages=1;
//            }else {
//                totalPages = size%pageSize==0?size/pageSize:size/pageSize+1;
//            }
//            jsonNew.put("totalPages",totalPages);
//            return ValueUtil.toJson(jsonNew);
//        }
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = articleService.findAll(pageModel);
//        PageModel pageModel = articleService.findByColumnName(pageNo,pageSize);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }


}