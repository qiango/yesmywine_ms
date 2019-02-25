package com.yesmywine.goods.service;

/**
 * Created by light on 2017/4/12.
 */
public interface FilePathService {
    String getFilePath();

    String[] getFormalFilePath(String module, String id, String fileName);

    String[] getFormalFilePathType(String module, String id, String fileName);
}
