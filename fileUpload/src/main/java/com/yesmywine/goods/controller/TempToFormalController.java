package com.yesmywine.goods.controller;

import com.yesmywine.goods.dao.ImgFileDao;
import com.yesmywine.goods.entity.ImgFile;
import com.yesmywine.goods.entity.PicFile;
import com.yesmywine.goods.service.FilePathService;
import com.yesmywine.goods.service.GetHashService;
import com.yesmywine.goods.service.Impl.PicFileServiceImpl;
import com.yesmywine.goods.service.PicFileService;
import com.yesmywine.goods.util.Ftp;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

/**
 * Created by light on 2017/4/12.
 */
@RestController
@RequestMapping("/fileUpload/tempToFormal")
public class TempToFormalController {

    @Autowired
    private PicFileServiceImpl picFileService;
    @Autowired
    private FilePathService filePathService;
    @Autowired
    private GetHashService getHashService;
    @Autowired
    private ImgFileDao imgFileDao;


    @RequestMapping(method = RequestMethod.POST)
    public String saveTo(String module, Integer mId, String id, Integer type) throws IOException {

        try {
            ValueUtil.verify(module);
            ValueUtil.verify(id);
            ValueUtil.verify(mId);
            String[] ids = id.split(",");
            String ext = "png";
            Integer mPath = mId%64;
            Map<String, String>[] maps = new Map[ids.length];
            if (ValueUtil.isEmpity(type)|| ValueUtil.notEmpity(type)) {
                for (int j = 0; j < ids.length; j++) {
                    ImgFile byId = this.imgFileDao.findById(Integer.valueOf(ids[j]));
                    if (ValueUtil.isEmpity(byId)) {
                        continue;
                    }
                    String fromPath = "/var/ftp/" + byId.getUrl() + "_l." + ext;
                    String toPath1 = "/var/ftp/file/img/" + module+"/" +mPath + "/" + mId + "/" + "l/" + byId.getFileName() + ".png";
                    String toPath2 = "/var/ftp/file/img/" + module+"/" +mPath + "/" + mId + "/" + "s/" + byId.getFileName() + ".png";
                    String toPath3  = "/var/ftp/file/img/" + module+"/" +mPath + "/" + mId + "/" + "m/" + byId.getFileName() + ".png";

                    this.picFileService.resizePNG_L(fromPath, toPath1, byId.getWidth(), byId.getHeight());
                    this.picFileService.resizePNG_M(fromPath, toPath3, byId.getWidth(), byId.getHeight());
                    this.picFileService.resizePNG_S(fromPath, toPath2, byId.getWidth(), byId.getHeight());


                    Map<String, String> map = new HashMap<>();
                    map.put("id", ids[j]);
                    map.put("name", byId.getFileName());
                    maps[j] = map;

                }
            } else {
                for (int j = 0; j < ids.length; j++) {
                    ImgFile byId = this.imgFileDao.findById(Integer.valueOf(ids[j]));
                    if (ValueUtil.isEmpity(byId)) {
                        continue;
                    }
                    String fromPath = "/var/ftp/" + byId.getUrl() + "_l." + ext;
                    String toPath1 = "/var/ftp/file/img/" + module + "/" + mId + "/" + "l/" + byId.getFileName() + ".png";
                    this.picFileService.resizePNG_L(fromPath, toPath1, byId.getWidth(), byId.getHeight());

                    Map<String, String> map = new HashMap<>();
                    map.put("id", ids[j]);
                    map.put("name", byId.getFileName());
                    maps[j] = map;
                }

            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, maps);
        } catch (YesmywineException e) {
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }



    @RequestMapping(value = "/itf", method = RequestMethod.POST)
    public String saveToItf(String module, Integer mId, String id, Integer type) throws IOException {

        try {
            ValueUtil.verify(module);
            ValueUtil.verify(id);
            ValueUtil.verify(mId);
            String[] ids = id.split(",");
            String ext = "png";
            Integer mPath = mId%64;
            Map<String, String>[] maps = new Map[ids.length];
            if (ValueUtil.isEmpity(type)|| ValueUtil.notEmpity(type)) {
                for (int j = 0; j < ids.length; j++) {
                    ImgFile byId = this.imgFileDao.findById(Integer.valueOf(ids[j]));
                    if (ValueUtil.isEmpity(byId)) {
                        continue;
                    }
                    String fromPath = "/var/ftp/" + byId.getUrl() + "_l." + ext;
                    String toPath1 = "/var/ftp/file/img/" + module+"/" +mPath + "/" + mId + "/" + "l/" + byId.getFileName() + ".png";
                    String toPath2 = "/var/ftp/file/img/" + module+"/" +mPath + "/" + mId + "/" + "s/" + byId.getFileName() + ".png";
                    String toPath3 = "/var/ftp/file/img/" + module+"/" +mPath + "/" + mId + "/" + "m/" + byId.getFileName() + ".png";

                    this.picFileService.resizePNG_L(fromPath, toPath1, byId.getWidth(), byId.getHeight());
                    this.picFileService.resizePNG_M(fromPath, toPath3, byId.getWidth(), byId.getHeight());
                    this.picFileService.resizePNG_S(fromPath, toPath2, byId.getWidth(), byId.getHeight());


                    Map<String, String> map = new HashMap<>();
                    map.put("id", ids[j]);
                    map.put("name", byId.getFileName());
                    maps[j] = map;

                }
            } else {
                for (int j = 0; j < ids.length; j++) {
                    ImgFile byId = this.imgFileDao.findById(Integer.valueOf(ids[j]));
                    if (ValueUtil.isEmpity(byId)) {
                        continue;
                    }
                    String fromPath = "/var/ftp/" + byId.getUrl() + "_l." + ext;
                    String toPath1 = "/var/ftp/file/img/" + module + "/" + mId + "/" + "l/" + byId.getFileName() + ".png";
                    this.picFileService.resizePNG_L(fromPath, toPath1, byId.getWidth(), byId.getHeight());

                    Map<String, String> map = new HashMap<>();
                    map.put("id", ids[j]);
                    map.put("name", byId.getFileName());
                    maps[j] = map;
                }

            }
            return ValueUtil.toJson(HttpStatus.SC_CREATED, maps);
        } catch (YesmywineException e) {
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (Exception e) {
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }



    @RequestMapping(value = "/old", method = RequestMethod.POST)
    public String toFormal(String module, Integer mId, String id, Integer type){
        if(ValueUtil.isEmpity(type)) {
            try {
                ValueUtil.verify(module);
                ValueUtil.verify(id);
                ValueUtil.verify(mId);
                String[] ids = id.split(",");
                Ftp ftp = new Ftp();
//            String[] splitUrl = url.split(";");
                String resultUrl = "";
                String ext = "png";
//            PicFile picFile = new PicFile();
                Map<String, String>[] maps = new Map[ids.length];
//            String[] hs = new String[ids.length];
                for (int j = 0; j < ids.length; j++) {
                    PicFile byId = this.picFileService.findById(Integer.valueOf(ids[j]));

                    InputStream inputStream = ftp.downFile(byId.getUrl() + "_l." + ext);

                    if (ValueUtil.notEmpity(inputStream)) {


                        //获得文件hash值
                        String hash = this.getHashService.getHash(inputStream, MD5);

                        String[] laterPath = this.filePathService.getFormalFilePath(module, mId.toString(), hash + "." + ext);
                        PicFile picFileNew = new PicFile();

                        if (ValueUtil.notEmpity(hash)) {
                            PicFile[] picFile = this.picFileService.queryByFormalFileName(hash + ".png");
                            if (picFile.length > 0) {
                                String module1 = picFile[0].getModule();
                                Integer mId1 = picFile[0].getmId();
                                String[] originalPath = this.filePathService.getFormalFilePath(module1, mId1.toString(), hash + "." + ext);
                                if (ftp.copy(originalPath, laterPath)) {
                                    picFileNew.setUrl(picFile[0].getUrl());
                                    picFileNew.setFormalFileName(hash + ".png");
                                    picFileNew.setTempFileName(hash + ".png");
                                    picFileNew.setOriginName(picFile[0].getOriginName());
                                    picFileNew.setModule(module);
                                    picFileNew.setmId(mId);
                                    Object save = this.picFileService.save(picFileNew);
                                    if (ValueUtil.notEmpity(save)) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("id", ids[j]);
                                        map.put("name", hash);
                                        maps[j] = map;
//                                hs[j] = hash;
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }

                                } else {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            } else {


//                String filePath = "";

//                InputStream shrink1 = this.picFileService.shrink(inputStream1, 80, 50);
                                InputStream[] shrink = this.picFileService.shrinks(ftp.downFile(byId.getUrl() + "_l." + ext));
                                String rlt = "";
                                for (int i = 0; i < shrink.length; i++) {
                                    if (ValueUtil.notEmpity(shrink[i])) {
//                        String last = "_s.";
//                        filePath = FileUploadUrl.FILEFORMALURL + "/" + module + "/" + id + last + ext;
                                        String restult = ftp.uploadFile(laterPath[i], shrink[i]);
                                        if (i > 0) {
                                            resultUrl = resultUrl + ";" + rlt;
                                        } else {
                                            resultUrl = resultUrl + rlt;
                                        }
                                        if (restult.equals("failed")) {
                                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                        }
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }

                                }

                                picFileNew.setUrl(byId.getUrl());
                                picFileNew.setId(Integer.valueOf(ids[j]));
                                picFileNew.setFormalFileName(hash + ".png");
                                picFileNew.setTempFileName(byId.getTempFileName());
                                picFileNew.setOriginName(byId.getOriginName());
                                picFileNew.setModule(module);
                                picFileNew.setmId(mId);
                                Object save = this.picFileService.save(picFileNew);
                                if (ValueUtil.isEmpity(save)) {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            }
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ids[j]);
                            map.put("name", hash);
                            maps[j] = map;
//                    hs[j] = hash;
                        } else {
                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                        }

                    } else {
                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                    }

                }

                return ValueUtil.toJson(HttpStatus.SC_CREATED, maps);

            } catch (YesmywineException e) {
                return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Erro");
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
        }else {
            try {
                ValueUtil.verify(module);
                ValueUtil.verify(id);
                ValueUtil.verify(mId);
                String[] ids = id.split(",");
                Ftp ftp = new Ftp();
                String resultUrl = "";
                String ext = "png";
                Map<String, String>[] maps = new Map[ids.length];
                for (int j = 0; j < ids.length; j++) {
                    PicFile byId = this.picFileService.findById(Integer.valueOf(ids[j]));

                    InputStream inputStream = ftp.downFile(byId.getUrl() + "_l." + ext);

                    if (ValueUtil.notEmpity(inputStream)) {


                        //获得文件hash值
                        String hash = this.getHashService.getHash(inputStream, MD5);

                        String[] laterPath = this.filePathService.getFormalFilePathType(module, mId.toString(), hash + "." + ext);
                        PicFile picFileNew = new PicFile();

                        if (ValueUtil.notEmpity(hash)) {
                            PicFile[] picFile = this.picFileService.queryByFormalFileName(hash + ".png");
                            if (picFile.length > 0) {
                                String module1 = picFile[0].getModule();
                                Integer mId1 = picFile[0].getmId();
                                String[] originalPath = this.filePathService.getFormalFilePathType(module1, mId1.toString(), hash + "." + ext);
                                if (ftp.copy(originalPath, laterPath)) {
                                    picFileNew.setUrl(picFile[0].getUrl());
                                    picFileNew.setFormalFileName(hash + ".png");
                                    picFileNew.setTempFileName(hash + ".png");
                                    picFileNew.setOriginName(picFile[0].getOriginName());
                                    picFileNew.setModule(module);
                                    picFileNew.setmId(mId);
                                    Object save = this.picFileService.save(picFileNew);
                                    if (ValueUtil.notEmpity(save)) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("id", ids[j]);
                                        map.put("name", hash);
                                        maps[j] = map;
//                                hs[j] = hash;
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }

                                } else {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            } else {


//                String filePath = "";

//                InputStream shrink1 = this.picFileService.shrink(inputStream1, 80, 50);
                                InputStream shrink = this.picFileService.shrinksType(ftp.downFile(byId.getUrl() + "_l." + ext));
                                String rlt = "";
//                                for (int i = 0; i < shrink.length; i++) {
                                    if (ValueUtil.notEmpity(shrink)){
//                        String last = "_s.";
//                        filePath = FileUploadUrl.FILEFORMALURL + "/" + module + "/" + id + last + ext;
                                        String restult = ftp.uploadFile(laterPath[0], shrink);
//                                        if (i > 0) {
//                                            resultUrl = resultUrl + ";" + rlt;
//                                        } else {
//                                            resultUrl = resultUrl + rlt;
//                                        }
//                                        if (restult.equals("failed")) {
//                                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
//                                        }
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }



                                picFileNew.setUrl(byId.getUrl());
                                picFileNew.setId(Integer.valueOf(ids[j]));
                                picFileNew.setFormalFileName(hash + ".png");
                                picFileNew.setTempFileName(byId.getTempFileName());
                                picFileNew.setOriginName(byId.getOriginName());
                                picFileNew.setModule(module);
                                picFileNew.setmId(mId);
                                Object save = this.picFileService.save(picFileNew);
                                if (ValueUtil.isEmpity(save)) {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            }
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ids[j]);
                            map.put("name", hash);
                            maps[j] = map;
//                    hs[j] = hash;
                        } else {
                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                        }

                    } else {
                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                    }

                }

                return ValueUtil.toJson(HttpStatus.SC_CREATED, maps);

            } catch (YesmywineException e) {
                return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Erro");
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
        }
    }

    @RequestMapping(value = "/itfOld",method = RequestMethod.POST)
    public String toFormalitf(String module, Integer mId, String id, Integer type){
        if(ValueUtil.isEmpity(type)) {
            try {
                ValueUtil.verify(module);
                ValueUtil.verify(id);
                ValueUtil.verify(mId);
                String[] ids = id.split(",");
                Ftp ftp = new Ftp();
//            String[] splitUrl = url.split(";");
                String resultUrl = "";
                String ext = "png";
//            PicFile picFile = new PicFile();
                Map<String, String>[] maps = new Map[ids.length];
//            String[] hs = new String[ids.length];
                for (int j = 0; j < ids.length; j++) {
                    PicFile byId = this.picFileService.findById(Integer.valueOf(ids[j]));

                    InputStream inputStream = ftp.downFile(byId.getUrl() + "_l." + ext);

                    if (ValueUtil.notEmpity(inputStream)) {


                        //获得文件hash值
                        String hash = this.getHashService.getHash(inputStream, MD5);

                        String[] laterPath = this.filePathService.getFormalFilePath(module, mId.toString(), hash + "." + ext);
                        PicFile picFileNew = new PicFile();

                        if (ValueUtil.notEmpity(hash)) {
                            PicFile[] picFile = this.picFileService.queryByFormalFileName(hash + ".png");
                            if (picFile.length > 0) {
                                String module1 = picFile[0].getModule();
                                Integer mId1 = picFile[0].getmId();
                                String[] originalPath = this.filePathService.getFormalFilePath(module1, mId1.toString(), hash + "." + ext);
                                if (ftp.copy(originalPath, laterPath)) {
                                    picFileNew.setUrl(picFile[0].getUrl());
                                    picFileNew.setFormalFileName(hash + ".png");
                                    picFileNew.setTempFileName(hash + ".png");
                                    picFileNew.setOriginName(picFile[0].getOriginName());
                                    picFileNew.setModule(module);
                                    picFileNew.setmId(mId);
                                    Object save = this.picFileService.save(picFileNew);
                                    if (ValueUtil.notEmpity(save)) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("id", ids[j]);
                                        map.put("name", hash);
                                        maps[j] = map;
//                                hs[j] = hash;
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }

                                } else {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            } else {


//                String filePath = "";

//                InputStream shrink1 = this.picFileService.shrink(inputStream1, 80, 50);
                                InputStream[] shrink = this.picFileService.shrinks(ftp.downFile(byId.getUrl() + "_l." + ext));
                                String rlt = "";
                                for (int i = 0; i < shrink.length; i++) {
                                    if (ValueUtil.notEmpity(shrink[i])) {
//                        String last = "_s.";
//                        filePath = FileUploadUrl.FILEFORMALURL + "/" + module + "/" + id + last + ext;
                                        String restult = ftp.uploadFile(laterPath[i], shrink[i]);
                                        if (i > 0) {
                                            resultUrl = resultUrl + ";" + rlt;
                                        } else {
                                            resultUrl = resultUrl + rlt;
                                        }
                                        if (restult.equals("failed")) {
                                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                        }
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }

                                }

                                picFileNew.setUrl(byId.getUrl());
                                picFileNew.setId(Integer.valueOf(ids[j]));
                                picFileNew.setFormalFileName(hash + ".png");
                                picFileNew.setTempFileName(byId.getTempFileName());
                                picFileNew.setOriginName(byId.getOriginName());
                                picFileNew.setModule(module);
                                picFileNew.setmId(mId);
                                Object save = this.picFileService.save(picFileNew);
                                if (ValueUtil.isEmpity(save)) {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            }
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ids[j]);
                            map.put("name", hash);
                            maps[j] = map;
//                    hs[j] = hash;
                        } else {
                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                        }

                    } else {
                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                    }

                }

                return ValueUtil.toJson(HttpStatus.SC_CREATED, maps);

            } catch (YesmywineException e) {
                return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Erro");
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
        }else {
            try {
                ValueUtil.verify(module);
                ValueUtil.verify(id);
                ValueUtil.verify(mId);
                String[] ids = id.split(",");
                Ftp ftp = new Ftp();
                String resultUrl = "";
                String ext = "png";
                Map<String, String>[] maps = new Map[ids.length];
                for (int j = 0; j < ids.length; j++) {
                    PicFile byId = this.picFileService.findById(Integer.valueOf(ids[j]));

                    InputStream inputStream = ftp.downFile(byId.getUrl() + "_l." + ext);

                    if (ValueUtil.notEmpity(inputStream)) {


                        //获得文件hash值
                        String hash = this.getHashService.getHash(inputStream, MD5);

                        String[] laterPath = this.filePathService.getFormalFilePathType(module, mId.toString(), hash + "." + ext);
                        PicFile picFileNew = new PicFile();

                        if (ValueUtil.notEmpity(hash)) {
                            PicFile[] picFile = this.picFileService.queryByFormalFileName(hash + ".png");
                            if (picFile.length > 0) {
                                String module1 = picFile[0].getModule();
                                Integer mId1 = picFile[0].getmId();
                                String[] originalPath = this.filePathService.getFormalFilePathType(module1, mId1.toString(), hash + "." + ext);
                                if (ftp.copy(originalPath, laterPath)) {
                                    picFileNew.setUrl(picFile[0].getUrl());
                                    picFileNew.setFormalFileName(hash + ".png");
                                    picFileNew.setTempFileName(hash + ".png");
                                    picFileNew.setOriginName(picFile[0].getOriginName());
                                    picFileNew.setModule(module);
                                    picFileNew.setmId(mId);
                                    Object save = this.picFileService.save(picFileNew);
                                    if (ValueUtil.notEmpity(save)) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("id", ids[j]);
                                        map.put("name", hash);
                                        maps[j] = map;
//                                hs[j] = hash;
                                    } else {
                                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                    }

                                } else {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            } else {


//                String filePath = "";

//                InputStream shrink1 = this.picFileService.shrink(inputStream1, 80, 50);
                                InputStream shrink = this.picFileService.shrinksType(ftp.downFile(byId.getUrl() + "_l." + ext));
                                String rlt = "";
//                                for (int i = 0; i < shrink.length; i++) {
                                if (ValueUtil.notEmpity(shrink)){
//                        String last = "_s.";
//                        filePath = FileUploadUrl.FILEFORMALURL + "/" + module + "/" + id + last + ext;
                                    String restult = ftp.uploadFile(laterPath[0], shrink);
//                                        if (i > 0) {
//                                            resultUrl = resultUrl + ";" + rlt;
//                                        } else {
//                                            resultUrl = resultUrl + rlt;
//                                        }
//                                        if (restult.equals("failed")) {
//                                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
//                                        }
                                } else {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }



                                picFileNew.setUrl(byId.getUrl());
                                picFileNew.setId(Integer.valueOf(ids[j]));
                                picFileNew.setFormalFileName(hash + ".png");
                                picFileNew.setTempFileName(byId.getTempFileName());
                                picFileNew.setOriginName(byId.getOriginName());
                                picFileNew.setModule(module);
                                picFileNew.setmId(mId);
                                Object save = this.picFileService.save(picFileNew);
                                if (ValueUtil.isEmpity(save)) {
                                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                                }
                            }
                            Map<String, String> map = new HashMap<>();
                            map.put("id", ids[j]);
                            map.put("name", hash);
                            maps[j] = map;
//                    hs[j] = hash;
                        } else {
                            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                        }

                    } else {
                        return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                    }

                }

                return ValueUtil.toJson(HttpStatus.SC_CREATED, maps);

            } catch (YesmywineException e) {
                return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Erro");
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
        }
    }

}
