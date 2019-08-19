package com.tedu.base.common.model;

public class QueryModel {
	private String type;//user...
	private String id;
	private String name;
	private String param;//用于构造数据源的初始筛选条件.
	
	private String tableName;
	private String fieldName;
	private String columname;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getValue() {
		return id;
	}

	public String getText() {
		return name;
	}
}
