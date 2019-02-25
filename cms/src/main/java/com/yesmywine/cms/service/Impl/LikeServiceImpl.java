package com.yesmywine.cms.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.cms.dao.GoodsDao;
import com.yesmywine.cms.dao.LikeFirstDao;
import com.yesmywine.cms.dao.LikeSecentDao;
import com.yesmywine.cms.entity.FlashPurchaseSecent;
import com.yesmywine.cms.entity.Goods;
import com.yesmywine.cms.entity.LikeFirst;
import com.yesmywine.cms.entity.LikeSecent;
import com.yesmywine.cms.service.GoodsService;
import com.yesmywine.cms.service.LikeService;
import com.yesmywine.util.basic.ValueUtil;
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
 * Created by hz on 2017/5/16.
 */
@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeFirstDao likeFirstDao;
    @Autowired
    private LikeSecentDao likeSecentDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsService goodsService;


    @Override
    public Object findOne(Integer likeFirstId) {
        LikeFirst one = this.likeFirstDao.findOne(likeFirstId);
        if(ValueUtil.isEmpity(one)){
            return "没有此栏目";
        }
        JSONArray jsonArray= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", one.getId());
        jsonObject.put("name", one.getName());
        List<LikeSecent> byLikeFirstId = this.likeSecentDao.findByLikeFirstId(one.getId());

        for(LikeSecent likeSecent: byLikeFirstId){
            JSONObject jsonObjectSecent = new JSONObject();
            jsonObjectSecent.put("id", likeSecent.getId());
            Goods one2 = this.goodsService.findById(likeSecent.getGoodsId());
            jsonObjectSecent.put("secentGoodsId", likeSecent.getGoodsId());
            jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
            jsonArray.add(jsonObjectSecent);
        }
        jsonObject.put("likeSecent", jsonArray);
        return jsonObject;
    }

    @Override
    public Object findAll() {
        List<LikeFirst> all = this.likeFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(LikeFirst one: all) {
            JSONArray jsonArray= new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", one.getId());
            jsonObject.put("name", one.getName());
            List<LikeSecent> byLikeFirstId = this.likeSecentDao.findByLikeFirstId(one.getId());

            for(LikeSecent likeSecent: byLikeFirstId){
                JSONObject jsonObjectSecent = new JSONObject();
                jsonObjectSecent.put("id", likeSecent.getId());
                Goods one2 = this.goodsService.findById(likeSecent.getGoodsId());
                jsonObjectSecent.put("secentGoodsId", likeSecent.getGoodsId());
                jsonObjectSecent.put("secentGoodsName", one2.getGoodsName());
                jsonArray.add(jsonObjectSecent);
            }
            jsonObject.put("likeSecent", jsonArray);
            jsonArrayRe.add(jsonObject);
        }
        return jsonArrayRe;
    }

    @Override
    public Object FrontfindAll(Integer pageNo, Integer pageSize) {
        List<LikeFirst> all = this.likeFirstDao.findAll();
        JSONArray jsonArrayRe = new JSONArray();
        for(LikeFirst one: all) {
//            JSONArray jsonArray= new JSONArray();
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name", one.getName());
//            List<LikeSecent> byLikeFirstId = this.likeSecentDao.findByLikeFirstId(one.getId());

//            for(LikeSecent likeSecent: byLikeFirstId){
//                JSONObject jsonObjectSecent = new JSONObject();
//                Goods one2 = this.goodsDao.findOne(likeSecent.getGoodsId());
//                jsonObjectSecent.put("id", likeSecent.getGoodsId());
//                jsonObjectSecent.put("name", one2.getGoodsName());
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
            Page<LikeSecent> byLikeFirstId1 = likeSecentDao.findByLikeFirstId(one.getId(),pageable);
            JSONArray jsonArray1 = new JSONArray();
            for(LikeSecent likeSecent: byLikeFirstId1){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", likeSecent.getGoodsId());
                Goods goods = this.goodsService.findById(likeSecent.getGoodsId());
                jsonObject1.put("name", goods.getGoodsName());
                jsonObject1.put("picture", goods.getPicture());
                jsonArray1.add(jsonObject1);
            }
            JSONObject jsonObjectRe = new JSONObject();
            jsonObjectRe.put("totalElements", byLikeFirstId1.getTotalElements());
            jsonObjectRe.put("totalPages", byLikeFirstId1.getTotalPages());
            jsonObjectRe.put("pageNo", pageNo);
            jsonObjectRe.put("pageSize", pageSize);
            jsonObjectRe.put("goods", jsonArray1);
            jsonObjectRe.put("name", one.getName());
//            return jsonObjectRe;

            jsonArrayRe.add(jsonObjectRe);
        }
        return jsonArrayRe;
    }

    @Override
    public String insert(String name, String jsonString) {
        try {
            LikeFirst byName = this.likeFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName)){
                return "此栏目已存在";
            }
            LikeFirst likeFirst = new LikeFirst();
            likeFirst.setName(name);
            this.likeFirstDao.save(likeFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    LikeSecent likeSecent = new LikeSecent();
                    likeSecent.setGoodsId(goodsId);
                    likeSecent.setLikeFirstId(likeFirst.getId());
                    this.likeSecentDao.save(likeSecent);
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
            LikeFirst byName = this.likeFirstDao.findByName(name);
            if(ValueUtil.notEmpity(byName) && byName.getId() != id){
                return "此栏目已存在";
            }
            LikeFirst likeFirst = new LikeFirst();
            likeFirst.setName(name);
            likeFirst.setId(id);
            this.likeFirstDao.save(likeFirst);

            if(ValueUtil.notEmpity(jsonString)){
                JSONArray jsonArray = JSON.parseArray(jsonString);
                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Integer goodsId = Integer.valueOf(jsonObject.get("goodsId").toString());
                    LikeSecent likeSecent = new LikeSecent();
                    likeSecent.setGoodsId(goodsId);
                    likeSecent.setLikeFirstId(likeFirst.getId());
                    List<LikeSecent> byGoodsIdAndLikeFirstId = this.likeSecentDao.findByGoodsIdAndLikeFirstId(goodsId, likeFirst.getId());
                    if(byGoodsIdAndLikeFirstId.size()>0){
                        likeSecent.setId(byGoodsIdAndLikeFirstId.get(0).getId());
                    }
                    this.likeSecentDao.save(likeSecent);
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
            this.likeFirstDao.delete(id);
            this.likeSecentDao.deleteByLikeFirstId(id);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "网络异常";
        }
        return "success";
    }

    @Override
    public String deleteSecent(Integer id) {
        try{
            this.likeSecentDao.delete(id);
        }catch (Exception e){
            return "网络异常";
        }
        return "success";
    }
}
