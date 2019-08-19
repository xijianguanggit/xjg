package com.tedu.base.engine.aspect;

import java.util.Map;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.ConstraintsValidator;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;

/**
 * 和save logic prepareModel一样，都是对inputPanel进行处理
 * 按panel id删除对应表的记录
 * @author wangdanfeng
 *
 */
public class DeleteLogic extends AbstractLogic{

	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		Map<String,Object> data = requestObj.getData();
		FormModel formModel = genModelByPanel(requestObj,Param.P_InputPanelId);
		formModel.setPrimaryFieldValue(data.get("id"));
		
		Procedure proc = FormConfiguration.getProcedure(getServerTokenData());
		String constraints = proc.getParam(Param.P_Constraints)==null?"":proc.getParam(Param.P_Constraints).getValue();
		String cascade = proc.getParam(Param.P_Cascade)==null?"":proc.getParam(Param.P_Cascade).getValue();
		
		ConstraintsValidator.setDeleteConstraints(formModel,constraints,cascade);
		
	    //保存前的表达式预设值
		FormConfiguration.setControlDefault(formModel, formModel.getData(), formModel.getData(),FormConstants.METHOD_GETTARGET);
		return formModel;
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj,
		 FormEngineResponse res) {
	}
}
