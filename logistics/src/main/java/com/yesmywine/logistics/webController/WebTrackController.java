package com.yesmywine.logistics.webController;

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
@RequestMapping("/web/logistics/track")
public class WebTrackController {
    @Autowired
    private TrackService trackService;

    @RequestMapping(method = RequestMethod.GET)
    public String save(@RequestParam Map<String, String> param) {
        try {
            return ValueUtil.toJson(HttpStatus.SC_OK, trackService.track(param));
        } catch (YesmywineException e) {
            return ValueUtil.toError(e.getCode(),e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            return ValueUtil.toError("500", e.toString());
        } catch (UnsupportedEncodingException e) {
            return ValueUtil.toError("500", e.toString());
        } catch (NullPointerException e) {
            return ValueUtil.toError("500", "没找到此单号");
        }

    }

}
