/**
 * Licensed Property to China UnionPay Co., Ltd.
 * <p>
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 * All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * =============================================================================
 * Author         Date          Description
 * ------------ ---------- ---------------------------------------------------
 * xshu       2014-05-28       HTTP通信工具类
 * =============================================================================
 */
package com.yesmywine.pay.unionpay.sdk;

import com.alibaba.fastjson.JSON;
import com.yesmywine.pay.unionpay.util.Config;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import com.pay.Config;

public class HttpClient {
    /**
     * 目标地址
     */
    private URL url;

    /**
     * 通信连接超时时间
     */
    private int connectionTimeout;

    /**
     * 通信读超时时间
     */
    private int readTimeOut;

    /**
     * 通信结果
     */
    private String result;

    /**
     * 获取通信结果
     *
     * @return
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置通信结果
     *
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 构造函数
     *
     * @param url               目标地址
     * @param connectionTimeout HTTP连接超时时间
     * @param readTimeOut       HTTP读写超时时间
     */
    public HttpClient(String url, int connectionTimeout, int readTimeOut) {
        try {
            this.url = new URL(url);
            this.connectionTimeout = connectionTimeout;
            this.readTimeOut = readTimeOut;
        } catch (MalformedURLException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
        }
    }

    /**
     * 发送信息到服务端
     *
     * @param data
     * @param encoding
     * @return
     * @throws Exception
     */
    public int send(Map<String, String> data, String encoding) throws Exception {
        try {
            HttpURLConnection httpURLConnection = createConnection(encoding);
            if (null == httpURLConnection) {
                throw new Exception("创建联接失败");
            }
            String sendData = this.getRequestParamString(data, encoding);
            LogUtil.writeLog("请求报文:[" + sendData + "]");
            this.requestServer(httpURLConnection, sendData,
                    encoding);
            this.result = this.response(httpURLConnection, encoding);
            LogUtil.writeLog("同步返回报文:[" + result + "]");
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 发送信息到服务端 GET方式
     *
     * @param encoding
     * @return
     * @throws Exception
     * @params data
     */
    public int sendGet(String encoding) throws Exception {
        try {
            HttpURLConnection httpURLConnection = createConnectionGet(encoding);
            if (null == httpURLConnection) {
                throw new Exception("创建联接失败");
            }
            this.result = this.response(httpURLConnection, encoding);
            LogUtil.writeLog("同步返回报文:[" + result + "]");
            return httpURLConnection.getResponseCode();
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * HTTP Post发送消息
     *
     * @param connection
     * @param message
     * @throws IOException
     */
    private void requestServer(final URLConnection connection, String message, String encoder)
            throws Exception {
        PrintStream out = null;
        try {
            connection.connect();
            out = new PrintStream(connection.getOutputStream(), false, encoder);
            out.print(message);
            out.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * 显示Response消息
     *
     * @param connection
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @params CharsetName
     */
    private String response(final HttpURLConnection connection, String encoding)
            throws URISyntaxException, IOException, Exception {
        InputStream in = null;
        StringBuilder sb = new StringBuilder(1024);
        BufferedReader br = null;
        try {
            if (200 == connection.getResponseCode()) {
                in = connection.getInputStream();
                sb.append(new String(read(in), encoding));
            } else {
                in = connection.getErrorStream();
                sb.append(new String(read(in), encoding));
            }
            LogUtil.writeLog("HTTP Return Status-Code:["
                    + connection.getResponseCode() + "]");
            return sb.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != br) {
                br.close();
            }
            if (null != in) {
                in.close();
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
    }

    public static byte[] read(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        int length = 0;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        while ((length = in.read(buf, 0, buf.length)) > 0) {
            bout.write(buf, 0, length);
        }
        bout.flush();
        return bout.toByteArray();
    }

    /**
     * 创建连接
     *
     * @return
     * @throws ProtocolException
     */
    private HttpURLConnection createConnection(String encoding) throws ProtocolException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
            return null;
        }
        httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
        httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
        httpURLConnection.setDoInput(true); // 可读
        httpURLConnection.setDoOutput(true); // 可写
        httpURLConnection.setUseCaches(false);// 取消缓存
        httpURLConnection.setRequestProperty("Content-type",
                "application/x-www-form-urlencoded;charset=" + encoding);
        httpURLConnection.setRequestMethod("POST");
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
            husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            husn.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
            return husn;
        }
        return httpURLConnection;
    }

    /**
     * 创建连接
     *
     * @return
     * @throws ProtocolException
     */
    private HttpURLConnection createConnectionGet(String encoding) throws ProtocolException {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            LogUtil.writeErrorLog(e.getMessage(), e);
            return null;
        }
        httpURLConnection.setConnectTimeout(this.connectionTimeout);// 连接超时时间
        httpURLConnection.setReadTimeout(this.readTimeOut);// 读取结果超时时间
        httpURLConnection.setUseCaches(false);// 取消缓存
        httpURLConnection.setRequestProperty("Content-type",
                "application/x-www-form-urlencoded;charset=" + encoding);
        httpURLConnection.setRequestMethod("GET");
        if ("https".equalsIgnoreCase(url.getProtocol())) {
            HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
            husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            husn.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());//解决由于服务器证书问题导致HTTPS无法访问的情况
            return husn;
        }
        return httpURLConnection;
    }

    /**
     * 将Map存储的对象，转换为key=value&key=value的字符
     *
     * @param requestParam
     * @param coder
     * @return
     */
    private String getRequestParamString(Map<String, String> requestParam, String coder) {
        if (null == coder || "".equals(coder)) {
            coder = "UTF-8";
        }
        StringBuffer sf = new StringBuffer("");
        String reqstr = "";
        if (null != requestParam && 0 != requestParam.size()) {
            for (Entry<String, String> en : requestParam.entrySet()) {
                try {
                    sf.append(en.getKey()
                            + "="
                            + (null == en.getValue() || "".equals(en.getValue()) ? "" : URLEncoder
                            .encode(en.getValue(), coder)) + "&");
                } catch (UnsupportedEncodingException e) {
                    LogUtil.writeErrorLog(e.getMessage(), e);
                    return "";
                }
            }
            reqstr = sf.substring(0, sf.length() - 1);
        }
        LogUtil.writeLog("请求报文(已做过URLEncode编码):[" + reqstr + "]");
        return reqstr;
    }


    /**
     * 把接收到的json字符串转换为实体类的集合
     *
     * @param map
     * @return
     * @params URL
     * @params clazz
     */
    public static Map<String, Object> getCode(Map<String, String> map) throws IllegalStateException, IOException {

        Map<String, Object> retMap = new HashMap<String, Object>();
        // 创建httpclient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post
        HttpPost httpPost = new HttpPost(Config.SSO_URL);

        // 创建参数 列表
        ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        // 添加参数（参数名 参数值）
        for (Entry<String, String> entry : map.entrySet()) {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        CloseableHttpResponse response = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(paramsList, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);
            // 执行
            response = httpClient.execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 获取状态行
        StatusLine statusLine = response.getStatusLine();
        // 获取状态值
        int code = statusLine.getStatusCode();
        // 通过状态值判断是否需要解析返回值
        System.out.println("code   ::  " + code);
        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        InputStreamReader inputStreamReader = new InputStreamReader(
                content);
        // 读字符串用的。
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String s;
        StringBuilder result = new StringBuilder();
        // 判断获取到的流不为null
        while (((s = reader.readLine()) != null)) {
            result.append(s);
        }
        reader.close();// 关闭输入流

        // 在这里把result这个字符串个给JSONObject。解读里面的内容。
        String string = result.toString();
        if (code == 201 | code == 200) {
            string = string.replace("jsonpCallback(", "").replace(")", "").replace("[", "").replace("]", "");
            //JSONObject json = JSONObject.fromObject(string);
            retMap.put("json", string);
        }
        //JSON.parseArray(string, Map.class);
        retMap.put("code", code);
        return retMap;
    }
    /*public static int getCode(String URL, Map<String, String> map) {

		// 创建httpclient 实例
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 创建post
		HttpPost httpPost = new HttpPost(URL);

		// 创建参数 列表
		ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
		// 添加参数（参数名 参数值）
		for (Map.Entry<String, String> entry : map.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		CloseableHttpResponse response = null;
		try {
			urlEncodedFormEntity = new UrlEncodedFormEntity(paramsList, "utf-8");
			httpPost.setEntity(urlEncodedFormEntity);
			// 执行
			response = httpClient.execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 获取状态行
		StatusLine statusLine = response.getStatusLine();
		// 获取状态值
		int code = statusLine.getStatusCode();
		// 通过状态值判断是否需要解析返回值
		System.out.println("code   ::  " + code);
		return code;
	}*/

    /**
     * 通过给定的URL，参数集合，实体类类型 把接收到的json字符串转换为实体类的集合
     *
     * @param URL
     * @param map
     * @param clazz
     * @return
     */
    public static List<?> jsonToClass(String URL, Map<String, String> map,
                                      Class<?> clazz) {

        // 创建httpclient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建post
        HttpPost httpPost = new HttpPost(URL);

        // 创建参数 列表
        ArrayList<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        // 添加参数（参数名 参数值）
        for (Entry<String, String> entry : map.entrySet()) {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        CloseableHttpResponse response = null;
        List<?> list = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(paramsList, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);
            httpClient.getParams().setParameter("http.socket.timeout", new Integer(30000));
            // 执行
            response = httpClient.execute(httpPost);
            // 获取状态行
            StatusLine statusLine = response.getStatusLine();
            // 获取状态值
            int code = statusLine.getStatusCode();
            // 通过状态值判断是否需要解析返回值
            System.out.println("code   ::  " + code);
            if (code == 201 | code == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        content);
                // 读字符串用的。
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String s;
                StringBuilder result = new StringBuilder();
                // 判断获取到的流不为null
                while (((s = reader.readLine()) != null)) {
                    result.append(s);
                }
                reader.close();// 关闭输入流

                // 在这里把result这个字符串个给JSONObject。解读里面的内容。
                String string = result.toString();
                list = JSON.parseArray(string, clazz);
            } else {
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
