package com.yesmywine.sso.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * Created by SJQ on 2017/6/19.
 */
@RestController
@RequestMapping("/web/sso/captcha")
public class CaptchaController {
    @Autowired
    private Producer captchaProducer;

    @RequestMapping(method = RequestMethod.GET)
    public String getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        System.out.println("请求sessionId==>"+sessionId);
        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        RedisCache.set(sessionId,code);
//        RedisCache.expire(sessionId,30);
        System.out.println("******************验证码是: " + code + "******************");

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");


        // create the text for the image
        String capText = captchaProducer.createText();
        System.out.println("放入session中的验证码为==》"+capText);
        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return ValueUtil.toJson(sessionId);
    }

    @RequestMapping(value = "/getCode",method = RequestMethod.GET)
    public String getCode(HttpServletRequest request, String captcha,String sessionId) {
        try {
            ValueUtil.verify(captcha,"captcha");
            HttpSession session = request.getSession();
            System.out.println("验证的的sessionId==>"+session.getId());
            String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
            System.out.println("获取验证的code==>"+code);
            if(code==null){
                ValueUtil.isError("验证码已失效");
            }else{
                if (!captcha.toLowerCase().equals(code.toLowerCase())){
                    ValueUtil.isError("验证码错误");
                }
            }
            return ValueUtil.toJson("SUCCESS");
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }
    }

}
