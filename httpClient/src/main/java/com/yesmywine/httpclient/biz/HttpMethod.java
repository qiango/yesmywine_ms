package com.yesmywine.httpclient.biz;

import com.google.gson.Gson;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.TwHttpResponse;
import com.yesmywine.util.basic.Threads;
import com.yesmywine.util.properties.PropertiesUtil;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WANG, RUIQING on 11/28/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class HttpMethod {

//    to.socketTimeout=8000
//    to.connectTimeout=8000
//    to.connectionRequestTimeout=8000

    public static final String SOCKET_TIMEOUT = PropertiesUtil.getString("to.socketTimeout", "timeout.properties");
    public static final String CONNECT_TIMEOUT = PropertiesUtil.getString("to.connectTimeout", "timeout.properties");
    public static final String CONNECTREQUEST_TIMEOUT = PropertiesUtil.getString("to.connectionRequestTimeout", "timeout.properties");


    public static RequestConfig defaultRequestConfig = RequestConfig.custom()
            //    socketTimeout—请求超时时间
            .setSocketTimeout(Integer.valueOf(SOCKET_TIMEOUT))
            //    connectTimeout—连接超时时间
            .setConnectTimeout(Integer.valueOf(CONNECT_TIMEOUT))
            //    connectionRequestTimeout—从连接池中取连接的超时时间
            .setConnectionRequestTimeout(Integer.valueOf(CONNECTREQUEST_TIMEOUT))
            .setStaleConnectionCheckEnabled(true)
            .build();

    public static Gson gson = new Gson();

    public static TwHttpResponse get(HttpBean bean) {

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();

        try {
            String param = "";
            if (null != bean.getParameters()) {
                StringBuffer stringBuffer = new StringBuffer();
                bean.getParameters().forEach((k, v) -> {
                    stringBuffer.append("&" + k + "=" + v);
                });
//				param = URLEncoder.encode( stringBuffer.toString().replace("&", "?"),"UTF-8");
                param = stringBuffer.toString().replaceFirst("\\&", "?");
            }

            // 创建httpget.
            HttpGet httpget = new HttpGet(bean.getUrl() + param);
            System.out.println(httpget.getURI());

            if (null != bean.getRequestHeaders()) {
                bean.getRequestHeaders().forEach((k, v) -> {
                    httpget.addHeader(new Header() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public HeaderElement[] getElements() throws ParseException {
                            return new HeaderElement[0];
                        }
                    });
                });
            }
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            // 获取响应实体
            HttpEntity entity = response.getEntity();

            TwHttpResponse twHttpResponse = new TwHttpResponse();
            twHttpResponse.setStatusCode(response.getStatusLine().getStatusCode());
            twHttpResponse.setResponseContent(EntityUtils.toString(entity, "UTF-8"));
            response.close();
            httpclient.close();
            return twHttpResponse;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Threads.createExceptionFile("http",e.getMessage());
        }
        return null;
    }

    public static TwHttpResponse post(HttpBean bean) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(bean.getUrl());
        // 创建参数队列
        UrlEncodedFormEntity uefEntity;
        try {
            if (null != bean.getParameters()) {
                List<NameValuePair> paramList = new ArrayList<>();
                bean.getParameters().forEach((k, v) -> {
                    paramList.add(new NameValuePair() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return String.valueOf(v);
                        }
                    });
                });
                uefEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
                httppost.setEntity(uefEntity);
            }
            if (null != bean.getRequestHeaders()) {
                bean.getRequestHeaders().forEach((k, v) -> {
                    httppost.addHeader(new Header() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public HeaderElement[] getElements() throws ParseException {
                            return new HeaderElement[0];
                        }
                    });
                });
            }
//			System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            TwHttpResponse twHttpResponse = new TwHttpResponse();
            if (entity != null) {
                twHttpResponse.setStatusCode(response.getStatusLine().getStatusCode());
                twHttpResponse.setResponseContent(EntityUtils.toString(entity, "UTF-8"));
                HeaderIterator cookieIterator = response.headerIterator("Set-Cookie");
                while (cookieIterator.hasNext()) {
                    twHttpResponse.addCookie(cookieIterator.nextHeader());
                }
                HeaderIterator headerIterator = response.headerIterator();
                while (headerIterator.hasNext()) {
                    twHttpResponse.addHeader(headerIterator.nextHeader());
                }
            }
            response.close();
            httpclient.close();
            return twHttpResponse;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static TwHttpResponse uploadFile(HttpBean bean) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(bean.getUrl());

//			HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            if (null != bean.getFiles()) {
                bean.getFiles().forEach((k, v) -> {
                    builder.addPart(k, new FileBody(new File(v)));
                });
            }
            if (null != bean.getParameters()) {
                bean.getParameters().forEach((k, v) -> {
                    builder.addPart(k, new StringBody(String.valueOf(v), ContentType.TEXT_PLAIN));
                });
            }
            HttpEntity reqEntity = builder.build();
            httppost.setEntity(reqEntity);
            if (null != bean.getRequestHeaders()) {
                bean.getRequestHeaders().forEach((k, v) -> {
                    httppost.addHeader(new Header() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public HeaderElement[] getElements() throws ParseException {
                            return new HeaderElement[0];
                        }
                    });
                });
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            TwHttpResponse twHttpResponse = new TwHttpResponse();
            if (resEntity != null) {
                twHttpResponse.setStatusCode(response.getStatusLine().getStatusCode());
                twHttpResponse.setResponseContent(EntityUtils.toString(reqEntity));
            }
            EntityUtils.consume(resEntity);
            response.close();
            httpclient.close();
            return twHttpResponse;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TwHttpResponse put(HttpBean bean) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(bean.getUrl());

        UrlEncodedFormEntity uefEntity;
        try {
            if (null != bean.getParameters()) {
                List<NameValuePair> paramList = new ArrayList<>();
                bean.getParameters().forEach((k, v) -> {
                    paramList.add(new NameValuePair() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return String.valueOf(v);
                        }
                    });
                });
                uefEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
                httpPut.setEntity(uefEntity);
            }

            CloseableHttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            if (null != bean.getRequestHeaders()) {
                bean.getRequestHeaders().forEach((k, v) -> {
                    httpPut.addHeader(new Header() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public HeaderElement[] getElements() throws ParseException {
                            return new HeaderElement[0];
                        }
                    });
                });
            }
            TwHttpResponse twHttpResponse = new TwHttpResponse();
            if (entity != null) {
                twHttpResponse.setStatusCode(response.getStatusLine().getStatusCode());
                twHttpResponse.setResponseContent(EntityUtils.toString(entity, "UTF-8"));
            }
            response.close();
            httpClient.close();
            return twHttpResponse;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TwHttpResponse delete(HttpBean bean) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(bean.getUrl());
        try {
            if (null != bean.getRequestHeaders()) {
                bean.getRequestHeaders().forEach((k, v) -> {
                    httpDelete.addHeader(new Header() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public HeaderElement[] getElements() throws ParseException {
                            return new HeaderElement[0];
                        }
                    });
                });
            }

            CloseableHttpResponse response = httpClient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            TwHttpResponse twHttpResponse = new TwHttpResponse();
            if (entity != null) {
                twHttpResponse.setStatusCode(response.getStatusLine().getStatusCode());
                twHttpResponse.setResponseContent(EntityUtils.toString(entity, "UTF-8"));
            }
            response.close();
            httpClient.close();
            return twHttpResponse;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static TwHttpResponse patch(HttpBean bean) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPatch httpPatch = new HttpPatch(bean.getUrl());

        UrlEncodedFormEntity uefEntity;
        try {
            if (null != bean.getParameters()) {
                List<NameValuePair> paramList = new ArrayList<>();
                bean.getParameters().forEach((k, v) -> {
                    paramList.add(new NameValuePair() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return String.valueOf(v);
                        }
                    });
                });
                uefEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
                httpPatch.setEntity(uefEntity);
            }
            if (null != bean.getRequestHeaders()) {
                bean.getRequestHeaders().forEach((k, v) -> {
                    httpPatch.addHeader(new Header() {
                        @Override
                        public String getName() {
                            return k;
                        }

                        @Override
                        public String getValue() {
                            return v;
                        }

                        @Override
                        public HeaderElement[] getElements() throws ParseException {
                            return new HeaderElement[0];
                        }
                    });
                });
            }

            CloseableHttpResponse response = httpClient.execute(httpPatch);
            HttpEntity entity = response.getEntity();
            TwHttpResponse twHttpResponse = new TwHttpResponse();
            if (entity != null) {
                twHttpResponse.setStatusCode(response.getStatusLine().getStatusCode());
                twHttpResponse.setResponseContent(EntityUtils.toString(entity, "UTF-8"));
            }
            response.close();
            httpClient.close();
            return twHttpResponse;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
