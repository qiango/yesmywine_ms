
package com.yesmywine.base.record.bean;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by WANG, RUIQING on 12/22/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class QueryCondition {

    private ConditionType conditionType;
    private String field;
    private Object value;
    private String className;

    public QueryCondition(ConditionType conditionType, String field, Object value) {
        this.conditionType = conditionType;
        this.field = field;
        this.value = value;
        this.className = null;
    }

    public QueryCondition(ConditionType conditionType, String field, Object value, String clzPath) {
        this.conditionType = conditionType;
        this.field = field;
        this.value = value;
        this.className = clzPath;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public void setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public <T> Expression<Boolean> getExpression(Root<T> root, CriteriaBuilder cb) {
        if (null != className) {
            try {
                Class clz = Class.forName(className);
//				Method method = clz.getMethod("getFromString",String.class);
//				value = method.invoke(clz.,value.toString());
                value = Enum.valueOf(clz, value.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        switch (conditionType) {
            case equal:
                Date date = null;
                if(field.indexOf("Time")>=0){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        date = sdf.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar gt_c = Calendar.getInstance();
                    Calendar lt_c = Calendar.getInstance();
                    lt_c.setTime(date);
                    lt_c.add(Calendar.DAY_OF_YEAR,-1);
                    Date lt_date = lt_c.getTime();
                    Date gt_date = gt_c.getTime();
                    return cb.between(root.get(field),  lt_date,gt_date);
                }
                return cb.equal(root.get(field), value);
            case notEqual:
                return cb.notEqual(root.get(field), value);
            case like:
                return cb.like(root.get(field), "%" + value + "%");
            case notLike:
                return cb.notLike(root.get(field), "%" + value + "%");
            case greaterThan:

                Date gtDate = null;
                if(field.indexOf("Time")>=0){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        gtDate = sdf.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return cb.greaterThan(root.get(field), gtDate);
                }
                System.out.println(new Date());
                System.out.println(gtDate);
                return cb.greaterThan(root.get(field), value.toString());

                //                return cb.greaterThan(root.get(field), value);
            case lessThan:
                Date lessDate = new Date();
                if(field.indexOf("Time")>=0){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        lessDate = sdf.parse(value.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return cb.lessThan(root.get(field), lessDate);
                }
                return cb.lessThan(root.get(field), value.toString());
            case in:
                Object[] objs =  value.toString().split(",");
                List list = new ArrayList();
                try{
                    Integer.valueOf(objs[0].toString());
                    for(Object obj:objs){
                        list.add(Integer.valueOf(obj.toString()));
                    }
                }catch (Exception e){
                    for(Object obj:objs){
                        list.add(obj);
                    }
                }

                return cb.in(root.get(field)).value(list);
            default:
                break;
        }
//		return cb.equal(root.<Integer>get("difficulty"),Integer.valueOf(value.toString())) ;
        return null;
    }


//	private  <T> Expression<Boolean> expString(Root<T> root, CriteriaBuilder cb) {
//		return cb.equal(root.get(field),value) ;
//	}


}
