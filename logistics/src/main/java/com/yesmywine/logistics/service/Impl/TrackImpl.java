package com.yesmywine.logistics.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.logistics.service.TrackService;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/19.
 */
@Service
public class TrackImpl implements TrackService {

    public Object trackZJS(Map<String, String> param)throws YesmywineException {

//        String name = "MuShiMaoYi";
//        String logisticsNumber = "8392393391";
        String name="MuShiMaoYi";
        String logisticsNumber=param.get("expressNo");
        HttpBean httpBean = new HttpBean("http://edi.zjs.com.cn/service/query.asmx/GetMd5", RequestMethod.get);
        httpBean.addParameter("userId", name);
        StringBuffer sb = new StringBuffer();
        sb.append("<BatchQueryRequest><logisticProviderID>");
        sb.append(name);
        sb.append("</logisticProviderID><orders><order><orderNo>");
        sb.append(logisticsNumber);
        sb.append("</orderNo></order></orders></BatchQueryRequest>");
        System.out.print(sb);
        httpBean.addParameter("strInfo", UriEncoder.encode(sb.toString()));
        httpBean.run();
        String result = httpBean.getResponseContent();
        String md5 = StringUtils.substringBetween(result,"<string xmlns=\"http://edi.zjs.com.cn/\">","</string>");

        HttpBean httpBean1 = new HttpBean("http://edi.zjs.com.cn/service/query.asmx/QueryOrderInfo",RequestMethod.get);
        httpBean1.addParameter("userId", name);
        httpBean1.addParameter("xml", UriEncoder.encode(sb.toString()));
        httpBean1.addParameter("md5",md5);
        httpBean1.run();
        String result1 = httpBean1.getResponseContent();
        System.out.print(result1);
        String aaa = StringUtils.substringBetween(result1,"<string xmlns=\"http://edi.zjs.com.cn/\">","</string>");
        System.out.println("..............."+aaa);
        String bbb = null;
        try {
            bbb = URLDecoder.decode(aaa,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String xml=bbb.replaceAll("&gt;",">").replaceAll("&lt;","<");
        System.out.println(xml);
        Object json = xml2JSON(xml);
        System.out.println("json=" + json);
        return json;
    }

    @Override
    public Object track(Map<String, String> param) throws YesmywineException, NoSuchAlgorithmException, UnsupportedEncodingException, NullPointerException {
        HttpBean httpBean = new HttpBean(Dictionary.DIC_HOST+"/dic/sysCode/itf", RequestMethod.get);
        httpBean.addParameter("sysCode", "shipperCode");
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String data = ValueUtil.getFromJson(temp, "data");
        JSONArray jsonArray = JSONArray.parseArray(data);
        for(int j=0;j<jsonArray.size(); j++) {
            JSONObject jsonObject = jsonArray.getJSONObject(j);
            if (jsonObject.get("entityValue").equals(param.get("shipperCode")) ) {
                if (jsonObject.get("entityCode").equals("zjs") ) {
                    Object o = this.trackZJS(param);
                    JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
                    Object orders = jsonObject1.get("orders");
                    JSONArray jsonArrayZJS = JSONArray.parseArray(orders.toString());
                    JSONArray jsonArrayResult = new JSONArray();
                    for(int i=0;i<jsonArrayZJS.size(); i++){
                        JSONObject jsonObjectZJS = jsonArrayZJS.getJSONObject(i);
                        Object steps = jsonObjectZJS.get("steps");
                        JSONArray jsonArraySteps = JSONArray.parseArray(steps.toString());
                        for(int y=0;y<jsonArraySteps.size(); y++){
                            JSONObject jsonObjectSteps = jsonArraySteps.getJSONObject(y);
                            JSONObject jsonObjectResult = new JSONObject();
                            String acceptAddress = jsonObjectSteps.get("acceptAddress").toString();
                            String acceptTime = jsonObjectSteps.get("acceptTime").toString();
                            jsonObjectResult.put("acceptAddress", acceptAddress);
                            jsonObjectResult.put("acceptTime", acceptTime);
                            jsonArrayResult.add(jsonObjectResult);
                        }
                    }
                    return jsonArrayResult;
                }else if (jsonObject.get("entityCode").equals("wx") ) {
                    Object o = this.trackWX(param);
                    JSONArray jsonArrayData = JSONArray.parseArray(o.toString());
                    JSONArray jsonArrayResult = new JSONArray();
                    for(int i=0;i<jsonArrayData.size(); i++){
                        JSONObject jsonObjectData = jsonArrayData.getJSONObject(i);
                        JSONObject jsonObjectResult = new JSONObject();
                        String operatedep = jsonObjectData.get("operatedep").toString();
                        String remark = jsonObjectData.get("remark").toString();
                        String operatetime = jsonObjectData.get("operatetime").toString();
                        jsonObjectResult.put("acceptAddress", "["+operatedep+"]"+" "+remark);
                        jsonObjectResult.put("acceptTime", operatetime);
                        jsonArrayResult.add(jsonObjectResult);
                    }
                    return jsonArrayResult;
                }else if (jsonObject.get("entityCode").equals("yt") ) {
                    Object o = this.trackYT(param);
                    JSONObject jsonObject1 = JSONObject.parseObject(o.toString());
                    Object orders = jsonObject1.get("orders");
                    JSONObject jsonObject2 = JSONObject.parseObject(orders.toString());
                    Object order = jsonObject2.get("order");
                    JSONObject jsonObjectZJS = JSONObject.parseObject(order.toString());
                    JSONArray jsonArrayResult = new JSONArray();
                    Object steps = jsonObjectZJS.get("steps");
                    JSONArray jsonArraySteps = JSONArray.parseArray(steps.toString());
                    for(int y=0;y<jsonArraySteps.size(); y++){
                        JSONObject jsonObjectSteps = jsonArraySteps.getJSONObject(y);
                        JSONObject jsonObjectResult = new JSONObject();
                        String acceptAddress = jsonObjectSteps.get("acceptAddress").toString();
                        String name = jsonObjectSteps.get("name").toString();
                        String remark = jsonObjectSteps.get("remark").toString();
                        String acceptTime = jsonObjectSteps.get("acceptTime").toString();
                        jsonObjectResult.put("acceptAddress", acceptAddress+" "+name+" "+remark);
                        jsonObjectResult.put("acceptTime", acceptTime);
                        jsonArrayResult.add(jsonObjectResult);
                    }
                    return jsonArrayResult;
                }
            }
        }
        return null;
    }

    @Override
    public Object trackWX(Map<String, String> param) throws YesmywineException {
        String bill_no=param.get("expressNo");
        String cust_code=param.get("cust_code");
        HttpBean httpBean = new HttpBean("http://175.102.133.190:8383/A1_INTE/BillState/queryXmlHistory.do", RequestMethod.get);
        httpBean.addParameter("bill_no", bill_no);
        httpBean.addParameter("cust_code", cust_code);
        httpBean.run();
        String result = httpBean.getResponseContent();
        System.out.println("---------------------------------------"+result);
//        String md5 = StringUtils.substringBetween(result,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<response>","</response>");
//        System.out.println("---------------------------------------"+md5);
        String bbb = null;
        try {
            bbb = URLDecoder.decode(result,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String xml=bbb.replaceAll("&gt;",">").replaceAll("&lt;","<");
        System.out.println(xml);
        Object json = xml2JSON(xml);
        System.out.println("json=" + json);
        return json;
    }


    @Override
    public Object trackYT(Map<String, String> param) throws YesmywineException, UnsupportedEncodingException, NoSuchAlgorithmException {
        String expressNo=param.get("expressNo");
//        String generate = this.generate(expressNo);
//        StringBuilder serviceUrl1 =new StringBuilder();
//        StringBuilder serviceUrl2 =new StringBuilder();
//        StringBuilder serviceUrl3 =new StringBuilder();

        String verifyData = this.getVerifyData(expressNo);
//        serviceUrl1.append(URLEncoder.encode(getXml(expressNo),"utf-8"));

        String xml1 = this.getXml(expressNo);
//        serviceUrl2.append(URLEncoder.encode(getVerifyData(expressNo),"utf-8"));

        String clientID1 = URLEncoder.encode(clientID, "UTF-8");
//        serviceUrl3.append(URLEncoder.encode(clientID, "UTF-8"));

        HttpBean httpBean = new HttpBean(url, RequestMethod.post);
        httpBean.addParameter("logistics_interface", xml1);
        httpBean.addParameter("data_digest", verifyData);
        httpBean.addParameter("clientId", clientID1);
        httpBean.run();
        String result = httpBean.getResponseContent();
        System.out.println("---------------------------------------"+result);
//        String md5 = StringUtils.substringBetween(result,"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<response>","</response>");
//        System.out.println("---------------------------------------"+md5);
        String bbb = null;
        try {
            bbb = URLDecoder.decode(result,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String xml=bbb.replaceAll("&gt;",">").replaceAll("&lt;","<");
        System.out.println(xml);
        Object json = xml2JSON(xml);
        System.out.println("json=" + json);
        return json;
    }


    public static Object xml2JSON(String xml) {
        System.out.print(xml);
        return new XMLSerializer().read(xml);
    }



    public String generate(String expressNo) {

        StringBuilder serviceUrl=new StringBuilder();
        serviceUrl.append("logistics_interface=");
        try {
            serviceUrl.append(URLEncoder.encode(getXml(expressNo),"utf-8"));
            serviceUrl.append("&data_digest=");
            serviceUrl.append(URLEncoder.encode(getVerifyData(expressNo),"utf-8"));
            serviceUrl.append("&clientId=").append(URLEncoder.encode(clientID, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch(Exception e){

        }
        return serviceUrl.toString();
    }



    private String getXml(String expressNo){
        StringBuilder xml=new StringBuilder();
        xml.append("<BatchQueryRequest><logisticProviderID>");
        xml.append(account);
        xml.append("</logisticProviderID>");
        xml.append("<clientID>");
        xml.append(clientID);
        xml.append("</clientID>");
        xml.append("<orders><order><mailNo>");
        xml.append(expressNo);
        xml.append("</mailNo></order>");
        xml.append("</orders></BatchQueryRequest>");
        return xml.toString();
    }



    private String getVerifyData(String expressNo) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String str = getXml(expressNo)+partnered;
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        messagedigest.update((str).getBytes("UTF-8"));
        byte[] abyte0 = messagedigest.digest();
        return  new String(Base64.encodeBase64(abyte0));
    }

    private String url = "http://jingang.yto56.com.cn/ordws/Vip15Servlet";
    private String name = "圆通";
    private String clientID = "YMW";
    private String account = "YTO";
    private String partnered = "njkgjbio";

//	<bean >
//		<property name="url" value="http://jingang.yto56.com.cn/ordws/Vip15Servlet"/>
//		<property name="name" value="圆通"/>
//		<property name="clientID" value="YMW"/>
//		<property name="account" value="YTO"/>
//		<property name="partnered" value="njkgjbio"/>
//	</bean>
}
