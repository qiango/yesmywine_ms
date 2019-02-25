package com.yesmywine.goods.controller;

import com.yesmywine.goods.dao.ImgFileDao;
import com.yesmywine.goods.entity.FileUploadUrl;
import com.yesmywine.goods.entity.ImgFile;
import com.yesmywine.goods.entity.PicFile;
import com.yesmywine.goods.entity.Upload;
import com.yesmywine.goods.service.FilePathService;
import com.yesmywine.goods.service.GetHashService;
import com.yesmywine.goods.service.Impl.PicFileServiceImpl;
import com.yesmywine.goods.service.PicFileService;
import com.yesmywine.goods.util.Ftp;
import com.yesmywine.util.basic.ValueUtil;
import com.yesmywine.util.error.YesmywineException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

/**
 * Created by WANG, RUIQING on 12/19/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
@RestController
@RequestMapping("/fileUpload/picUpload/test")
public class TestController {

    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[] { ".bmp", ".jpg", ".jpeg", ".gif", ".png" ,"svg"};

    @Autowired
    private PicFileServiceImpl picFileService;
    @Autowired
    private GetHashService getHashService;
    @Autowired
    private FilePathService filePathService;
    @Autowired
    private ImgFileDao imgFileDao;

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



    @RequestMapping(value = "/toFile", method = RequestMethod.POST)
    public String saveTo(String module, Integer mId, String id, Integer type) throws IOException {

        try{
            ValueUtil.verify(module);
            ValueUtil.verify(id);
            ValueUtil.verify(mId);
            String[] ids = id.split(",");
            String ext = "png";
            Map<String, String>[] maps = new Map[ids.length];
            if(ValueUtil.isEmpity(type)) {
                for (int j = 0; j < ids.length; j++) {
                    ImgFile byId = this.imgFileDao.findById(Integer.valueOf(ids[j]));
                    if (ValueUtil.isEmpity(byId)) {
                        continue;
                    }
                    String fromPath = "/var/ftp/" +byId.getUrl() + "_l." + ext;
                    String toPath1 = "/var/ftp/file/img/" +module+"/"+mId+"/"+"l/"+byId.getFileName()+".png";
                    String toPath2 = "/var/ftp/file/img/" +module+"/"+mId+"/"+"s/"+byId.getFileName()+".png";
                    String toPath3 = "/var/ftp/file/img/" +module+"/"+mId+"/"+"m/"+byId.getFileName()+".png";

                    this.picFileService.resizePNG_L(fromPath, toPath1,byId.getWidth(),byId.getHeight());
                    this.picFileService.resizePNG_M(fromPath, toPath2,byId.getWidth(),byId.getHeight());
                    this.picFileService.resizePNG_S(fromPath, toPath3,byId.getWidth(),byId.getHeight());


                    Map<String, String> map = new HashMap<>();
                    map.put("id", ids[j]);
                    map.put("name", byId.getFileName());
                    maps[j] = map;

                }
            }else {
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
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "erro");
        }catch (Exception e){
            return ValueUtil.toError(HttpStatus.SC_INTERNAL_SERVER_ERROR, "erro");
        }

//        return "Unable to upload. File is empty.";

    }






    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String addCircle(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam("uploadFiles")MultipartFile uploadFiles) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");


        String savePath = request.getSession().getServletContext().getRealPath(
                "");//保存的服务器地址

        savePath = "/image/";

        File file = new File(savePath);
        //判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            System.out.println(savePath+"目录不存在，需要创建");
            //创建目录
            file.mkdir();
        }

        String origName = uploadFiles.getOriginalFilename();

        InputStream in = uploadFiles.getInputStream();

//        InputStream in = item.getInputStream();
        //创建一个文件输出流
        FileOutputStream out = new FileOutputStream(savePath  + origName);
        //创建一个缓冲区
        byte buffer[] = new byte[1024];
        //判断输入流中的数据是否已经读完的标识
        int len = 0;
        //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
        while((len=in.read(buffer))>0){
            //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
            out.write(buffer, 0, len);
        }
        //关闭输入流
        in.close();
        //关闭输出流
        out.close();




        //消息提示
        String message = "";
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据
                return null;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
//            for(FileItem item : list){
//                //如果fileitem中封装的是普通输入项的数据
//                if(item.isFormField()){
//                    String name = item.getFieldName();
//                    //解决普通输入项的数据的中文乱码问题
//                    String value = item.getString("UTF-8");
//                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
//                    System.out.println(name + "=" + value);
//                }else{//如果fileitem中封装的是上传文件
//                    //得到上传的文件名称，
//                    String filename = item.getName();
//                    System.out.println(filename);
//                    if(filename==null || filename.trim().equals("")){
//                        continue;
//                    }
//                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
//                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
//                    filename = filename.substring(filename.lastIndexOf("\\")+1);
//                    //获取item中的上传文件的输入流
//                    InputStream in = item.getInputStream();
//                    //创建一个文件输出流
//                    FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
//                    //创建一个缓冲区
//                    byte buffer[] = new byte[1024];
//                    //判断输入流中的数据是否已经读完的标识
//                    int len = 0;
//                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
//                    while((len=in.read(buffer))>0){
//                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
//                        out.write(buffer, 0, len);
//                    }
//                    //关闭输入流
//                    in.close();
//                    //关闭输出流
//                    out.close();
//                    //删除处理文件上传时生成的临时文件
//                    item.delete();
//                    message = "文件上传成功！";
//                }
//            }
        }catch (Exception e) {
            message= "文件上传失败！";
            e.printStackTrace();

        }
        return message;
    }


}
