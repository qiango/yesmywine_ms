package com.yesmywine.goods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdUtil {
    /**
     * 生成id
     * <p>
     * 主站用调示例
     * orderId = IdUtil.genId("yyMMdd1{s}{s}{s}{r}{r}{s}{s}{r}{r}", seq, 5);
     *
     * @param format {r}一位随机数 ;  {s} 数据库产生的seq ;  yyyymmdd 日期格式
     * @param seq
     * @param seqLeng seq的长度
     * @return
     */
    protected static Random r = new Random();

    public static long genId(String format, long seq, int seqLeng) {
        // 1 替换seq部分
        String strSeq = fillZero("" + seq, seqLeng);
        for (int i = 0; i < seqLeng; i++) {
            format = replaceFirst(format, "{s}", "" + strSeq.charAt(i));
        }
        // 2 替换随机数部分
        String newValue = replaceFirst(format, "{r}", "" + r.nextInt(10));
        while (!newValue.equals(format)) {
            format = newValue;
            newValue = replaceFirst(format, "{r}", "" + r.nextInt(10));
        }
        format = newValue;
        // 3 替换日期部分
        return Long.parseLong(new SimpleDateFormat(format).format(new Date()), 10);
    }

    /**
     * 长度不足的补0， 过多的作切除
     *
     * @param v
     * @param length
     * @return
     */
    protected static String fillZero(String v, int length) {
        if (v.length() == length) return v;
        if (v.length() > length) return v.substring(v.length() - length);
        while (v.length() < length) {
            v = '0' + v;
        }
        return v;
    }

    /**
     * 普通匹配（非正则）情况下，替换第1个值
     *
     * @param string
     * @param target
     * @param replacement
     * @return
     */
    protected static String replaceFirst(String string, String target, String replacement) {
        return Pattern.compile(target, Pattern.LITERAL).matcher(
                string).replaceFirst(Matcher.quoteReplacement(replacement));
    }
//	public static void main(String[] args) {
//		System.out.println(IdUtil.genId("yyMMdd1{s}{s}{s}{r}{r}{s}{s}{r}{r}", 123456, 5));
//	}

}
