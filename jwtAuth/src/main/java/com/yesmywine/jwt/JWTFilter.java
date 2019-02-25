package com.yesmywine.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.db.base.biz.RedisCache;
import com.yesmywine.jwt.requestFilter.XssRequestWrapper;
import com.yesmywine.util.basic.Constants;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import com.yesmywine.jwt.thread.LogThread;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SJQ on 2017/6/6.
 */
public class JWTFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    /**
     * 封装，不需要过滤的list列表
     */
    protected static List<String> whiteList = new ArrayList<String>();
    /**
     * 封装，必须登录的接口
     */
    protected static List<String> mustLoginList = new ArrayList<String>();
    /**
     * 交互的接口，用于安全验证
     */
    protected static List<String> interactionList = new ArrayList<String>();

    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
        //未登录也可访问
        whiteList.add(("/getMenus"));
        whiteList.add(("/web"));
        whiteList.add(("/verifyPerm"));
        whiteList.add(("/getPerms"));
        whiteList.add(("/pay/back"));
        whiteList.add(("/login"));//登陆
        whiteList.add(("/logout"));//退出
        whiteList.add(("/task"));
        whiteList.add(("/push"));
        whiteList.add(("/druid"));

        interactionList.add(("/oms"));
        interactionList.add(("/wms"));
        interactionList.add(("/itf"));
        interactionList.add(("/syn"));//同步

        //登陆后可访问
        mustLoginList.add("/member");
        mustLoginList.add("/pay/pc");
        mustLoginList.add("/pay/app");
        mustLoginList.add("/pay/refresh");
        mustLoginList.add("/pay/result");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setContentType("textml;charset=UTF-8");
//        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers", " Origin, X-Requested-With, Content-Type, Accept, Connection, User-Agent, Cookie, Authorization, RequestPerm");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("XDomainRequestAllowed","1");


        String auth = httpRequest.getHeader("Authorization");
        String requestPerm = httpRequest.getHeader("RequestPerm");

        StringBuffer url = httpRequest.getRequestURL();//  eg: http://api.hzbuvi.com/user/login
        String partlyUrl = httpRequest.getRequestURI();//  eg:/user/login
        String method = httpRequest.getMethod();//eg：　POST
        logger.info("请求路径为==》"+partlyUrl +"  "+method);
        logger.info("jwtToke ==》"+auth);
        logger.info("请求报文为 ==》"+HttpRequestUtils.getAllParams(httpRequest));
        String authorize = partlyUrl+"/"+method;

        if(method.equals("OPTIONS")){
            chain.doFilter(request, response);
            return;
        }
        try{
            if(isPassed(partlyUrl,httpRequest)){
                chain.doFilter(new XssRequestWrapper(httpRequest), response);
                return;
            }

            if ((auth != null) && (auth.length() > 7))
            {
                String HeadStr = auth.substring(0, 6).toLowerCase();
                if (HeadStr.compareTo("bearer") == 0)
                {
                    auth = auth.substring(7, auth.length());
                    Claims claims = JwtHelper.parseJWT(auth, Audience.base64Secret);
                    if ( claims!= null)
                    {
                        String username = claims.getSubject();
                        String userInfo = RedisCache.get(Constants.USER_INFO+username);
                        if(userInfo != null){
                            if(mustLogin(partlyUrl)){//登录后即可访问
                                chain.doFilter(new XssRequestWrapper(httpRequest), response);
                                return;
                            }
                            if(requestPerm==null){//请求权限参数头不可为空
                                ValueUtil.isError("请求权限 RequestPerm 参数为空");
                                return;
                            }
                            if(isAllPerms(userInfo)){//判断该用户是否拥有所有权限
                                //记录操作日志
                                String operation = getOperation(requestPerm,userInfo);
                                if(operation==null){
                                    ValueUtil.isError("请求权限 RequestPerm 参数，无效的操作");
                                }
                                LogThread logThread = new LogThread(username,operation);
                                Thread thread = new Thread(logThread);
                                thread.start();
                                chain.doFilter(new XssRequestWrapper(httpRequest), response);
                                return;
                            }
                            int authorizeIsExist = userInfo.indexOf(authorize);
                            int urlIsExist = userInfo.indexOf(requestPerm);
                            if(authorizeIsExist >= 0||urlIsExist >= 0){
                                //记录操作日志
                                String operation = getOperation(requestPerm,userInfo);
                                if(operation==null){
                                    ValueUtil.isError("参数错误，无效的操作");
                                }
                                LogThread logThread = new LogThread(username,operation);
                                Thread thread = new Thread(logThread);
                                thread.start();
                                chain.doFilter(new XssRequestWrapper(httpRequest), response);
                                return;
                            }
                            ValueUtil.isError("无此权限");
                            return;
                        }
                    }
                }
            }
            ValueUtil.isError("用户未登录或已登录超时,请重新登录");
            return;
        }catch (YesmywineException e){
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json; charset=utf-8");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.getWriter().write(ValueUtil.toError(999,e.getMessage()));

        }
    }

    private boolean canRequest(String url, HttpServletRequest request) throws YesmywineException {
        return false;
    }

    private String getOperation(String requestPerm,String userInfo) {
        String userPerms = ValueUtil.getFromJson(userInfo,"allPerms");
        JSONObject jsonObject = JSON.parseObject(userPerms);
        return jsonObject.getString(requestPerm);
    }

    private Boolean mustLogin(String partlyUrl) {
        for (String beLogin : mustLoginList) {
            int index = partlyUrl.indexOf(beLogin);
            if(index>=0){
                return true;
            }
        }
        return false;
    }

    private boolean isAllPerms(String userInfo) {
        if(userInfo==null){
            return false;
        }
        JSONObject jsonObject = JSON.parseObject(userInfo);
        String roles = jsonObject.getString("roles");
        JSONArray roleArr = JSON.parseArray(roles);
        for(int m=0;m<roleArr.size();m++){
            JSONObject role = (JSONObject)roleArr.get(m);
            String haveAllPerms = role.getString("haveAllPerms");
            if(haveAllPerms.equals("true")||haveAllPerms.equals("1")){
                return true;
            }
        }
        return false;
    }

    public void destroy() {
        // TODO Auto-generated method stub

    }

    /**
     * 是否需要过滤
     * @param url
     * @return
     */
    private boolean isPassed(String url,HttpServletRequest request) throws YesmywineException {
        for (String whitePath : whiteList) {
            int index = url.indexOf(whitePath);
            if(index>=0){
                return true;
            }
        }

        for (String interactionIo : interactionList) {//系统间交互验证参数是否合法
            int index = url.indexOf(interactionIo);
            if(index>=0){
//                String token = request.getParameter("token");
//                String sign = request.getParameter("sign");
//                String timestamp = request.getParameter("timestamp");
//                if(sign==null||timestamp==null){
//                    ValueUtil.isError("请求参数非法！");
//                }
//                String localSign = MD5.MD5Encode("token="+Dictionary.TOKEN_KEY+"&timestamp="+timestamp);
//                Date now = new Date();
//                Long nowTime = now.getTime();
//                Long requestTime = DateUtil.toDate(timestamp,"yyyy-MM-dd HH:mm:ss").getTime();
//
//                if(sign.equals(localSign)&&token.equals(Dictionary.TOKEN_KEY)){
//                    return true;
//                }
//                ValueUtil.isError("请求参数非法！");
                return true;
            }
        }

        return false;
    }
}
