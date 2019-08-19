package com.tedu.base.engine.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.util.TemplateUtil;
@Repository("logDao")
public class FormLogMapper extends SqlSessionDaoSupport{
	public final Logger log = Logger.getLogger(this.getClass());
	private static final String METHOD_SELECT_ONE = "selectOne";

	@Resource
	TemplateUtil templateUtil;
	
	 @Autowired
	 public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate){
		 super.setSqlSessionTemplate(sqlSessionTemplate);
	 }
	 
	/**
	 * 按ID获取对应表的一条完整数据
	 * 用通用查询方式配置条件
	 * @param tableName
	 * @param idField
	 * @param idValue
	 */
	public Map<String,Object> selectById(String tableName,String idField,String idValue){
		QueryPage queryPage = new QueryPage();
		queryPage.addCondition(idField, idValue);//拼条件
		String sql = "select * from " + tableName ;
		queryPage.setQueryParam(sql);
		queryPage.setPage(1);
		queryPage.setRows(1);		
		return getSqlSession().selectOne(METHOD_SELECT_ONE, queryPage);
	}
}
