package com.tedu.base.engine.aspect;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
/**
 * SQL中可用${data.xx}引用
 * @author wangdanfeng
 *
 */
public class ListLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		Procedure p = FormConfiguration.getProcedure(serverTokenData);
		QueryPage query = new QueryPage();

		Param paramSql = p.getParam(Param.P_SQLId);
		if(paramSql==null){
			FormLogger.logFlow( String.format("UI{%s}的List未配置Sql(非必填)", serverTokenData.getUiName()),
					FormLogger.LOG_TYPE_WARN);//应抛异常
			query.setQueryParam("");
		}else{
			query.setQueryParam(paramSql.getValue());
			query.setDataByMap(requestObj.getData());
		}
		requestObj.setQuery(query);
		return null;
	}

	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
	}
}
