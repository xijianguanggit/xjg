package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;

public class EmptyTokenLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		return null;
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
		//do nothing
	}
	
}
