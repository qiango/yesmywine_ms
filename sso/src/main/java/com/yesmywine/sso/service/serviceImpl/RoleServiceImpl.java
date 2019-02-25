package com.yesmywine.sso.service.serviceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.bean.Roles;
import com.yesmywine.sso.dao.AdminUserDao;
import com.yesmywine.sso.dao.PermDao;
import com.yesmywine.sso.dao.RoleDao;
import com.yesmywine.sso.service.RoleService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJQ on 2017/6/6.
 */
@Service
@Transactional
public class RoleServiceImpl  extends BaseServiceImpl<Roles,Integer> implements RoleService {
    @Autowired
    private PermDao permDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private AdminUserDao userDao;

    @Override
    public Roles create(String roleName, Integer[] permIds) throws YesmywineException {
        Roles roles = new Roles();
        roles.setRoleName(roleName);
        if(permIds!=null){
            for(Integer permId:permIds){
                Perms perm = permDao.findOne(permId);
                ValueUtil.verifyNotExist(perm,"包含不存在的权限");
                roles.addToPerms(perm);
            }
        }
        roleDao.save(roles);
        return roles;
    }

    @Override
    public Roles update(Integer id, String roleName, Integer[] permIds) throws YesmywineException {
        Roles role = roleDao.findOne(id);
        role.setRoleName(roleName);
        role.getPerms().clear();
        for(Integer permId:permIds){
            Perms perm = permDao.findOne(permId);
            ValueUtil.verifyNotExist(perm,"包含不存在的权限");
            role.addToPerms(perm);
        }
        roleDao.save(role);
        return role;
    }

    @Override
    public String delete(Integer id) throws YesmywineException {
        Roles roles = roleDao.findOne(id);
        if(roles.getCanDelete()!=null&&!roles.getCanDelete()){
            ValueUtil.isError("该角色禁止删除");
        }
        List<Integer> userIdList =  userDao.findByRoleId(id);
        if(userIdList.size()>0){
            ValueUtil.isError("该角色下存在用户，无法删除");
        }else{
            roleDao.delete(id);
        }
        return "SUCCESS";
    }

    @Override
    public String quanxian(List<Perms> list) throws YesmywineException {

        List<Perms>  perms1 =new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).getLevel()==1){
                perms1.add(list.get(i));
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
            List<Perms> perms2 = fliter(list,keyId);
            if(perms2.size()!=0){
                for (int j = 0; j <perms2.size() ; j++) {
                    JSONObject jsonObject2 = new JSONObject();
                    Perms perm2 =perms2.get(j);
                    jsonObject2.put("title",perm2.getPermName());
                    jsonObject2.put("id",perm2.getId());
                    List<Perms> perms3 =fliter(list,perm2.getId());
                    JSONArray jsonArray3 = new JSONArray();
                    if(perms3.size()!=0){
                        for (int k = 0; k <perms3.size() ; k++) {
                            JSONObject jsonObject3 = new JSONObject();
                            Perms perm3 =perms3.get(k);
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

    public List<Perms> fliter(List<Perms> list, Integer keyId){
        List<Perms> newList = new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).getParentId()==keyId){
                newList.add(list.get(i));
            }
        }
        return newList;
    }
}
