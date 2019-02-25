package com.yesmywine.goods.util;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static <T> T test(Class<T> clazz,T t, Map<String,Object> map) {
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.newInstance(); // 创建 JavaBean 对象
            for(Map.Entry entry : map.entrySet()){
                String key = (String)entry.getKey();
                Object value = entry.getValue();
//                clazz.getClass().getMethod("set"+key)
            }

//            Method after = clazz.getMethod("afterFind", String.class);


            // 给 JavaBean 对象的属性赋值

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    if ("".equals(value)) {
                        value = null;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    try {
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (InvocationTargetException e) {
                        System.out.println("字段映射失败");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println("实例化 JavaBean 失败");
        } catch (IntrospectionException e) {
            System.out.println("分析类属性失败");
        } catch (IllegalArgumentException e) {
            System.out.println("映射错误");
        } catch (InstantiationException e) {
            System.out.println("实例化 JavaBean 失败");
        }

        return (T) obj;
    }


    /*
    *@Author Gavin
    *@Description 判断字符串是否为数字
    *@Date 2017/3/20 16:49
    *@Email gavinsjq@sina.com
    *@Params
    */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 字符串是否为数值
     *
     * @return
     * @params string
     */
    public static boolean isNum(String string) {
        if (string == null || "".equals(string)) {
            return false;
        }
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isDigit(string.charAt(i))) {
                return false;
            }
        }
        return true;
    }


//    public static void main(String[] args) {
//        System.out.println(isNum("1"));
////    }

    public static <T> T toBean(Class<T> clazz, Map map) {
        T obj = null;
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            obj = clazz.newInstance(); // 创建 JavaBean 对象

            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (map.containsKey(propertyName)) {
                    // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                    Object value = map.get(propertyName);
                    if ("".equals(value)) {
                        value = null;
                    }
                    Object[] args = new Object[1];
                    args[0] = value;
                    try {
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (InvocationTargetException e) {
                        System.out.println("字段映射失败");
                    }
                }
            }
        } catch (IllegalAccessException e) {
            System.out.println("实例化 JavaBean 失败");
        } catch (IntrospectionException e) {
            System.out.println("分析类属性失败");
        } catch (IllegalArgumentException e) {
            System.out.println("映射错误");
        } catch (InstantiationException e) {
            System.out.println("实例化 JavaBean 失败");
        }
        return (T) obj;
    }

}
