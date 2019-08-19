package com.tedu.base.common.page;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.SessionUtils;

public class QueryPage extends BasePage{
	private boolean optimize = false;
    private List<FieldCondition> params = new ArrayList<FieldCondition>();//其他的参数我们把它分装成一个Map对象
    private String queryParam;//额外构造,用于构造queryList时的view条件
    private String queryType;
    private int single = 1;//单选
    private String sqlId;
    private boolean queryable = true;
	private Map<String,Object> data = new HashMap<String,Object>();//可以在构造通用查询时，内层SQL使用field = #{data.field}构造动态条件
	private Map<String,Object> model = new HashMap<String,Object>();//可以在构造通用查询时，内层SQL使用field = #{data.field}构造动态条件
	
	private String filter;//额外SQL条件,直接在拦截器中拼接
	
	public List<FieldCondition> getParams() {
		return params;
	}

	public int getSingle() {
		return single;
	}

	public void setSingle(int single) {
		this.single = single;
	}



	public boolean isOptimize() {
		return optimize;
	}

	public void setOptimize(boolean optimize) {
		this.optimize = optimize;
	}

	public String getQueryParam() {
		return queryParam;
	}



	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}



	public String getQueryType() {
		return queryType;
	}



	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}



	/**
     * 将查询条件相关的key放入params
     * 如果组件名以[]结尾，按数组方式获取名值
     * @param request
     * @param reset 是否重置查询条件
     */
    public void setParams(HttpServletRequest request,boolean reset) {
    	Enumeration pNames=request.getParameterNames();
        String currentKey = "";
        String currentVal = "";
    	while(pNames.hasMoreElements() ){
    		currentKey = (String)pNames.nextElement();
    		if(!FieldCondition.isCondition(currentKey)) continue;
    		if(currentKey.endsWith("[]")){//数组值,如tagbox的value
    			String[] arrCurrentVal = request.getParameterValues(currentKey);
    			currentVal = StringUtils.join(arrCurrentVal, ",");
    			currentKey = currentKey.substring(0, currentKey.lastIndexOf("[]"));
    		}else{
    			currentVal = request.getParameter(currentKey); 
    		}
     	   if(!ObjectUtils.toString(currentVal).isEmpty()){
     		  addCondition(currentKey,currentVal);
    	   }
    	}
    }
    
    public void addCondition(String field,String value){
		if(!FieldCondition.isCondition(field)){
			field =  FieldCondition.OP_EQUAL.concat("_").concat(field);
		}
    	FieldCondition condition = new FieldCondition(field,value);
    	params.add(condition);	
    }
    
    public void setParams(HttpServletRequest request) {
    	setParams(request,false);
    }

    public void setParamsByMap(Map<String,Object> mapParam) {
    	String currentKey;
    	String currentVal;
    	for (Map.Entry<String, Object> entry : mapParam.entrySet()) {
    		currentKey = entry.getKey();
    		if(!FieldCondition.isCondition(currentKey)) continue;
    		currentVal = ObjectUtils.toString(entry.getValue()); 
     	   if(!ObjectUtils.toString(currentVal).isEmpty()){
     		  addCondition(currentKey,currentVal);
    	   }
    	}
    }
    
	public Map<String, Object> getData() {
		return data;
	}
	
	/**
	 * @deprecated
	 * @param request
	 */
	public void setData(HttpServletRequest request) {
    	Enumeration pNames=request.getParameterNames();
        String currentKey = "";
        String currentVal = "";
    	while(pNames.hasMoreElements() ){
    		currentKey = (String)pNames.nextElement();
   			currentVal = request.getParameter(currentKey);
   			data.put(currentKey, currentVal);
    	}
    	data.put(ConstantUtil.USER_INFO, SessionUtils.getUserInfo());
	}
	
    public void setDataByMap(Map<String,Object> mapParam) {
    	String currentKey;
    	String currentVal;
    	for (Map.Entry<String, Object> entry : mapParam.entrySet()) {
    		currentKey = entry.getKey();
    		currentVal = ObjectUtils.toString(entry.getValue()); 
     		data.put(currentKey, currentVal);
    	}
    	data.put(ConstantUtil.USER_INFO, SessionUtils.getUserInfo());
    }

	public String getSqlId() {
		return sqlId;
	}

	public void setSqlId(String sqlId) {
		this.sqlId = sqlId;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public boolean isQueryable() {
		return queryable;
	}

	public void setQueryable(boolean queryable) {
		this.queryable = queryable;
	}


}
