package com.tedu.base.common.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.XML;
/**
 * Session工具类
 * @author xijianguang
 *
 */
public class ContextUtils {
	

	/**
	 * 获取当前用户Session
	 * 
	 */
	public static ServletContext getServletContext() {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		if(webApplicationContext==null)
			return null;
		else 
			return webApplicationContext.getServletContext();
	}
	/**
	 * 获取Session中key属性Session
	 *  @author xijianguang
	 */
	public static Object getAttrbute(String key) {
		if(StringUtils.isEmpty(key))
			return null;
		if(getServletContext()==null)
			return null;
		return getServletContext().getAttribute(key);
	}
	/**
	 * 获取Session中key属性Session返回string
	 *  @author xijianguang
	 */
	public static String getAttrbuteStr(String key) {
		if(getAttrbute(key)==null)
			return null;
		return getAttrbute(key).toString();
	}
	/**
	 * 添加Session
	 * @author xijianguang
	 */
	public static void setAttrbute(String key, Object val) {
		if(StringUtils.isEmpty(key))
			return ;
		getServletContext().setAttribute(key, val);
	}
	/**
	 * 移除key属性Session
	 * @author xijianguang
	 */
	public static void removeAttrbute(String key) {
		if(StringUtils.isEmpty(key))
			return ;
		getServletContext().removeAttribute(key);
	}
	/**
	 * 移除所有Session
	 * 
	 */
	public static void removeAll() {
		SecurityUtils.getSubject().logout();
	}
	
	
	/**
	 * 取得xml
	 * @author xijianguang
	 */
	public static XML getXML() {
		ServletContext servletContext = getServletContext();
		if(servletContext!=null && servletContext.getAttribute(ConstantUtil.XML)!=null)
			return (XML)servletContext.getAttribute(ConstantUtil.XML);
		else{
			FormLogger.logConf("无法获取 服务中的中的配置", "");
			return null;
		}
	}
	
	/**
	 * 取url context之后的部分
	 * @param request
	 * @return
	 */
    public static String getSourceUrl(HttpServletRequest request){
    	String url = request.getRequestURI();
    	return url.substring(request.getContextPath().length());
    }

    /**
     * 判断request请求的地址是否用于显示表单引擎的ui
     * @param request
     * @return
     */
    public static boolean isUIRequest(HttpServletRequest request){
    	 String url = ContextUtils.getSourceUrl(request);
    	 return url.startsWith(FormConstants.XML_UI_MAPPING);
    }
    
    /**
     * 分享给外部的链接
     * @param request
     * @return
     */
    public static boolean isPublicRequest(HttpServletRequest request){
   	 String url = ContextUtils.getSourceUrl(request);
   	 return url.indexOf(FormConstants.XML_PUBLIC_MAPPING)>=0;
   }
    
    public static boolean isPublicRequest(String url){
      	 return url.startsWith(FormConstants.XML_PUBLIC_MAPPING);
	}
    
    public static boolean isPublicRequest(SavedRequest savedRequest,HttpServletRequest request){
    	String url = savedRequest.getRequestURI();
    	String sourceUrl = url.substring(request.getContextPath().length());
    	return sourceUrl.startsWith(FormConstants.XML_PUBLIC_MAPPING);
	}
    
    /**
     * 缓存对外分享链接
     * @param request
     */
    public static void saveShareRequest(HttpServletRequest request){
	    if(isPublicRequest(request)){
			WebUtils.saveRequest(request);
			FormLogger.logFlow(String.format("当前是一个分享给外部的url，缓存{%s}，稍后在主框架tab内打开",request.getRequestURI()), 
					FormLogger.LOG_TYPE_INFO);					
		}
    }
    
    public static void saveRedirectRequest(HttpServletRequest request,boolean isRedirectable){
	    if(isRedirectable){
			WebUtils.saveRequest(request);
			FormLogger.logFlow(String.format("当前重定向的url，登录后应定向至",request.getRequestURI()), 
					FormLogger.LOG_TYPE_INFO);					
		}
    }
    
    
}
