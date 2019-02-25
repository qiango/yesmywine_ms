package com.yesmywine.logistics.controller;

import com.yesmywine.logistics.service.TrackService;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/19.
 */
@RestController
@RequestMapping("/logistics/track")
public class TrackController {
    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.GET)
    public String save(@RequestParam Map<String, String> param) {//宅急送物流跟踪
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, trackService.track(param));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            return ValueUtil.toError("500", e.toString());
        } catch (UnsupportedEncodingException e) {
            return ValueUtil.toError("500", e.toString());
        }
    }

    @RequestMapping(value = "/WX", method = RequestMethod.GET)
    public String saveWX(@RequestParam Map<String, String> param) {//万象物流跟踪
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, trackService.trackWX(param));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        }

    }

    @RequestMapping(value = "/YT", method = RequestMethod.GET)
    public String saveYT(@RequestParam Map<String, String> param) {//圆通物流跟踪
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, trackService.trackYT(param));
        } catch (Exception e) {
            return e.toString();
        }

    }

    @RequestMapping(value = "/aaa", method = RequestMethod.GET)
    public String saveaaa(String param) {//圆通物流跟踪
        try {
            return ValueUtil.toJson(HttpStatus.SC_CREATED, trackService.generate(param));
        } catch (Exception e) {
            return e.toString();
        }

    }

}
