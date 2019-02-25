package com.yesmywine.pay.wechat.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by SJQ on 2017/2/17.
 */
public class HttpRequest {
    public static String post(String url, String pama) throws IOException {
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();

        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        //设置超时时间
        connection.setConnectTimeout(10000);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // 默认是 GET方式
        connection.setRequestMethod("POST");

        // Post 请求不能使用缓存
        connection.setUseCaches(false);

        connection.setInstanceFollowRedirects(true);

        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致

        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
        out.writeBytes(pama);

        out.flush();
        out.close();

        int status = connection.getResponseCode();
        if (status == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            StringBuffer sb = new StringBuffer("");
            while ((line = reader.readLine()) != null) {

                line = new String(line);
                sb.append(line);
            }
            reader.close();
            connection.disconnect();
            if (sb.toString().length() > 0) {
                return sb.toString();
            }
            return String.valueOf(status);
        }
        return String.valueOf(status);
    }
}
