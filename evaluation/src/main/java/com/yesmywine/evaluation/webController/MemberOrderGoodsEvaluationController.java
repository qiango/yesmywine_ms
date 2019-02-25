package com.yesmywine.evaluation.webController;

import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 评论接口
 * Created by light on 2017/1/9.
 */
@RestController
@RequestMapping("/member/evaluation/comments")
public class MemberOrderGoodsEvaluationController {

    @Autowired
    private OrderGoodsEvaluationService orderGoodsEvaluationService;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private OrderGoodsEvaluationDao orderGoodsEvaluationDao;

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

    //回复
    @RequestMapping(value = "/reply", method = RequestMethod.POST)
    public String createReply(String replyString, Integer evaluationId, HttpServletRequest request) {
        try {
            ValueUtil.verify(replyString, "replyString");
            JSONObject userInfo = UserUtils.getUserInfo(request);
//            JSONObject userInfo =null;
//            String userId=ValueUtil.getFromJson(userInfo.toJSONString(),"userId");
//            String userName=ValueUtil.getFromJson(userInfo.toJSONString(),"userName");
//            String userUrl=ValueUtil.getFromJson(userInfo.toJSONString(),"userUrl");
            String userId = userInfo.get("id").toString();
            String userName = userInfo.get("userName").toString();
            Reply rep = new Reply();
            rep.setStatus(1);
            rep.setReply(replyString);
            rep.setUserId(Integer.valueOf(userId));
            rep.setUserName(userName);
//            reply.setUserImage(userUrl);
            String s = this.orderGoodsEvaluationService.saveReply(rep, evaluationId);
            if("erro".equals(s)){
                return ValueUtil.toError("500", s);
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED,s);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(), e.getMessage());
        }
    }

}