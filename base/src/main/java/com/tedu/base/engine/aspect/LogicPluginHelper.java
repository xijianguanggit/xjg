package com.tedu.base.engine.aspect;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.engine.exception.FormEngineException;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.task.SpringUtils;

/**
 * param=Plugin的配置值，格式为 beforeMethod|class|afterMethod
 * preMethod,afterMethod可以只有一个或者没有也不会出错
 * @author wangdanfeng
 *
 */
public class LogicPluginHelper {
	private String pluginServiceName;
	private Object pluginObj;
	private Object beforeResult;//前置方法执行后的结果等存放
	
	/**
	 * 根据配置构造
	 * 非必要配置
	 * @param serverTokenData
	 */
	public LogicPluginHelper(ServerTokenData serverTokenData){
		//instantiate plugin class
		if(serverTokenData==null) return;
		String logicName = serverTokenData.getLogic();
		if(logicName.equals(FormConstants.LOGIC_TRANSFORM)) return;//
		Procedure p = FormConfiguration.getProcedure(serverTokenData);
		if(p!=null){
			Param paramPlugin = p.getParam(Param.P_Plugin);
			init(paramPlugin);
		}
	}

    public void init(Param paramPlugin) {
    	if(paramPlugin==null) return;
    	pluginServiceName = ObjectUtils.toString(paramPlugin==null?"":paramPlugin.getValue()).trim();
    	try {
    		pluginObj = SpringUtils.getBean(pluginServiceName);
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.CLASS_MISSING,pluginServiceName);
		} 
    }
    
    public void doBefore(FormEngineRequest requestObj,FormModel formModel){
    	if(pluginObj==null) return;
		try{
			Class[] parameterTypes = new Class[]{FormEngineRequest.class,FormModel.class};
			this.beforeResult = MethodUtils.invokeMethod(pluginObj, "doBefore", new Object[]{requestObj,formModel},parameterTypes);
			FormLogger.logFlow(String.format("Plugin[%s]前置方法[%s]执行完毕", pluginServiceName,"doBefore"),
					FormLogger.LOG_TYPE_INFO);
		}catch(Exception e){
		   if(e instanceof InvocationTargetException) {
			   if(((InvocationTargetException) e).getTargetException() instanceof ValidException){
				   ValidException ve = (ValidException)(((InvocationTargetException) e).getTargetException());
				   FormLogger.logFlow(ve.getMessage(), FormLogger.LOG_TYPE_ERROR);
				   throw ve;
			   }else{
				   FormLogger.logFlow((((InvocationTargetException) e).getTargetException()).getMessage(), FormLogger.LOG_TYPE_ERROR);
			   }
		   }else{
			   FormLogger.logFlow(e.getMessage(), FormLogger.LOG_TYPE_ERROR);
			   throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,String.format("%s.%s", pluginServiceName,"doBefore"));
		   }
		}
    }
    
    public void doAfter(FormEngineRequest requestObj,FormModel formModel,FormEngineResponse responseObj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
    	if(pluginObj==null) return;
		Object param = beforeResult== null?"":beforeResult;
		Class[] parameterTypes = new Class[]{FormEngineRequest.class,FormModel.class,Object.class,FormEngineResponse.class};
		try{
			MethodUtils.invokeMethod(pluginObj, "doAfter", new Object[]{requestObj,formModel,param,responseObj},parameterTypes);
		}catch(NoSuchMethodException | IllegalAccessException e){
			throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,e.getMessage());
		}catch(InvocationTargetException e){
			if(e.getTargetException() instanceof FormEngineException){
				throw (RuntimeException)e.getTargetException();
			}
		}
		FormLogger.logFlow(String.format("Plugin[%s]后置方法[%s]执行完毕", pluginServiceName,"doAfter"),FormLogger.LOG_TYPE_INFO);
    }
}
