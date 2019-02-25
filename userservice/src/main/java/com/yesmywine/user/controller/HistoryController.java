
package com.yesmywine.user.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.user.dao.LevelHistoryDao;
import com.yesmywine.user.entity.LevelHistory;
import com.yesmywine.user.service.HistoryService;
import com.yesmywine.util.basic.Threads;
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
 * Created by ${shuang} on 2017/4/12.
 */
@RestController
@RequestMapping("/userservice/history")
public class HistoryController {

        @Autowired
        private HistoryService historyService;
        @Autowired
        private LevelHistoryDao levelHistoryDao;


    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer userId) {
        if(ValueUtil.isEmpity(userId)){
            if(null!=params.get("all")&&params.get("all").toString().equals("true")){
                return ValueUtil.toJson(historyService.findAll());
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
            pageModel = historyService.findAll(pageModel);
            return ValueUtil.toJson(pageModel);
        }else {
           List<LevelHistory> levelHistories = levelHistoryDao.findByUserId(userId);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,levelHistories);
        }
    }

    @RequestMapping( method = RequestMethod.DELETE)
    public String delete(String idList){
        try {
          return   historyService.delete(idList);
        } catch (YesmywineException e) {
            Threads.createExceptionFile("userservice",e.getMessage());
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }
}
