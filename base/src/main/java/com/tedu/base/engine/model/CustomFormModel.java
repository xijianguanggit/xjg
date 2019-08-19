package com.tedu.base.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义增删改查
 * 和普通FormModel不同的是，updateSql来自于其他设置途径，而非自动根据panel拼装
 * @author wangdanfeng
 *
 */
public class CustomFormModel extends FormModel{
	private Object[] resultArray = null;
	public CustomFormModel(){}
	public CustomFormModel(String uiName,String panelName,Map<String,Object> data){
		super(uiName,panelName,data);
	}
	
	private Map<String,Object> session = new HashMap<String,Object>();
		
	@Override
	public String getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	public String getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
	public Object[] getResultArray() {
		return resultArray;
	}
	public void setResultArray(Object[] resultArray) {
		this.resultArray = resultArray;
	}

	
	
}
