package com.tedu.base.engine.aspect;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.util.UnknownClassException;

import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;

public class LogicFactory {
	public static AbstractLogic getLogic(FormTokenService formTokenService,String token,ServerTokenData serverTokenData){
		AbstractLogic logic;
		String logicName = serverTokenData==null?"":serverTokenData.getLogic();
		if(token.isEmpty() || serverTokenData==null){
			logic = getEmptyLogic();
		}else{
			String className =  "com.tedu.base.engine.aspect."+logicName + "Logic";
			try{
				logic = (AbstractLogic)ClassUtils.newInstance(className);
			}catch(UnknownClassException e){
				logic = getEmptyLogic();
				FormLogger.logFlow(logicName + "前置处理类未找到,忽略。", FormLogger.LOG_TYPE_INFO);
			}
		}
		logic.setServerTokenData(serverTokenData);//token data
		logic.setFormTokenService(formTokenService);
		return logic;
	}
	
	public static AbstractLogic getEmptyLogic(){
		return new EmptyTokenLogic();
	}
}
