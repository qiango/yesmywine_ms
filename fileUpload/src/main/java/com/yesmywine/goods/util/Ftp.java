package com.yesmywine.goods.util;

import java.io.*;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ftp extends BaseFtp {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(Ftp.class);

	public String storeByExt(String path, String ext, InputStream in) throws IOException {
		String filename = UploadUtils.generateFilename(path, ext);
		store(filename, in);
		return filename;
	}

	public void storeByExt(String path, InputStream in) throws IOException {
		store(path, in);
	}

	public String storeByFilename(String filename, InputStream in) throws IOException {
		store(filename, in);
		return filename;
	}

	public File retrieve(String name, String fileName) throws IOException {
		String path = System.getProperty("java.io.tmpdir");
		File file = new File(path, fileName);
		file = UploadUtils.getUniqueFile(file);
		FTPClient ftp = getClient();
		OutputStream output = new FileOutputStream(file);
		ftp.retrieveFile(FtpInfo.FTP_BASEDIR + name, output);
		output.close();
		ftp.logout();
		ftp.disconnect();
		return file;
	}

	public Boolean copy(String[] originalPath, String[] laterPath) {
		try {
			for (int i = 0; i < originalPath.length; i++) {
				FTPClient ftp = getClient();

				if (ftp != null) {
					String filename = FtpInfo.FTP_PATH + laterPath[i];
					String name = FilenameUtils.getName(filename);
					String path = FilenameUtils.getFullPath(filename);
					if (!ftp.changeWorkingDirectory(path)) {
						String[] ps = StringUtils.split(path, '/');
						String p = "/";
						ftp.changeWorkingDirectory(p);
						for (String s : ps) {
							p += s + "/";
							if (!ftp.changeWorkingDirectory(p)) {
								ftp.makeDirectory(s);
								ftp.changeWorkingDirectory(p);
								System.out.println("文件夹  " + s + "创建成功");
							} else {
								System.out.println("文件夹  " + s + "创建失败");
							}
						}
					}

					ftp.setBufferSize(1024);
					ByteArrayOutputStream fos = new ByteArrayOutputStream();
					ftp.retrieveFile(FtpInfo.FTP_PATH + originalPath[i], fos);
					ByteArrayInputStream in = new ByteArrayInputStream(fos.toByteArray());
					ftp.storeFile(FtpInfo.FTP_PATH + laterPath[i], in);
					fos.close();
					in.close();
				}
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

		public boolean restore(String name, File file) throws IOException {
		store(name, FileUtils.openInputStream(file));
		file.deleteOnExit();
		return true;
	}

	public int storeByFloder(String folder, String rootPath) {
		String fileAbsolutePath;
		String fileRelativePath;
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				List<File> files = MyFileUtils.getFiles(new File(folder));
				for (File file : files) {
					String filename = getPath() + file.getName();
					String name = FilenameUtils.getName(filename);
					String path = FilenameUtils.getFullPath(filename);
					fileAbsolutePath = file.getAbsolutePath();
					fileRelativePath = fileAbsolutePath.substring(rootPath.length() + 1,
							fileAbsolutePath.indexOf(name));
					path += fileRelativePath.replace("\\", "/");
					if (!ftp.changeWorkingDirectory(path)) {
						String[] ps = StringUtils.split(path, '/');
						String p = "/";
						ftp.changeWorkingDirectory(p);
						for (String s : ps) {
							p += s + "/";
							if (!ftp.changeWorkingDirectory(p)) {
								ftp.makeDirectory(s);
								ftp.changeWorkingDirectory(p);
							}
						}
					}
					// 解决中文乱码问题
					name = new String(name.getBytes(getEncoding()), "iso-8859-1");
					if (!file.isFile()) {
						ftp.makeDirectory(name);
					} else {
						InputStream in = new FileInputStream(file.getAbsolutePath());
						ftp.storeFile(name, in);
						in.close();
					}
				}
				ftp.logout();
				ftp.disconnect();
			}
			return 0;
		} catch (SocketException e) {
			log.error("ftp store error", e);
			return 3;
		} catch (IOException e) {
			log.error("ftp store error", e);
			return 4;
		}
	}

	private int store(String remote, InputStream in) {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				String filename = FtpInfo.FTP_BASEDIR + remote;
				String name = FilenameUtils.getName(filename);
				String path = FilenameUtils.getFullPath(filename);
				if (!ftp.changeWorkingDirectory(path)) {
					String[] ps = StringUtils.split(path, '/');
					String p = "/";
					ftp.changeWorkingDirectory(p);
					for (String s : ps) {
						p += s + "/";
						if (!ftp.changeWorkingDirectory(p)) {
							ftp.makeDirectory(s);
							ftp.changeWorkingDirectory(p);
						}
					}
				}
				ftp.storeFile(name, in);
				ftp.logout();
				ftp.disconnect();
			}
			in.close();
			return 0;
		} catch (SocketException e) {
			log.error("ftp store error", e);
			return 3;
		} catch (IOException e) {
			log.error("ftp store error", e);
			return 4;
		}
	}

	private FTPClient getClient() throws SocketException, IOException {
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		// ftp.setDefaultPort(getPort());
		ftp.connect(FtpInfo.FTP_HOST);
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.warn("FTP server refused connection: {}", FtpInfo.FTP_HOST);
			ftp.disconnect();
			return null;
		}
		if (!ftp.login(FtpInfo.FTP_USERNAME, FtpInfo.FTP_PASSWORD)) {
			log.warn("FTP server refused login: {}, user: {}", FtpInfo.FTP_HOST, FtpInfo.FTP_USERNAME);
			ftp.logout();
			ftp.disconnect();
			return null;
		}
		ftp.setControlEncoding("utf-8");
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		return ftp;
	}

	/* [CONSTRUCTOR MARKER BEGIN] */
	public Ftp() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public Ftp(Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public Ftp(Integer id, String name, String ip, Integer port,
			String encoding, String url) {

		super(id, name, ip, port, encoding, url);
	}

	public String uploadFile(String filePath, InputStream is) {
		FTPClient ftpClient = new FTPClient();
		Boolean result = null;
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
//		String remote = UploadUtils.generateFilename(filePath, ext, last);
		String remote = filePath;
		// ftp.setDefaultPort(getPort());
		try {
			ftpClient.connect(FtpInfo.FTP_HOST);

			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				log.warn("FTP server refused connection: {}", FtpInfo.FTP_HOST);
				ftpClient.disconnect();
				return null;
			}
			if (!ftpClient.login(FtpInfo.FTP_USERNAME, FtpInfo.FTP_PASSWORD)) {
				System.out.println("登录失败");
				log.warn("FTP server refused login: {}, user: {}", FtpInfo.FTP_HOST, FtpInfo.FTP_USERNAME);
				ftpClient.logout();
				ftpClient.disconnect();
				return null;
			}
			System.out.println("文件服务器登录成功");
			ftpClient.setControlEncoding("utf-8");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();

			if (ftpClient != null) {
				String filename = FtpInfo.FTP_PATH + remote;
				String name = FilenameUtils.getName(filename);
				String path = FilenameUtils.getFullPath(filename);
				if (!ftpClient.changeWorkingDirectory(path)) {
					String[] ps = StringUtils.split(path, '/');
					String p = "/";
					ftpClient.changeWorkingDirectory(p);
					for (String s : ps) {
						p += s + "/";
						if (!ftpClient.changeWorkingDirectory(p)) {
							ftpClient.makeDirectory(s);
							ftpClient.changeWorkingDirectory(p);
							System.out.println("文件夹  " + s + "创建成功");
						}else{
							System.out.println("文件夹  " + s + "创建失败");
						}
					}
				}
				result = ftpClient.storeFile(name, is);
				System.out.println("文件上传结果：" + result);
				ftpClient.logout();
				ftpClient.disconnect();
			}
			is.close();
			System.out.println("上传后文件地址为：" + remote);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result){
			return remote;
		}else{
			return "failed";
		}
	}

	public static byte[] downFile(String filePath, Ftp ftp) throws SocketException, IOException {
		// TODO Auto-generated method stub
		String fileName = FilenameUtils.getName(filePath);
		String ftpPath = FilenameUtils.getPath(filePath);
		int ftpPathStart = ftpPath.indexOf("knowledge");
		if (ftpPathStart < 0) {
			ftpPath = FtpInfo.FTP_BASEDIR+"/" + ftpPath;
		} else {
			ftpPath = "/"+ftpPath.substring(ftpPath.indexOf("knowledge"), ftpPath.length());
		}
	
		InputStream in = null;
		byte[] b = null;
		FTPClient ftpClient = new FTPClient();
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		// ftp.setDefaultPort(getPort());
		ftpClient.connect(FtpInfo.FTP_HOST);
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.warn("FTP server refused connection: {}", FtpInfo.FTP_HOST);
			ftpClient.disconnect();
			return null;
		}
		if (!ftpClient.login(FtpInfo.FTP_USERNAME, FtpInfo.FTP_PASSWORD)) {
			log.warn("FTP server refused login: {}, user: {}", FtpInfo.FTP_HOST, FtpInfo.FTP_USERNAME);
			ftpClient.logout();
			ftpClient.disconnect();
			return null;
		}
		ftpClient.setControlEncoding(FtpInfo.FTP_ENCODING);
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.enterLocalPassiveMode();
		try {
			ftpClient.setBufferSize(1024);
			ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes("UTF-8"), "iso-8859-1"));
			in = ftpClient.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
			if (in == null) {
				return null;
			}
			b = ftp.inputStreamToByte(in);
		} catch (Exception e) {
			log.error("downFile is err", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				ftpClient.logout();
				ftpClient.disconnect();
			} catch (IOException e) {
			}
		}
		return b;
	}

	public static InputStream downFile(String filePath) throws SocketException, IOException {
		// TODO Auto-generated method stub
		String fileName = FilenameUtils.getName(filePath);
		String ftpPath = FilenameUtils.getPath(filePath);
		int ftpPathStart = ftpPath.indexOf("knowledge");
		if (ftpPathStart < 0) {
//			ftpPath = FtpInfo.FTP_BASEDIR+"/" + ftpPath;
			ftpPath = FtpInfo.FTP_PATH+"/" + ftpPath;
		} else {
			ftpPath = "/"+ftpPath.substring(ftpPath.indexOf("knowledge"), ftpPath.length());
		}

		InputStream in = null;
		byte[] b = null;
		FTPClient ftpClient = new FTPClient();
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		// ftp.setDefaultPort(getPort());
		ftpClient.connect(FtpInfo.FTP_HOST);
		int reply = ftpClient.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.warn("FTP server refused connection: {}", FtpInfo.FTP_HOST);
			ftpClient.disconnect();
			return null;
		}
		if (!ftpClient.login(FtpInfo.FTP_USERNAME, FtpInfo.FTP_PASSWORD)) {
			log.warn("FTP server refused login: {}, user: {}", FtpInfo.FTP_HOST, FtpInfo.FTP_USERNAME);
			ftpClient.logout();
			ftpClient.disconnect();
			return null;
		}
		ftpClient.setControlEncoding(FtpInfo.FTP_ENCODING);
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.enterLocalPassiveMode();
		try {
			ftpClient.setBufferSize(1024);
			ftpClient.changeWorkingDirectory(new String(ftpPath.getBytes("UTF-8"), "iso-8859-1"));
			in = ftpClient.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
			if (in == null) {
				return null;
			}
			return in;
		} catch (Exception e) {
			log.error("downFile is err", e);
		}
		return null;
	}

	private byte[] inputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int ch;
		/**
		 * 
		 * */
		while ((ch = is.read(buffer)) != -1) {
			bytestream.write(buffer, 0, ch);
		}
		byte data[] = bytestream.toByteArray();
		bytestream.close();
		return data;
	}
	/* [CONSTRUCTOR MARKER END] */

}