package com.yesmywine.sso.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.dao.PermDao;
import com.yesmywine.sso.service.PermService;
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

/**
 * Created by SJQ on 2017/6/8.
 */
@RestController
@RequestMapping("/sso/perms")
public class PermController {
    @Autowired
    private PermService permService;
    @Autowired
    private PermDao permDao;

    /*
    *@Author Gavin
    *@Description 创建新权限
    *@Date 2017/6/6 19:01
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.POST)
    public String create(Perms perms, @RequestParam Map<String,String> params){
        try {
            ValueUtil.verify(params,new String[]{"permName","permKey","isMenu"});
            permService.create(perms);
            return ValueUtil.toJson(HttpStatus.SC_CREATED,perms);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 修改权限
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.PUT)
    public String update(Perms perms,@RequestParam Map<String,String> params){
        try {
            ValueUtil.verify(params,new String[]{"id","permName","permKey","isMenu"});
            return ValueUtil.toJson(HttpStatus.SC_CREATED,permService.update(perms));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 查看权限
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id){
        MapUtil.cleanNull(params);
        if (id != null) {
            Perms perm = permService.findOne(id);

            return ValueUtil.toJson(perm);
        }

        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(permService.findAll());
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
        pageModel = permService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }

    /*
    *@Author Gavin
    *@Description 删除权限
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(method = RequestMethod.DELETE)
    public String delete(Integer id){
        try {
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,permService.delete(id));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author Gavin
    *@Description 权限树结构
    *@Date 2017/6/6 19:02
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/tree",method = RequestMethod.GET)
    public String tree(){
        List<Perms>  perms1 =new ArrayList<>();
        List<Perms>  perms = permDao.findAll();
        for (int i = 0; i <perms.size() ; i++) {
            if(ValueUtil.isEmpity(perms.get(i).getParentId())){
                perms1.add(perms.get(i));
            }
        }
        JSONArray jsonArray1 = new JSONArray();
        for (int i = 0; i <perms1.size() ; i++) {
            JSONObject jsonObject1 = new JSONObject();
            Perms perm1 = perms1.get(i);
            String title = perm1.getPermName();
            Integer keyId = perm1.getId();
            jsonObject1.put("title",title);
            jsonObject1.put("id",keyId);
            JSONArray jsonArray2 = new JSONArray();
            List<Perms> perms2 = permDao.findByParentId(keyId);
            if(perms2.size()!=0){
                for (int j = 0; j <perms2.size() ; j++) {
                    JSONObject jsonObject2 = new JSONObject();
                    Perms perm2 =perms2.get(j);
                    if(perm2.getLevel()==3){
                        jsonObject2.put("Function",true);
                        jsonObject2.put("level2",perm2.getParentId());
                        jsonObject2.put("level1",permDao.findOne(perm2.getParentId()).getParentId());
                    }
                    jsonObject2.put("title",perm2.getPermName());
                    jsonObject2.put("id",perm2.getId());
                    List<Perms> perms3 = permDao.findByParentId(perm2.getId());
                    JSONArray jsonArray3 = new JSONArray();
                    if(perms3.size()!=0){
                        for (int k = 0; k <perms3.size() ; k++) {
                            JSONObject jsonObject3 = new JSONObject();
                            Perms perm3 =perms3.get(k);
                            if(perm3.getLevel()==3){
                                jsonObject3.put("Function",true);
                                jsonObject3.put("level2",perm3.getParentId());
                                jsonObject3.put("level1",permDao.findOne(perm3.getParentId()).getParentId());
                            }
                           jsonObject3.put("title",perm3.getPermName());
                           jsonObject3.put("id",perm3.getId());
                           jsonArray3.add(jsonObject3);
                        }
                        jsonObject2.put("children",jsonArray3);
                    }
                    jsonArray2.add(jsonObject2);
                }
                jsonObject1.put("children",jsonArray2);
            }
            jsonArray1.add(jsonObject1);
        }
        return ValueUtil.toJson(jsonArray1);
    }

}
