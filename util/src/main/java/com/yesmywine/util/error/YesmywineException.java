package com.yesmywine.util.error;


/**
 * Created by WANG, RUIQING on 2/9/17
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class YesmywineException extends Exception {

    private String code;
    private String msg;

    private String message;

    @Override
    public String getMessage() {
//        HttpBean httpBean = new HttpBean("http://47.89.18.26:8181/dic/" + getCode(), RequestMethod.get);
//        ;
//        httpBean.run();
//        String result = httpBean.getResponseContent();
//        System.out.println(result);
//        String message = ValueUtil.getFromJson(result, "data");

//        return message;
        return msg;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public YesmywineException(String code) {
        this.code = code;
        this.msg = "";
    }

    public YesmywineException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
