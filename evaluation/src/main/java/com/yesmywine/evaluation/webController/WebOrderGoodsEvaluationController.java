package com.yesmywine.evaluation.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.evaluation.bean.OrderGoodsEvaluation;
import com.yesmywine.evaluation.service.OrderGoodsEvaluationService;
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
 * 评论接口
 * Created by light on 2017/1/9.
 */
@RestController
@RequestMapping("/web/evaluation/comments")
public class WebOrderGoodsEvaluationController {

    @Autowired
    private OrderGoodsEvaluationService orderGoodsEvaluationService;

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
        PageModel pageModel = new PageModel(null == pageNo ? 1 : pageNo, null == pageSize ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (null != pageNo) params.remove(params.remove("pageNo").toString());
        if (null != pageSize) params.remove(params.remove("pageSize").toString());
//        if (null != params.get("goodsName_l")){
//            List<Goods> goodsess = this.goodsDao.findByNameLike(params.get("goodsName_l").toString());
//            PageModel page = new PageModel();
//            for(Goods goods:goodsess){
//                List<OrderGoodsEvaluation> byGoods = this.orderGoodsEvaluationDao.findByGoods(goods);
//            }
//            params.remove("goodsName_l");
//        }
        pageModel.addCondition(params);
        PageModel all = this.orderGoodsEvaluationService.findAll(pageModel);
        List<OrderGoodsEvaluation> content = all.getContent();
        for(OrderGoodsEvaluation orderGoodsEvaluation:content){
            orderGoodsEvaluation.setOrderNo(null);
        }
        return ValueUtil.toJson(HttpStatus.SC_OK,all);
    }

//    //插入
//    @RequestMapping(method = RequestMethod.POST)
//    public String create(String jsonArray,HttpServletRequest request) {
//        try {
//            JSONObject userInfo = UserUtils.getUserInfo(request);
////            JSONObject userInfo =null;
//            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsEvaluationService.create(jsonArray, userInfo));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(), e.getMessage());
//        }
//    }
//
//    //回复
//    @RequestMapping(value = "/reply", method = RequestMethod.POST)
//    public String createReply(Reply reply, Integer evaluationId,HttpServletRequest request) {
//        try {
//            ValueUtil.verify(reply.getReply(), "reply");
//            JSONObject userInfo = UserUtils.getUserInfo(request);
////            JSONObject userInfo =null;
//            String userId=ValueUtil.getFromJson(userInfo.toJSONString(),"userId");
//            String userName=ValueUtil.getFromJson(userInfo.toJSONString(),"userName");
//            String userUrl=ValueUtil.getFromJson(userInfo.toJSONString(),"userUrl");
//            reply.setStatus(1);
//            reply.setUserId(Integer.valueOf(userId));
//            reply.setUserName(userName);
//            reply.setUserImage(userUrl);
//            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsEvaluationService.saveReply(reply, evaluationId));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(), e.getMessage());
//        }
//    }


    //好评率
    @RequestMapping(value="/goodCommentsNum",method = RequestMethod.GET)
    public String getGoods(Integer goodsId) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,orderGoodsEvaluationService.getShuffNum(goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    //好评率
    @RequestMapping(value="/goodComments",method = RequestMethod.GET)
    public String getGoods(Integer type,Integer goodsId) {//type:1好评,2中评,3差评
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,orderGoodsEvaluationService.getShuff2(goodsId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }



}