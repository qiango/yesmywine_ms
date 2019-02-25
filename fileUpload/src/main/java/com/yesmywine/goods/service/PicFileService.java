package com.yesmywine.goods.service;


import com.yesmywine.goods.entity.PicFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wangdiandian on 2017/3/15.
 */
public interface PicFileService {

    PicFile findById(Integer id);

    PicFile save(PicFile picFile);

    PicFile[] queryByTempFileName(String tempFileName);

    PicFile[] queryByFormalFileName(String formalFileName);

    InputStream shrink(InputStream file) throws IOException;

//    InputStream shrink(File file) throws IOException;

    InputStream shrink(InputStream file, Integer height, Integer width) throws IOException;

    InputStream[] shrinks(InputStream file) throws IOException;

    InputStream shrinksType(InputStream file) throws IOException;
}
