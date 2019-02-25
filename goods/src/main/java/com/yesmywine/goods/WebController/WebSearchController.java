
package com.yesmywine.goods.WebController;

import com.yesmywine.goods.service.SearchService;
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
@RequestMapping("/web/goods/search")
public class WebSearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping(method = RequestMethod.GET)
    public String get(String keyWords,String screen, String sort, String order, Integer page, Integer rows) {
        try {
            ValueUtil.verify(keyWords,"keyWords");
            Object search = searchService.searchGoods(keyWords, screen, sort,order, page, rows);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            return e.toString();
        }
    }

    @RequestMapping(value = "/prop", method = RequestMethod.GET)
    public String getProp(String keyWords, String screen) {
        try {
            ValueUtil.verify(keyWords,"keyWords");
            Object search = searchService.searchProp(keyWords, screen);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            return e.toString();
        }
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String getCategory(String keyWords,String screen, String sort, String order, Integer page, Integer rows) {
        try {
            ValueUtil.verify(keyWords,"keyWords");
            Object search = searchService.searchGoodsByCategoryId(keyWords, screen, sort,order, page, rows);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            return e.toString();
        }
    }

    @RequestMapping(value = "/categoryProp", method = RequestMethod.GET)
    public String getCategoryProp(String keyWords, String screen) {
        try {
            ValueUtil.verify(keyWords,"keyWords");
            Object search = searchService.searchPropByCategoryId(keyWords, screen);
            return ValueUtil.toJson(HttpStatus.SC_OK,search);
        } catch (Exception e) {
            return e.toString();
        }
    }

}