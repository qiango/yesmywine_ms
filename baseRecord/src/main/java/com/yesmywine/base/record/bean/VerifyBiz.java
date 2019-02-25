package com.yesmywine.base.record.bean;

import com.yesmywine.base.record.error.BaseRecordError;
import org.apache.commons.lang3.math.NumberUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WANG, RUIQING on 12/20/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class VerifyBiz {

    public static void verify(VerifyBean bean, Object obj) throws BaseRecordError {
        switch (bean.getType()) {
            case isNumber:
                isNumber(bean.getFields(), obj);
                break;
            case notNull:
                notNull(bean.getFields(), obj);
                break;
            case numberRange:
                numberRange(bean, obj);
                break;
            case email:
                isEmail(bean.getFields(), obj);
                break;
        }
    }

    private static void notNull(List<String> fields, Object obj) throws BaseRecordError {
        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = obj.getClass().getDeclaredField(fields.get(i));
                field.setAccessible(true);
                Object value = field.get(obj);
                if (null == value) {
                    throw new BaseRecordError("dataVerifyError", fields.get(i), VerifyType.notNull);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void isEmail(List<String> fields, Object obj) throws BaseRecordError {
        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = obj.getClass().getDeclaredField(fields.get(i));
                field.setAccessible(true);
                Object value = field.get(obj);
                if (null == value) {
                    throw new BaseRecordError("dataVerifyError", fields.get(i), VerifyType.notNull);
                } else {
                    Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
                    Matcher m = p.matcher(value.toString());
                    if (!m.matches()) {
                        throw new BaseRecordError("dataVerifyError", fields.get(i), VerifyType.email);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private static void isNumber(List<String> fields, Object obj) throws BaseRecordError {
        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = obj.getClass().getDeclaredField(fields.get(i));
                field.setAccessible(true);
                Object value = field.get(obj);
                if (null == value || !NumberUtils.isDigits(value.toString())) {
                    throw new BaseRecordError("dataVerifyError", fields.get(i), VerifyType.isNumber);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void numberRange(VerifyBean bean, Object obj) throws BaseRecordError {
        List<String> fields = bean.getFields();
        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = obj.getClass().getDeclaredField(fields.get(i));
                field.setAccessible(true);
                Object value = field.get(obj);
                if (null == value || !NumberUtils.isDigits(value.toString())) {
                    Long val = Long.parseLong(value.toString());
                    if (bean.getMinValue() > val || bean.getMaxValue() < val) {
                        throw new BaseRecordError("dataVerifyError", fields.get(i), VerifyType.numberRange);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
