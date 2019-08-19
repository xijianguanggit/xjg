package com.tedu.base.engine.model;

/**
 * 
 * @author wangdanfeng
 *
 */
public class ConstraintsModel {
	private String table;
	private String field;
	private String value;
	private String msg;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getSelectSql() {
		return String.format("select 1 from %s where %s=#{value} limit 1", table,field);
	}

	public String getDeleteSql() {
		return String.format("delete from %s where %s=#{value}", table,field);
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String toExceptionString(){
		return String.format(getMsg() + "[数据在%s表中已被引用]", getTable());
	}
	
}
