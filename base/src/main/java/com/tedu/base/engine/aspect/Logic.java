package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

public interface Logic {
	public FormModel doBefore(FormEngineRequest requestObj);
	public void doAfter(FormEngineRequest requestObj,FormEngineResponse res);
	public boolean isAntiDup();
	public boolean isControlUpdatable();
	
}
