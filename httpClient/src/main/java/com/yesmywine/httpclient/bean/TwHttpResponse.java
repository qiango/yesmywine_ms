package com.yesmywine.httpclient.bean;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 11/28/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class TwHttpResponse {

    private Integer statusCode;
    private String responseContent;
    private List<Header> cookies;
    private List<Header> headers;

    public void addCookie(Header header) {
        if (null == cookies) {
            cookies = new ArrayList<>();
        }
        cookies.add(header);
    }

    public void addHeader(Header header) {
        if (null == headers) {
            headers = new ArrayList<>();
        }
        headers.add(header);
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Map<String, String> getCookies() {
        Map<String, String> cookiesMap = new HashMap<>();
        if (null != cookies) {
            cookies.forEach(h -> {
                cookiesMap.put(h.getName(), h.getValue());
            });
        }
        return cookiesMap;
    }

    public void setCookies(List<Header> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headersMap = new HashMap<>();
        if (null != headers) {
            headers.forEach(h -> {
                headersMap.put(h.getName(), h.getValue());
            });
        }
        return headersMap;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
}
