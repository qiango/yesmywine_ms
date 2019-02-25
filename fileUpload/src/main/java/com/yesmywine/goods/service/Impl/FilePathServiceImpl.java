package com.yesmywine.goods.service.Impl;

import com.yesmywine.goods.entity.FileUploadUrl;
import com.yesmywine.goods.service.FilePathService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by light on 2017/4/12.
 */
@Service
public class FilePathServiceImpl implements FilePathService {
    @Override
    public String getFilePath() {
        String baseFolder =  new FileUploadUrl().FILETEMPURL;

        Date nowDate = new Date();
        // yyyy/MM/dd
        baseFolder = baseFolder + "/" + new DateTime(nowDate).toString("yyyy");

        baseFolder = baseFolder + "/" + new DateTime(nowDate).toString("MM");

        baseFolder = baseFolder + "/" + new DateTime(nowDate).toString("dd");

//        String fileName = new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")
//                + RandomUtils.nextInt(100, 9999)+ last + "." + ext;
        return baseFolder;
    }

    @Override
    public String[] getFormalFilePath(String module, String id, String fileName) {
        String[] path = new String[3];
        path[0] = FileUploadUrl.FILEFORMALURL + "/" + module+ "/"+ id +"/s" +"/" +fileName;
        path[1] = FileUploadUrl.FILEFORMALURL + "/" + module+ "/"+ id +"/m" +"/" +fileName;
        path[2] = FileUploadUrl.FILEFORMALURL + "/" + module+ "/"+ id +"/l" +"/" +fileName;
        return path;
    }

    @Override
    public String[] getFormalFilePathType(String module, String id, String fileName) {
        String[] path = new String[1];
        path[0] = FileUploadUrl.FILEFORMALURL + "/" + module+ "/"+ id +"/l" +"/" +fileName;
        return path;
    }
}
