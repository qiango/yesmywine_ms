package com.yesmywine.goods.webController;

import com.yesmywine.goods.dao.ImgFileDao;
import com.yesmywine.goods.entity.ImgFile;
import com.yesmywine.goods.entity.PicFile;
import com.yesmywine.goods.service.FilePathService;
import com.yesmywine.goods.service.GetHashService;
import com.yesmywine.goods.service.Impl.PicFileServiceImpl;
import com.yesmywine.goods.service.PicFileService;
import com.yesmywine.goods.util.Ftp;
import com.yesmywine.util.basic.ValueUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;


/**
 * Created by light on 2017/4/5.
 */
@RestController
@RequestMapping("/web/fileUpload/picUpload")
public class WebPicUploadController {
    @Autowired
    private PicFileServiceImpl picFileService;
    @Autowired
    private FilePathService filePathService;
    @Autowired
    private GetHashService getHashService;
    @Autowired
    private ImgFileDao imgFileDao;



    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" ,"svg"};

    @RequestMapping(value = "/old",method = RequestMethod.POST)
    public String upload(@RequestParam("uploadFiles")MultipartFile uploadFiles)
            throws Exception {


        String origName = uploadFiles.getOriginalFilename();
        Map<String ,String> map = new HashMap<>();
        map.put("name", origName);

        // 校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(origName, type)) {
                isLegal = true;
                break;
            }
        }
        if(!isLegal){
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件格式不正确");
        }

        InputStream inputStream = uploadFiles.getInputStream();
        String hash = this.getHashService.getHash(inputStream, MD5);
        PicFile[] picFile = this.picFileService.queryByTempFileName(hash+".png");
        if(picFile.length > 0){
            map.put("id", picFile[0].getId().toString());
            map.put("url", picFile[0].getUrl());
            return ValueUtil.toJson(HttpStatus.SC_CREATED,map);
        }else {
            String ext = "png";
            Ftp ftp = new Ftp();
            String last = "_l";
            String filePath = this.filePathService.getFilePath();
            filePath = filePath+"/"+hash+last+"."+ext;
            String restult = ftp.uploadFile(filePath, uploadFiles.getInputStream());
            String rlt = StringUtils.substringBefore(restult, "_");
            if(restult.equals("failed")){
                return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
            }
//            JSONObject obj = new JSONObject();
//            obj.put("url",rlt);
//            obj.put("url",picFile.getUrl());


            InputStream shrink = this.picFileService.shrink(uploadFiles.getInputStream());
            if(ValueUtil.notEmpity(shrink)){
                filePath = StringUtils.substringBefore(filePath, "_") + "_s."+ ext;
                String s = ftp.uploadFile(filePath, shrink);
                if(s.equals("failed")){
                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
                }
            }

            PicFile picFile1 = new PicFile();
            picFile1.setTempFileName(hash+".png");
            picFile1.setUrl(rlt);
            picFile1.setOriginName(origName);
            PicFile save = this.picFileService.save(picFile1);
            if(ValueUtil.notEmpity(save)){
                map.put("id", save.getId().toString());
                map.put("url", picFile1.getUrl());
                return ValueUtil.toJson(HttpStatus.SC_CREATED, map);
            }

            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件服务器出现问题，文件上传失败");
        }

    }



    @RequestMapping(method = RequestMethod.POST)
    public String save(@RequestParam("uploadFiles")MultipartFile uploadFiles) throws IOException {


        String origName = null;
        if (!uploadFiles.isEmpty()) {
            try {
                origName = uploadFiles.getOriginalFilename();

                Map<String ,String> map = new HashMap<>();
                map.put("name", origName);

                // 校验图片格式
                boolean isLegal = false;
                for (String type : IMAGE_TYPE) {
                    if (StringUtils.endsWithIgnoreCase(origName, type)) {
                        isLegal = true;
                        break;
                    }
                }
                if(!isLegal){
                    return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "文件格式不正确");
                }

                InputStream inputStream = uploadFiles.getInputStream();
                BufferedImage Bi = ImageIO.read(uploadFiles.getInputStream());
                int height = Bi.getHeight();
                int width = Bi.getWidth();
                String hash = null;
                try {
                    hash = this.getHashService.getHash(inputStream, MD5);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ImgFile[] picFile = this.imgFileDao.findByFileName(hash);
                if(picFile.length > 0){
                    map.put("id", picFile[0].getId().toString());
                    map.put("url", picFile[0].getUrl());
                    return ValueUtil.toJson(HttpStatus.SC_CREATED,map);
                }else {
                    String ext = "png";
                    String last = "_l";
                    String returnPath =this.filePathService.getFilePath();
                    String filePath = "/var/ftp"+returnPath;

                    byte[] bytes = uploadFiles.getBytes();

                    File file = new File(filePath);
//                File file = new File("E:\\imagess\\");
                    if (!file.exists()) {
                        try {
//                        file.createNewFile();
                            file.mkdirs();
                        } catch (Exception e) {
                            return e.toString();
                        }
                    }
                    String fromFile = filePath + "/" + hash + last + "." + ext;
                    String toFile = filePath + "/" + hash + "_s" + "." + ext;
                    file = new File(fromFile);

                    BufferedOutputStream buffStream =
                            new BufferedOutputStream(new FileOutputStream(file));
                    buffStream.write(bytes);
                    buffStream.close();

                    this.picFileService.resizePNG(fromFile, toFile,100,100,true);

                    ImgFile imgFile = new ImgFile();
                    imgFile.setOriginName(origName);
                    imgFile.setFileName(hash);
                    imgFile.setUrl(returnPath + "/" + hash);
                    imgFile.setHeight(height);
                    imgFile.setWidth(width);
                    this.imgFileDao.save(imgFile);
                    map.put("id", imgFile.getId().toString());
                    map.put("url", imgFile.getUrl());
                    return ValueUtil.toJson(HttpStatus.SC_CREATED,map);
                }
            } catch (Exception e) {
                return "You failed to upload " + origName + ": " + e.getMessage();
            }
        } else {
            return "Unable to upload. File is empty.";
        }


    }
}
