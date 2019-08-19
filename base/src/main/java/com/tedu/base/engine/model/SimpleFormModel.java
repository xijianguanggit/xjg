package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.Map;

import com.tedu.base.engine.util.FormSQLUtil;
import com.tedu.base.engine.util.FormUtil;

/**
 * 简单model，可以在运行时刻prepar动态SQL增删改查
 * @author wangdanfeng
 *
 */
public class SimpleFormModel implements Serializable,BasicFormModel{
	private String insertSql;
	private Map<String, Object> data;
	private Object pk = null;
	private String tableName;
	private String primaryField;
	private String updateSql;
	private Object primaryFieldValue;
	/**
	 * 实现TableModel接口的对象已知表和维护字段（不含自增主键）
	 * 自动构建INSERT SQL + 对象实例，可用于直接入库
	 * @param model
	 */
	public SimpleFormModel(TableModel model){
		Map<String, Object> data = FormUtil.getBeanMap(model,false);
		//updateSql页可以明确:唯一性字段+更新字段
		setInsertSql(FormSQLUtil.getInsertSql(model,data));
		setData(data);
	}
	
	public SimpleFormModel(TableModel model,Map<String,Object> data){
		//updateSql页可以明确:唯一性字段+更新字段
		setInsertSql(FormSQLUtil.getInsertSql(model,data));
		setData(data);
	}
	
	public void setUpdateSql(String updateSql){
		this.updateSql = updateSql;
	}
	
	public String getUpdateSql(){
		return updateSql;
	}
	
	public void setInsertSql(String sql){
		insertSql = sql;
	}
	@Override
	public String getInsertSql() {
		return insertSql;
	}

	public void setData(Map<String, Object> data){
		this.data = data;
	}
	
	@Override
	public Map<String, Object> getData() {
		return data;
	}

	@Override
	public String getTableName() {
		return tableName;
	}
	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Override
	public String getPrimaryField() {
		return primaryField;
	}
	@Override
	public void setPrimaryField(String primaryField) {
		this.primaryField = primaryField;
	}

	public Object getPrimaryFieldValue() {
		return primaryFieldValue;
	}

	public void setPrimaryFieldValue(Object primaryFieldValue) {
		this.primaryFieldValue = primaryFieldValue;
	}
	
	
}
