package com.yesmywine.goods.service.Impl;

import com.yesmywine.goods.dao.PicFileDao;
import com.yesmywine.goods.entity.PicFile;
import com.yesmywine.goods.service.PicFileService;
import com.yesmywine.httpclient.bean.HttpBean;
import com.yesmywine.httpclient.bean.RequestMethod;
import com.yesmywine.util.basic.Dictionary;
import com.yesmywine.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wangdiandian on 2017/3/15.
 */
@Service
public class PicFileServiceImpl implements PicFileService {

    @Autowired
    private PicFileDao picFileDao;

    @Override
    public PicFile findById(Integer id){
        return this.picFileDao.findOne(id);
    }

    @Override
    public PicFile save(PicFile picFile) {
        this.picFileDao.save(picFile);
        return picFile;
    }

    @Override
    public PicFile[] queryByTempFileName(String tempFileName) {
        return this.picFileDao.findByTempFileName(tempFileName);
    }

    @Override
    public PicFile[] queryByFormalFileName(String formalFileName) {
        return this.picFileDao.findByFormalFileName(formalFileName);
    }

    @Override
    public InputStream shrink(InputStream file) throws IOException {
        BufferedImage Bi = ImageIO.read(file);
        Image Itemp = Bi.getScaledInstance (50,50,Bi.SCALE_SMOOTH);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(Itemp), "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }

    @Override
    public InputStream shrink(InputStream file, Integer height, Integer width) throws IOException {
        BufferedImage Bi = ImageIO.read(file);
        Image Itemp = Bi.getScaledInstance (height,width,Bi.SCALE_SMOOTH);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(Itemp), "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }

    @Override
    public InputStream[] shrinks(InputStream file) throws IOException {
        BufferedImage Bi1 = ImageIO.read(file);
        int height1 = Bi1.getHeight();
        int width1 = Bi1.getWidth();
        Integer proportion = null;
        HttpBean httpBean = new HttpBean(Dictionary.DIC_HOST+"/dic/sysCode/itf", RequestMethod.get);
        httpBean.addParameter("sysCode", "img");
        httpBean.run();
        String temp = httpBean.getResponseContent();
        String data = ValueUtil.getFromJson(temp, "data");
        com.alibaba.fastjson.JSONArray jsonArray1 = com.alibaba.fastjson.JSONArray.parseArray(data);
        for(int j=0;j<jsonArray1.size(); j++) {
            com.alibaba.fastjson.JSONObject jsonObject = jsonArray1.getJSONObject(j);
            if (jsonObject.get("entityCode").equals("size")) {
                if(height1>Integer.valueOf(jsonObject.get("entityValue").toString())){
//                    double integer = Integer.valueOf(jsonObject.get("entityValue").toString());
//                    double height2 = height1;
//                    double entityValue = integer / height2;
                    double entityValue = (double)Integer.valueOf(jsonObject.get("entityValue").toString()) / height1;
                    width1 = (int)(width1 * entityValue);
                    height1 = Integer.valueOf(jsonObject.get("entityValue").toString());
                }
            }
            if(jsonObject.get("entityCode").equals("proportion")){
                proportion = Integer.valueOf(jsonObject.get("entityValue").toString());
            }
        }


        BufferedImage Bi2 = Bi1;
        BufferedImage Bi3 = Bi1;

        Image Itemp1 = Bi1.getScaledInstance (width1/proportion,height1/proportion,Bi1.SCALE_SMOOTH);
        ByteArrayOutputStream os1 = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(Itemp1), "png", os1);
        InputStream is1 = new ByteArrayInputStream(os1.toByteArray());

        Image Itemp2 = Bi2.getScaledInstance ((width1/proportion)/proportion,(height1/proportion)/proportion,Bi2.SCALE_SMOOTH);
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(Itemp2), "png", os2);
        InputStream is2 = new ByteArrayInputStream(os2.toByteArray());

        Image Itemp3 = Bi3.getScaledInstance (width1,height1,Bi3.SCALE_SMOOTH);
        ByteArrayOutputStream os3 = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(Itemp3), "png", os3);
        InputStream is3 = new ByteArrayInputStream(os3.toByteArray());

        InputStream[] inputStreams = new InputStream[3];
        inputStreams[0] = is1;
        inputStreams[1] = is2;
        inputStreams[2] = is3;
        return inputStreams;
    }



    public void resizePNG_L(String fromFile, String toFile, int outputWidth, int outputHeight) {
        try {
            File f2 = new File(fromFile);

            BufferedImage bi2 = ImageIO.read(f2);
            int newWidth;
            int newHeight;

            Double proportion = 0.1;
            HttpBean httpBean = new HttpBean(Dictionary.DIC_HOST+"/dic/sysCode/itf", RequestMethod.get);
            httpBean.addParameter("sysCode", "img");
            httpBean.run();
            String temp = httpBean.getResponseContent();
            String data = ValueUtil.getFromJson(temp, "data");
            com.alibaba.fastjson.JSONArray jsonArray1 = com.alibaba.fastjson.JSONArray.parseArray(data);
            for(int j=0;j<jsonArray1.size(); j++) {
                com.alibaba.fastjson.JSONObject jsonObject = jsonArray1.getJSONObject(j);
                if (jsonObject.get("entityCode").equals("size")) {
                    if(outputHeight>Integer.valueOf(jsonObject.get("entityValue").toString())){
                        double entityValue = (double)Integer.valueOf(jsonObject.get("entityValue").toString()) / outputHeight;
                        outputWidth = (int)(outputWidth * entityValue);
                        outputHeight = Integer.valueOf(jsonObject.get("entityValue").toString());
                    }
                }
                if(jsonObject.get("entityCode").equals("proportion1")){
                    proportion = Double.valueOf(jsonObject.get("entityValue").toString());
                }
            }

            newHeight = outputHeight;
            newWidth = outputWidth;

            BufferedImage to = new BufferedImage(newWidth, newHeight,

                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = to.createGraphics();

            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,

                    Transparency.TRANSLUCENT);

            g2d.dispose();

            g2d = to.createGraphics();

            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            File file = new File(toFile);

            if (!file.exists()) {
                file.mkdirs();
            }

            ImageIO.write(to, "png", file);

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }


    public void resizePNG_M(String fromFile, String toFile, int outputWidth, int outputHeight) {
        try {
            File f2 = new File(fromFile);

            BufferedImage bi2 = ImageIO.read(f2);
            int newWidth;
            int newHeight;

            Double proportion1 = 1.0;
//            Double proportion2 = 1.0;
            HttpBean httpBean = new HttpBean(Dictionary.DIC_HOST+"/dic/sysCode/itf", RequestMethod.get);
            httpBean.addParameter("sysCode", "img");
            httpBean.run();
            String temp = httpBean.getResponseContent();
            String data = ValueUtil.getFromJson(temp, "data");
            com.alibaba.fastjson.JSONArray jsonArray1 = com.alibaba.fastjson.JSONArray.parseArray(data);
            for(int j=0;j<jsonArray1.size(); j++) {
                com.alibaba.fastjson.JSONObject jsonObject = jsonArray1.getJSONObject(j);
                if (jsonObject.get("entityCode").equals("size")) {
                    if(outputHeight>Integer.valueOf(jsonObject.get("entityValue").toString())){
                        double entityValue = (double)Integer.valueOf(jsonObject.get("entityValue").toString()) / outputHeight;
                        outputWidth = (int)(outputWidth * entityValue);
                        outputHeight = Integer.valueOf(jsonObject.get("entityValue").toString());
                    }
                }
                if(jsonObject.get("entityCode").equals("proportion1")){
                    proportion1 = Double.valueOf(jsonObject.get("entityValue").toString());
                }
            }

            newHeight = (int)(outputHeight / proportion1);
            newWidth = (int)(outputWidth / proportion1);

            BufferedImage to = new BufferedImage(newWidth, newHeight,

                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = to.createGraphics();

            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,

                    Transparency.TRANSLUCENT);

            g2d.dispose();

            g2d = to.createGraphics();

            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            File file = new File(toFile);

            if (!file.exists()) {
                file.mkdirs();
            }

            ImageIO.write(to, "png", file);

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }


    public void resizePNG_S(String fromFile, String toFile, int outputWidth, int outputHeight) {
        try {
            File f2 = new File(fromFile);

            BufferedImage bi2 = ImageIO.read(f2);
            int newWidth;
            int newHeight;

            Double proportion1 = 1.0;
            Double proportion2 = 1.0;
            HttpBean httpBean = new HttpBean(Dictionary.DIC_HOST+"/dic/sysCode/itf", RequestMethod.get);
            httpBean.addParameter("sysCode", "img");
            httpBean.run();
            String temp = httpBean.getResponseContent();
            String data = ValueUtil.getFromJson(temp, "data");
            com.alibaba.fastjson.JSONArray jsonArray1 = com.alibaba.fastjson.JSONArray.parseArray(data);
            for(int j=0;j<jsonArray1.size(); j++) {
                com.alibaba.fastjson.JSONObject jsonObject = jsonArray1.getJSONObject(j);
                if (jsonObject.get("entityCode").equals("size")) {
                    if(outputHeight>Integer.valueOf(jsonObject.get("entityValue").toString())){
                        double entityValue = (double)Integer.valueOf(jsonObject.get("entityValue").toString()) / outputHeight;
                        outputWidth = (int)(outputWidth * entityValue);
                        outputHeight = Integer.valueOf(jsonObject.get("entityValue").toString());
                    }
                }
                if(jsonObject.get("entityCode").equals("proportion1")){
                    proportion1 = Double.valueOf(jsonObject.get("entityValue").toString());
                }
                if(jsonObject.get("entityCode").equals("proportion2")){
                    proportion2 = Double.valueOf(jsonObject.get("entityValue").toString());
                }
            }

            newHeight = (int) ((outputHeight / proportion1)/proportion2);
            newWidth = (int) ((outputWidth / proportion1)/proportion2);

            BufferedImage to = new BufferedImage(newWidth, newHeight,

                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = to.createGraphics();

            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,

                    Transparency.TRANSLUCENT);

            g2d.dispose();

            g2d = to.createGraphics();

            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            File file = new File(toFile);

            if (!file.exists()) {
                file.mkdirs();
            }

            ImageIO.write(to, "png", file);

        } catch (IOException e) {

            System.out.println(e.getMessage());

        }

    }




    public void resizePNG(String fromFile, String toFile, int outputWidth, int outputHeight,boolean proportion) {
        try {
            File f2 = new File(fromFile);

            BufferedImage bi2 = ImageIO.read(f2);
            int newWidth;
            int newHeight;
            // 判断是否是等比缩放
            if (proportion == true) {
                // 为等比缩放计算输出的图片宽度及高度
                double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
                double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
                // 根据缩放比率大的进行缩放控制
                double rate = rate1 < rate2 ? rate1 : rate2;
                newWidth = (int) (((double) bi2.getWidth(null)) / rate);
                newHeight = (int) (((double) bi2.getHeight(null)) / rate);
            } else {
                newWidth = outputWidth; // 输出的图片宽度
                newHeight = outputHeight; // 输出的图片高度
            }
            BufferedImage to = new BufferedImage(newWidth, newHeight,

                    BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = to.createGraphics();

            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,

                    Transparency.TRANSLUCENT);

            g2d.dispose();

            g2d = to.createGraphics();

            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();

            File file = new File(toFile);

            if (!file.exists()) {
                file.mkdirs();
            }

            ImageIO.write(to, "png", file);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public File inputstreamtofile(InputStream ins,File file) throws IOException {
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
        return file;
    }


    public InputStream download(String urlString) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

//        // 1K的数据缓冲
//        byte[] bs = new byte[1024];
//        // 读取到的数据长度
//        int len;
//        // 输出的文件流
//        File sf=new File(savePath);
//        if(!sf.exists()){
//            sf.mkdirs();
//        }
//        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
//        // 开始读取
//        while ((len = is.read(bs)) != -1) {
//            os.write(bs, 0, len);
//        }
//        // 完毕，关闭所有链接
//        os.close();
        return is;
    }


    @Override
    public InputStream shrinksType(InputStream file) throws IOException {
        BufferedImage Bi1 = ImageIO.read(file);
        int height1 = Bi1.getHeight();
        int width1 = Bi1.getWidth();


        Image Itemp1 = Bi1.getScaledInstance (width1,height1,Bi1.SCALE_SMOOTH);
        ByteArrayOutputStream os1 = new ByteArrayOutputStream();
        ImageIO.write(this.toBufferedImage(Itemp1), "png", os1);
        InputStream is1 = new ByteArrayInputStream(os1.toByteArray());
        return is1;
    }

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        //boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
           /* if (hasAlpha) {
             transparency = Transparency.BITMASK;
             }*/

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                    image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
            /*if (hasAlpha) {
             type = BufferedImage.TYPE_INT_ARGB;
             }*/
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }
}
