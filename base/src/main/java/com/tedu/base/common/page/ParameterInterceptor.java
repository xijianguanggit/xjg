package com.tedu.base.common.page;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.log4j.Logger;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;


//@Intercepts(
//	@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) )
public class ParameterInterceptor implements Interceptor {

	
	// 日志记录器
	public final Logger log = Logger.getLogger(this.getClass());

	/**
	 * 拦截后要执行的方法
	 */
	public Object intercept(Invocation invocation) throws Throwable {
//		((com.tedu.base.auth.login.model.UserModel)invocation.getArgs()[1]).setUpdateTime(new Date())
		if (invocation.getArgs().length > 1) {  
			Date date = getSysDate(invocation);
			UserModel userModel = SessionUtils.getUserInfo();
			if(userModel!=null){
				Object parameter = invocation.getArgs()[1];
				Method setCreateTime = getDeclaredMethod(parameter, "setCreateTime", Date.class);
				Method setCreateBy = getDeclaredMethod(parameter, "setCreateBy", Long.class);
				Method setUpdateTime = getDeclaredMethod(parameter, "setUpdateTime", Date.class);
				Method setUpdateBy = getDeclaredMethod(parameter, "setUpdateBy", Long.class);
				if (setCreateTime != null)
					setCreateTime.invoke(parameter, date);
				if (setCreateBy != null)
					setCreateBy.invoke(parameter, userModel.getEmpId());
				if (setUpdateBy != null)
					setUpdateBy.invoke(parameter, userModel.getEmpId());
				if (setUpdateTime != null)
					setUpdateTime.invoke(parameter, date);
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
	private Date getSysDate(Invocation invocation) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Date date = new Date();
		try {
			pstmt = ((Connection) invocation.getArgs()[0]).prepareStatement("SELECT SYSDATE()");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				date = DateUtils.getStrToDate("yyyy-MM-dd hh:mm:ss", rs.getString(1));
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
			return date;
		}
	}
	/**
	 * 拦截器对应的封装原始对象的方法
	 */
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}


	/**
	 * 设置注册拦截器时设定的属性
	 */
	public void setProperties(Properties p) {

	}

}