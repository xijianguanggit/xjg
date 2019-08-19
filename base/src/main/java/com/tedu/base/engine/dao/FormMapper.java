package com.tedu.base.engine.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Repository;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.error.ValidException;
import com.tedu.base.common.model.FieldCondition;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.BasicFormModel;
import com.tedu.base.engine.model.ConstraintsModel;
import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.model.FormModel;
import com.tedu.base.engine.model.UniqueModel;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.TemplateUtil;
import com.tedu.base.engine.util.UniqueValidator;
import com.tedu.base.initial.model.xml.ui.FormConstants;

import freemarker.template.Template;
@Repository("simpleDao")
public class FormMapper extends SqlSessionDaoSupport {
	public final Logger log = Logger.getLogger(this.getClass());
	private static final String METHOD_INSERT = "mapInsert";
	private static final String METHOD_UPDATE = "mapUpdate";
	private static final String METHOD_SELECT = "mapSelect";
	private static final String METHOD_DELETE = "deleteById";

	private static final String METHOD_QUERY = "query";
	private static final String METHOD_SELECT_ONE = "selectOne";
	
	@Resource
	TemplateUtil templateUtil;
	
	/**
	 * 
	 * In a DAO implementation use SqlSessionTemplate instead of SqlSessionDaoSupport.
	 * Inject bean db1SqlSessionTemplate or db2SqlSessionTemplate.
	 * When extending SqlSessionDaoSupport the context Spring does not know that you use SqlSession.
	 */
	 @Autowired
	 public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate){
		 super.setSqlSessionTemplate(sqlSessionTemplate);
	 }
	 
	/**
	 * 通用查询 ，表格数据源解决方案
	 * 前端使用Query.js调用
	 * 根据sqlId获取SQL配置，在模板中执行。
	 * 拦截器在传参是QueryPage时自动加上分页特性
	 * @param queryPage
	 * @return
	 */
	public List<Map<String,Object>> query(QueryPage queryPage){
		String sqlId = queryPage.getQueryParam();
		Template sqlTemplate = templateUtil.getSqlTemplate(sqlId);
		if(sqlTemplate == null){
			log.error("can't find sqlid " + sqlId);
			return Collections.emptyList();
		}
		String sql = sqlTemplate.toString();
		queryPage.setQueryParam(sql);
		//sql及参数
		FormLogger.logJDBC(sql, 
				queryPage.getParams().stream().
				map(FieldCondition::toString ).collect(Collectors.joining(","))+"\n"+
				queryPage.getModel());
		
		Map<String,Object> session = new HashMap<>();
		session.put(FormConstants.AVIATOR_ENV_SESSION_USERINFO, SessionUtils.getUserInfo());
		queryPage.getData().put(FormConstants.AVIATOR_ENV_SESSION, session);
		try{
			return getSqlSession().selectList(METHOD_QUERY, queryPage);
		}catch(Exception e){
			FormLogger.logFlow(String.format("[Query] %s => 执行SQL{%s}失败 [%s]",
					SessionUtils.getSessionId(),
					sqlId,
					e.getMessage()), FormLogger.LOG_TYPE_ERROR,e);
		}
		return Collections.emptyList();
	}
	
	/**
	 * 为避免影响，不和QueryById共用逻辑，一般用于复杂需转义的SQL
	 * queryParam为SQL语句内容
	 * @param queryPage
	 * @return
	 */
	public List<Map<String,Object>> queryBySQL(QueryPage queryPage){
		//sql及参数
		FormLogger.logJDBC(queryPage.getQueryParam(), 
				queryPage.getParams().stream().
				map(FieldCondition::toString ).collect(Collectors.joining(","))+"\n"+
				queryPage.getModel());
		
		Map<String,Object> session = new HashMap<>();
		session.put(FormConstants.AVIATOR_ENV_SESSION_USERINFO, SessionUtils.getUserInfo());
		queryPage.getData().put(FormConstants.AVIATOR_ENV_SESSION, session);
		try{
			return getSqlSession().selectList(METHOD_QUERY, queryPage);
		}catch(Exception e){
			FormLogger.logFlow(String.format("[Query] %s => 执行SQL{%s}失败 [%s]",
					SessionUtils.getSessionId(),
					queryPage.getQueryParam(),
					e.getMessage()), FormLogger.LOG_TYPE_ERROR,e);
		}
		return Collections.emptyList();
	}
	
	/**
	 * @param queryPage
	 * @return
	 */
	public Map<String,Object> selectOne(QueryPage queryPage){
		String sqlId = queryPage.getQueryParam();
		String sql = templateUtil.getSql(sqlId);
		queryPage.setQueryParam(sql);
		queryPage.setPage(1);
		queryPage.setRows(1);
		return getSqlSession().selectOne(METHOD_SELECT_ONE, queryPage);
	}
	
	/**
	 * 
	 * @param modelMap
	 * @return
	 */
	public int insert(BasicFormModel formModel){
		try{
			return getSqlSession().insert(METHOD_INSERT, formModel);
		}catch(org.springframework.dao.DuplicateKeyException e){
			throw new ServiceException(ErrorCode.SQL_DUPLICATE_KEY,e.getMessage());//已存在
		}catch(Exception e){
			throw new ServiceException(ErrorCode.SQL_PREPARE_FAILED,e.getMessage());
//            FormLogger.logFlow(String.format("insert失败 {%s}",e.getMessage()), FormLogger.LOG_TYPE_ERROR);
//			return -1;
		}
	}
	
	/**
	 * 事物?
	 * @param s
	 * @param formModel
	 * @return
	 */
	public int tInsert(SqlSession s,BasicFormModel formModel){
		return s.insert(METHOD_INSERT, formModel);
	}
	
	/**
	 * 实现了updateSql方法的model
	 * @param 
	 * @return
	 */
	public int update(BasicFormModel formModel){
		int n = -1;
		try{
			return getSqlSession().update(METHOD_UPDATE, formModel);
		}catch(org.springframework.dao.DuplicateKeyException e){
			throw new ServiceException(ErrorCode.SQL_DUPLICATE_KEY,e.getMessage());//已存在
		}catch(UncategorizedSQLException e){
			FormLogger.logFlow(String.format("可能是配置或表结构和配置不一致引起的update失败 ::%n{%s}",
					e.getMessage()), FormLogger.LOG_TYPE_ERROR);
		}catch(Exception e){
			FormLogger.logFlow(String.format("update失败 {%s}",e.getMessage()), FormLogger.LOG_TYPE_ERROR);
		}
		return n;
	}
	
	/**
	 * 实现了deleteSql方法的model
	 * @param formModel
	 * @return
	 */
	public int delete(FormModel formModel){
		return getSqlSession().delete(METHOD_DELETE, formModel);
	}
	
	/**
	 * 拼凑唯一性检测SQL
	 * @param formModel
	 * @return
	 */
	public void checkUnique(FormModel formModel){
		List<UniqueModel> lstUnique = UniqueValidator.validate(formModel);//检验配置并构造检验SQL
		if(lstUnique==null || lstUnique.isEmpty()) return;
		//循环执行select语句，以执行返回结果为零为continue条件
		for(UniqueModel uniqueModel:lstUnique){
			formModel.setSelectSql(uniqueModel.getSql());
			Map<String,Object> data = getSqlSession().selectOne(METHOD_SELECT, formModel);
			if(data != null  ) throw new ValidException(ErrorCode.SQL_UNIQUE_KEY_VIOLATION,
					uniqueModel);//已存在
		}
	}
	
	/**
	 * 外键检验
	 * @param formModel
	 */
	public void checkConstraints(List<ConstraintsModel> lstModel){
		if(lstModel!=null && !lstModel.isEmpty()){
			//循环执行select语句，以执行返回结果为零为continue条件
			for(ConstraintsModel constraints:lstModel){
				Object data = getSqlSession().selectOne(METHOD_SELECT, constraints);
				if(data != null) {
					throw new ValidException(ErrorCode.SQL_CONSTRAINTS_VIOLATION,
							constraints.getField(),constraints.getMsg());
				}
			}
		}
	}
	/**
	 * 级联删除
	 * @param formModel
	 */
	public void deleteCascade(List<ConstraintsModel> lstModel){
		if(lstModel!=null && !lstModel.isEmpty()){
			//循环执行select语句，以执行返回结果为零为continue条件
			for(ConstraintsModel cascade:lstModel){
				int ret = getSqlSession().delete(METHOD_DELETE, cascade);
				FormLogger.logFlow(String.format("级联删除 {%s} {%s} 条", cascade.getTable(),ret), FormLogger.LOG_TYPE_DEBUG);
			}
		}
	}

	/**
	 * 按ID获取数据
	 * @param tableName
	 * @param idField
	 * @param idValue
	 */
	public Map<String,Object> selectById(String tableName,String idField,String idValue){
		getSqlSession().clearCache();
		QueryPage queryPage = new QueryPage();
		queryPage.addCondition(idField, idValue);
		String sql = "select * from " + tableName ;
		queryPage.setQueryParam(sql);
		return getSqlSession().selectOne(METHOD_SELECT_ONE, queryPage);
	}
	
	/**
	 * 增删改自定义SQL执行
	 * 多语句之间分号隔开 ,
	 * @param formModel
	 * @return
	 */
	public int saveCustom(CustomFormModel formModel){
		int ret = -1;
		if(formModel == null){
			throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION,
					"saveCustom.formModel is null ");
		}
		Template sqlTemplate = templateUtil.getSqlTemplate(formModel.getSqlId());
		try {
			if(sqlTemplate == null) {
				throw new ServiceException(ErrorCode.TEMPLATE_NOT_EXISTS,formModel.getSqlId());
			}
			
			Map<String,Object> session = new HashMap<>();
			session.put(FormConstants.AVIATOR_ENV_SESSION_USERINFO, SessionUtils.getUserInfo());
			formModel.getData().put(FormConstants.AVIATOR_ENV_SESSION, session);
			String sqls = sqlTemplate.toString() ;
			
			String[] arrSql = sqls.split(";");
			int len = arrSql.length;
			formModel.setResultArray(new Object[len]);

			String strSQL = "";
			String sql = "";
			for(int i=0;i<len;i++){
				sql = arrSql[i];
				if(sql.trim().isEmpty()) continue;
				
				strSQL = StringUtils.trim(sql);//去掉不可见
				if(strSQL.toLowerCase().contains("insert")){
					formModel.setInsertSql(sql);//这里如果是多语句
					ret = getSqlSession().update(METHOD_INSERT, formModel);
					formModel.getResultArray()[i] = ret ;
				}else if(strSQL.toLowerCase().contains("update")||sql.toLowerCase().contains("delete")){
					formModel.setUpdateSql(sql);//这里如果是多语句
					ret = getSqlSession().update(METHOD_UPDATE, formModel);
					formModel.getResultArray()[i] = ret ;
				}else{
					formModel.setSelectSql(sql);
					Map<String,Object> map =  getSqlSession().selectOne(METHOD_SELECT, formModel);
					formModel.getResultArray()[i] = map ;
					if (map != null) {
						ret = 0;
					}
				}
			}
		} catch (Exception e) {
			throw new ServiceException(ErrorCode.SQL_PREPARE_FAILED,
					"请检查sql中的参数是否在model中存在"+e.getMessage());
		}
		return ret;
	}
}
