package com.yesmywine.base.record.error;

import com.yesmywine.base.record.bean.VerifyType;

/**
 * Created by WANG, RUIQING on 12/20/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class BaseRecordError extends Exception {
    private String code;
    private String field;
    private VerifyType verifyType;

    public BaseRecordError() {
    }

    public BaseRecordError(String code, String field, VerifyType verifyType) {
        this.code = code;
        this.field = field;
        this.verifyType = verifyType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public VerifyType getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(VerifyType verifyType) {
        this.verifyType = verifyType;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        System.out.println("code:" + code);
        System.out.println("field:" + field);
        System.out.println("verifyType:" + verifyType.name());
    }
}
