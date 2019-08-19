package com.tedu.base.engine.util;

import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.common.utils.SessionUtils;

/**
 * 表单引擎日志
 * @author wangdanfeng
 *
 */
public class FormLogger {
	private static final String ACTION_VALIDATE = "校验";
	public static final String LOG_TYPE_INFO = "info";
	public static final String LOG_TYPE_WARN = "warn";
	public static final String LOG_TYPE_DEBUG = "debug";
	public static final String LOG_TYPE_ERROR = "error";
	private static final String ACTION_TOKEN = "权限";
	private static final String ACTION_JDBC = "执行";
	private static final String ACTION_BUILD_SQL = "构建";
	private static final String PATTERN = "[%s] %s => %s";

	public static void info(String content){
		LogUtil.info(content);
	}
	public static void warn(String actionContent){
		LogUtil.warn(actionContent);
	}
	public static void error(String action,String actionContent,String objectType){
		LogUtil.error(LogUtil.TYPE_ENGINE,action,actionContent,objectType);
	}
	
	public static void error(String s,Throwable...t){
		LogUtil.error(s,t);
	}
	
	public static void debug(String action){
		LogUtil.debug(LogUtil.TYPE_ENGINE,action,"","");
	}
	
	public static void logValidate(String actionContent,String objectType){
		LogUtil.info(LogUtil.TYPE_ENGINE,ACTION_VALIDATE,actionContent,objectType);
	}
	
	//DEBUG
	public static void logToken(String actionContent,String objectType){
		LogUtil.debug(LogUtil.TYPE_ENGINE,ACTION_TOKEN,actionContent,objectType);
	}
	
	public static void logJDBC(String actionContent,String objectType){
		LogUtil.debug(LogUtil.TYPE_ENGINE,ACTION_JDBC,actionContent,"\n"+objectType);//参数和SQL分开,方便阅读
	}
	
	public static final String TYPE_MAJOR = "主要";
	public static final String TYPE_BLOCKER = "阻塞";
	public static final String TYPE_MINOR = "轻微";
	
	
	//大量调用,暂时保留,转换为UI类 warn log
	public static void logConf(String actionContent,String objectType){
		logUi(actionContent + "{" + objectType + "}",LOG_TYPE_WARN);
	}

	public static void logToken(String content){
		log("ACL", content, LOG_TYPE_INFO);
	}
	public static void logInit(String content, String type){
		log("Init", content,type);
	}
	
	public static void logUi(String content, String type){
		log("UI", content,type);
	}
	public static void logControl(String content, String type){
		log("Control", content,type);
	}
	public static void logPanel(String content){
		log("Panel", content,LOG_TYPE_INFO);
	}
	public static void logModule(String content, String type){
		log("Module", content,type);
	}
	
	private static String getSessionId(){
		return SessionUtils.getSession()==null?"system":SessionUtils.getSession().getId().toString();
	}
	
	/**
	 * 统一格式
	 * @param categoryName
	 * @param content
	 * @param type
	 */
	private static void log(String categoryName,String content, String type,Throwable...t){
		if(type.equals(LOG_TYPE_WARN)){
			LogUtil.warn(String.format(PATTERN,categoryName, getSessionId(),content));
		}else if(type.equals(LOG_TYPE_DEBUG)){
			LogUtil.debug(String.format(PATTERN, categoryName,getSessionId(),content));
		}else if(type.equals(LOG_TYPE_ERROR)){
			LogUtil.error(String.format(PATTERN, categoryName,getSessionId(),content),t);
		}else{
			LogUtil.info(String.format(PATTERN, categoryName,getSessionId(),content));
		}
	}
	
	public static void logFlow(String content, String type,Throwable...t){
		log("Flow", content,type,t);
	}

	public static void logFlow(String content, String type){
		log("Flow", content,type);
	}
	public static void logSchedule(String content, String type,Throwable...t){
		if(FormLogger.LOG_TYPE_DEBUG.equals(type))
			log("Schedule", content,FormLogger.LOG_TYPE_DEBUG);
		else if(FormLogger.LOG_TYPE_INFO.equals(type))
			log("Schedule", content,FormLogger.LOG_TYPE_INFO);
		else if(FormLogger.LOG_TYPE_ERROR.equals(type))
			log("Schedule", content,FormLogger.LOG_TYPE_ERROR);
	}
	
	private static final String PATTERN_MSG = "[MSG] %s => %s";
	public static void logMSG(String content, String type){
		log("MSG", content,type);
	}
	public static void logProcedure(String content, String type){
		log("Procedure", content,type);
	}
	public static void logCheckXSD(String content, String type){
		log("CheckXSD", content,type);
	}
	public static void logBegin(String actionContent){
		LogUtil.info(String.format("[Flow] %s => 执行流程{%s}...", getSessionId(),actionContent));
	}
	public static void logEnd(String content){
		LogUtil.info(String.format("[Flow] %s => 执行流程{%s}完成", getSessionId(),
				content));
	}
	
	public static void logExpression(String actionContent,String objectType){
		LogUtil.info(String.format("[Rule] %s => {%s} {%s}", getSessionId(),
				actionContent,objectType));
	}
}
