package com.yesmywine.goods.util;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileCustom {
	private static final Logger log = LoggerFactory.getLogger(FileCustom.class);

	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			System.out.println("创建目录" + destDirName + "成功！");
			return true;
		} else {
			System.out.println("创建目录" + destDirName + "失败！");
			return false;
		}
	}

	public static boolean createFile(String destFileName) {
		File file = new File(destFileName);
		if (file.exists()) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
			return false;
		}
		if (destFileName.endsWith(File.separator)) {
			System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.getParentFile().mkdirs()) {
				System.out.println("创建目标文件所在目录失败！");
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				System.out.println("创建单个文件" + destFileName + "成功！");
				return true;
			} else {
				System.out.println("创建单个文件" + destFileName + "失败！");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
			return false;
		}
	}

	/**
	 * @Title: FileCustom.java
	 * @Package com.jeecms.common.file
	 * @Description: TODO(用一句话描述该文件做什么)
	 * @author SJQ
	 * @date 2016年10月31日 下午5:20:55
	 * @version V1.0
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @param dirName
	 *            file文件 new File("D:\\temp")
	 * @return
	 */
	public static String createTempFile(String prefix, String suffix, String dirName) {
		File tempFile = null;
		if (dirName == null) {
			try {
				// 在默认文件夹下创建临时文件
				tempFile = File.createTempFile(prefix, suffix);
				// 返回临时文件的路径
				return tempFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("创建临时文件失败！" + e.getMessage());
				return null;
			}
		} else {
			File dir = new File(dirName);
			// 如果临时文件所在目录不存在，首先创建
			if (!dir.exists()) {
				if (!FileCustom.createDir(dirName)) {
					System.out.println("创建临时文件失败，不能创建临时文件所在的目录！");
					return null;
				}
			}
			try {
				// 在指定目录下创建临时文件
				tempFile = File.createTempFile(prefix, suffix, dir);
				// tempFile.deleteOnExit();
				return tempFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("创建临时文件失败！" + e.getMessage());
				return null;
			}
		}
	}

	public static boolean writeInTxtFile(String txt, String path) {
		File file = new File(path);
		try {
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
			BufferedWriter bw = new BufferedWriter(osw);

			if (txt != null) {
				Pattern p = Pattern.compile("\\s*|\t|\t|\n");
				Matcher m = p.matcher(txt);
				txt = m.replaceAll("");
			}
			bw.write(txt);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String readTxt(String desc) {
		InputStreamReader read = null;
		String txt = null;
		try {
			read = new InputStreamReader(new FileInputStream(desc));
			BufferedReader bufferedReader = new BufferedReader(read);

			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				txt += lineTxt;
			}
			read.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (txt != null) {
			Pattern p = Pattern.compile("\\s*|\t|\t|\n");
			Matcher m = p.matcher(txt);
			txt = m.replaceAll("");
		}
		return txt;
	}

//	public static void main(String[] args) {
//		FileInputStream in = null;
//		try {
//			in = new FileInputStream(new File("D:/ftptest.txt"));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			// FileCustom.ftpUploadFile("123.57.164.181", 22, "anonymous",
//			// "Passwd12345@qq.com", "dict111.txt", in);
//			FileCustom.ftpUploadFile("123.57.164.181", 22, "ftp_dict", "1", "dict111.txt", in);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	/**
	 * @Title: FileCustom.java
	 * @Package com.jeecms.common.file
	 * @Description: ftp上传文件
	 * @author SJQ
	 * @date 2016年11月4日 上午10:38:36
	 * @version V1.0
	 * @param host
	 *            主机ip
	 * @param port
	 *            端口号
	 * @param user
	 *            用户名
	 * @param password
	 *            密码
	 * @param dir
	 *            需要切换到的目录
	 * @param is
	 *            文件输入流
	 * @return
	 * @throws Exception
	 */
	public static boolean ftpUploadFile(String host, int port, String user, String password, String dir,
			InputStream is) {
		FTPClient ftp = new FTPClient();
		Boolean result = null;
		try {

			ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			 ftp.setDefaultPort(21);
			ftp.connect(host);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				log.warn("FTP server refused connection: {}", host);
				ftp.disconnect();
				return false;
			}
			if (!ftp.login(user, password)) {
				log.warn("FTP server refused login: {}, user: {}", host, user);
				ftp.logout();
				ftp.disconnect();
				return false;
			}
			ftp.setControlEncoding("UTF-8");
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();
			ftp.changeWorkingDirectory(dir);
//			ftp.changeWorkingDirectory("/home");
			result = ftp.storeFile("dict.txt", is);
			if (result) {
				log.info(new Date() + "uplod dict.txt success");
			} else {
				log.info(new Date() + "uplod dict.txt faild");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;
	}

	// 文件下载
	public static void ftpDownFile(String host, int port, String user, String password, String remoteFilename,
			OutputStream os) throws Exception {
		FTPClient ftpclient = new FTPClient();

		try {
			// 设置服务器名和端口
			ftpclient.connect(host, port);
			int reply = ftpclient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				// 连接错误
				Exception ee = new Exception("Can't Connect to :" + host);
				throw ee;
			}

			// 登录
			if (ftpclient.login(user, password) == false) {
				// invalid user/password
				Exception ee = new Exception("Invalid user/password");
				throw ee;
			}

			// 设置传送模式
			ftpclient.setFileType(FTP.BINARY_FILE_TYPE);

			// 取得文件
			ftpclient.retrieveFile(remoteFilename, os);

		} catch (IOException e) {
			throw e;
		} finally {
			try {
				ftpclient.disconnect(); // 解除连接
			} catch (IOException e) {
			}
		}
	}
}
