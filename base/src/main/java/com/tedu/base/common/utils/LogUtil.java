package com.tedu.base.common.utils;

import org.apache.log4j.Logger;
/**
 * 统一记录系统日志
 * @author wangdanfeng
 *
 */
public class LogUtil {
	public static final Logger log = Logger.getLogger(LogUtil.class);
	public static final String TYPE_ENGINE = "表单引擎";
	public static final String TYPE_BASE = "基础框架";
	public static final String ACTION_BEGIN = "...";
	public static final String ACTION_END = "结束";
	public static final String ACTION_EXCEPTION = "异常";
	
	public static void info(String type,String action,String actionContent,String objectType){
		log.info(String.format("%s:%s %s %s", type,action,actionContent,objectType));
	}
	
	public static void info(String s){
		log.info(s);
	}
	
	public static void warn(String actionContent){
		log.warn(actionContent);
	}
	
	public static void debug(String actionContent){
		log.debug(actionContent);
	}
	
	public static void error(String actionContent,Throwable...t){
		log.error(actionContent);
	}
	
	public static void error(String type,String action,String actionContent,String objectType){
		log.error(String.format("%s [%s][%s]%s", type,action,actionContent,objectType));
	}	
	
	public static void debug(String type,String action,String actionContent,String objectType){
		log.debug(String.format("%s:%s:%s:%s", type,action,actionContent,objectType));
	}	
}
