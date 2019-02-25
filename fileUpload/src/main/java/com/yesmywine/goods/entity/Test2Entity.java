//package com.yesmywine.goods.entity;
//
//import com.yesmywine.base.record.bean.VerifyType;
//import com.yesmywine.base.record.entity.BaseEntity;
//import org.apache.struts2.ServletActionContext;
//
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import java.io.*;
//import java.util.Date;
//import java.util.UUID;
//
///**
// * Created by WANG, RUIQING on 12/19/16
// * Twitter : @taylorwang789
// * E-mail : i@wrqzn.com
// */
//
//public class Test2Entity {
//
//    private File uploadFile;//上传文件
//    private String uploadFileFileName;//文件名
//    public File getUploadFile() {
//        return uploadFile;
//    }
//
//
//    public void setUploadFile(File uploadFile) {
//        this.uploadFile = uploadFile;
//    }
//    public String getUploadFileFileName() {
//        return uploadFileFileName;
//    }
//
//
//    public void setUploadFileFileName(String uploadFileFileName) {
//        this.uploadFileFileName = uploadFileFileName;
//    }
//    public String uploadImg() throws IOException {
//
//
//        String uuid = UUID.randomUUID().toString().replace("-", "");//重命名文件的文件体
//        InputStream is=new FileInputStream(uploadFile);//将<input>标签里面的图片文件写入流文件InpuStream
////        String uploadPath= ServletActionContext.getServletContext().getRealPath("/photos/");
////        File toFile=new File(uploadPath,this.getUploadFileFileName());//目标文件，由文件位置和文件名（请求文件的文件名）组成
//        /**
//         * 这段代码用于重命名文件，以免文件被覆盖
//         */
//        int pot=toFile.getName().lastIndexOf(".");
//        String ext="";
//        if(pot!=-1){
//            ext=toFile.getName().substring(pot);
//        }else{
//            ext="";
//        }
//        String newName=uuid+ext;
//        toFile=new File(toFile.getParent(),newName); //重命名文件完成
//        /**
//         * 将客户端的二进制数据流写入到服务器本地
//         */
//        OutputStream os=new FileOutputStream(toFile);
//        byte[] buffer=new byte[1024];//缓冲空间大小 单位为KB
//        int length=0;
//        while((length=is.read(buffer))>0){
//            os.write(buffer,0, length);
//        }
//        is.close();
//        os.close();  //文件写入本地完成
//
////        String webRoot=req.getSession().getServletContext().getRealPath("/");//获取文件在服务器项目文件夹的绝对路径
////        String basePath="/photos/"+toFile.getName();//文件的相对路径
////        String url=webRoot+basePath;//文件的完整路径，而接下来我们只需要将这个完整路径存入数据库即可
////        PrintWriter out=resp.getWriter();
////        out.print(urL);
////        out.print("||success");
//
//        return null;
//
//    }
//
//}
