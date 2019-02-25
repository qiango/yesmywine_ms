package com.yesmywine.sso.service.serviceImpl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.base.record.biz.BaseServiceImpl;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.sso.bean.AdminUser;
import com.yesmywine.sso.bean.Perms;
import com.yesmywine.sso.bean.Roles;
import com.yesmywine.sso.dao.AdminUserDao;
import com.yesmywine.sso.dao.PermDao;
import com.yesmywine.sso.dao.RoleDao;
import com.yesmywine.sso.service.AdminUserService;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * Created by light on 2016/12/19.
 */
@Service
@Transactional
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser,Integer> implements AdminUserService {
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private PermDao permDao;
    @Autowired
    private RoleDao roleDao;


    //token时间设置为0
    public String updateByToken(String token, HttpServletRequest request) throws YesmywineException {
        String username = UserUtils.getUserName(request);
        if(username!=null){
            RedisCache.expire(Constants.USER_INFO+username, 0);
        }else{
            ValueUtil.isError("登录超时，或用户未登录");
        }

        return "success";
    }

    //查询userId
    public String queryByToken(String token) {
        String data = RedisCache.get(token);
        if (StringUtils.isEmpty(data)) {
            return null;
        } else {
            RedisCache.expire(token, 30 * 60);
//            return data;
            //          return String.valueOf(ValueUtil.getFromJson(data));
            return ValueUtil.getFromJson(data, "data", "userId");
        }
    }

    @Override
    public AdminUser findByUsername(String loginName) {
        return adminUserDao.findByUserName(loginName);
    }

    @Override
    public Object getUserMenus(Integer userId) {
        AdminUser adminUser = adminUserDao.findOne(userId);
        Set<Roles> userRoles = adminUser.getRoles();
        List<Perms> list = permDao.findUserMenu(userId,"1");
        for(Roles role:userRoles){
            if(role.getHaveAllPerms()!=null&&role.getHaveAllPerms()){
                list = permDao.findByLevel(1);
            }
        }

        JSONArray resultArray = new JSONArray();
        JSONArray firstArray = new JSONArray();
        //第一级
        for(Perms perm:list){
            JSONObject firstObj = new JSONObject();
            String permKey = perm.getPermKey();
            Integer id = perm.getId();
            List<Perms> children = permDao.findByParentIdAndIsMenu(id,"true");
            //判断子集
            if(children.size()>0){
                JSONArray secArray = new JSONArray();
                for(Perms sec_perm:children){
                    JSONObject secObj = new JSONObject();
                    String sec_permKey = sec_perm.getPermKey();
                    Integer sec_id = sec_perm.getId();
                    List<Perms> sec_children = permDao.findByParentIdAndIsMenu(sec_id,"true");
                    if(sec_children.size()>0){
                        JSONArray thirdArray = new JSONArray();
                        for(Perms third_perm:sec_children){
                            JSONObject thirdObj = new JSONObject();
                            String third_permKey = third_perm.getPermKey();
                            Integer third_id = third_perm.getId();
//                            List<Perms> third_children = permDao.findByParentId(sec_id);
                            thirdObj.put("key",third_permKey);
                            thirdObj.put("id",third_id);
                            thirdArray.add(thirdObj);
                        }
                        secObj.put("key",sec_permKey);
                        secObj.put("id",sec_id);
                        secObj.put("children",thirdArray);

                    }else{
                        secObj.put("key",sec_permKey);
                        secObj.put("id",sec_id);
                        secArray.add(secObj);
                    }
                }
                firstObj.put("key",permKey);
                firstObj.put("id",id);
                firstObj.put("children",secArray);
                resultArray.add(firstObj);
            }else{
                firstObj.put("key",permKey);
                firstObj.put("id",id);
                resultArray.add(firstObj);
            }
        }


        return resultArray;
    }

    @Override
    public Object getUserPerms(Integer userId, Integer menuId) {
        List<Perms> permsList = permDao.findUserMenuFunction(userId,menuId);
        return permsList;
    }

    @Override
    public String configUserRoles(Integer userId, Integer[] rolesIds) throws YesmywineException {
        AdminUser adminUser = adminUserDao.findOne(userId);
        Set<Roles> rolesSet = adminUser.getRoles();
        rolesSet.clear();
        ValueUtil.verifyNotExist(adminUser,"用户不存在");
        for (Integer roleId:rolesIds){
            Roles role = roleDao.findOne(roleId);
            ValueUtil.verifyNotExist(role,"包含不存在的角色，请重新编辑");
            adminUser.addToRoles(role);
        }
        adminUserDao.save(adminUser);
        return "SUCCESS";
    }

    @Override
    public Boolean verifyPermKey(String userInfo, String key) throws YesmywineException {
        int isExist = userInfo.indexOf(key);
        if(isExist>=0){
            return true;
        }else{
            ValueUtil.isError("无此权限");
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        adminUserDao.delete(id);
    }
}
