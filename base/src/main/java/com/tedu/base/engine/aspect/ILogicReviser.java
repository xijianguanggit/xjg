package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

public interface ILogicReviser {
	public FormModel beforeLogic(FormEngineRequest requestObj);
	public void afterLogic(FormEngineRequest requestObj,FormEngineResponse responseObj);
}
