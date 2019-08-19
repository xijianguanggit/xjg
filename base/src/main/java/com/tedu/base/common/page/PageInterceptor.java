package com.tedu.base.common.page;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.tedu.base.engine.util.FormLogger;

/**
 * 
 * 分页拦截器，用于拦截需要进行分页查询的操作，然后对其进行分页处理。 利用拦截器实现Mybatis分页的原理：
 * 要利用JDBC对数据库进行操作就必须要有一个对应的Statement对象，
 * Mybatis在执行Sql语句前就会产生一个包含Sql语句的Statement对象，而且对应的Sql语句
 * 是在Statement之前产生的，所以我们就可以在它生成Statement之前对用来生成Statement的Sql语句下手。
 * 在Mybatis中Statement语句是通过RoutingStatementHandler对象的
 * prepare方法生成的。所以利用拦截器实现Mybatis分页的一个思路就是拦截StatementHandler接口的prepare方法，
 * 然后在拦截器方法中把Sql语句改成对应的分页查询Sql语句，之后再调用
 * StatementHandler对象的prepare方法，即调用invocation.proceed()。
 * 对于分页而言，在拦截器里面我们还需要做的一个操作就是统计满足当前条件的记录一共有多少，这是通过获取到了原始的Sql语句后，
 * 把它改为对应的统计语句再利用Mybatis封装好的参数和设 置参数的功能把Sql语句中的参数进行替换，之后再执行查询记录数的Sql语句进行总记录数的统计。
 * 
 */

@Intercepts(
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) )
public class PageInterceptor implements Interceptor {

	private String dialect = "";// 数据库方言
	private String pageSqlId = ""; // mapper.xml中需要拦截的ID(正则匹配)
	// 日志记录器
	private static final Logger log = Logger.getLogger(PageInterceptor.class);

	/**
	 * 拦截后要执行的方法
	 */
	public Object intercept(Invocation invocation) throws Throwable {
		if (invocation.getTarget() instanceof RoutingStatementHandler) {
			RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();
			StatementHandler delegate = (StatementHandler) MyReflectUtil.getFieldValue(statementHandler, "delegate");
			BoundSql boundSql = delegate.getBoundSql();
			Object obj = boundSql.getParameterObject();
			String version = "";
			if (obj != null) {
				Field[] fields = obj.getClass().getDeclaredFields();
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].getName().equals("version")) {
						fields[i].setAccessible(true);
						version = fields[i].get(obj) == null ? "" : fields[i].get(obj).toString();
					}
				}
			}
			if (!"".equals(version)) {
				String sql = boundSql.getSql();
				StringBuffer sb = new StringBuffer(sql);
				sb.append(" and version='").append(version).append("'");
				MyReflectUtil.setFieldValue(boundSql, "sql", sb.toString());
			}

			// 通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
			MappedStatement mappedStatement = (MappedStatement) MyReflectUtil.getFieldValue(delegate,
					"mappedStatement");
			if (obj instanceof BasePage) {
				BasePage page = (BasePage) obj;
				// 拦截到的prepare方法参数是一个Connection对象
				Connection connection = (Connection) invocation.getArgs()[0];
				// 获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句
				String sql = boundSql.getSql();
				// 给当前的page参数对象设置总记录数
				Object parameterObj = boundSql.getParameterObject();
				if(((QueryPage)parameterObj).getPage()!=null)
					this.setTotalRecord(page, parameterObj, mappedStatement, connection);
				// 获取分页Sql语句
				String pageSql = this.getPageSql(page, sql);
				// 利用反射设置当前BoundSql对应的sql属性为我们建立好的分页Sql语句
				MyReflectUtil.setFieldValue(boundSql, "sql", pageSql);
			}
		}
		return invocation.proceed();
	}

	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
				return method;
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 拦截器对应的封装原始对象的方法
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 根据page对象获取对应的分页查询Sql语句，这里只做了两种数据库类型，Mysql和Oracle 其它的数据库都 没有进行分页
	 *
	 * @param page
	 *            分页对象
	 * @param sql
	 *            原sql语句
	 * @return
	 */
	private String getPageSql(BasePage page, String sql) {
		StringBuffer sqlBuffer = new StringBuffer(sql);
		return getMysqlPageSql(page, sqlBuffer);
	}

//	private Date getSysDate(Invocation invocation) throws Exception {
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Date date = new Date();
//		try {
//			pstmt = ((Connection) invocation.getArgs()[0]).prepareStatement("SELECT SYSDATE()");
//			rs = pstmt.executeQuery();
//			if (rs.next()) {
//				date = DateUtils.getStrToDate("yyyy-MM-dd hh:mm:ss", rs.getString(1));
//			}
//
//		} catch (SQLException e) {
//			log.error("context", e);
//		} finally {
//			try {
//				if (rs != null)
//					rs.close();
//				if (pstmt != null)
//					pstmt.close();
//			} catch (SQLException e) {
//				log.error("context", e);
//			}
//			return date;
//		}
//	}

	private void setWhere(QueryPage queryPage,StringBuffer sqlBuffer){
		String strWhere;
		if(queryPage.isOptimize()){
			strWhere = QueryUtil.getSqlFilter(queryPage);
			String whereTag = "${where}";
			int pos = sqlBuffer.indexOf(whereTag);
			if(pos>0){
				if(strWhere.isEmpty()) strWhere = " (1=1) ";
				sqlBuffer.replace(pos,pos+whereTag.length(), strWhere);
			}
		}else{
			strWhere = QueryUtil.getSqlWhere(queryPage);
			sqlBuffer.append(strWhere);
			if(!strWhere.isEmpty() && queryPage.getSqlId()!=null){//用于跟踪接口查询条件
				FormLogger.logFlow(queryPage.getSqlId() + ":::condition:::" + strWhere, FormLogger.LOG_TYPE_INFO);
			}
		}
	}
	
	/**
	 * 获取Mysql数据库的分页查询语句
	 * 
	 * @param page
	 *            分页对象
	 * @param sqlBuffer
	 *            包含原sql语句的StringBuffer对象
	 * @return Mysql数据库分页语句
	 */
	private String getMysqlPageSql(BasePage page, StringBuffer sqlBuffer) {
		if (page instanceof QueryPage) {
			QueryPage queryPage = (QueryPage) page;
			setWhere(queryPage,sqlBuffer);
		}
		// 计算第一条记录的位置，Mysql中记录的位置是从0开始的。
		if (!StringUtils.isEmpty(page.getSort()) && !StringUtils.isEmpty(page.getOrder())) {
			sqlBuffer.append(" order by ");
			String[] sort = page.getSort().split(",");
			String[] order = page.getOrder().split(",");
			for(int i=0;i<sort.length;i++){
				sqlBuffer.append(sort[i]);
				sqlBuffer.append(" ");
				sqlBuffer.append(order[i]);
				sqlBuffer.append(",");
			}
			sqlBuffer = new StringBuffer(sqlBuffer.substring(0, sqlBuffer.lastIndexOf(",")));
		}
		if(page.getPage() != null){
			int offset = (page.getPage() - 1) * page.getRows();
			sqlBuffer.append(" limit ").append(offset).append(",").append(page.getRows());
		}//根据需要分页
		return sqlBuffer.toString();
	}

	/**
	 * 给当前的参数对象page设置总记录数
	 *
	 * @param page
	 *            Mapper映射语句对应的参数对象
	 * @param mappedStatement
	 *            Mapper映射语句
	 * @param connection
	 *            当前的数据库连接
	 */
	private void setTotalRecord(BasePage page, Object parameterObject, MappedStatement mappedStatement,
			Connection connection) {
		//一页显示一条的查询，视为不分页
		if(page.getRows()==1) {
			page.setTotal(1L);
			page.setTotalRecord(1);
			return;
		}
		BoundSql boundSql = mappedStatement.getBoundSql(page);
		
		String sql = boundSql.getSql();
		String countSql = this.getCountSql(sql);
		if (page instanceof QueryPage) {
			StringBuffer stringBuff = new StringBuffer(countSql);
			setWhere((QueryPage) page,stringBuff);
			countSql = stringBuff.toString();
		}
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings,
				parameterObject);
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject,
				countBoundSql);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(countSql);
			// 通过parameterHandler给PreparedStatement对象设置参数
			parameterHandler.setParameters(pstmt);
			// 之后就是执行获取总记录数的Sql语句和获取结果了。
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int totalRecord = rs.getInt(1);
				// 给当前的参数page对象设置总记录数
				page.setTotalRecord(totalRecord);
			}

		} catch (SQLException e) {
			log.error("context", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				log.error("context", e);
			}
		}

	}

	/**
	 * 根据原Sql语句获取对应的查询总记录数的Sql语句
	 * 
	 * @param sql
	 * @return
	 */
	private String getCountSql(String sql) {
		return "select count(*) from (" + sql + ") countsql";
	}

	/**
	 * 设置注册拦截器时设定的属性
	 */
	public void setProperties(Properties p) {

	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getPageSqlId() {
		return pageSqlId;
	}

	public void setPageSqlId(String pageSqlId) {
		this.pageSqlId = pageSqlId;
	}

	/**
	 * 利用反射进行操作的一个工具类
	 *
	 */
	private static class MyReflectUtil {
		/**
		 * 利用反射获取指定对象的指定属性
		 * 
		 * @param obj
		 *            目标对象
		 * @param fieldName
		 *            目标属性
		 * @return 目标属性的值
		 * @throws IllegalAccessException
		 * @throws IllegalArgumentException
		 */
		public static Object getFieldValue(Object obj, String fieldName)
				throws IllegalArgumentException, IllegalAccessException {
			Object result = null;
			Field field = MyReflectUtil.getField(obj, fieldName);
			if (field != null) {
				field.setAccessible(true);
				result = field.get(obj);
			}
			return result;
		}

		/**
		 * 利用反射获取指定对象里面的指定属性
		 * 
		 * @param obj
		 *            目标对象
		 * @param fieldName
		 *            目标属性
		 * @return 目标字段
		 */
		private static Field getField(Object obj, String fieldName) {
			Field field = null;
			for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
				try {
					field = clazz.getDeclaredField(fieldName);
					break;
				} catch (NoSuchFieldException e) {
					// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
				}
			}
			return field;
		}

		/**
		 * 利用反射设置指定对象的指定属性为指定的值
		 * 
		 * @param obj
		 *            目标对象
		 * @param fieldName
		 *            目标属性
		 * @param fieldValue
		 *            目标值
		 */
		public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
			Field field = MyReflectUtil.getField(obj, fieldName);
			if (field != null) {
				try {
					field.setAccessible(true);
					field.set(obj, fieldValue);
				} catch (IllegalArgumentException e) {
					log.error(e.getMessage());
				} catch (IllegalAccessException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
}