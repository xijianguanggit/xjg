package com.tedu.base.engine.aspect;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.initial.model.xml.ui.Param;

/**
 * Out
 * @author wangdanfeng
 *
 */
public class ValidateLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		FormModel formModel = genCustomModelByPanel(requestObj,Param.P_InputPanelId);
		return formModel;
	}

	@Override
	public void prepareResponse( FormEngineRequest requestObj,
		 FormEngineResponse res) {
	}
}
