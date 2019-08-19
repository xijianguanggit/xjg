package com.tedu.base.engine.aspect;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.rule.AviatorUtil;

public class SaveCustomLogic extends AbstractLogic{
	@Override
	public CustomFormModel prepareModel(FormEngineRequest requestObj) {
		long t1 = System.currentTimeMillis();
		Map<String,Object> data = requestObj.getData();
		CustomFormModel formModel = genCustomModelByPanel(requestObj,Param.P_InputPanelId);
		Procedure proc = FormConfiguration.getProcedure(serverTokenData);
    	Param paramSql = proc.getParam(Param.P_SQLId);//Param.P_InputP`anelId
		String sqlId = paramSql==null?"":ObjectUtils.toString(paramSql.getValue());
		//校验必填参数
		if(sqlId.isEmpty()){
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].Sql未配置", 
							serverTokenData.getUiName(),serverTokenData.getFlowId()));			
		}
		formModel.setSqlId(sqlId);
		
		translateForm(formModel);
		formModel.setEditMode(getEditMode(requestObj));
		Map<String,Object> env = formModel.getData();
		AviatorUtil.prepareSessionEnv(env);
		FormConfiguration.setControlDefault(formModel, env, formModel.getData(),FormConstants.METHOD_GETTARGET);
		formModel.setPrimaryFieldValue(data.get("id"));//和delete js的传参名对应
		validate(formModel);
		formModel.getSession().put(FormConstants.AVIATOR_ENV_SESSION_USERINFO, SessionUtils.getUserInfo());
		return formModel;
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
	}

}
