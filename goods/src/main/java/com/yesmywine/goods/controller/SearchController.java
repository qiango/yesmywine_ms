
package com.yesmywine.goods.controller;

import com.yesmywine.goods.service.SearchService;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hz on 2016/12/9.
 */
@RestController
@RequestMapping("/goods/search")
public class SearchController {
    @Autowired
    private SearchService searchService;


    @RequestMapping(method = RequestMethod.POST)
    public String synchronous() {
        try {
            searchService.insert();
            return ValueUtil.toJson(HttpStatus.SC_CREATED,"success");
        } catch (Exception e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(String goodsIds) {
        try {
            String[] split = goodsIds.split(",");
            for(String goodsId: split) {
                searchService.delete(Integer.valueOf(goodsId));
            }
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,"success");
        } catch (Exception e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return e.toString();
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String get(String keyWords,String screen, String sort, String order, Integer page, Integer rows) {
        try {
            Object search = searchService.searchGoods(keyWords, screen, sort,order, page, rows);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return e.toString();
        }
    }

    @RequestMapping(value = "/prop", method = RequestMethod.GET)
    public String getProp(String keyWords, String screen) {
        try {
            Object search = searchService.searchProp(keyWords, screen);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return e.toString();
        }
    }


    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String getCategory(String keyWords,String screen, String sort, String order, Integer page, Integer rows) {
        try {
            Object search = searchService.searchGoodsByCategoryId(keyWords, screen, sort,order, page, rows);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return e.toString();
        }
    }

    @RequestMapping(value = "/categoryProp", method = RequestMethod.GET)
    public String getCategoryProp(String keyWords, String screen) {
        try {
            Object search = searchService.searchPropByCategoryId(keyWords, screen);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            Threads.createExceptionFile("goods",e.getMessage());
            return e.toString();
        }
    }


}