package com.yesmywine.cms.webController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.cms.dao.ArticleDao;
import com.yesmywine.cms.dao.ColumnDao;
import com.yesmywine.cms.entity.ArticleEntity;
import com.yesmywine.cms.entity.ColumnsEntity;
import com.yesmywine.cms.service.ArticleService;
import com.yesmywine.cms.service.ColumnService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by hz on 6/27/17.
 */
@RestController
@RequestMapping("/web/cms/column")
public class WebConlumnController {
    @Autowired
    private ColumnService columnService;
    @Autowired
    private ColumnDao columnDao;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleDao articleDao;

    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize){
        MapUtil.cleanNull(params);
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(articleService.findAll());
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
        return ValueUtil.toJson(HttpStatus.SC_OK,articleService.findAll(pageModel));
    }

    @RequestMapping(value = "/showHelp",method = RequestMethod.GET)
    public String showHelp(){//帮助中心下的文章
        List<ColumnsEntity> list1=columnDao.findByPId(1);
        JSONArray jsonArray=new JSONArray();
        for (ColumnsEntity c:list1){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",c.getId());
            jsonObject.put("label",c.getColumnsName());
            List title=articleDao.findIdAndTitle(c.getId());
            if(title.size()!=0){
                JSONArray jsonArray1=new JSONArray();
                for(int i=0;i<title.size();i++){
                    Object[] object=(Object[])title.get(i);
                    Integer id = (Integer)object[0];
                    String titles = object[1].toString();
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("id",id);
                    jsonObject1.put("title",titles);
                    jsonArray1.add(jsonObject1);
                }
                jsonObject.put("children",jsonArray1);
            }
            jsonArray.add(jsonObject);
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonArray);
    }
////            List<ArticleEntity> list=articleDao.findByColumnsEntity(c);
//            if(null!=list){
//                JSONArray jsonArray1=new JSONArray();
//                for(ArticleEntity a:list){
//                    JSONObject jsonObject1=new JSONObject();
////                    jsonObject1.put("articleContent",a.getArticleContent());
//                    jsonObject1.put("id",a.getId());
//                    jsonObject1.put("title",a.getTitle());
//                    jsonArray1.add(jsonObject1);
//                }
//                jsonObject.put("children",jsonArray1);
//            }
//            jsonArray.add(jsonObject);
//        }
//        return ValueUtil.toJson(HttpStatus.SC_OK,jsonArray);
//    }

    /**
     * 根据文章id查询接口
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String show(Integer id) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,articleService.show(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }

    @RequestMapping(value = "/getArticle", method = RequestMethod.GET)
    public String show(String code,Integer number) {//促销公告
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,articleService.showContent(code,number));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }

    }
    @RequestMapping(value = "/showColumn", method = RequestMethod.GET)
    public String showColumn() {
        JSONArray jsonArray=new JSONArray();
       List<ColumnsEntity> columnsEntity= columnDao.findByPId(0);
        for(ColumnsEntity columnsEntity1:columnsEntity){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("label",columnsEntity1.getColumnsName());
            jsonObject.put("value",columnsEntity1.getId());
            List<ColumnsEntity> list=columnDao.findByPId(columnsEntity1.getId());
            if(list.size()!=0){
                JSONArray jsonArray1=new JSONArray();
                jsonObject.put("children",jsonArray1);
                for(ColumnsEntity columnsEntity2:list){
                    JSONObject jsonObject1=new JSONObject();
                    jsonObject1.put("label",columnsEntity2.getColumnsName());
                    jsonObject1.put("value",columnsEntity2.getId());
                    jsonArray1.add(jsonObject1);
                }
            }
            jsonArray.add(jsonObject);
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonArray);
    }
}
