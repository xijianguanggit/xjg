package com.tedu.base.engine.aspect;

import org.apache.commons.lang.ClassUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
/**
 * @author wangdanfeng
 *
 */
public class TransformLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
    	Procedure p = FormConfiguration.getProcedure(serverTokenData);
    	Param paramPlugin = p.getParam(Param.P_Plugin);
    	FormModel formModel = null;
    	if(paramPlugin!=null){
			try {
				ILogicReviser logicReviser = (ILogicReviser)(ClassUtils.getClass(paramPlugin.getValue(), true).newInstance());
				formModel = logicReviser.beforeLogic(requestObj);
			} catch (InstantiationException e) {
				FormLogger.logFlow("InstantiationException", FormLogger.LOG_TYPE_ERROR);
			} catch (IllegalAccessException e) {
				FormLogger.logFlow("IllegalAccessException", FormLogger.LOG_TYPE_ERROR);
			} catch (ClassNotFoundException e) {
				throw new ServiceException(ErrorCode.CLASS_MISSING,paramPlugin.getValue());
			}
    	}
		return formModel;
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse responseObj) {
    	Procedure p = FormConfiguration.getProcedure(serverTokenData);
    	Param paramPlugin = p.getParam(Param.P_Plugin);		
		try {
			ILogicReviser logicReviser = (ILogicReviser)(ClassUtils.getClass(paramPlugin.getValue(), true).newInstance());
			logicReviser.afterLogic(requestObj, responseObj);
		} catch (Exception e) {
			FormLogger.logFlow("TransformLogic.prepareResponse exception " + e.getMessage(), FormLogger.LOG_TYPE_ERROR);
		}
		
		
	}
}
