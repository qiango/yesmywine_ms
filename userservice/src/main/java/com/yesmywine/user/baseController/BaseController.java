package com.yesmywine.user.baseController;

import com.yesmywine.db.base.bean.PageResult;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.db.base.biz.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by ${shuang} on 2016/12/9.
 */
@RestController
@RequestMapping("/userbase")
public class BaseController {
    @RequestMapping(value = "/{tableName}", method = RequestMethod.GET)
    public String index(@PathVariable("tableName") String tableName) {
        return ValueUtil.toJson(tableName, BaseQuery.run(tableName));
    }

    @RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
    public String add(@PathVariable("tableName") String tableName, @RequestParam Map<String, Object> params) {
        return ValueUtil.toJson(tableName, BaseInsert.run(tableName, params));
    }

    @RequestMapping(value = "/{tableName}/{id}", method = RequestMethod.GET)
    public String index(@PathVariable("tableName") String tableName, @PathVariable("id") Integer id) {
        return ValueUtil.toJson(tableName, BaseQuery.run(tableName, "id = " + id));
    }

    @RequestMapping(value = "/{tableName}", method = RequestMethod.PUT)
    public String update(@PathVariable("tableName") String tableName, @RequestParam Map<String, Object> params) {
        return ValueUtil.toJson(tableName, BaseUpdate.run(tableName, params, "id=" + params.remove("id")));
    }

    @RequestMapping(value = "/{tableName}/{id}", method = RequestMethod.DELETE)
    public String delete(@PathVariable("tableName") String tableName, @PathVariable("id") Integer id) {
        return ValueUtil.toJson(tableName, BaseDelete.run(tableName, "id=" + id));
    }

    @RequestMapping(value = "/page/{tableName}", method = RequestMethod.POST)
    public String showPage(@PathVariable("tableName") String tableName, Integer page, Integer pageSize, String condition) {
        PageResult pageResult = new PageResult(page, pageSize);
        if (null != condition) {
            pageResult.setCondition(condition);
        }
        pageResult = PageQuery.run(tableName, pageResult);
        return ValueUtil.toJson(tableName, pageResult);
    }
}
