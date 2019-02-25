package com.yesmywine.pay.unionpay.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.*;

/**
 * OA专用的XML解析器
 *
 * @author Akuo
 */
public class XMLUtils {

    private static Log logger = LogFactory.getLog(XMLUtils.class);


//    /**
//     * 测试
//     *
//     * @throws IOException
//     * @throws JDOMException
//     * @author Akuo
//     * @params args
//     */
//    public static void main(String[] args) throws JDOMException, IOException {
//        String xml = "%3C%3Fxml+version%3D%221.0%22+encoding%3D%22GB2312%22%3F%3E%3Cdatas%3E%0D%0A%3Cdata%3E%0D%0A%3CorgNo%3E100000%3C%2ForgNo%3E%0D%0A%3CconferenceNo%3E1000009666B39BBC0391CE48257CF700105519%3C%2FconferenceNo%3E%0D%0A%3CconferenceName%3E%BB%E1%B9%DC%B5%DA%B6%FE%B4%CE%B2%E2%CA%D4%A3%AC%C7%EB%CE%F0%B0%EC%C0%ED%A1%A3%3C%2FconferenceName%3E%0D%0A%3CconferenceContent%3E%3C%2FconferenceContent%3E%0D%0A%3CconferenceType%3E1%3C%2FconferenceType%3E%0D%0A%3CconferenceCategory%3E0%3C%2FconferenceCategory%3E%0D%0A%3CconferenceRequire%3E1%3C%2FconferenceRequire%3E%0D%0A%3CindemnityService%3E3%3C%2FindemnityService%3E%0D%0A%3CfreeSpeak%3EMeetingSpeak%3C%2FfreeSpeak%3E%0D%0A%3CspecialSpeak%3EMeetingSpeak%3C%2FspecialSpeak%3E%0D%0A%3CSpeakControl%3E%3C%2FSpeakControl%3E%0D%0A%3CotherRequire%3E%3C%2FotherRequire%3E%0D%0A%3CcreateUser%3E%D7%DC%D6%B5%B0%E0%CA%D2%3C%2FcreateUser%3E%0D%0A%3CapplyUser%3E%D7%DC%D6%B5%B0%E0%CA%D2%3C%2FapplyUser%3E%0D%0A%3CapplyPhone%3E13810823389%3C%2FapplyPhone%3E%0D%0A%3CapplyDept%3E%B0%EC%B9%AB%CC%FC%3C%2FapplyDept%3E%0D%0A%3CbeginTime%3E2014-06-14+12%3A30%3A00%3C%2FbeginTime%3E%0D%0A%3CendTime%3E2014-06-14+13%3A30%3A00%3C%2FendTime%3E%0D%0A%3CroomNo%3E91812009904%3C%2FroomNo%3E%0D%0A%3CroomName%3E%D2%F8%D7%F92%C2%A5202%B8%DF%C7%E5%CA%D3%C6%B5%BB%E1%D2%E9%CA%D2%3C%2FroomName%3E%0D%0A%3CorgNoList%3E250000%3C%2ForgNoList%3E%0D%0A%3CorgNoListDetail%3E250000%7C250100%7C250200%3C%2ForgNoListDetail%3E%0D%0A%3CresourceNumLv1%3E2%3C%2FresourceNumLv1%3E%0D%0A%3CresourceNumLv2%3E3%3C%2FresourceNumLv2%3E%0D%0A%3CisFromTemplate%3E0%3C%2FisFromTemplate%3E%0D%0A%3Cstatus%3E2%3C%2Fstatus%3E%0D%0A%3C%2Fdata%3E%0D%0A%3C%2Fdatas%3E";
////		List<Map<String, String>> list = praseXML(xmlStr);
////		System.out.println(list.get(0).toString());
////		System.out.println(createXML(true, list));
//        String decode = URLDecoder.decode(xml, "gb2312");
//        System.out.println(decode);
//        System.out.println(URLEncoder.encode(decode, "gb2312"));
//    }


    public static List<Map<String, String>> parseXML(String xml) throws JDOMException, IOException {

        return parseXML(xml, false, "");

    }

    /**
     * 解析服务器传回的XML
     *
     * @throws IOException
     * @throws JDOMException
     * @author Akuo
     * @params xml
     * @params isPrint 是否打印传入的Xml
     * @params printPrefix 打印xml时加入前缀标识 类型
     */
    public static List<Map<String, String>> parseXML(String xml, boolean isPrint, String event) throws JDOMException, IOException {

        List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
        if (StringUtils.isEmpty(xml)) {
            return maps;
        }
        xml = URLDecoder.decode(xml, "GB2312");
        if (isPrint) {
            logger.info(new StringBuilder(event).append("[xml-->]:\n").append(xml));
        }

        StringReader reader = new StringReader(xml);
        InputSource inputSource = new InputSource(reader);
        inputSource.setEncoding("GBK");
        SAXBuilder saxBuilder = new SAXBuilder();
        Document document = null;
        document = saxBuilder.build(inputSource);
        Element rootElement = document.getRootElement();
        List<Element> rootChilden = rootElement.getChildren();
        List<Element> dataElements;
        if ("xtbgdatas".equals(rootElement.getName())) {
            Map<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < rootChilden.size(); i++) {
                for (Element element2 : rootChilden) {
                    map.put(element2.getName(), element2.getValue());
                }
            }
            maps.add(map);
            return maps;
        } else if ("datas".equals(rootElement.getName())) {
            Map<String, String> map = new HashMap<String, String>();
            Element element = rootChilden.get(0);
            List<Element> dataElements2 = element.getChildren();
            for (Element element2 : dataElements2) {
                map.put(element2.getName(), element2.getValue());
            }
            maps.add(map);
            return maps;
        } else {
            return maps;
        }

    }

    public static List<Map<String, String>> processXML(String xml, boolean isPrint, String event) throws Exception {

        List<Map<String, String>> resultList = parseXML(xml, isPrint, event);
        if (isPrint) {
            logger.info(new StringBuilder(event).append("[xmlToList-->]:\n").append(resultList.toString()));
        }


        if (resultList == null || resultList.size() <= 0) {
            String msg = event + "操作失败！解析OA参数为空";
            logger.error(msg);
            throw new Exception("");
        }

        return resultList;

    }

    /**
     * 生成XML字符串
     *
     * @return
     * @params isClient
     * HST是否作为客户端
     * @params responceMap
     * 要返回的map
     */
    public static String createXML(boolean isClient, List<Map<String, String>> responceMaps) throws Exception {
        Element rootElement = null;
        Element baseElement = null;
        Document document;

        if (isClient) {
            baseElement = new Element("datas");
            rootElement = new Element("data");
        } else {
            rootElement = new Element("responce");
        }

        for (Map<String, String> map : responceMaps) {
            Set<String> keySet = map.keySet();
            for (String key : keySet) {
                Element element = new Element(key);
                element.setText((String) map.get(key));
                rootElement.addContent(element);
            }
        }

        if (isClient) {
            baseElement.addContent(rootElement);
            document = new Document(baseElement);
        } else {
            document = new Document(rootElement);
        }

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getCompactFormat().setEncoding("GBK"));

        return outputter.outputString(document);
    }

    /**
     * 生成XML字符串
     * 描述: 之前的方法构建的xml并不完整，需进行扩展
     *
     * @return
     * @author lele
     * @paramss isClient HST是否作为客户端
     * @paramss responceMap 要返回的map
     */
    public static String createXML(List<Map<String, Object>> responceMaps, Exception e) {
        Document document = null;

        Element responseElement = null;
        Element resultCodeElement = null;
        Element resultMessageElement = null;
        Element datasElement = null;

        responseElement = new Element("response");
        resultCodeElement = new Element("resultCode");
        resultMessageElement = new Element("resultMessage");

        if (null == responceMaps && e != null) {
            resultCodeElement.setText("0");
            resultMessageElement.setText("查询失败");
        } else {
            Element dataElement = null;

            datasElement = new Element("datas");

            resultCodeElement.setText("1");
            resultMessageElement.setText("查询成功");

            for (Map<String, Object> map : responceMaps) {
                dataElement = new Element("data");
                Set<String> keySet = map.keySet();
                for (String key : keySet) {
                    Element element = new Element(key);
                    element.setText(map.get(key).toString());
                    dataElement.addContent(element);
                }
                datasElement.addContent(dataElement);
            }

        }
        responseElement.addContent(resultCodeElement);
        responseElement.addContent(resultMessageElement);
        if (datasElement == null)
            ;
        else
            responseElement.addContent(datasElement);

        document = new Document(responseElement);

        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getCompactFormat().setEncoding("GBK"));

        return outputter.outputString(document);
    }
}
