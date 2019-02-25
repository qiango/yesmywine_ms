package com.yesmywine.pay.unionpay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;

/**
 * @author Thinking
 * @since 1.0
 */
public abstract class ResourceUtil {

    protected static final Log logger = LogFactory.getLog(ResourceUtil.class);

    /**
     * 将classpath路径下文件路径 转化为URL
     *
     * @param p_location
     * @return
     */
    public static URL getResourceURL(String p_location) {

        //当文件放在class目录下时且不含classpath:前缀
        URL url = null;
        try {
            //ClassLoader cl= Threads.currentThread().getContextClassLoader();
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            url = cl.getResource(p_location);
        } catch (Exception e) {
            logger.warn(new StringBuilder(p_location).append(": not to URL, URL Return null."));
            return null;
        }
        return url;
    }

    /**
     * 根据资源名称获取输入流
     *
     * @param p_location The resource name
     * @return
     */
    public static InputStream getResourceAsStream(String p_location) {
        ClassLoader cl = ClassUtils.getDefaultClassLoader();
        InputStream in = cl.getResourceAsStream(p_location);
        return in;
    }


    /**
     * 装载资源到文件
     *
     * @param p_location 资源的绝对路径/资源的classpath
     * @return
     * @see #getResourceURL(String)
     */
    public static File getResourceFile(String p_location) throws IOException {
        File file = null;
        URL url = getResourceURL(p_location);
        try {
            if (url == null) {
                //1 当采用绝对路径时  D:/chen/chen.properties
                //2 或者 含有classpath:前缀
                file = ResourceUtils.getFile(p_location);
            } else {
                //传统方式中  File URL 只能以"file://" 开头的 。
                //uap 模块中的使用的是  "束"协议bundleresource:// 例如：bundleresource://150.fwk17482403:1/diag-debug.properties
                file = ResourceUtils.getFile(url);
                //TODO: "束" bundleresource://
            }
            return file;
        } catch (Exception e) {
            throw new IOException("loading file failed. from " + p_location);
        }
    }


    public static void main(String[] args) {
        long time1 = 1442304831125L;
        Timestamp time = new Timestamp(1442304831125L);
        System.out.println(time);
    }

}
