package com.tedu.base.engine.aspect;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormTokenUtil;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.log.model.LogModel;
import com.tedu.base.log.service.LogService;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
/**
 * form engine logic controller切片
 */
@Component 
@Aspect
public class LogicAspect {
	public static final Logger log = Logger.getLogger(LogicAspect.class);
	@Resource
	FormTokenService formTokenService;
	@Resource
	FormLogService formLogService;
	@Autowired
	private LogService logService;
    @Value("${accessLog.status}")
    private String accessLog_status;
    @Value("${accessLog.request}")
    private String accessLog_request;
    @Value("${accessLog.response}")
    private String accessLog_response;
	private static String STATUS_OPEN = "1";
	private static String STATUS_CLOSE = "0";
	//使用token的Controller做切面
    @Pointcut("execution(* com.tedu.base.engine.controller.*.*(..))||execution(* com.tedu.base.workflow.controller.WorkFlowLogicController.*(..))")
    public void processLogic(){
    	//do nothing
    }
	@Before("processLogic()")
	public void beforeMethod(JoinPoint point) {
		FormLogger.debug("before");
	}
    
	@After("processLogic()")
	public void afterMethod(JoinPoint point) {
		FormLogger.debug("after");
	}
	
	/**
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("processLogic()")
	public Object aroundHandler(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
	    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	    FormEngineRequest requestObj = getRequestObj(request,proceedingJoinPoint);//spring映射到对象
	    if(requestObj!=null){
	    	requestObj.setClientIp(FormUtil.getClientIP(request));
	    }
	    String token = getToken(request);
		Object value = null;
		AbstractLogic logic;
		//防重复提交逻辑会消费掉token,其余只是读取
		ServerTokenData serverTokenData = formTokenService.consumeToken(token);
		logBegin(serverTokenData);//基于flow的开始日志
		//获取逻辑处理类实例
		logic = LogicFactory.getLogic(formTokenService,token,serverTokenData);
		LogicPluginHelper pluginHelper = new LogicPluginHelper(serverTokenData);//方法之间传值等
		try{
			Object[] args = proceedingJoinPoint.getArgs();
			FormModel formModel = logic.doBefore(requestObj);//注入准备好的model,执行logic
			pluginHelper.doBefore(requestObj,formModel);//插件前置方法
			updateArgs(formModel, serverTokenData, args);
			Long time = (new Date()).getTime();
			
			
			value = proceedingJoinPoint.proceed(args);//调用action args

	    	if(STATUS_OPEN.equals(accessLog_status)){
	    		insertAccess(request, requestObj.toString(), value, time);
	    	}
			
			if(value instanceof FormEngineResponse){
				logic.doAfter(requestObj,(FormEngineResponse)value);//修饰response:token,数据变形等
				pluginHelper.doAfter(requestObj, formModel,(FormEngineResponse)value);//插件后置方法
			}
		}catch(ServiceException | ValidException e){
			//校验不通过时保留
			serverTokenData.setToken(token);//用ui生成时构造的token恢复。此时防重复提交逻辑的token已renew
			FormTokenUtil.restoreToken(formTokenService,serverTokenData);
			throw e;//被ResponseAdvice类拦截,统一处理
		}
		logAction(serverTokenData);//基于flow的UserLog
		logEnd(serverTokenData);//基于flow的结束日志
		return value;
	}
	
	private void logAction(ServerTokenData serverTokenData){
		if(serverTokenData!=null && serverTokenData.isBeginLogic()){//END LOG可能找不到,所以以begin时为准
			formLogService.logAction(serverTokenData);//基于flow的UserLog
		}
	}
		
	private FormEngineRequest getRequestObj(HttpServletRequest request,ProceedingJoinPoint proceedingJoinPoint){
		for(Object o:proceedingJoinPoint.getArgs()){
			if(o instanceof FormEngineRequest){
				return (FormEngineRequest)o;
			}
		}
		FormEngineRequest requestObj = new FormEngineRequest();
		requestObj.setData(request.getParameterMap());
		return null;
	}
	/**
	 * @param formModel
	 * @param serverTokenData
	 * @param args
	 */
	public void updateArgs(FormModel formModel,ServerTokenData serverTokenData,Object[] args){
	    for(int i = 0; i < args.length; i++){
	        Object arg = args[i];
	        if ( arg instanceof FormModel ) {
	            args[i] = formModel;
	        }else if ( arg instanceof ServerTokenData ) {
	            args[i] = serverTokenData;
	        }
	    }
	}
	
	@ExceptionHandler(ServiceException.class)
	public @ResponseBody
	String handleException(Exception e, HttpServletResponse response) {
	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    return e.getMessage();
	}
	
	private void logBegin(ServerTokenData serverTokenData){
		if(serverTokenData!=null && serverTokenData.isBeginLogic()){
			FormLogger.logBegin(String.format("%s:%s:%s ", 
					serverTokenData.getUiName(),serverTokenData.getTrigger().isEmpty()?serverTokenData.getUiName():serverTokenData.getTrigger(),
					serverTokenData.getEvent()));
		}
	}
	
	private void logEnd(ServerTokenData serverTokenData){
		if(serverTokenData!=null && serverTokenData.isEndLogic()){
			FormLogger.logEnd(String.format("%s:%s:%s ", 
					serverTokenData.getUiName(),serverTokenData.getTrigger().isEmpty()?serverTokenData.getUiName():serverTokenData.getTrigger(),
					serverTokenData.getEvent()));
		}	
	}
	
	/**
	 * 优先取header，否则取request parameter
	 * @param request
	 * @return
	 */
	private String getToken(HttpServletRequest request){
		 String token = ObjectUtils.toString(request.getHeader(FormConstants.REQUEST_TOKEN));
    	if(request.getMethod().equals("GET") || token==null){
    		token = request.getParameter(FormConstants.REQUEST_TOKEN);
    	}
    	return token;
	}
	private void insertAccess(HttpServletRequest request, String requestObj, Object respones, Long time){
    	LogModel logModel = new LogModel();
    	if(STATUS_OPEN.equals(accessLog_request)){
    		logModel.setRequest(requestObj);
    	}
    	if(STATUS_OPEN.equals(accessLog_response)){
    		JsonConfig jsonConfig = new JsonConfig();
    		jsonConfig.registerJsonValueProcessor(java.sql.Date.class,new JsonValueProcessor() {
    		           private final String format="yyyy-MM-dd";
    		           public Object processObjectValue(String key, Object value,JsonConfig arg2){
    		             if(value==null)
    		                   return "";
    		             if (value instanceof java.sql.Date) {
    		                   String str = new SimpleDateFormat(format).format((java.sql.Date) value);
    		                   return str;
    		             }
    		                   return value.toString();
    		           }
    		     
    		           public Object processArrayValue(Object value, JsonConfig arg1){
    		                      return null;
    		           }
    		           
    		        });
    		JSONArray ja = new JSONArray();
    		if (respones instanceof FormEngineResponse){
    			ja = JSONArray.fromObject(respones, jsonConfig);
    			if(ja.toString().length()>10000){
    				logModel.setRespones(ja.toString().substring(0, 9999));
    			}else {
    				logModel.setRespones(ja.toString());
    			}
    		} else if(respones instanceof String){
    			logModel.setRespones(respones.toString());
    		}
    	}
    	String ip = FormUtil.getClientIP(request);
    	logModel.setClientIp(ip);
    	UserModel userModel = SessionUtils.getUserInfo();
    	if(userModel!=null){
    		logModel.setEmpId(userModel.getEmpId());
    		logModel.setUserId(userModel.getUserId());
    	}
    	logModel.setUrl(request.getServletPath());
    	if (respones instanceof FormEngineResponse){
    		logModel.setResult(((FormEngineResponse)respones).getCode());
    	} else {
    		logModel.setResult("0");
    	}
    	logModel.setServerIp(request.getHeader("Host"));
    	logModel.setServerPort(String.valueOf(request.getLocalPort()));
    	logModel.setUserAgent(request.getHeader("User-Agent"));
    	logModel.setCost((new Date()).getTime() - time);
    	logService.addLog(logModel);
	}
}
