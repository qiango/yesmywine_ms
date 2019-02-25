package com.yesmywine.db.base.ehcache;

/**
 * Created by SJQ on 2017/6/15.
 */
public class CacheStatement {
    //这里的单引号不能少，否则会报错，被识别是一个对象;
    public static final String ACTIVITY_KEY = "'activity'";
    public static final String ACTIVITY_KEY_ = "'activity_'";

    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml
     */
    public static final String ACTIVITY_VALUE = "activity";


    public static final String PROPERTIES_VALUE = "properties";

    public static final String GOODS_VALUE = "goods";

    public static final String CART_VALUE = "cart";

}
