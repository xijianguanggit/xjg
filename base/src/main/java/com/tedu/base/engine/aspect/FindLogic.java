package com.tedu.base.engine.aspect;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
/**
 * find逻辑需携带两个重要参数
 * 1、queryParam:sql模板文件
 * 2、id:用于动态构造sql 在sql模板文件中使用#{data.id}引用
 * 3、In:panel，可以在SQL模板的body部分用model.controlName引用值。多用于联动等场景
 * 
 * sqlId:去掉前缀sel
 * 
 * 
 * 
 * @author wangdanfeng
 *
 */
public class FindLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		ServerTokenData serverTokenData = getServerTokenData();
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
			//find逻辑使用通用查询时，参与查询条件的只有${filterName}
			Map<String,Object> param = new HashMap<>();
			param.put("id", requestObj.getData().get("id"));
			String filterName = ObjectUtils.toString(requestObj.getData().get("filterName"));
			Object filterValue = requestObj.getData().get(filterName);
			param.put(filterName, filterValue);
			query.setParamsByMap(param);//只有搜索框部分参与搜索.In参数中的Panel值仅在解释SQL模板body时使用			
			
			query.getData().put("id", requestObj.getData().get("id"));//兼容之前的data.id。
			query.setModel(paramData);//避免查询条件类Panel传入条件被查询拦截器使用。换成In参数表述
			
			Param paramAlias = p.getParam(Param.P_Alias);
			if(paramAlias!=null && !paramAlias.getValue().isEmpty()){
				String[] arr = paramAlias.getValue().split("[|]");
				for(String field:arr){
					String[] arrControlAlias = field.split(",");
					if(arrControlAlias.length<2) throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,"Find逻辑别名配置不正确");
					String controlName = arrControlAlias[0];
					String alias = arrControlAlias[1];
					if(paramData.get(controlName)!=null){
						paramData.put(alias, paramData.get(controlName));//组件->别名，可在model里引用，增加重用性
					}
				}
			}
			SessionUtils.setAttrbute(serverTokenData.getFlowId(),filterValue);
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
