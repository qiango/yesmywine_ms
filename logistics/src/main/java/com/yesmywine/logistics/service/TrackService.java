package com.yesmywine.logistics.service;

import com.yesmywine.util.error.YesmywineException;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by wangdiandian on 2017/5/19.
 */
public interface TrackService {
    Object track(Map<String, String> param) throws YesmywineException,NoSuchAlgorithmException, UnsupportedEncodingException;

    Object trackWX(Map<String, String> param)throws YesmywineException;

    Object trackYT(Map<String, String> param) throws YesmywineException, UnsupportedEncodingException, NoSuchAlgorithmException;

    String generate(String param);
}
