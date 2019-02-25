package com.yesmywine.pay.wechat.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by SJQ on 2017/2/17.
 */
public class XmlUtils {
    /**
     * 解析XML字符串
     *
     * @param xml
     * @return
     * @throws
     */
    public static SortedMap<String, Object> parseXmlStr(String xml) {
        SortedMap<String, Object> map = new TreeMap<String, Object>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Iterator<Element> it = root.elementIterator();
            while (it.hasNext()) {
                Element element = it.next();
                map.put(element.getName(), element.getTextTrim());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }
}
