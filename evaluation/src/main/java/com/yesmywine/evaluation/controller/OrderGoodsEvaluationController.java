package com.yesmywine.evaluation.controller;

import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.evaluation.bean.OrderGoodsEvaluation;
import com.yesmywine.evaluation.bean.Reply;
import com.yesmywine.evaluation.dao.GoodsDao;
import com.yesmywine.evaluation.dao.OrderGoodsEvaluationDao;
import com.yesmywine.evaluation.service.OrderGoodsEvaluationService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 评论接口
 * Created by light on 2017/1/9.
 */
@RestController
@RequestMapping("/evaluation/comments")
public class OrderGoodsEvaluationController {

    @Autowired
    private OrderGoodsEvaluationService orderGoodsEvaluationService;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private OrderGoodsEvaluationDao orderGoodsEvaluationDao;

    //查询
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {
        if(id!=null){
            OrderGoodsEvaluation orderGoodsEvaluation = orderGoodsEvaluationService.findOne(id);
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluation);
        }
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(orderGoodsEvaluationService.findAll());
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
        return ValueUtil.toJson(HttpStatus.SC_OK,this.orderGoodsEvaluationService.findAll(pageModel));
    }

    //插入
    @RequestMapping(method = RequestMethod.POST)
    public String create(String jsonArray,HttpServletRequest request) {
        try {
            JSONObject userInfo = UserUtils.getUserInfo(request);
//            JSONObject userInfo =null;
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsEvaluationService.create(jsonArray, userInfo));
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
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsEvaluationService.updateEvaluation(idList,status));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //删除
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(String idList) {
        try {
            ValueUtil.verify(idList);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
        String[] ids = idList.split(",");
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,this.orderGoodsEvaluationService.delelteById(ids));
    }


    //回复
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public String createReply(Reply reply, Integer evaluationId, HttpServletRequest request) {
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
            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsEvaluationService.saveReply(reply, evaluationId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    //好评率
    @RequestMapping(value="/goodComments",method = RequestMethod.GET)
    public String getGoods(Integer type,Integer goodsId) {//type:1好评,2中评,3差评
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,orderGoodsEvaluationService.getShuff(type, goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    //好评率
    @RequestMapping(value="/goodComments/itf",method = RequestMethod.GET)
    public String getGoodsItf(Integer type,Integer goodsId) {//type:1好评,2中评,3差评
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,orderGoodsEvaluationService.getShuff(type, goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping(value = "/buy",method = RequestMethod.GET)
    public String getBuy(){//刚刚被购买
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluationService.getGoodByBuy());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/buy/itf",method = RequestMethod.GET)
    public String getBuyItf(){//刚刚被购买
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluationService.getGoodByBuy());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/buyCondition",method = RequestMethod.GET)
    public String getCondition(){//酒友品鉴
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluationService.conditionAssessment());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/buyCondition/itf",method = RequestMethod.GET)
    public String getConditionItf(){//酒友品鉴
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluationService.conditionAssessment());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/buyGoodComment",method = RequestMethod.GET)
    public String buyGoodComment(){//刚刚被好评
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluationService.getGoodComment());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

    @RequestMapping(value = "/buyGoodComment/itf",method = RequestMethod.GET)
    public String buyGoodCommentItf(){//刚刚被好评
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsEvaluationService.getGoodComment());
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }
}