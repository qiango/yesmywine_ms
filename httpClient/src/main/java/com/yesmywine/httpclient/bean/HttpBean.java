package com.yesmywine.httpclient.bean;

import com.yesmywine.httpclient.biz.HttpMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WANG, RUIQING on 11/28/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class HttpBean {

    // send
    private String url;
    private RequestMethod requestMethod;
    private Integer statusCode;
    private String contentType;
    //	private Map<String,String> headers;
    private Map<String, Object> parameters;
    private Map<String, String> files;
    private Map<String, String> requestHeaders;

    // recived
    private String responseContent;
    private Map<String, String> cookies;
    private Map<String, String> responseHeaders;


    public HttpBean() {
    }

    public HttpBean(String url) {
        this.url = url;
    }

    public HttpBean(String url, RequestMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    public void addParameter(String key, Object value) {
        if (null == this.parameters) {
            this.parameters = new HashMap<>();
        }
        this.parameters.put(key, value);
    }

    public void addHeader(String key, String value) {
        if (null == this.requestHeaders) {
            this.requestHeaders = new HashMap<>();
        }
        requestHeaders.put(key, value);
    }

    public void addFile(String key, String filePath) {
        if (null == this.files) {
            this.files = new HashMap<>();
        }
        this.files.put(key, filePath);
    }

    public void run() {
        if (null == this.requestMethod) {
            this.requestMethod = RequestMethod.get;
        }
        System.out.println("-----http parameters --------------");
        System.out.println(HttpMethod.gson.toJson(this));
        System.out.println("-----http parameters end-----------");
        switch (requestMethod) {
            case get:
                getResult(HttpMethod.get(this));
                break;
            case post:
                if (null != this.files) {
                    getResult(HttpMethod.uploadFile(this));
                } else {
                    getResult(HttpMethod.post(this));
                }
                break;
            case put:
                getResult(HttpMethod.put(this));
                break;
            case delete:
                getResult(HttpMethod.delete(this));
                break;
            case patch:
                getResult(HttpMethod.patch(this));
                break;
        }
    }

    private void getResult(TwHttpResponse twHttpResponse) {
        this.statusCode = twHttpResponse.getStatusCode();
        this.responseContent = twHttpResponse.getResponseContent();
        this.cookies = twHttpResponse.getCookies();
        this.responseHeaders = twHttpResponse.getHeaders();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }


    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }


    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }


    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }
}
