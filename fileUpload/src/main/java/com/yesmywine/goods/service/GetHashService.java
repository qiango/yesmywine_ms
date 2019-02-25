package com.yesmywine.goods.service;

import java.io.InputStream;

/**
 * Created by light on 2017/4/13.
 */
public interface GetHashService {

    String getHash(InputStream is, String hashType) throws Exception;

}
