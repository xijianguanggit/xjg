package com.tedu.base.common.page;

import org.apache.commons.lang3.ObjectUtils;

import com.tedu.base.engine.util.FormLogger;

public class QueryUtil {
	/**
	 * 根据查询条件拼装SQL
	 * @param page
	 * @return
	 */                                                                           
	public static String getSqlFilter(QueryPage queryPage){
		StringBuilder sql = new StringBuilder();
		int i = 0;
		int len = queryPage.getParams().size();
		for(;i<len;i++){
			String cond = queryPage.getParams().get(i).toString();
			if(cond.isEmpty()) cond = " (1=1) ";
			sql.append(cond);
			if(i<len-1){
				sql.append(" and");
			}
		}
		String filter = ObjectUtils.toString(queryPage.getFilter());
		if(!filter.isEmpty()){
			FormLogger.logFlow("附加权限条件\n" + filter , FormLogger.LOG_TYPE_INFO);
			if(sql.length()>0){
				sql.append(" and");
			}
			sql.append(" ( ").append(filter).append(" ) ") ;			
		}
		
		return sql.toString();
	}
	
	/**
	 * 完整where 字句
	 * 条件为空时返回空字符串
	 * @param queryPage
	 * @return
	 */
	public static String getSqlWhere(QueryPage queryPage){
		String filter = getSqlFilter(queryPage);
		if(!filter.isEmpty()){
			return " where ".concat(filter);
		}
		return " ";
	}
}
