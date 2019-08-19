package com.tedu.base.engine.model;

import java.util.Map;

public interface BasicFormModel {
	public String getInsertSql();
	public String getUpdateSql();
	public Map<String,Object> getData();
	public String getTableName();
	public void setTableName(String tableName);
	public String getPrimaryField();
	public void setPrimaryField(String primary) ;
}
