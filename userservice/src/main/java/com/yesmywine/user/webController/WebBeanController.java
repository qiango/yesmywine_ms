package com.yesmywine.user.webController;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.jwt.UserUtils;
import com.yesmywine.user.dao.BeanFlowDao;
import com.yesmywine.user.entity.BeanFlow;
import com.yesmywine.user.service.BeanFlowService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ${shuang} on 2017/4/12.
 */


@RestController
@RequestMapping("/member/userservice/beans")
public class WebBeanController {

    @Autowired
    private BeanFlowService beanFlowService;
    @Autowired
    private BeanFlowDao beanFlowDao;

    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize,HttpServletRequest request) {
        MapUtil.cleanNull(params);
        String userInfo = null;
        Integer userId =null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
             userId = UserUtils.getUserId(request);
            if(ValueUtil.isEmpity(userInfo)){
              ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
           params.put("userId",userId);
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(beanFlowService.findAll());
            }else  if(null!=params.get("all")){
                params.remove(params.remove("all").toString());
            }
            PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
            if (null != params.get("showFields")) {
                pageModel.setFields(params.remove("showFields").toString());
            }
            if (pageNo != null) params.remove(params.remove("pageNo").toString());
            if (pageSize != null) params.remove(params.remove("pageSize").toString());
            for (String key :params.keySet()) {
                if(ValueUtil.isEmpity(params.get(key))){
                    params.remove(key);
                }
            }
            pageModel.addCondition(params);
            pageModel = beanFlowService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public Object index(String statusType, Integer pageSize, Integer pageNumber,HttpServletRequest request){
        String userInfo = null;
        try {
            userInfo = UserUtils.getUserInfo(request).toJSONString();
            if(ValueUtil.isEmpity(userInfo)){
                ValueUtil.isError("未登录");
            }
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        if(ValueUtil.isEmpity(pageSize)){
            pageSize=10;
        }if(ValueUtil.isEmpity(pageNumber)){
            pageNumber=0;
        }
        Pageable pageable = new PageRequest(pageNumber, pageSize, sort);
        String [] status= statusType.split(",");
        List<String> list= new ArrayList<>();
        for (int i = 0; i <status.length ; i++) {
            list.add(status[i]);
        }
        Page<BeanFlow> pages=beanFlowDao.findByStatusIn(list,pageable);
        return pages;
    }


}
