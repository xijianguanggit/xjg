package com.tedu.base.engine.aspect;

import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;

/**
 * markDown编辑器
 * @author hejk
 *
 */
public class MarkDownLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
    	Procedure p = FormConfiguration.getProcedure(serverTokenData);
		
		Param paramSql = p.getParam(Param.P_SQLId);
		if(paramSql==null){
			FormLogger.logConf("未配置Sql", serverTokenData.getFlowId());//应抛异常
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].Sql未配置", 
							serverTokenData.getUiName(),serverTokenData.getFlowId()));			
			
		}
		String sqlId = "sel" + paramSql.getValue();
		QueryPage query = new QueryPage();
		query.setQueryParam(sqlId);
		
		Map<String,Object> paramData = requestObj.getData();
		
		if(paramData != null){
			//分页属性
			if(paramData.get(DataGrid.PROPERTY_PAGE) != null){
				query.setPage(requestObj.getData()==null?null:Integer.parseInt(paramData.get(DataGrid.PROPERTY_PAGE).toString()));
			}
			if(paramData.get(DataGrid.PROPERTY_ROWS) != null){
				query.setRows(paramData==null?null:Integer.parseInt(paramData.get(DataGrid.PROPERTY_ROWS).toString()));
			}
			query.setSort(ObjectUtils.toString(paramData.get(DataGrid.PROPERTY_SORT)));
			query.setOrder(ObjectUtils.toString(paramData.get(DataGrid.PROPERTY_ORDER)));
			//分页属性 end
			
			query.setParamsByMap(requestObj.getData());
			query.setDataByMap(requestObj.getData());
			requestObj.setQuery(query);
		}
		return null;
	}
	/**
	 * 是固定项，显示在表格顶部
	 */
	public static boolean isFixedOption(String option){
		if(option == null) return false;
		return 
				option.indexOf(FieldCondition.NOT_NULL_FIELD_VALUE)>=0 ||
				option.indexOf(FieldCondition.NULL_FIELD_VALUE)>=0 ||
				option.indexOf(FormConstants.RESET_FIELD_VALUE)>=0 ;
	}
	/**
	 * 将Options配置中的选项追加到rows前
	 * 
	 */
	@Override
	public void prepareResponse(FormEngineRequest requestObj, FormEngineResponse res) {
	}
}
