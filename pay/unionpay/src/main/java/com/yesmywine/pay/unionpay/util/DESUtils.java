package com.yesmywine.pay.unionpay.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.*;
import java.security.SecureRandom;

public class DESUtils {
    /* 加密算法,可用 DES DESede Blowfish */
    private final static String ALGORITHM = "DES";

    /**
     * 获取key
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String getKey(String path) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sb.append(tmp + "\n");
        }
        br.close();
        return sb.toString().replaceAll("\\n$", "");
    }

    /**
     * 使用默认路径获取key
     *
     * @return
     * @throws Exception
     */
    public static String getKey() throws Exception {
        return getKey(DESUtils.class.getResource("/key.txt").toURI().getPath());
    }

    /**
     * 使用默认路径获取key
     *
     * @return
     * @throws Exception
     */
    public static byte[] getKey2() throws Exception {

        FileInputStream fis = new FileInputStream(DESUtils.class.getResource("/key.txt").toURI().getPath());
        int len = fis.available();
        byte[] xml = new byte[len];

        fis.read(xml);
        System.out.println(xml.length);
        for (int i = 0; i < xml.length; i++) {
            System.out.println(xml[i]);
        }
        fis.close();
        System.out.println(xml);

        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DESUtils.class.getResource("/key.txt").toURI().getPath()), "UTF-8"));
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
            sb.append(tmp + "\n");
        }
        br.close();
        System.out.println(sb.toString().getBytes("utf-8"));


        return sb.toString().getBytes("utf-8");


    }

    /**
     * 对数据进行DES加密.
     *
     * @param data 待进行DES加密的数据
     * @param key  密钥
     * @return 返回经过DES加密后的数据
     * @throws Exception
     */

    // 字符串解密
    public final static String decode(String data, String key) throws Exception {
        return new String(decrypt(hex2byte(data.getBytes("utf-8")),
                key.getBytes("utf-8")));
    }

    /**
     * 默认解密字符串
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static String decode(String data) throws Exception {
        return decode(data, getKey());
    }

    // 对文件解密
    public static File decode(File file) throws Exception {
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            if (fis != null) {
                int len = fis.available();
                byte[] xml = new byte[len];
                fis.read(xml);
                FileOutputStream fos = new FileOutputStream("D:/projects/php/php-mars/ws/1/bin/test.zip.de");
                fos.write(decrypt(hex2byte(xml), getKey().getBytes("UTF-8")));

                //fos.write(decrypt(new String(hex2byte(xml),"utf-8").getBytes("utf-8"),getKey().getBytes("UTF-8")));
                //fos.write(decrypt(hex2byte(new String(xml,"utf-8").getBytes("utf-8")),getKey().getBytes("UTF-8")));
                fis.close();
                fos.close();
            }
        }
        return file;
    }

    /**
     * 对用DES加密过的数据进行解密.
     *
     * @param data DES加密数据
     * @param key  密钥
     * @return 返回解密后的数据
     * @throws Exception
     */

    // 对字符串加密
    public final static String encode(byte[] data, String key) throws Exception {
        return byte2hex(encrypt(data, key.getBytes("utf-8")));
    }

    //public final static String encode(String data) throws Exception {
    //	return encode(data, getKey());
    //}

    // 对文件加密
    public static File encode(File file) throws Exception {
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            if (fis != null) {
                int len = fis.available();
                byte[] xml = new byte[len];
                fis.read(xml);
                String data = new String(xml, "UTF-8");
                FileOutputStream fos = new FileOutputStream("D:/projects/php/php-mars/ws/1/bin/test.zip.en");
                fos.write(encode(xml, getKey()).getBytes("UTF-8"));
                fis.close();
                fos.close();
            }
        }
        return file;
    }

    /**
     * 用指定的key对数据进行DES加密.
     *
     * @param data 待加密的数据
     * @param key  DES加密的key
     * @return 返回DES加密后的数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(data);
    }

    /** */
    /**
     * 用指定的key对数据进行DES解密.
     *
     * @param data 待解密的数据
     * @param key  DES解密的key
     * @return 返回DES解密后的数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        byte[] s = cipher.doFinal(data);
        return s;
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public static String byte2hex(byte[] b) {
        int len = b.length;
        StringBuffer sb = new StringBuffer(len);
        String stmp = "";
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                sb = sb.append("0");
            }
            sb = sb.append(stmp);
        }
        return sb.toString();
    }

    // 调用加密、解密方法
//    public static void main(String args[]) throws Exception {
//        //DESUtils.encode(new File("D:/projects/php/php-mars/ws/1/bin/test.zip"));// 加密文件
//        System.out.println();
//        DESUtils.decode(new File("D:/projects/php/php-mars/ws/1/bin/test.zip.n1"));//解密文件
//
//
//        //System.out.println(decode("81199e30f148020e83be069058652f72", "knap1019"));
//    }
}
