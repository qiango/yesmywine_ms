package com.yesmywine.util.date;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;


/**
 * Created by taylor on 8/7/16.
 * twitter: @taylorwang789
 */
public class DateUtil {


    public static Date toDate(String date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static long toMillisecond(String date, String dateFormat) {
        return toDate(date, dateFormat).getTime();
    }

    public static String toString(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    public static String getNowTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }


    public static Date getTime(String s) {    //将时间戳转换成date类型的时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long time = new Long(s);
        String d = format.format(time);
        Date date = null;
        try {
            date = format.parse(d);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


}
