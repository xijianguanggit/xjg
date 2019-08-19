package com.tedu.base.engine.aspect;



import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
/**
 * 生成model
 * 必要参数Sql从配置获取，不再从前端传递
 * @author wangdanfeng
 *
 */
public class QueryByIdLogic extends AbstractLogic {
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
    	Procedure p = FormConfiguration.getProcedure(serverTokenData);
		QueryPage query = new QueryPage();
		Param paramSql = p.getParam(Param.P_SQLId);
		if(paramSql==null){
			FormLogger.logConf("未配置Sql", serverTokenData.getFlowId());//应抛异常
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].Sql未配置", 
							serverTokenData.getUiName(),serverTokenData.getFlowId()));
		}
		String keyValue = ObjectUtils.toString(requestObj.getData().get("eq_id"));
		if(keyValue.isEmpty()){
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].id为空", 
							serverTokenData.getUiName(),serverTokenData.getFlowId()));			
		}
		query.setQueryParam(paramSql.getValue());
		query.setParamsByMap(requestObj.getData());
		requestObj.setQuery(query);
		FormModel formModel = prepareOutputModel(requestObj);
		formModel.setEditMode(SessionUtils.getWindowMode(requestObj.getWid()));//用于解析表达式
		return formModel;
	}

	public void prepareResponse(FormEngineRequest requestObj,FormEngineResponse res){
	}
}
