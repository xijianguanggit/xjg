package com.tedu.base.engine.aspect;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.reflect.MethodUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.engine.exception.FormEngineException;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.task.SpringUtils;

/**
 * 事物型插件
 * param=DAOPlugin @see ILogicServicePlugin
 * @author wangdanfeng
 *
 */
public class ServicePluginHelper {
	private String pluginServiceName;
	private Object pluginObj;
	private Object beforeResult;//存放doBefore方法执行后的结果
	private static final String METHOD_BEFORE = "doBefore";
	private static final String METHOD_AFTER = "doAfter";
	/**
	 * 根据配置构造
	 * 非必要配置
	 * @param serverTokenData
	 */
	public ServicePluginHelper(ServerTokenData serverTokenData){
		//instantiate plugin class
		if(serverTokenData==null) return;
		String logicName = serverTokenData.getLogic();
		if(logicName.equals(FormConstants.LOGIC_TRANSFORM)) return;//
		Procedure p = FormConfiguration.getProcedure(serverTokenData);
		if(p!=null){
			Param paramPlugin = p.getParam(Param.P_DAO_Plugin);
			init(paramPlugin);
		}
	}

    public void init(Param paramPlugin) {
    	if(paramPlugin==null) return;
    	pluginServiceName = ObjectUtils.toString(paramPlugin.getValue()).trim();
    	try {
    		pluginObj = SpringUtils.getBean(pluginServiceName);
    		if(!(pluginObj instanceof ILogicServicePlugin)){
    			pluginObj = null;
    			FormLogger.logFlow("[" + pluginServiceName + "]" + "mush implement ILogicServicePlugin", FormLogger.LOG_TYPE_INFO);
    		}
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.CLASS_MISSING,pluginServiceName);
		} 
    }
    
    public void doBefore(FormEngineRequest requestObj,FormModel formModel){
    	if(pluginObj==null) return;
		try{
			Class[] parameterTypes = new Class[]{FormEngineRequest.class,FormModel.class};
			this.beforeResult = MethodUtils.invokeMethod(pluginObj, METHOD_BEFORE, new Object[]{requestObj,formModel},parameterTypes);
			FormLogger.logFlow(String.format("DAOPlugin[%s]前置方法[%s]执行完毕", pluginServiceName,METHOD_BEFORE),
					FormLogger.LOG_TYPE_INFO);
		}catch(InvocationTargetException e){
		   if(e.getTargetException() instanceof ValidException){
			   ValidException ve = (ValidException)(e.getTargetException());
			   FormLogger.logFlow(ve.getMessage(), FormLogger.LOG_TYPE_ERROR);
			   throw ve;
		   }else{
			   FormLogger.logFlow((e.getTargetException()).getMessage(), FormLogger.LOG_TYPE_ERROR);
		   }
		}catch(Exception e){
		   FormLogger.logFlow(e.getMessage(), FormLogger.LOG_TYPE_ERROR);
		   throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,String.format("%s.%s", pluginServiceName,METHOD_BEFORE));
		}
    }
    
    public void doAfter(FormEngineRequest requestObj,FormModel formModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
    	afterLogic(requestObj,formModel);
    }
    
    public void afterLogic(FormEngineRequest requestObj,FormModel formModel) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
    	if(pluginObj==null) return;
		Object param = beforeResult== null?"":beforeResult;
		Class[] parameterTypes = new Class[]{FormEngineRequest.class,FormModel.class,Object.class};
		try{
			MethodUtils.invokeMethod(pluginObj, METHOD_AFTER, new Object[]{requestObj,formModel,param},parameterTypes);
		}catch(NoSuchMethodException | IllegalAccessException e){
			throw new ServiceException(ErrorCode.PLUGIN_EXCEPTION,e.getMessage());
		}catch(InvocationTargetException e){
			if(e.getTargetException() instanceof FormEngineException){
				throw (RuntimeException)e.getTargetException();
			}
		}
		FormLogger.logFlow(String.format("DAOPlugin[%s]后置方法[%s]执行完毕", pluginServiceName,METHOD_AFTER),FormLogger.LOG_TYPE_INFO);
    }
}
