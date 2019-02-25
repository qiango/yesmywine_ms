package com.yesmywine.sso.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.bean.Roles;
import com.yesmywine.sso.service.RoleService;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by SJQ on 2017/6/6.
 */
@RestController
@RequestMapping("/sso/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /*
    *@Author Gavin
    *@Description 创建角色
    *@Date 2017/6/6 19:01
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.POST)
    public String create(String roleName,Integer[] permIds,@RequestParam Map<String,String> params){
        try {
            ValueUtil.verify(params,new String[]{"roleName"});
            Roles roles = roleService.create(roleName,permIds);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,roles);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 修改角色
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.PUT)
    public String update(Integer id,String roleName,Integer[] permIds,@RequestParam Map<String,String> params){
        try {
            ValueUtil.verify(params,new String[]{"id","roleName"});
            Roles roles = roleService.update(id,roleName,permIds);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,roles);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 查看角色
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id){
        MapUtil.cleanNull(params);
        if (id != null) {
            Roles role = roleService.findOne(id);
            Set<Perms> Perms = role.getPerms();
            List<Perms> newPerms= new ArrayList<>();
           String newPerm =null;
            for(Perms value : Perms){
                newPerms.add(value);
            }
            try {
                newPerm =  roleService.quanxian(newPerms);
            } catch (YesmywineException e) {
                return ValueUtil.toError(e.getCode(),e.getMessage());
            }
            return newPerm;
        }

        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(roleService.findAll());
        } else if (null != params.get("all")) {
            params.remove(params.remove("all").toString());
        }

        PageModel pageModel = new PageModel(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        if (null != params.get("showFields")) {
            pageModel.setFields(params.remove("showFields").toString());
        }
        if (pageNo != null) params.remove(params.remove("pageNo").toString());
        if (pageSize != null) params.remove(params.remove("pageSize").toString());
        pageModel.addCondition(params);
        pageModel = roleService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }

    /*
    *@Author Gavin
    *@Description 删除角色
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id){
        try {
            roleService.delete(id);
        } catch (YesmywineException e) {
            e.printStackTrace();
        }
        return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,"SUCCESS");
    }
}
