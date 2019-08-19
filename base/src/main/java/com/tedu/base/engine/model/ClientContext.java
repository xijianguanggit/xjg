package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.tedu.base.engine.util.FormUtil;
/**
 * view上下文
 * body时定义
 * 在所有function中传递
 * @author wangdanfeng
 *
 */
public class ClientContext implements Serializable{
	public ClientContext(){}
	private String uiName;
	private String functionId;
	private Map<String,String> items;
	private String token;//当前逻辑的
	private String filterStatement = "";//default
	public String toString(){
	  ObjectMapper mapper = new ObjectMapper();  
	  mapper.setSerializationInclusion(Inclusion.NON_NULL); 
	  mapper.setSerializationInclusion(Inclusion.NON_EMPTY); 
      try{
    	  return mapper.writeValueAsString(this);  
      }catch(Exception e){
    	  return "{error:error}";
      }
	}
	public ClientContext(String name){
		uiName = name;
	}
	public String getUiName() {
		return uiName;
	}

	/**
	 * 生成UI时返回的客户端对象
	 * @param uiName
	 * @param tokens
	 */
	public ClientContext(String uiName,Map<String, String> tokens) {
		this.uiName = uiName;
		this.items = tokens;
	}
	
	/**
	 * 执行完逻辑时携带的
	 * 客户端优先读取items
	 * @param uiName
	 * @param token
	 */
	public ClientContext(ServerTokenData serverTokenData) {
		this.uiName = serverTokenData.getUiName();
		this.token = serverTokenData.getToken();
		this.functionId = serverTokenData.getFunctionId();
	}
	
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	public Map<String, String> getItems() {
		return items;
	}
	public void setItems(Map<String, String> token) {
		this.items = token;
	}
	public String getFilterStatement() {
		return filterStatement;
	}
	public void setFilterStatement(String filterStatement) {
		this.filterStatement = filterStatement;
	}
	
	public void setFilterStatement(List<String> sList) {
		this.filterStatement = FormUtil.getStatements(sList);
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	
	
}
