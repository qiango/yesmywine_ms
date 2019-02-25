
package com.yesmywine.goods.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.goods.bean.CanSearch;
import com.yesmywine.goods.bean.EntryMode;
import com.yesmywine.goods.entityProperties.Category;
import com.yesmywine.goods.entityProperties.Properties;
import com.yesmywine.goods.entityProperties.PropertiesValue;
import com.yesmywine.goods.service.ProService;
import com.yesmywine.goods.service.ProperValueService;
import com.yesmywine.goods.service.SearchService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.goods.dao.*;
import com.yesmywine.goods.entity.*;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

/**
 * Created by wangdiandian on 2016/12/9.
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService{

//    @Autowired
//    private HttpSolrServer httpSolrServer;
    @Autowired
    private SetSearchService setSearchService;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private PropertiesDao propertiesDao;
    @Autowired
    private ProperValueDao properValueDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private CategoryPropertyDao categoryPropertyDao;
    @Autowired
    private ProService proService;
    @Autowired
    private ProperValueService properValueService;

    public void insert() throws Exception{
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();
        Goods goods = new Goods();
        goods.setGoodsName("来一碗好酒好不好111");
        goods.setId(6622);
//        goods.setPrice("4444");
//        goods.setCategoryId(3);
//        goods.setGoodsEnName("lywhjhbh");
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("id", "100");
//        jsonObject.put("name", "30ea6aada526f5bcefd717ae4f276c14");
//        jsonArray.add(jsonObject);
//        goods.setGoodsImageUrl(jsonArray.toJSONString().replaceAll( "\"", "\'"));
//        goods.setSales(222);
//        goods.setSalePrice("4443");
//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("颜色", "黄色");
//        jsonObject1.put("味道", "苦");
////        goods.setPropString(jsonObject1.toJSONString().replaceAll( "\"", "\'"));
//        JSONArray jsonArray1 = new JSONArray();
//        jsonArray1.add(jsonObject1);
//        goods.setJsonArrayProp(jsonArray1);
        httpSolrServer.addBean(goods);
        httpSolrServer.commit();
    }


    public String delete(Integer goodsId) throws Exception{
        try {
            HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();
            httpSolrServer.deleteByQuery("id:" + goodsId);
            httpSolrServer.deleteById(goodsId.toString());
            httpSolrServer.commit();
        }catch (Exception e){
            return e.toString();
        }
        return "success";
    }


    public Object search(String keyWords, Integer page, Integer rows) throws Exception {
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();

        SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
//        solrQuery.setQuery("goodsName:"+ keyWords+" OR goodsEnName:"+keyWords+" OR categoryName:"+keyWords); // 搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
        solrQuery.setRows(rows);


        // 执行查询(根据goodsName查询)
        solrQuery.setQuery("goodsName:"+ keyWords);
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        long numFound = results.getNumFound();
        List<Goods> goods = queryResponse.getBeans(Goods.class);
        List<Goods> goodsEn = null;
        List<Goods> goodsCN = null;

//        if(numFound<rows){
            solrQuery.setQuery("goodsEnName:"+ keyWords);
            QueryResponse queryResponseEn = httpSolrServer.query(solrQuery);
            numFound = numFound + queryResponseEn.getResults().getNumFound();
            goodsEn = queryResponseEn.getBeans(Goods.class);
//        }

//        if(numFound<rows){
            solrQuery.setQuery("categoryName:"+ keyWords);
            QueryResponse queryResponseCN = httpSolrServer.query(solrQuery);
            List<Category> categories = queryResponseCN.getBeans(Category.class);
            if(categories.size()>0) {
                for (Category category : categories) {
                    goodsCN = goodsDao.findByCategoryId(category.getId());
                }
            }
//        }

        if(ValueUtil.notEmpity(goodsCN)){
            goods.addAll(goodsCN);
            numFound = numFound + goodsCN.size();
        }
        if(ValueUtil.notEmpity(goodsEn)){
            goods.addAll(goodsEn);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalRows", numFound);
        JSONArray jsonArray = new JSONArray();

//        List<Goods> listTemp= new ArrayList<Goods>();
//        Iterator<Goods> it=goods.iterator();
//        while(it.hasNext()){
//            Goods a=it.next();
//            if(listTemp.contains(a)){
//                it.remove();
//            }
//            else{
//                listTemp.add(a);
//            }
//        }
        for(int i=(page*rows);i<((page+1)*rows);i++){
            if(i==goods.size()){
                break;
            }
            Goods good = goods.get(i);
//            String propString = good.getPropString();
            JSONObject jsonObjectG = new JSONObject();
            jsonObjectG.put("id", good.getId());
            jsonObjectG.put("goodsName", good.getGoodsName());
            jsonObjectG.put("goodsEnName", good.getGoodsEnName());
            jsonObjectG.put("price", good.getPrice());
            jsonObjectG.put("image", good.getGoodsImageUrl());
            jsonObjectG.put("categoryId", good.getCategoryId());
            jsonObjectG.put("categoryName", good.getCategoryName());
            jsonArray.add(jsonObjectG);
        }
        jsonObject.put("goods", jsonArray);
        jsonObject.put("page", page);
        jsonObject.put("rows", rows);
        return jsonObject;
    }



    public Object searchGoods(String keyWords, String screen, String sort, String order, Integer page, Integer rows) throws Exception {
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();
        if(ValueUtil.isEmpity(page)){
            page=0;
        }
        if(ValueUtil.isEmpity(rows)){
            rows=10;
        }

        SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
        solrQuery.setQuery("goodsName:"+ keyWords+" OR goodsEnName:"+keyWords+" OR categoryName:"+keyWords); // 搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
//        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
//        solrQuery.setRows(rows);
        solrQuery.setStart(page*rows);
        solrQuery.setRows(rows);
        if(ValueUtil.notEmpity(screen)) {
            String[] split = screen.split(",");
            String goodsPropString = "goodsPropString:";
            for(int k=0;k<split.length;k++) {
                if(k==0) {
                    goodsPropString = goodsPropString + split[k];
                }else {
                    goodsPropString = goodsPropString + " AND goodsPropString:"+ split[k];
                }
            }
            solrQuery.set("fq", goodsPropString);
        }
        if(ValueUtil.notEmpity(sort)&& ValueUtil.notEmpity(order)) {
            solrQuery.set("sort", sort + " "+order);
        }
        // 执行查询(根据goodsName查询)
//        solrQuery.setQuery("goodsName:"+ keyWords);
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        long numFound = results.getNumFound();
        List<GoodsSearch> goods = null;
        try {
            goods = queryResponse.getBeans(GoodsSearch.class);
        }catch (Exception e){

        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalRows", numFound);
        JSONArray jsonArray = new JSONArray();

//        List<Goods> listTemp= new ArrayList<Goods>();
//        Iterator<Goods> it=goods.iterator();
//        while(it.hasNext()){
//            Goods a=it.next();
//            if(listTemp.contains(a)){
//                it.remove();
//            }
//            else{
//                listTemp.add(a);
//            }
//        }

        for(int i=0;i<results.size();i++){
            if(i==goods.size() || 0 == goods.size()){
                break;
            }
            GoodsSearch good = goods.get(i);
//            String propString = good.getPropString();
            JSONObject jsonObjectG = new JSONObject();
            jsonObjectG.put("id", good.getId());
            jsonObjectG.put("goodsName", good.getGoodsName());
            jsonObjectG.put("goodsEnName", good.getGoodsEnName());
            jsonObjectG.put("price", good.getPrice());
            jsonObjectG.put("salePrice", good.getSalePrice());
            jsonObjectG.put("comments", good.getComments());
            jsonObjectG.put("praise", good.getPraise());
            jsonObjectG.put("goStatus", good.getGoStatus());
            jsonObjectG.put("listedTime", good.getListedTime());
            jsonObjectG.put("sales", good.getSales());
            jsonObjectG.put("image", good.getGoodsImageUrl());
            jsonObjectG.put("categoryId", good.getCategoryId());
            jsonObjectG.put("categoryName", good.getCategoryName());
            jsonArray.add(jsonObjectG);
        }
        jsonObject.put("goods", jsonArray);
        jsonObject.put("page", page);
        jsonObject.put("rows", rows);
        return jsonObject;
    }


    public Object searchGoodsByCategoryId(String keyWords, String screen, String sort, String order, Integer page, Integer rows) throws Exception {
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();

        if(ValueUtil.isEmpity(page)){
            page=0;
        }
        if(ValueUtil.isEmpity(rows)){
            rows=10;
        }

        SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
        solrQuery.setQuery("categoryId:"+ keyWords+" OR categoryIdParent:"+keyWords+" OR categoryIdGrandpa:"+keyWords); // 搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
//        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
//        solrQuery.setRows(rows);
        solrQuery.setStart(page*rows);
        solrQuery.setRows(rows);
        if(ValueUtil.notEmpity(screen)) {
            String[] split = screen.split(",");
            String goodsPropString = "goodsPropString:";
            for(int k=0;k<split.length;k++) {
                if(k==0) {
                    goodsPropString = goodsPropString + split[k];
                }else {
                    goodsPropString = goodsPropString + " AND goodsPropString:"+ split[k];
                }
            }
            solrQuery.set("fq", goodsPropString);
        }
        if(ValueUtil.notEmpity(sort)&& ValueUtil.notEmpity(order)) {
            solrQuery.set("sort", sort + " "+order);
        }
        // 执行查询(根据goodsName查询)
//        solrQuery.setQuery("goodsName:"+ keyWords);
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        long numFound = results.getNumFound();
        List<GoodsSearch> goods = null;
        try {
            goods = queryResponse.getBeans(GoodsSearch.class);
        }catch (Exception e){

        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalRows", numFound);
        JSONArray jsonArray = new JSONArray();

//        List<Goods> listTemp= new ArrayList<Goods>();
//        Iterator<Goods> it=goods.iterator();
//        while(it.hasNext()){
//            Goods a=it.next();
//            if(listTemp.contains(a)){
//                it.remove();
//            }
//            else{
//                listTemp.add(a);
//            }
//        }

        for(int i=0;i<results.size();i++){
            if(i==goods.size() || 0 == goods.size()){
                break;
            }
            GoodsSearch good = goods.get(i);
//            String propString = good.getPropString();
            JSONObject jsonObjectG = new JSONObject();
            jsonObjectG.put("id", good.getId());
            jsonObjectG.put("goodsName", good.getGoodsName());
            jsonObjectG.put("goodsEnName", good.getGoodsEnName());
            jsonObjectG.put("price", good.getPrice());
            jsonObjectG.put("salePrice", good.getSalePrice());
            jsonObjectG.put("comments", good.getComments());
            jsonObjectG.put("praise", good.getPraise());
            jsonObjectG.put("goStatus", good.getGoStatus());
            jsonObjectG.put("listedTime", good.getListedTime());
            jsonObjectG.put("sales", good.getSales());
            jsonObjectG.put("image", good.getGoodsImageUrl());
            jsonObjectG.put("categoryId", good.getCategoryId());
            jsonObjectG.put("categoryName", good.getCategoryName());
            jsonArray.add(jsonObjectG);
        }
        jsonObject.put("goods", jsonArray);
        jsonObject.put("page", page);
        jsonObject.put("rows", rows);
        return jsonObject;
    }


    public Object searchPropByCategoryId(String keyWords, String screen) throws Exception {
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();

        SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
        solrQuery.setQuery("categoryId:"+ keyWords+" OR categoryIdParent:"+keyWords+" OR categoryIdGrandpa:"+keyWords); // 搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
//        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
        solrQuery.setRows(10000);
        if(ValueUtil.notEmpity(screen)) {
            String[] split = screen.split(",");
            String goodsPropString = "goodsPropString:";
            for(int k=0;k<split.length;k++) {
                if(k==0) {
                    goodsPropString = goodsPropString + split[k];
                }else {
                    goodsPropString = goodsPropString + " AND goodsPropString:"+ split[k];
                }
            }
            solrQuery.set("fq", goodsPropString);
        }
//        solrQuery.set("sort", sort + " desc");


        // 执行查询(根据goodsName查询)
//        solrQuery.setQuery("goodsName:"+ keyWords);
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        List<GoodsSearch> goods = null;
        try {
            goods = queryResponse.getBeans(GoodsSearch.class);
        }catch (Exception e){

        }

        List<Integer> props = new ArrayList<>();
        List<Properties> byCanSearch = this.propertiesDao.findByCanSearch(CanSearch.no);
        for(Properties properties: byCanSearch){
            props.add(properties.getId());
        }

        Map<String, String > map = new HashMap<>();
//        JSONObject map = new JSONObject();
        for(GoodsSearch goodsSearch:goods){
            String goodsPropCode = goodsSearch.getGoodsPropCode();
            String goodsCategoryCode = goodsSearch.getGoodsCategoryCode();
            String goodsSkuCode = goodsSearch.getGoodsSkuCode();
//            Integer categoryId = goodsSearch.getCategoryId();
            JSONObject jsonArrayProp = JSONObject.parseObject(goodsPropCode);
//            JSONObject jsonArrayCategory = JSONObject.parseObject(goodsCategoryCode);
//            JSONObject jsonArraySku = JSONObject.parseObject(goodsSkuCode);
            JSONArray jsonArraySku = JSONArray.parseArray(goodsSkuCode);
//            List<CategoryProperty> byCategoryIdAndType = this.categoryPropertyDao.findByCategoryIdAndType(categoryId , 1);
//            for(CategoryProperty categoryProperty: byCategoryIdAndType){
//                props.add(categoryProperty.getPropertyId());
//            }

            for(String key:jsonArrayProp.keySet()){
                if(null != map.get(key)){
                    map.put(key,map.get(key)+","+jsonArrayProp.get(key).toString());
                }else {
                    map.put(key,jsonArrayProp.get(key).toString());
                }
            }
//            for(String key:jsonArrayCategory.keySet()){
//                if(null != map.get(key)){
//                    map.put(key,map.get(key)+","+jsonArrayCategory.get(key).toString());
//                }else {
//                    map.put(key,jsonArrayCategory.get(key).toString());
//                }
//            }

            for(int j=0;j<jsonArraySku.size();j++) {
                JSONObject jsonObject = jsonArraySku.getJSONObject(j);
                for (String key : jsonObject.keySet()) {
                    if (null != map.get(key)) {
                        map.put(key, map.get(key) + "," + jsonObject.get(key).toString());
                    } else {
                        map.put(key, jsonObject.get(key).toString());
                    }
                }
            }

//            for(String key:jsonArraySku.keySet()){
//                if(null != map.get(key)){
//                    map.put(key,map.get(key)+","+jsonArraySku.get(key).toString());
//                }else {
//                    map.put(key,jsonArraySku.get(key).toString());
//                }
//            }
        }

        JSONArray jsonArrayRe = new JSONArray();
//        JSONObject jsonObjectResult = new JSONObject();
        for(String key:map.keySet()){
//            Properties properties = this.propertiesDao.findOne(Integer.valueOf(key));
            Properties properties = null;
            try {
                properties = this.proService.findById(Integer.valueOf(key));

            }catch (Exception e){
                e.printStackTrace();
            }
//            jsonObjectRe.put()
            String[] split = map.get(key).split(",");
            Set<String> set = new HashSet<>();
            for(String sp:split){
                set.add(sp);
            }
            JSONObject jsonArrayR = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(String se:set){
                JSONObject jsonObjectRe = new JSONObject();
                try {

                    if(props.contains(properties.getId())){
                        continue;
                    }

//                    PropertiesValue propertiesValue = this.properValueDao.findOne(Integer.valueOf(se));
                    PropertiesValue propertiesValue = this.properValueService.findById(Integer.valueOf(se));
//                if(ValueUtil.isEmpity(jsonArrayR.get("value"))){
//                    jsonArrayR.put("value", propertiesValue.getCnValue());
//                }else {
//                    jsonArrayR.put("value", jsonArrayR.get("value")+","+propertiesValue.getCnValue());
//                }

                    jsonObjectRe.put("value", propertiesValue.getCnValue());
                }catch (Exception e){
                    jsonObjectRe.put("value", se);
                }
                if(ValueUtil.notEmpity(properties)) {
                    jsonArrayR.put("name", properties.getCnName());
                }else {
                    jsonArrayR.put("name", "");
                }
//                jsonObjectRe.put(key, se);

                jsonArray.add(jsonObjectRe);
                jsonArrayR.put("value", jsonArray);
//                jsonArrayR.add(jsonObjectRe);
            }
//            jsonObjectResult.put(key, map.get(key));
            jsonArrayRe.add(jsonArrayR);
        }

        return jsonArrayRe;

    }


    public Object searchProp(String keyWords, String screen) throws Exception {
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();

        SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
        solrQuery.setQuery("goodsName:"+ keyWords+" OR goodsEnName:"+keyWords+" OR categoryName:"+keyWords); // 搜索关键词
        // 设置分页 start=0就是从0开始，，rows=5当前返回5条记录，第二页就是变化start这个值为5就可以了。
//        solrQuery.setStart((Math.max(page, 1) - 1) * rows);
        solrQuery.setRows(10000);
        if(ValueUtil.notEmpity(screen)) {
            String[] split = screen.split(",");
            String goodsPropString = "goodsPropString:";
            for(int k=0;k<split.length;k++) {
                if(k==0) {
                    goodsPropString = goodsPropString + split[k];
                }else {
                    goodsPropString = goodsPropString + " AND goodsPropString:"+ split[k];
                }
            }
            solrQuery.set("fq", goodsPropString);
        }
//        solrQuery.set("sort", sort + " desc");


        // 执行查询(根据goodsName查询)
//        solrQuery.setQuery("goodsName:"+ keyWords);
        QueryResponse queryResponse = httpSolrServer.query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        List<GoodsSearch> goods = null;
        try {
            goods = queryResponse.getBeans(GoodsSearch.class);
        }catch (Exception e){

        }

        List<Integer> props = new ArrayList<>();
        List<Properties> byCanSearch = this.propertiesDao.findByCanSearch(CanSearch.no);
        for(Properties properties: byCanSearch){
            props.add(properties.getId());
        }

        Map<String, String > map = new HashMap<>();
//        JSONObject map = new JSONObject();
        for(GoodsSearch goodsSearch:goods){
            String goodsPropCode = goodsSearch.getGoodsPropCode();
            String goodsCategoryCode = goodsSearch.getGoodsCategoryCode();
            String goodsSkuCode = goodsSearch.getGoodsSkuCode();
//            Integer categoryId = goodsSearch.getCategoryId();
            JSONObject jsonArrayProp = JSONObject.parseObject(goodsPropCode);
//            JSONObject jsonArrayCategory = JSONObject.parseObject(goodsCategoryCode);
            JSONArray jsonArraySku = JSONArray.parseArray(goodsSkuCode);

            for(String key:jsonArrayProp.keySet()){
                if(null != map.get(key)){
                    map.put(key,map.get(key)+","+jsonArrayProp.get(key).toString());
                }else {
                    map.put(key,jsonArrayProp.get(key).toString());
                }
            }
//            for(String key:jsonArrayCategory.keySet()){
//                if(null != map.get(key)){
//                    map.put(key,map.get(key)+","+jsonArrayCategory.get(key).toString());
//                }else {
//                    map.put(key,jsonArrayCategory.get(key).toString());
//                }
//            }
            for(int j=0;j<jsonArraySku.size();j++) {
                JSONObject jsonObject = jsonArraySku.getJSONObject(j);
                for (String key : jsonObject.keySet()) {
                    if (null != map.get(key)) {
                        map.put(key, map.get(key) + "," + jsonObject.get(key).toString());
                    } else {
                        map.put(key, jsonObject.get(key).toString());
                    }
                }
            }
        }

        JSONArray jsonArrayRe = new JSONArray();
//        JSONObject jsonObjectResult = new JSONObject();
        for(String key:map.keySet()){
            Integer integer = Integer.valueOf(key);
//            Properties properties = this.propertiesDao.findOne(integer);
            Properties properties = this.proService.findById(integer);
//            jsonObjectRe.put()
            String[] split = map.get(key).split(",");
            Set<String> set = new HashSet<>();
            for(String sp:split){
                set.add(sp);
            }
//            JSONArray jsonArrayR = new JSONArray();
            JSONObject jsonArrayR = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            for(String se:set){

                if(props.contains(properties.getId())){
                    continue;
                }

                JSONObject jsonObjectRe = new JSONObject();
                try {
                    Integer integer1 = Integer.valueOf(se);
//                    PropertiesValue propertiesValue = this.properValueDao.findOne(integer1);
                    PropertiesValue propertiesValue = this.properValueService.findById(integer1);
//                jsonObjectRe.put(key, se);
//                if(ValueUtil.isEmpity(jsonArrayR.get("value"))){
//                    jsonArrayR.put("value", propertiesValue.getCnValue());
//                }else {
//                    jsonArrayR.put("value", jsonArrayR.get("value")+","+propertiesValue.getCnValue());
//                }
                    jsonObjectRe.put("value", propertiesValue.getCnValue());
                }catch (Exception e){
                    jsonObjectRe.put("value", se);
                }
                jsonArray.add(jsonObjectRe);
                jsonArrayR.put("value", jsonArray);
//                jsonObjectRe.put("value", propertiesValue.getCnValue());
//                jsonArrayR.put("value", jsonObjectRe);
                jsonArrayR.put("name", properties.getCnName());
            }
//            jsonObjectResult.put(key, map.get(key));
            jsonArrayRe.add(jsonArrayR);
        }

        return jsonArrayRe;

    }


    public void saveGoodsSearch(Goods goods) throws IOException, SolrServerException {
        HttpSolrServer httpSolrServer = this.setSearchService.getHttpSolrServer();
        GoodsSearch goodsSearch = new GoodsSearch();
        goodsSearch.setId(goods.getId());
        goodsSearch.setGoodsName(goods.getGoodsName());
        goodsSearch.setGoodsImageUrl(goods.getGoodsImageUrl());
        goodsSearch.setGoodsEnName(goods.getGoodsEnName());
        goodsSearch.setPrice(Double.valueOf(goods.getPrice()));
        goodsSearch.setSalePrice(Double.valueOf(goods.getSalePrice()));
        goodsSearch.setListedTime(goods.getListedTime());
        goodsSearch.setComments(goods.getComments());
        goodsSearch.setSales(goods.getSales());
        goodsSearch.setCategoryId(goods.getCategoryId());
        Category one = this.categoryDao.findOne(goods.getCategoryId());
        Category parentName = one.getParentName();
        if(ValueUtil.notEmpity(parentName)){
            goodsSearch.setCategoryIdParent(parentName.getId());
            Category one1 = this.categoryDao.findOne(parentName.getId());
            if(ValueUtil.notEmpity(one1.getParentName())){
                goodsSearch.setCategoryIdGrandpa(one1.getParentName().getId());
            }
        }

        goodsSearch.setCategoryName(one.getCategoryName());
        List<GoodsProp> goodsProp = goods.getGoodsProp();
        JSONObject goodsPropCode = new JSONObject();
        String goodsPropString = "";
        for(GoodsProp goodsPro: goodsProp){
            Integer propId = goodsPro.getPropId();
            String propValue = goodsPro.getPropValue();
            goodsPropCode.put(propId.toString(), propValue);
            Properties properties = this.propertiesDao.findOne(propId);
            if(CanSearch.yes==properties.getCanSearch()) {
                if (EntryMode.manual == properties.getEntryMode()) {
                    goodsPropString = goodsPropString + properties.getCnName() + " " + propValue + " ";
                } else {
                    PropertiesValue propertiesValue = this.properValueDao.findOne(Integer.valueOf(propValue));
                    goodsPropString = goodsPropString + properties.getCnName() + " " + propertiesValue.getCnValue() + " ";
                }
            }
        }

        List<GoodsSku> goodsSkus = goods.getGoodsSku();
        JSONArray jsonArray1 = new JSONArray();
        String goodsSkuString = "";
        for(GoodsSku goodsSku: goodsSkus){
            JSONObject goodsSkuCode = new JSONObject();
            Integer skuId = goodsSku.getSkuId();
            Sku sku = this.skuDao.findOne(skuId);
            List<SkuProp> skuProps = sku.getSkuProp();
            for(SkuProp skuProp:skuProps){
                Integer propId = skuProp.getPropId();
                Integer propValueId = skuProp.getPropValueId();
                goodsSkuCode.put(propId.toString(), propValueId.toString());
                Properties prop = this.propertiesDao.findOne(propId);
                if(CanSearch.yes==prop.getCanSearch()){
                    PropertiesValue propertiesValue = this.properValueDao.findOne(propValueId);
                    goodsSkuString = goodsSkuString + prop.getCnName() + " " + propertiesValue.getCnValue() + " ";
                }
            }
            jsonArray1.add(goodsSkuCode);

        }


//        Integer categoryId = goods.getCategoryId();
////        Category category = this.categoryDao.findOne(categoryId);
//        List<CategoryProperty> byCategoryId = this.categoryPropertyDao.findByCategoryId(categoryId);
//        JSONObject goodsCategoryCode = new JSONObject();
//        String goodsCategoryString = "";
//        for(CategoryProperty categoryProperty:byCategoryId){
//            Integer propertyId = categoryProperty.getPropertyId();
//            PropertiesValue propertyValue = categoryProperty.getPropertyValue();
//            Properties one1 = this.propertiesDao.findOne(propertyId);
//            if(CanSearch.yes == one1.getCanSearch()) {
//                if(ValueUtil.isEmpity(propertyValue)){
//                    goodsCategoryCode.put(propertyId.toString(), "");
//                }else {
//                    goodsCategoryCode.put(propertyId.toString(), propertyValue.getId());
//                }
//                if(ValueUtil.notEmpity(propertyValue)) {
//                    goodsCategoryString = goodsCategoryString + one1.getCnName() + " " + propertyValue.getCnValue() + " ";
//                }else {
//                    goodsCategoryString = goodsCategoryString + one1.getCnName() + " " + "" + " ";
//                }
//            }
//        }
//        category
//
//
//        List<GoodsSku> goodsSkus = goods.getCategoryId();
//        JSONObject goodsSkuCode = new JSONObject();
//        String goodsSkuString = "";
//        for(GoodsSku goodsSku: goodsSkus){
//            Integer skuId = goodsSku.getSkuId();
//            Sku sku = this.skuDao.findOne(skuId);
//            List<SkuProp> skuProps = sku.getSkuProp();
//            for(SkuProp skuProp:skuProps){
//                Integer propId = skuProp.getPropId();
//                Integer propValueId = skuProp.getPropValueId();
//                goodsSkuCode.put(propId.toString(), propValueId.toString());
//                Properties prop = this.propertiesDao.findOne(propId);
//                if(CanSearch.yes==prop.getCanSearch()){
//                    PropertiesValue propertiesValue = this.properValueDao.findOne(propValueId);
//                    goodsSkuString = goodsSkuString + prop.getCnName() + " " + propertiesValue.getCnValue() + " ";
//                }
//            }
//
//        }

//        goodsPropString = goodsPropString + " " + goodsSkuString+" "+goodsCategoryString;
        goodsPropString = goodsPropString + " " + goodsSkuString;
//        goodsSearch.setGoodsCategoryCode(goodsCategoryCode.toJSONString());
        goodsSearch.setGoodsPropCode(goodsPropCode.toJSONString());
//        StringBuffer stringBuffer = new StringBuffer(goodsPropString);
        goodsSearch.setGoodsPropString(goodsPropString);
        goodsSearch.setGoodsSkuString(goodsSkuString);
        goodsSearch.setGoodsSkuCode(jsonArray1.toJSONString());
//        goodsSearch.setGoodsSkuString(goodsSkuString);
        goodsSearch.setGoStatus(goods.getGoStatus());

        httpSolrServer.addBean(goodsSearch);
        httpSolrServer.commit();
    }



}
