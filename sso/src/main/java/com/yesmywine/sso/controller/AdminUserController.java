package com.yesmywine.sso.controller;

import com.yesmywine.base.record.bean.PageModel;
import com.yesmywine.sso.bean.AdminUser;
import com.yesmywine.sso.service.AdminUserService;
import com.yesmywine.sso.service.PermService;
import com.yesmywine.sso.utils.PasswordUtils;
import com.yesmywine.util.basic.MapUtil;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.UserUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 用户登录接口
 * Created by light on 2016/12/19.
 */
@RestController
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/sso/user")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private PermService permService;

    /*
    *@Author Gavin
    *@Description 配置用户角色
    *@Date 2017/6/8 10:47
    *@Email gavinsjq@sina.com
    *@Params
    */
    @RequestMapping(value = "/configRoles",method = RequestMethod.PUT)
    public String configUserRoles(Integer userId,Integer roleIds[]){
        try {
            if(userId==1){
                ValueUtil.isError("此用户为初始化用户，无法修改");
            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED,adminUserService.configUserRoles(userId,roleIds));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    //获取用户权限目录
    @RequestMapping(value = "/getMenus", method = RequestMethod.GET)
    public String getUserMenus(Integer userId,HttpServletRequest request) {
        try {
            userId = UserUtils.getUserId(request);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        return ValueUtil.toJson(adminUserService.getUserMenus(userId));
    }

    //获取用户权限功能点
    @RequestMapping(value = "/getPerms", method = RequestMethod.GET)
    public String getUserPerms(Integer userId,Integer menuId,HttpServletRequest request) {
        try {
            userId = UserUtils.getUserId(request);
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
        return ValueUtil.toJson(adminUserService.getUserPerms(userId,menuId));
    }

    //验证权限
    @RequestMapping(value = "/verifyPerm", method = RequestMethod.GET)
    public String verifyPermKey(String key,HttpServletRequest request) {
        try {
            String userInfo = UserUtils.getUserInfo(request).toJSONString();
            return ValueUtil.toJson(adminUserService.verifyPermKey(userInfo,key));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }


    @RequestMapping( method = RequestMethod.POST)
    public String createAdmin(AdminUser adminUser, @RequestParam Map<String,String> params) {
        try {
            ValueUtil.verify(params,new String[]{"userName","password"});
            adminUser.setPassword(PasswordUtils.encodePassword(adminUser.getPassword()));
            return ValueUtil.toJson(HttpStatus.SC_CREATED,adminUserService.save(adminUser));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.PUT)
    public String update(AdminUser adminUser, @RequestParam Map<String,String> params) {
        try {
            ValueUtil.verify(params,new String[]{"id","userName","password"});
            AdminUser dbAdminUser = adminUserService.findOne(adminUser.getId());
            adminUser.setRoles(dbAdminUser.getRoles());
            return ValueUtil.toJson(HttpStatus.SC_CREATED,adminUserService.save(adminUser));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    @RequestMapping( method = RequestMethod.DELETE)
    public String delete(Integer id) {
        try {
            ValueUtil.verify(id,"id");
            if(id==1){
                ValueUtil.isError("无法删除超级管理员");
            }
            adminUserService.delete(id);
            return ValueUtil.toJson(HttpStatus.SC_NO_CONTENT,"SUCCESS");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

    /*
    *@Author:Gavin
    *@Email:gavinsjq@sina.com
    *@Date:  2017/7/7
    *@Param
    *@Description:管理员列表
    */
    @RequestMapping( method = RequestMethod.GET)
    public String index(@RequestParam Map<String, Object> params, Integer pageNo, Integer pageSize, Integer id) {
        MapUtil.cleanNull(params);
        if (id != null) {
            AdminUser adminUser = adminUserService.findOne(id);
            return ValueUtil.toJson(adminUser);
        }

        if (null != params.get("all") && params.get("all").toString().equals("true")) {
            return ValueUtil.toJson(adminUserService.findAll());
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
        pageModel = adminUserService.findAll(pageModel);
        return ValueUtil.toJson(pageModel);
    }

//    //获取用户权限
//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    public String Login(UserInformation inUser) {
//        try {
//
//            String loginName = inUser.getUserName();
//            UserInformation dbUser = adminUserService.findByUsername(loginName);
//            if(dbUser==null&&dbUser.getPassword().equals(inUser.getPassword())){
//                ValueUtil.isError("用户名或密码错误！");
//            }
//        RedisCache.set(Statement.USER_INFO+loginName,dbUser);
//
////        UsernamePasswordToken token = new UsernamePasswordToken(loginName,user.getPassword());
////        //获取当前的Subject
////        Subject currentUser = SecurityUtils.getSubject();
//        String accessToken = "";
////        try {
//            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
//            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
//            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
//            logger.info("对用户[" + loginName + "]进行登录验证..验证开始");
////            currentUser.login(token);
////            String aaa = (String)currentUser.getPrincipal();
//            logger.info("对用户[" + loginName + "]进行登录验证..验证通过");
//
////            accessToken = JwtHelper.createJWT(dbUser.getUserName(), dbUser.getId().toString(),
////                    dbUser.getRoles().toString(), Audience.clientId, Audience.name,
////                    Audience.expiresSecond * 1000, Audience.base64Secret);
//            accessToken = JwtHelper.createJWT( dbUser.getId().toString(),dbUser.getUserName(),
//                    Audience.expiresSecond * 1000);
//
//
////        } catch (UnknownAccountException uae) {
////            logger.info("对用户[" + loginName + "]进行登录验证..验证未通过,未知账户");
////        } catch (IncorrectCredentialsException ice) {
////            logger.info("对用户[" + loginName + "]进行登录验证..验证未通过,错误的凭证");
////        } catch (LockedAccountException lae) {
////            logger.info("对用户[" + loginName + "]进行登录验证..验证未通过,账户已锁定");
////        } catch (ExcessiveAttemptsException eae) {
////            logger.info("对用户[" + loginName + "]进行登录验证..验证未通过,错误次数过多");
////        } catch (AuthenticationException ae) {
////            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
////            logger.info("对用户[" + loginName + "]进行登录验证..验证未通过,堆栈轨迹如下");
////            ae.printStackTrace();
////        }
//        //验证是否登录成功
////        if (currentUser.isAuthenticated()) {
////            logger.info("用户[" + loginName + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
////            //返回accessToken
//            AccessToken accessTokenEntity = new AccessToken();
//            accessTokenEntity.setAccess_token(accessToken);
//            accessTokenEntity.setExpires_in(Audience.expiresSecond);
//            accessTokenEntity.setToken_type("bearer");
//            return ValueUtil.toJson(accessTokenEntity);
////        } else {
////            token.clear();
////            return "redirect:/login";
////        }
//
//        } catch (YesmywineException e) {
//            return ValueUtil.toError(e.getCode(),e.getMessage());
//        }
//    }
}
