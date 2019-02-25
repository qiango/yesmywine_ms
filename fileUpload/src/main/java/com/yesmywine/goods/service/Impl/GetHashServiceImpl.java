package com.yesmywine.goods.service.Impl;

import com.yesmywine.goods.service.GetHashService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;

import static org.apache.tomcat.util.buf.HexUtils.toHexString;

/**
 * Created by light on 2017/4/13.
 */
@Service
public class GetHashServiceImpl implements GetHashService {

    //获得文件的hash值
    //hashType的值："MD5"，"SHA1"，"SHA-256"，"SHA-384"，"SHA-512"
    @Override
    public String getHash(InputStream is, String hashType) throws Exception {
        byte buffer[] = new byte[1024];
        MessageDigest md5 = MessageDigest.getInstance(hashType);
        for(int numRead = 0; (numRead = is.read(buffer)) > 0;)
        {
            md5.update(buffer, 0, numRead);
        }
        is.close();
        return toHexString(md5.digest());
    }
}
