package com.yesmywine.evaluation.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.evaluation.bean.OrderGoodsAdvice;
import com.yesmywine.evaluation.bean.Reply;
import com.yesmywine.evaluation.dao.OrderGoodsAdviceDao;
import com.yesmywine.evaluation.service.OrderGoodsAdviceService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询接口
 * Created by light on 2017/1/9.
 */
@RestController
@RequestMapping("/evaluation/advice")
public class OrderGoodsAdviceController {

    @Autowired
    private OrderGoodsAdviceService orderGoodsAdviceService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderGoodsAdviceDao orderGoodsAdviceDao;

    //查询
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {
        if(id!=null){
            OrderGoodsAdvice orderGoodsAdvice = orderGoodsAdviceService.findOne(id);
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsAdvice);
        }
        if(ValueUtil.notEmpity(params.get("goodsName_l"))){
            return ValueUtil.toJson(HttpStatus.SC_OK,findByGoods(pageNo,pageSize,params.get("goodsName_l").toString()));
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(orderGoodsAdviceService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        PageModel pageModel = new PageModel(null == pageNo ? 1 : pageNo, null == pageSize ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (null != pageNo) params.remove(params.remove("pageNo").toString());
        if (null != pageSize) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        return ValueUtil.toJson(this.orderGoodsAdviceService.findAll(pageModel));
    }

    public PageModel findByGoods(Integer pageNo,Integer pageSize,String goodsName) {
        Query query = entityManager.createNativeQuery("SELECT * FROM orderGoodsAdvice WHERE goodsId IN (SELECT goods.goodsId FROM goods WHERE goodsName LIKE "+"'"+"%"+goodsName+"%"+"'"+")",OrderGoodsAdvice.class);
        query.setFirstResult((pageNo==null?0:pageNo-1)*(pageSize==null?10:pageSize));
        query.setMaxResults(pageSize==null?10:pageSize);
        List list = query.getResultList();
        Integer totalCount = orderGoodsAdviceDao.findByName(goodsName);
        PageModel pageModel = new PageModel(pageNo==null?1:pageNo,pageSize==null?10:pageSize);
        pageModel.setTotalRows(Long.valueOf(totalCount));
        long tempTPd = pageModel.getTotalRows() % pageModel.getPageSize();
        Integer tempTp = Integer.valueOf((pageModel.getTotalRows() / pageModel.getPageSize()) + "");
        if (tempTPd == 0) {
            pageModel.setTotalPages(tempTp);
        } else {
            pageModel.setTotalPages(tempTp + 1);
        }
        pageModel.setContent(list);
        return pageModel;
    }

    //插入
    @RequestMapping(method = RequestMethod.POST)
    public String create(OrderGoodsAdvice orderGoodsAdvice,Integer goodsId,HttpServletRequest request) {
        try {
            JSONObject userInfo = UserUtils.getUserInfo(request);
            HashMap<String, String> param = new HashMap<>();
            param.put("goodId", goodsId.toString());
            param.put("advice", orderGoodsAdvice.getAdvice());
            param.put("adviceType", orderGoodsAdvice.getAdviceType().toString());
            ValueUtil.verify(param, new String[]{"goodId", "advice", "adviceType"});
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsAdviceService.saveAdvice(orderGoodsAdvice, goodsId,userInfo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }


    //修改
    @RequestMapping(method = RequestMethod.PUT)
    public String update(String idList,Integer status) {
        try {
            ValueUtil.verify(idList, "idList");
            ValueUtil.verify(status, "status");
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsAdviceService.updateAdvice(idList,status));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //删除
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(@RequestParam("idList") String idList) {
        try {
            ValueUtil.verify(idList);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
        String[] ids = idList.split(",");
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,this.orderGoodsAdviceService.delelteById(ids));
    }


    //回复
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public String createReply(Reply reply, Integer adviceId, HttpServletRequest request) {
        try {
            ValueUtil.verify(reply.getReply(), "reply");
            JSONObject userInfo = UserUtils.getUserInfo(request);
//            JSONObject userInfo =null;
//            String userId=ValueUtil.getFromJson(userInfo.toJSONString(),"userId");
//            String userName=ValueUtil.getFromJson(userInfo.toJSONString(),"userName");
//            String userUrl=ValueUtil.getFromJson(userInfo.toJSONString(),"userUrl");
            String userId = userInfo.get("id").toString();
            String userName = userInfo.get("userName").toString();
            reply.setStatus(1);
            reply.setUserId(Integer.valueOf(userId));
            reply.setUserName(userName);
//            reply.setUserImage(userUrl);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsAdviceService.saveReply(reply, adviceId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

}
