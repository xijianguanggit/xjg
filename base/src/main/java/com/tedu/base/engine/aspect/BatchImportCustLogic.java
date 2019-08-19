package com.tedu.base.engine.aspect;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.FormModel.Mode;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
/**
 * save如有指mode，以指定为准，否则从request获取
 * @author tedu
 *
 */
public class BatchImportCustLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		Map<String,Object> data = requestObj.getData();
		if(data==null) return new FormModel();
		// 根据token获取导入逻辑的配置：主要是In，之后和save相同
		Procedure proc = FormConfiguration.getProcedure(serverTokenData);
    	Param paramIn = proc.getParam(Param.P_InputPanelId);
    	String uiPanelName = ObjectUtils.toString(paramIn.getValue());
    	String[] arr = uiPanelName.split("[.]");
    	if(arr!=null && arr.length>1){//BatchImport第一个请求是弹窗,无post data 
	    	FormModel formModel = new FormModel(arr[0],arr[1],data);
			formModel.setEditMode(Mode.Insert.value);
		    //保存前的表达式预设值
			FormConfiguration.setControlDefault(formModel, data, data,FormConstants.METHOD_GETTARGET);
			validate(formModel);
			FormLogger.logFlow(String.format("校验成功{%s}",serverTokenData.getUiName()), FormLogger.LOG_TYPE_INFO);
			return formModel;
    	}
		return null;
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
		// do nothing
	}
}
