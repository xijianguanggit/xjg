package com.tedu.base.engine.model;

import org.apache.commons.lang.StringUtils;

public class UniqueModel {
	private String sql;
	private String[] fieldsTitle;
	private String[] fieldsValue;
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
    public String getControlName(){
    	return StringUtils.join(fieldsTitle,"-");
    }

    public String getControlValue(){
    	return StringUtils.join(fieldsValue,"-");
    }
    
	public String[] getFieldsTitle() {
		return fieldsTitle;
	}
	public void setFieldsTitle(String[] fieldsTitle) {
		this.fieldsTitle = fieldsTitle;
	}
	public String[] getFieldsValue() {
		return fieldsValue;
	}
	public void setFieldsValue(String[] fieldsValue) {
		this.fieldsValue = fieldsValue;
	}
	public String toExceptionString(){
		return String.format("%s::%s 已存在", getControlName(),getControlValue());
	}
}
