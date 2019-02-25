package com.yesmywine.goods.WebController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.goods.service.GiftCardHistoryService;
import com.yesmywine.goods.service.GiftCardService;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/7/13.
 */
@RestController
public class WebGiftCardController {
    @Autowired
    private GiftCardService giftCardService;
    @Autowired
    private GiftCardHistoryService giftCardHistoryService;

    @RequestMapping(value = "/member/goods/giftCard/useOneGiftCard",method = RequestMethod.GET)
    public String useOneGiftCard( Long cardNumber,String password,HttpServletRequest request) {//使用礼品卡
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.useOneGiftCard(cardNumber,password));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value="/member/goods/giftCard", method = RequestMethod.GET)
    public String userGiftCart(HttpServletRequest request) throws  Exception{//查询个人礼品卡
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_OK, giftCardService.userGiftCart(userId));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value="/member/goods/giftCard/history", method = RequestMethod.GET)
    public String history(Long cardNumber,HttpServletRequest request) throws  Exception{//查询礼品卡消费记录
        try {
            Integer userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userId)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_OK, giftCardHistoryService.history(cardNumber));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
    @RequestMapping(value="/member/goods/giftCard/card/history" ,method= RequestMethod.GET)
    public String indexHistory(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize) throws  Exception{
        MapUtil.cleanNull(params);
        ValueUtil.verify(params.get("id"), "idNull");
        String giftCardId=params.get("id").toString();
//        params.put("deleteEnum", 0);
        params.remove(params.remove("id").toString());
        params.put("giftCardId",giftCardId);
        if(null!=params.get("all")&&params.get("all").toString().equals("true")){
            return ValueUtil.toJson(giftCardHistoryService.findAll());
        }else if(null!=params.get("all")){
            params.remove(params.remove("all").toString());
        }
        //查看礼品卡消费列表
        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = giftCardHistoryService.findAll(pageModel);
        return ValueUtil.toJson(HttpStatus.SC_OK,pageModel);
    }
    @RequestMapping(value = "/member/goods/giftCard/bound",method = RequestMethod.PUT)
    public String mallbound(Long cardNumber, String password,HttpServletRequest request) {//用户商城礼品卡绑定
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                return "未登录";
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, giftCardService.mallbound(cardNumber, password,userInfo));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }
}
