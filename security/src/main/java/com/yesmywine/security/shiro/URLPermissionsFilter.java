package com.yesmywine.security.shiro;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by SJQ on 2017/3/24.
 */
public class URLPermissionsFilter extends PermissionsAuthorizationFilter {
    /**
     *@param mappedValue 指的是在声明url时指定的权限字符串，如/User/create.do=perms[User:create].我们要动态产生这个权限字符串，所以这个配置对我们没用
     */
    public boolean isAccessAllowed(ServletRequest request,
                                   ServletResponse response, Object mappedValue) throws IOException {
//        HttpServletRequest httpRequest = (HttpServletRequest)request;
//        String auth = httpRequest.getHeader("Authorization");
//        String url = httpRequest.getRequestURI();
//        if(url.equals("/user/login")){
//            return true;
//        }
//        if ((auth != null) && (auth.length() > 7))
//        {
//            String HeadStr = auth.substring(0, 6).toLowerCase();
//            if (HeadStr.compareTo("bearer") == 0)
//            {
//
//                auth = auth.substring(7, auth.length());
//                if (JwtHelper.parseJWT(auth, Audience.base64Secret) != null)
//                {
//                    return super.isAccessAllowed(request, response, buildPermissions(request));
//                }
//            }
//        }

//        HttpServletResponse httpResponse = (HttpServletResponse) response;
//        httpResponse.setCharacterEncoding("UTF-8");
//        httpResponse.setContentType("application/json; charset=utf-8");
//        httpResponse.setStatus(HttpServletResponse.SC_OK);
//
//        httpResponse.getWriter().write(ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR,"无此权限"));

        return super.isAccessAllowed(request, response, buildPermissions(request));
    }
    /**
     * 根据请求URL产生权限字符串，这里只产生，而比对的事交给Realm
     * @param request
     * @return
     */
    protected String[] buildPermissions(ServletRequest request) {

        String[] perms = new String[1];
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getServletPath();
        perms[0] = path;//path直接作为权限字符串
        String regex = "/(.*?)/(.*?)\\.(.*)";
//        if(url.matches(regex)){
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(url);
//            String controller =  matcher.group(1);
//            String action = matcher.group(2);
//
//        }
        return perms;
    }
}
