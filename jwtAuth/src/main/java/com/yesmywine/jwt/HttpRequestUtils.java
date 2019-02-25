package com.yesmywine.jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * Created by by on 2017/8/1.
 */
public class HttpRequestUtils {
    public static String getAllParams(HttpServletRequest request){
        Enumeration parameterNames = request.getParameterNames();
        StringBuffer buffer = new StringBuffer();
        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = request.getParameter(key);
            buffer.append("key : " + key);
            buffer.append("  value : " + value +"  /  ");
        }
        return buffer.toString();
    }
}
