package com.tedu.base.task;

import java.io.Serializable;
/**
 * 服务于表单引擎的view model
 * 属性值用map存储
 * 常用于表单引擎构造的表单的处理及增删改查
 * @author wangdanfeng
 *
 */
public class SqlModel implements Serializable{
	private static final long serialVersionUID = -1158966438635067762L;
	public String insertSql;
	public String updateSql;
	public String deleteSql;
	public String getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	public String getUpdateSql() {
		return updateSql;
	}
	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}
	public String getDeleteSql() {
		return deleteSql;
	}
	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}
	
	
}