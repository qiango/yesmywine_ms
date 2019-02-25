package com.yesmywine.util.properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.*;

/**
 * 方案：<br>
 * 1.只加载一次,以后使用缓存<br>
 * 2.动态加载,对于修改的加载,没修改的使用缓存<br>
 * 3.每次都加载,比较浪费资源<br>
 * 
 * @author louxueming  2016-11-22
 * @version 1.0 
 *
 */
@SuppressWarnings("unused")
public class PropertiesUtil {
	
	  protected static final Log logger = LogFactory.getLog(PropertiesUtil.class);
	
	  //private static Properties properties;
	  private static Map<String,PropertiesExt> propsExtMap = new HashMap<String, PropertiesExt>();
	  
	  
	  private static final String  CHARSET_ISO_8859_1 = "ISO-8859-1";
	  private static final String  CHARSET_UTF_8 = "UTF-8";
      
	  /**
	   * 第二种加载方式,但是采用第二种加载方式时不能动态加载
	   * @param p_location
	   * @return
	   */
	  private static InputStream getTwoResourceAsStream(String p_location) throws IOException{
		  
		  try {
			   InputStream in= ResourceUtil.getResourceAsStream(p_location);
				if(in != null){
					 //禁止动态加载,切换至只加载一次模式
					 //setLoadType(p_location,PropertiesExt.OnlyoneLoad);
					 
				 }
				return in;
		    } catch (Exception e) {
		    	throw new IOException("failed loading Properties resource from " + p_location);
		    }
		 
	  }
	  /**
	   * 装载资源到出入流
	   * @param p_location 文件路径
	   * @return
	   * @see #getResourceFile(String)
	   * @since 1.0
	   */
	private static InputStream getResourceAsStream(String p_location){
	    InputStream in = null;
	    File file = null;
	    try {
	    	 
			   //第一种全面考虑的方法
  		       file = ResourceUtil.getResourceFile(p_location);
		       if(file != null){
		    	  in = new FileInputStream(file);
		       }
		     //FIXME:当第一种方法不能加载时采用第二种加载方式，但是采用第二种加载方式不能动态判断文件是否修改
		     //第二种方法
		       if(in == null){
		    	  in = getTwoResourceAsStream(p_location);
		       }
			} catch (Exception e) {
				e.printStackTrace();
	     }			
	    return in;
	  }

	  
	  /**
	   * 文件路径加载方式
	   * @param aFilePath
	   * @return
	   * @throws IOException
	   */
	  public static Properties loadPropertiesFile(String aFilePath)throws IOException {
		  InputStream in = null;
		  try {
				  Properties properties = new Properties();
				  File file = ResourceUtil.getResourceFile(aFilePath);
				  in = new FileInputStream(file);
				  properties.load(in);
				  return properties;
			  
			  } catch (Exception e) {
				  logger.warn(">>>>>>>>>>>>>>>>>>默认加载方式失败[即第一种加载方式].后续将采用第二种方式加载!");
				  throw new IOException(e.getMessage());
			  }finally{
				  if(in != null){
					  in.close();
				  }
			  }
	  }
	  
	  /**
	   * classpath加载方式
	   * @param aResourcePath
	   * @return
	   * @throws IOException
	   */
	  public static Properties loadPropertiesResource(String aResourcePath)throws IOException {
		  InputStream in  = getTwoResourceAsStream(aResourcePath);
		  try {
				  Properties properties = new Properties();
				  properties.load(in);
				  return properties;
			  
			  } catch (Exception e) {
				  throw new IOException(e.getMessage());
			  }finally{
				  if(in != null){
					  in.close();
				  }
			  }
	  }

      /**
       * 装载配置文件至Properties<br>
       * <b>NOTE：此方法每调用一次需要重新加载一下(比较耗时)</b>
       * @param location 文件路径
       * @return
       * @see #getResourceAsStream(String)
       * @see #getProperties(String) 
       */
	  public static Properties loadProperties(String location)
	  {
		  	if(location == null){
		  		throw  new NullPointerException("Properties File Path must not is null.");
		  	}
		    if(logger.isDebugEnabled()){
		    	logger.debug(new StringBuilder("Properties File Path: ").append(location));
		    }
		    Properties properties = null;
		    try{
		    	//文件路径加载
		    	properties = loadPropertiesFile(location);
		    	//XXX:设置加载方法
		    	setLoadMethod(location, PropertiesExt.LOAD_FOR_FILE);
		       } catch (IOException e) {
		    	   try {
		    		   logger.warn("cannot load " + location + " from filePath, will try from classpath");
		    		   //尝试classpath加载
		    		   properties = loadPropertiesResource(location);
		    		   //XXX:设置加载方法
		    		   setLoadMethod(location, PropertiesExt.LOAD_FOR_RESOURCE);
					   } catch (Exception e2) {
						   logger.error("cannot load properties file from " + location,e);
						   throw new RuntimeException("cannot load properties file from : " + location);
					   }
		    }
	    return properties;
	  }

	  /**
	   * 此方法启用缓存<br>
	   * 如果Properties已经装载则获取已经装载的Properties,否则重新装载
	   * @param location
	   * @return
	   * @see #getProperties(String, int)
	   */
	  public static Properties getProperties(String location)
	  {
	    return getProperties(location, PropertiesExt.DynamicLoad);
	  }
	  
	  /**
	   * 此方法可以采用加载方式<br>
	   * @param location
	   * @param loadType {@link PropertiesExt#DynamicLoad}
	   * @return
	   * @see PropertiesExt
	   * @see #initializePropertiesX(String, int)
	   */
	  public static Properties getProperties(String location,int loadType)
	  {
		//如果文件没有加载 或者 已经修改 则重新装载
		
		 switch (loadType) {
			case PropertiesExt.OnlyoneLoad:
				 if(!isLoad(location)){
					 initializePropertiesX(location, loadType);
				   }
				
				break;
			case PropertiesExt.DynamicLoad:
			    if(!isLoad(location) || isModified(location)){
			    	 initializePropertiesX(location, loadType);
				    
			    }
				break;
	
			default:
				break;
		}
		 
		Properties props = propsExtMap.get(location).getProperties();
	    return props;
	  }
	  
	  /**
	   * Properties文件初始化和文件被修改时调用此方法
	   * @param location
	   * @param loadType
	   * @see #loadProperties(String)
	   */
	  private static void initializePropertiesX (String location,int loadType){
		     PropertiesExt propsExt = propsExtMap.get(location);
		     if(propsExt == null){
		    	 propsExt = new PropertiesExt(loadType);
		    	 propsExtMap.put(location, propsExt);
		     }
		    
		     Properties properties = loadProperties(location);
		     propsExt.setProperties(properties);
		     propsExt.setHasLoad(true);
		  
	  }
	  /**
	   * 通过key获取属性的值
	   * @param key
	   * @param location
	   * @return
	   */
	  public static String getString(String key, String location)
	  {
		  
	    Properties  properties = getProperties(location);
	    String propertyValue = properties.getProperty(key);
	    
	    return propertyValue;
	  }

	  /**
	   * 模糊匹配相似的Key 返回匹配到List列表值
	   * @param key
	   * @param location
	   * @return
	   */
	  public static List<String> getList(String key, String location)
	  {
		  
	    Properties  properties = getProperties(location);
	    
	    List<String> list = new ArrayList<String>();
	    if ((key == null) || (key.equals("")))
	      return list;
	    Enumeration<?> propertyNames = properties.propertyNames();
	    while (propertyNames.hasMoreElements()) {
	      String propertyName = (String) propertyNames.nextElement();
	      if ((propertyName != null) && (propertyName.indexOf(key) >= 0))
	      {
	        String propertyValue = properties.getProperty(propertyName);
	        //转码
	       /* try {
	          propertyValue = new String(propertyValue.getBytes(CHARSET_ISO_8859_1),CHARSET_UTF_8);
	        }
	        catch (UnsupportedEncodingException e) {
	          throw new RuntimeException("the UTF-8 Encoding properties File Error!", e);
	        }*/
	        list.add(propertyValue);
	      }
	    }
	    return list;
	  }
	  
	  /*************************************** private area ************************************************/
	  /**
	   * 判断文件是否已经加载
	   * @param location 文件绝对路径 或者 URl文件路径
	   * @return
	   */
	  private static boolean isLoad(String location)
	  {
		 PropertiesExt propsExt =  propsExtMap.get(location);
		  
		  if(propsExt == null){
			 return false;
		  }
		
	    return propsExt.isHasLoad();
	  }
	  private static boolean isDynamicLoad(String location)
	  {
		  PropertiesExt propsExt =  propsExtMap.get(location);
		  //默认动态加载
		  if(propsExt == null){
			  return true;
		  }
		  
		  return propsExt.getLoadType()==PropertiesExt.DynamicLoad ? true:false;
	  }
	  private static void setLoadType(String location,int loadType)
	  {
		  PropertiesExt propsExt =  propsExtMap.get(location);
		  if(propsExt != null){
			  propsExt.setLoadType(loadType);
		  }
	  }
	  
	  
	  private static void setLoadMethod(String location,String loadMethod){
		  PropertiesExt propsExt =  propsExtMap.get(location);
		  if(propsExt != null){
			  propsExt.setLoadMethod(loadMethod);
		  }
	  }
	  private static String getLoadMethod(String location){
		  PropertiesExt propsExt =  propsExtMap.get(location);
		  if(propsExt == null){
			  return null;
		  }
		  return propsExt.getLoadMethod();
	  }
	  
	  
	  private static long getLastModif(String location)
	  {
		PropertiesExt propsExt =  propsExtMap.get(location);
		if(propsExt == null){
			return 0L;
		}
		
	    return propsExt.getLastModifTime();
	  }
	  private static void setLastModif(String location,long lastModif)
	  {
		PropertiesExt propsExt =  propsExtMap.get(location);
		if(propsExt != null){
			propsExt.setLastModifTime(lastModif);
		}
	  }
	  /**
	   * 判断文件是否已经修改
	   * @param location 文件绝对路径 或者 URl文件路径
	   * @return
	   */
	  private static boolean isModified(String location)
	  {
		  if(location ==null){
            throw new NullPointerException("Properties File Path not be null.");			  
		  }
		  try {
			  if(!isDynamicLoad(location)){
				  return false;
			  }
			  long newTime = 0 ;
			  //XXX：load for file
			  String loadMethod = getLoadMethod(location);
			  if(PropertiesExt.LOAD_FOR_FILE.equals(loadMethod)){
				  File file = ResourceUtil.getResourceFile(location);
				  if(file == null){
					  return false;
				  }
				  boolean fileExists  = file.exists();
				  if(fileExists){
					  newTime = file.lastModified();
				  }
				  
			  }else{
				 //XXX：load for resource 
				 URL url = ResourceUtil.getResourceURL(location);
				 URLConnection conn= url.openConnection();
				 newTime  = conn.getLastModified();
				  
			  }
			
    			long lastModif = getLastModif(location);
    			if (newTime > lastModif) {
    				lastModif = newTime;
    				setLastModif(location, lastModif);
    				if(logger.isInfoEnabled()){
    					logger.info(new StringBuilder(location).append("[file already Modified:").append(new Timestamp(lastModif)).append("]"));	
    				}
    				return true;
    			}
    		  
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	    return false;
	  }
	}

/**将Properties 加入一些状态
 * @author louxueming  2014-12-12
 */
class PropertiesExt{
	
	public final static int OnlyoneLoad = 1;
	public final static int DynamicLoad = 2;
	
	public final static String LOAD_FOR_FILE = "LoadForFile";
	public final static String LOAD_FOR_RESOURCE = "LoadForResource";
	
	private boolean hasLoad; 
	/**
	 * 动态加载/静态加载
	 */
	private int loadType; 
	
	/**
	 * 加载方法
	 */
	private String loadMethod;
	/**
	 * 最后修改时间
	 */
	private long lastModifTime = 0L;
	private Properties properties;
	
	public PropertiesExt(int loadType) {
		this.loadType = loadType;
	}
	
	public boolean isHasLoad() {
		return hasLoad;
	}
	public void setHasLoad(boolean hasLoad) {
		this.hasLoad = hasLoad;
	}
	public int getLoadType() {
		return loadType;
	}
	public void setLoadType(int loadType) {
		this.loadType = loadType;
	}
	public String getLoadMethod() {
		return loadMethod;
	}

	public void setLoadMethod(String loadMethod) {
		this.loadMethod = loadMethod;
	}

	public long getLastModifTime() {
		return lastModifTime;
	}
	public void setLastModifTime(long lastModifTime) {
		this.lastModifTime = lastModifTime;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	} 
}

