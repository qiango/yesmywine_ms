package com.yesmywine.evaluation.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.evaluation.bean.OrderGoodsAdvice;
import com.yesmywine.evaluation.service.OrderGoodsAdviceService;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 咨询接口
 * Created by light on 2017/1/9.
 */
@RestController
@RequestMapping("/web/evaluation/advice")
public class WebOrderGoodsAdviceController {

    @Autowired
    private OrderGoodsAdviceService orderGoodsAdviceService;

    //查询
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,Integer id) {
        if(id!=null){
            OrderGoodsAdvice orderGoodsAdvice = orderGoodsAdviceService.findOne(id);
            return ValueUtil.toJson(HttpStatus.SC_OK,orderGoodsAdvice);
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

//    //插入
//    @RequestMapping(method = RequestMethod.POST)
//    public String create(OrderGoodsAdvice orderGoodsAdvice,Integer goodsId,HttpServletRequest request) {
//        try {
//            JSONObject userInfo = UserUtils.getUserInfo(request);
//            HashMap<String, String> param = new HashMap<>();
//            param.put("goodsId", goodsId.toString());
//            param.put("advice", orderGoodsAdvice.getAdvice());
//            param.put("adviceType", orderGoodsAdvice.getAdviceType().toString());
//            ValueUtil.verify(param, new String[]{"goodsId", "advice", "adviceType"});
//            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsAdviceService.saveAdvice(orderGoodsAdvice, goodsId,userInfo));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(), e.getMessage());
//        }
//    }
//
//
//    //回复
//    @RequestMapping(value = "/reply", method = RequestMethod.POST)
//    public String createReply(Reply reply, Integer adviceId, HttpServletRequest request) {
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
//            return ValueUtil.toJson(HttpStatus.SC_CREATED,this.orderGoodsAdviceService.saveReply(reply, adviceId));
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(), e.getMessage());
//        }
//    }

}
