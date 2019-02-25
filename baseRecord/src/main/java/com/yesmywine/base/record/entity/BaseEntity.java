package com.yesmywine.base.record.entity;


import com.yesmywine.base.record.bean.VerifyBean;
import com.yesmywine.base.record.bean.VerifyBiz;
import com.yesmywine.base.record.bean.VerifyType;
import com.yesmywine.base.record.error.BaseRecordError;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@MappedSuperclass
public class BaseEntity<T> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;
    protected Date createTime;


    public BaseEntity() {
        this.createTime = new Date();
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    @Transient
    protected transient List<VerifyBean> beans = new ArrayList<>();
    // data format

    public void addVerify(VerifyType verifyType, String... fields) {
        VerifyBean bean = new VerifyBean(verifyType, fields);
        beans.add(bean);
    }

    public void verify() throws BaseRecordError {
        for (int i = 0; i < beans.size(); i++) {
            VerifyBiz.verify(beans.get(i), this);
        }
    }

    public void afterFind() {
    }

    public void afterFind(String fields) {
        if (null != fields) {
            String[] fieldArr = fields.split(",");
            List<String> notNullArr = new ArrayList<>();
            List<Field> nullArr = new ArrayList<>();


            Field[] thisFields = this.getClass().getDeclaredFields();


            for (int i = 0; i < fieldArr.length; i++) {
                notNullArr.add(fieldArr[i]);
            }

            for (int i = 0; i < thisFields.length; i++) {
                if (!notNullArr.contains(thisFields[i].getName())) {
                    nullArr.add(thisFields[i]);
                }
            }
            setNUll(nullArr);
            if (!notNullArr.contains("id")) {
                setId(null);
            }
            if (!notNullArr.contains("createTime")) {
                setCreateTime(null);
            }
            notNullArr.remove("id");
            notNullArr.remove("createTime");
            for (int i = 0; i < notNullArr.size(); i++) {
                setNotNull(notNullArr.get(i));
            }
        }
    }

    public void beforeSave() throws BaseRecordError {
        if (null == this.id && null == this.createTime) {
            this.createTime = new Date();
        }
        verify();
    }

    public void afterSave() {
    }


    //
    protected void setNotNull(String fieldName) {
        Field field = null;
        try {
            field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(this);
            if (null == value) {
                field.set(this, "");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void setNUll(List<Field> fields) {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            try {
                fields.get(i).set(this, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


}
