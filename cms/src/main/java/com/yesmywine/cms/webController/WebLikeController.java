
package com.yesmywine.cms.webController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.GoodsDao;
import com.yesmywine.cms.dao.LikeSecentDao;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.service.LikeService;
import com.yesmywine.cms.service.LikeServiceSecond;
import com.yesmywine.util.basic.RestJson;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页猜你喜欢
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/web/cms/like")
public class WebLikeController {

    @Autowired
    private LikeService likeService;
    @Autowired
    private LikeServiceSecond likeServiceSecond;
    @Autowired
    private LikeSecentDao likeSecentDao;
    @Autowired
    private GoodsDao goodsDao;

    @RequestMapping(method = RequestMethod.GET)
    public Object frontIndex(Integer pageNo, Integer pageSize) {   //查看
        Object all = likeService.FrontfindAll(pageNo,pageSize);
        RestJson restJson = new RestJson();
        restJson.setCode("200");
        restJson.setMsg("success");
        restJson.setData(all);
        return restJson;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Integer pageNo, Integer pageSize) {
        List<Integer> articleEntity=likeSecentDao.findByDistinct();
        if(null==pageSize){
            pageSize=10;
        }
        if(null==pageNo){
            pageNo=1;
        }
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonNew=new JSONObject();
        List list=new ArrayList();
        for(int i=(pageNo-1)*pageSize;i<pageNo*pageSize;i++){
            if(i>articleEntity.size()||i==articleEntity.size()){
                break;
            }
            JSONObject jsonNew1=new JSONObject();
            Integer goodsId=articleEntity.get(i);
            Goods goods=goodsDao.findOne(goodsId);
            jsonNew1.put("goodsId",goodsId);
            jsonNew1.put("goodsImage",goods.getPicture());
            jsonNew1.put("goodsName",goods.getGoodsName());
            jsonNew1.put("price",goods.getPrice());
            jsonNew1.put("salePrice",goods.getSalePrice());
            jsonArray.add(jsonNew1);
        }
        jsonNew.put("content",jsonArray);
        jsonNew.put("totalRows",articleEntity.size());
        jsonNew.put("page",pageNo);
        jsonNew.put("pageSize",pageSize);
        int totalPages;
        if(articleEntity.size()<pageSize){
            totalPages=1;
        }else {
            totalPages = articleEntity.size()%pageSize==0?articleEntity.size()/pageSize:articleEntity.size()/pageSize+1;
        }
        jsonNew.put("totalPages",totalPages);
        return ValueUtil.toJson(HttpStatus.SC_OK,jsonNew);
    }


}