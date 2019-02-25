package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.*;
import com.yesmywine.cms.entity.*;
import com.yesmywine.cms.service.BoutiqueService;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by wangdiandian on 2017/6/1.
 */
@Service
@Transactional
public class BoutiqueServiceImpl implements BoutiqueService {
    @Autowired
    private BoutiqueFirstDao boutiqueFirstDao;
    @Autowired
    private BoutiqueSecentDao boutiqueSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private PanicBuyingFirstDao panicBuyingFirstDao;
    @Autowired
    private PanicBuyingSecentDao panicBuyingSecentDao;
    @Autowired
    private FlashPurchaseFirstDao flashPurchaseFirstDao;
    @Autowired
    private FlashPurchaseSecentDao flashPurchaseSecentDao;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer boutiqueFirstId) {
        BoutiqueFirst one = this.boutiqueFirstDao.findOne(boutiqueFirstId);
        if(ValueUtil.isEmpity(one)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getName());
//        if(one.getId()==4||one.getId()==5){
//            jsonObject.put("fixed",1);
//        }
        List<BoutiqueSecent> boutiqueSecents = this.boutiqueSecentDao.findByBoutiqueFirstId(one.getId());

        for(BoutiqueSecent boutiqueSecent: boutiqueSecents){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", boutiqueSecent.getId());
            Goods one2 = this.goodsService.findById(boutiqueSecent.getGoodsId());
            jsonObjectSecent.put("secentGoodsId", one2.getId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("boutiqueSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<BoutiqueFirst> all = this.boutiqueFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(BoutiqueFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
//            if(one.getId()==4||one.getId()==5){
//                jsonObject.put("fixed",1);
//            }
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            List<BoutiqueSecent> boutiqueSecents = this.boutiqueSecentDao.findByBoutiqueFirstId(one.getId());
            for(BoutiqueSecent boutiqueSecent: boutiqueSecents){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", boutiqueSecent.getId());
                Goods one2 = this.goodsService.findById(boutiqueSecent.getGoodsId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonObjectSecent.put("secentGoodsId",one2.getId());
                jsonArray.add(jsonObjectSecent);
            }

            jsonObject.put("boutiqueSecent", jsonArray);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }


    @Override
    public Object FrontfindAll(Integer pageNo, Integer pageSize) {
        List<BoutiqueFirst> all = this.boutiqueFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(BoutiqueFirst one: all) {
//            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
//            if(one.getId()==4||one.getId()==5){
//                jsonObject.put("fixed",1);
//            }
//            List<BoutiqueSecent> boutiqueSecents = this.boutiqueSecentDao.findByBoutiqueFirstId(one.getId());
//            for(BoutiqueSecent boutiqueSecent: boutiqueSecents){
//                JSONObject jsonObjectSecent = new JSONObject();
//                Goods one2 = this.goodsDao.findOne(boutiqueSecent.getGoodsId());
//                jsonObjectSecent.put("name", one2.getGoodsName());
//                jsonObjectSecent.put("goodsId",one2.getId());
//                jsonObjectSecent.put("picture",one2.getPicture());
//                jsonArray.add(jsonObjectSecent);
//            }
//            jsonObject.put("goods", jsonArray);

            if(ValueUtil.isEmpity(pageNo)){
                pageNo = 1;
            }
            if(ValueUtil.isEmpity(pageSize)){
                pageSize = 4;
            }
            int page = pageNo.intValue();
            int size = pageSize.intValue();
//        Pageable pageable = (Pageable) new PageRequest(page, pageSize);
            Sort sort = new Sort(Sort.Direction.ASC, "id");
            Pageable pageable = new PageRequest(page-1, size, sort);
//        return goodsDao.findAllOrderBySales(pageable);
            Page<BoutiqueSecent> byLikeFirstId1 = boutiqueSecentDao.findByBoutiqueFirstId(one.getId(),pageable);
            JSONArray jsonArray1 = new JSONArray();
            for(BoutiqueSecent boutiqueSecent: byLikeFirstId1){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("goodsId", boutiqueSecent.getGoodsId());
                Goods goods = this.goodsService.findById(boutiqueSecent.getGoodsId());
                jsonObject1.put("name", goods.getGoodsName());
                jsonObject1.put("picture", goods.getPicture());
                jsonArray1.add(jsonObject1);
            }
//            JSONObject jsonObjectRe = new JSONObject();
            jsonObject.put("totalElements", byLikeFirstId1.getTotalElements());
            jsonObject.put("totalPages", byLikeFirstId1.getTotalPages());
            jsonObject.put("pageNo", pageNo);
            jsonObject.put("pageSize", pageSize);
            jsonObject.put("goods", jsonArray1);


            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }
    @Override
    public String insert(String name, String jsonString) {
        try {
            BoutiqueFirst byName = this.boutiqueFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)){
                return "此栏目已存在";
            }
            BoutiqueFirst boutiqueFirst = new BoutiqueFirst();
            boutiqueFirst.setName(name);
            this.boutiqueFirstDao.save(boutiqueFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    BoutiqueSecent boutiqueSecent = new BoutiqueSecent();
                    boutiqueSecent.setGoodsId(goodsId);
                    boutiqueSecent.setBoutiqueFirstId(boutiqueFirst.getId());
                    this.boutiqueSecentDao.save(boutiqueSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String update(Integer id, String name, String jsonString) {
        try {
            BoutiqueFirst byName = this.boutiqueFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName) && byName.getId() != id){
                return "此栏目已存在";
            }
            BoutiqueFirst boutiqueFirst = new BoutiqueFirst();
            boutiqueFirst.setName(name);
            boutiqueFirst.setId(id);
            this.boutiqueFirstDao.save(boutiqueFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    BoutiqueSecent boutiqueSecent = new BoutiqueSecent();
                    boutiqueSecent.setGoodsId(goodsId);
                    boutiqueSecent.setBoutiqueFirstId(boutiqueFirst.getId());
                    BoutiqueSecent byGoodsIdAndBoutiqueFirstId = this.boutiqueSecentDao.findByGoodsIdAndBoutiqueFirstId(goodsId, boutiqueFirst.getId());
                    if(ValueUtil.notEmpity(byGoodsIdAndBoutiqueFirstId)){
                        boutiqueSecent.setId(byGoodsIdAndBoutiqueFirstId.getId());
                    }
                    this.boutiqueSecentDao.save(boutiqueSecent);
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return e.toString();
        }
        return "success";
    }

    @Override
    public String deleteFirst(Integer id) {
        try{
            this.boutiqueFirstDao.delete(id);
            this.boutiqueSecentDao.deleteByBoutiqueFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.boutiqueSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }

    @Override
    public JSON getShuffling() throws YesmywineException {
        List<BoutiqueFirst> list=boutiqueFirstDao.findAll();//所有栏目
        com.alibaba.fastjson.JSONArray firstArray = new com.alibaba.fastjson.JSONArray();
        for(BoutiqueFirst boutiqueFirst:list){
            Integer id=boutiqueFirst.getId();
            if(id==4){
                PanicBuyingFirst panicBuyingFirst=panicBuyingFirstDao.findAll().get(0);//超值抢购的第一个栏目
                Integer pId=panicBuyingFirst.getId();
                com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
                com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
                List<PanicBuyingSecent> panicBuyingSecentList=panicBuyingSecentDao.findByPanicBuyingFirstId(pId);
                for(PanicBuyingSecent j:panicBuyingSecentList) {
                    com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                    Goods goods = goodsService.findById(j.getGoodsId());
                    fourth.put("goodPicture", goods.getPicture());
                    fourth.put("goodName", goods.getGoodsName());
                    scondArray.add(fourth);
                }
                third.put("goodsList",scondArray);
                third.put("classifyName",boutiqueFirstDao.findOne(4).getName());
//                third.put("fixed",1);//不能改
                firstArray.add(third);
            }else if(id==5){
                List<FlashPurchaseFirst> panicBuyingFirst=flashPurchaseFirstDao.findAll();//所有栏目
                FlashPurchaseFirst flashPurchaseFirst=panicBuyingFirst.get(0);
                com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
                com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
                List<FlashPurchaseSecent> list1=flashPurchaseSecentDao.findByFlashPurchaseFirstId(flashPurchaseFirst.getId());
                for(FlashPurchaseSecent first:list1){
                    com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                    Goods goods=goodsService.findById(first.getGoodsId());
                    fourth.put("goodPicture",goods.getPicture());
                    fourth.put("goodName",goods.getGoodsName());
                    scondArray.add(fourth);
                }
                third.put("classifyName",boutiqueFirstDao.findOne(5).getName());
                third.put("goodsList",scondArray);
//                third.put("fixed",1);//不能改
                firstArray.add(third);
            }else{
                List<BoutiqueSecent> list1 = boutiqueSecentDao.findByBoutiqueFirstId(id);
                com.alibaba.fastjson.JSONObject third = new com.alibaba.fastjson.JSONObject();
                com.alibaba.fastjson.JSONArray scondArray = new com.alibaba.fastjson.JSONArray();
                for(BoutiqueSecent b:list1){
                   Goods goods=goodsService.findById(b.getGoodsId());
                    com.alibaba.fastjson.JSONObject fourth = new com.alibaba.fastjson.JSONObject();
                    fourth.put("goodPicture",goods.getPicture());
                    fourth.put("goodName",goods.getGoodsName());
                    scondArray.add(fourth);
                }
                third.put("classifyName",boutiqueFirstDao.findOne(id).getName());
                third.put("goodsList",scondArray);
                firstArray.add(third);
            }

        }
        return firstArray;
    }
}
