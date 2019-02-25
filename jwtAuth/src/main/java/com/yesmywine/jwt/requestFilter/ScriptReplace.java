package com.yesmywine.jwt.requestFilter;

import java.util.regex.Pattern;

/**
 * XSS过滤参数规则
 * @author admin
 *
 */
public class ScriptReplace {
	
	public static String ScriptReplaceStr(String oldStr){
		if(null==oldStr){
			return null;
		}
		String newStr = "";
		newStr = oldStr.replace("", "");
		
		newStr = newStr.replace("(", "").replace(")", "").replace("+", "");
		
		newStr = newStr.replace("%3cscript", "").replace("%3c/script", "").replace("%3c", "");
		newStr = newStr.replace("%3CSCRIPT", "").replace("%3C/SCRIPT", "").replace("%3C", "");
		newStr = newStr.replace("%3Cscript", "").replace("%3C/script", "").replace("%3E", "");
		newStr = newStr.replace("%3cSCRIPT", "").replace("%3c/SCRIPT", "").replace("%3e", "");
		
		newStr = newStr.replace("%3cinput", "").replace("%3c/input", "").replace("%3c", "");
		newStr = newStr.replace("%3CINPUT", "").replace("%3C/INPUT", "").replace("%3C", "");
		newStr = newStr.replace("%3Cinput", "").replace("%3C/input", "").replace("%3E", "");
		newStr = newStr.replace("%3cINPUT", "").replace("%3c/INPUT", "").replace("%3e", "");
		
		newStr = newStr.replace("%3ciframe", "").replace("%3c/iframe", "").replace("%3c", "");
		newStr = newStr.replace("%3CIFRAME", "").replace("%3C/IFRAME", "").replace("%3C", "");
		newStr = newStr.replace("%3Ciframe", "").replace("%3C/iframe", "").replace("%3E", "");
		newStr = newStr.replace("%3cIFRAME", "").replace("%3c/IFRAME", "").replace("%3e", "");
		
		newStr = newStr.replace("alert(", "").replace("ALERT(", "").replace("alert", "").replace("ALERT", "");
		newStr = newStr.replace("<script", "").replace("<SCRIPT", "").replace("javascript:", "").replace("JAVASCRIPT:", "");
		newStr = newStr.replace("script", "").replace("SCRIPT", "").replace("</script>", "").replace("</SCRIPT>", "");
		newStr = newStr.replace("[window", "").replace("[WINDOW", "");
		newStr = newStr.replace("window", "").replace("WINDOW", "").replace("location", "").replace("LOCATION", "");
		newStr = newStr.replace("<link", "").replace("<LINK", "");
		newStr = newStr.replace("%3c/script%3e", "").replace("%3C/script%3e", "").replace("%3c/script%3E", "").replace("%3C/script%3E", "");
		
		
	
		
		if((
        		(newStr.toLowerCase().indexOf("select") >= 0 && newStr.toLowerCase().indexOf("from")>= 0) //select语句
        		|| (newStr.toLowerCase().indexOf("delete") >=0 && newStr.toLowerCase().indexOf("from") >= 0)//delete语句
        	    || (newStr.toLowerCase().indexOf("update") >= 0 && newStr.toLowerCase().indexOf("set") >= 0)//update语句
        		|| (newStr.toLowerCase().indexOf("drop") >= 0) || (newStr.toLowerCase().indexOf("truncate") >= 0)//drop语句
        		|| (newStr.toLowerCase().indexOf("insert") >= 0 || newStr.toLowerCase().indexOf("into") >= 0)//insert语句
        		|| (newStr.toLowerCase().trim().indexOf("in(") >= 0) )//in函数
        	)//&& newStr.toLowerCase().indexOf("?") >= 0 && newStr.toLowerCase().indexOf("=") >= 0//地址栏接sql语句的 ? 和 = 
		  {
			    newStr = newStr.replace("select", "").replace("from", "");
				newStr = newStr.replace("SELECT", "").replace("FROM", "");
				newStr = newStr.replace("delete", "").replace("from", "");
				newStr = newStr.replace("DELETE", "").replace("FROM", "");
				newStr = newStr.replace("update", "").replace("set", "");
				newStr = newStr.replace("UPDATE", "").replace("SET", "");
				newStr = newStr.replace("drop", "").replace("truncate", "");
				newStr = newStr.replace("DROP", "").replace("TRUNCATE", "");
				newStr = newStr.replace("insert", "").replace("into", "");
				newStr = newStr.replace("INSERT", "").replace("INTO", "");
				newStr = newStr.replace("?", "").replace("=", "");
        	}
		
		return newStr;
	}
	
	public static String xssClean(String value) {
		if (value == null) {
			return value;
		}
		value = value.replaceAll("", "");
		
		 //标准脚本和HTML<script>alert(999)</script>
		Pattern scriptPattern = Pattern.compile("<(.*?)>(.*)</(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        
   	  	//简化脚本和HTML<script type="" src=""/>
   		scriptPattern = Pattern.compile("<(.*?)/>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
         value = scriptPattern.matcher(value).replaceAll("");
         
        //</script>
        scriptPattern = Pattern.compile("</(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
        
        //不规范的脚本<script>
        scriptPattern = Pattern.compile("<(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPattern.matcher(value).replaceAll("");
		
        Pattern scriptPatterns = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPatterns.matcher(value).replaceAll("");
        
        scriptPatterns = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPatterns.matcher(value).replaceAll("");
        scriptPatterns = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
        value = scriptPatterns.matcher(value).replaceAll("");

        scriptPatterns = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        value = scriptPatterns.matcher(value).replaceAll("");
		return value;
	}
	
	public static void main(String[] args) {
		
	}

}
