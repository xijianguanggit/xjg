package com.tedu.base.file.util.io;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.SystemUtils;

public class NetPath {
	/**
	 * 
	 * @Description: 获取服务器硬盘路径
	 * @author: gaolu
	 * @date: 2017年8月15日 下午12:12:24  
	 * @param: @return      
	 * @return: String类型，返回路径的完整字符串     
	 * @throws
	 */
	public static String getServerPath(){
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		rootPath = rootPath.replace("%20", " ");
		rootPath = rootPath.substring(rootPath.indexOf("/")+1,rootPath.lastIndexOf("/"));
		rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
		rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
		rootPath = rootPath+"\\";
		
		//判断操作系统类型
		if(SystemUtils.IS_OS_WINDOWS){
			rootPath = rootPath.replace("/", "\\");
		}else if(SystemUtils.IS_OS_LINUX){
			rootPath = rootPath.replace("\\", "/");
			//判断linux系统是否以/开头
			if(!rootPath.substring(0, 1).equals("/")){
				rootPath = "/"+rootPath;
			}
		}
		
		return rootPath;
	}
	/**
	 * 
	 * @Description: 获取服务器硬盘路径
	 * @author: gaolu
	 * @date: 2017年8月16日 下午4:43:08  
	 * @param: @param request
	 * @return: String
	 */
	public static String getServerPath(HttpServletRequest request){
		String rootPath = request.getSession().getServletContext().getRealPath("/")+"\\";
		
		//判断操作系统类型
		if("windows".equals(BasicFile.WINDOWS_SYSTEM)){
			rootPath = rootPath.replace("/", "\\");
		}else if("linux".equals(BasicFile.LINUX_SYSTEM)){
			rootPath = rootPath.replace("\\", "/");
		}
		
		return rootPath;
	}
	
}
