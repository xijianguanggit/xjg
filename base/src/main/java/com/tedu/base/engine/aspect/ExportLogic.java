package com.tedu.base.engine.aspect;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.model.DataGrid;
import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.Control;
import com.tedu.base.initial.model.xml.ui.Filter;
import com.tedu.base.initial.model.xml.ui.FilterField;
import com.tedu.base.initial.model.xml.ui.Panel;
import com.tedu.base.initial.model.xml.ui.Param;
import com.tedu.base.initial.model.xml.ui.Procedure;
import com.tedu.base.rule.AviatorUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author wangdanfeng
 *
 */
public class ExportLogic extends AbstractLogic{
	@Override
	public FormModel prepareModel(FormEngineRequest requestObj) {
		String uiName = serverTokenData.getUiName();
    	Procedure p = serverTokenData.getProcedure();
		QueryPage query = new QueryPage();
		
		Param paramSql = p.getParam(Param.P_SQLId);
		if(paramSql==null){
			FormLogger.logConf("未配置Sql", serverTokenData.getFlowId());//应抛异常
			throw new ServiceException(ErrorCode.ILLEGAL_REQUEST_PARAMETER,
					String.format("[%s][%s].Sql未配置", 
							serverTokenData.getUiName(),serverTokenData.getFlowId()));
		}else{
			query.setSqlId(paramSql.getValue());//方便log
		}
		Param paramIn = p.getParam(Param.P_InputPanelId);
		Panel filterPanel = null;
		if(paramIn!=null){
			filterPanel = FormConfiguration.getPanel(serverTokenData.getUiName(), paramIn.getValue());
		}
		if(filterPanel!=null && filterPanel.isOptimize()) query.setOptimize(true);
		query.setQueryParam(paramSql.getValue());
		Map<String,Object> paramData = requestObj.getData();
//		query.setParamsByMap(requestObj.getData());
		setQueryCondition(filterPanel,query,paramData);//构造查询条件对象)
		query.setDataByMap(paramData);
		AviatorUtil.prepareSessionEnv(query.getData());
		preparePaging(query);//分页相关属性
		//分页属性 end
		requestObj.setQuery(query);
		//查询条件若有必填未填写时，不返回数据
		query.setQueryable(validateRequiredCondition(p,query));
		Param outParam = p.getParam(Param.P_OutputPannelId);
		if(outParam==null)
			return null;
		String strOutPanel = outParam.getValue();
		Panel outPanel = FormConfiguration.getPanel(uiName, strOutPanel);
		//chart数据顺序根据配置构造,表格的数据顺序在绘制表格初次按照配置设定，其余时刻根据用户点击列的行为动态决定
		if(outPanel != null && outPanel.isChart()){
			outPanel.setSortOrder();
			outPanel.setSortName();
			query.setOrder(outPanel.getSortName());
			query.setSort(outPanel.getSortOrder());
		}
		setFilter(query,outPanel);
		return null;
	}
	
	/**
	 * 根据当前角色,资源,构造查询
	 * @param queryPage
	 */
	private void setFilter(QueryPage queryPage,Panel outPanel){
		List<Filter> filterList = outPanel.getFilterList();
		String dataAuth = SessionUtils.getUrlDataAuth(FormUtil.getUIUrl(serverTokenData.getUiName()));
		if(filterList==null || filterList.isEmpty()) return;
		
		if (dataAuth == null || dataAuth.isEmpty()) {
			FormLogger.logFlow(serverTokenData.getUiName() + "配置了filter属性，但是此功能无数据授权。", FormLogger.LOG_TYPE_WARN);
			return;
		}

		Configuration cfg = new Configuration();
		cfg.setClassicCompatible(true);
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		String[] arrAuth = dataAuth.split(",");
		Map<String, Filter> filterMap = 
				filterList.stream().collect(Collectors.toMap(Filter::getName, filter -> filter));
		
		Map<String,Object> env = new HashMap<>();
		AviatorUtil.prepareSessionEnv(env);
		
		List<String> conditionList = new ArrayList<>();
		try{
			//按照当前用户权限列表中的dataAuth描述，逐个匹配配置中的filter，多个filter之间or关系
			for(String filterName:arrAuth){
				Filter filter = filterMap.get(filterName);
				if(filter==null) {
					FormLogger.logFlow("Query逻辑未实现数据权限中的" + filterName + "配置", FormLogger.LOG_TYPE_INFO);
					throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,
							serverTokenData.getUiName() + "未实现数据权限中的" + filterName + "配置");
				}
				
				StringBuilder sbuff = new StringBuilder(" (");
				List<FilterField> fieldList = filter.getFieldList();
				int len = fieldList.size();
				//一个filter的多字段描述。
				for(int i=0;i<len;i++){
					FilterField field = fieldList.get(i);
					Template t = new Template("filter", new StringReader(field.getCondition()), cfg);
					Writer out = new StringWriter();
					t.process(env, out);
					String conditionValue = out.toString();
					
					sbuff.append(String.format(" %s %s %s ", field.getName(),field.getSign(),conditionValue));
					if(i<len-1){
						sbuff.append(field.getConjunction());
					}
				}
				sbuff.append(") ");
				conditionList.add(sbuff.toString());
			}
		}catch(IOException | TemplateException e){
			FormLogger.logFlow("QueryLogic解析filter异常,"+e.getMessage(), FormLogger.LOG_TYPE_INFO);
		}
		queryPage.setFilter(StringUtils.join(conditionList," or "));
	}
	
	private void preparePaging(QueryPage query){
		Map<String,Object> paramData = query.getData();
		if(paramData!=null){
			//分页属性
			if(paramData.get(DataGrid.PROPERTY_PAGE) != null){
				query.setPage(Integer.parseInt(paramData.get(DataGrid.PROPERTY_PAGE).toString()));
			}
			if(paramData.get(DataGrid.PROPERTY_ROWS) != null){
				query.setRows(Integer.parseInt(paramData.get(DataGrid.PROPERTY_ROWS).toString()));
			}
			query.setSort(ObjectUtils.toString(paramData.get(DataGrid.PROPERTY_SORT)));
			query.setOrder(ObjectUtils.toString(paramData.get(DataGrid.PROPERTY_ORDER)));
		}
	}
	/**
	 * 校验查询逻辑的输入Panel中的查询类组件。非查询条件不校验
	 * @param p
	 * @param query
	 */
	private boolean validateRequiredCondition(Procedure p,QueryPage query){
		boolean isValidCondition = true;
		Param filterPanelParam = p.getParam(Param.P_InputPanelId);
		if(filterPanelParam!=null){
			Panel filterPanel = FormConfiguration.getPanel(serverTokenData.getUiName(), filterPanelParam.getValue());
			if(filterPanel!=null){
				List<Control> controlList = filterPanel.getControlList();
				List<Control> conditionControlList = new ArrayList<>();
				for(Control c:controlList){
					if(FieldCondition.isCondition(c.getName())){
						conditionControlList.add(c);
					}
				}
				BindingResult result = super.validateRequired4Query(conditionControlList,query.getData());
				if(result!=null && result.hasErrors()){
					//返回空结果
					query.addCondition(FieldCondition.OP_SQL+"_x", "1=2");
					isValidCondition = false;
				}
			}
		}
		return isValidCondition;
	}
	private void setQueryCondition(Panel condPanel,QueryPage query,Map<String,Object> paramData){
		if(condPanel==null){
			return;
		}else if(!condPanel.isOptimize()){
			query.setParamsByMap(paramData);
			return ;
		}
		List<Control> cList = condPanel.getControlList();
		for(Control c:cList){
			String alias = c.getAlias();
			String name = c.getName();
			if(alias==null || alias.isEmpty()){
				continue;
			}			
			if(FieldCondition.isCondition(name)){
				Object val = paramData.remove(name);//移除panel中control对应的数据,按alias作为key
				paramData.put(name.substring(0, 3).concat(alias),val);
				FormLogger.logFlow("Query.alias:" + alias, FormLogger.LOG_TYPE_INFO);
			}
		}
		query.setParamsByMap(paramData);
	}
	@Override
	public void prepareResponse(FormEngineRequest requestObj,FormEngineResponse res){
		if(res == null){
			FormLogger.debug("ExportLogic.prepareResponse.NULL RESPONSE");
			return;
		}
		if(!requestObj.getQuery().isQueryable()){
			res.setCode(ErrorCode.INVALIDATE_FORM_DATA.getCode());
			res.setMsg("请设置查询条件");
		}
	}
}
