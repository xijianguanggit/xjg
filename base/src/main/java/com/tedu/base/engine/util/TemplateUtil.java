package com.tedu.base.engine.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;

import freemarker.template.Template;

/**
 * 利用applicationcontext.xml中配置的fremarker config实例，和模板引擎工具类，解析指定名的模板 
 * @author wangdanfeng
 *
 */
@Component
public class TemplateUtil {
	@Autowired
	FreeMarkerConfigurer  configure;
	private static String SQL_SUFFIX = ".sql.ftl";
	/**
	 * sql模板加后缀
	 * 目前不需解析任何参数，仅获取模板内容
	 * @param templateName
	 * @param model
	 * @return
	 */
	public String getSql(String templateName){
		String sql =  getContent(templateName + SQL_SUFFIX,null);
		if(sql == null || sql.isEmpty()) {
			throw new ServiceException(ErrorCode.TEMPLATE_PARSING_FAILED,templateName + SQL_SUFFIX);
		}
		return sql;
	}
	/**
	 * SQL中的标签不需要解析
	 * @param templateName
	 * @return
	 */
	public Template getSqlTemplate(String templateName){
		try {
			return configure.getConfiguration().getTemplate(templateName + SQL_SUFFIX);
		} catch (Exception ex) {
            FormLogger.logFlow(String.format("getSqlTemplate {%s}异常{%s}",templateName, ex.getMessage()), FormLogger.LOG_TYPE_WARN);
		}
		return null;
	}
	
	public String getContent(String templateName, Map<String, Object> model){
		try {
			Template t = configure.getConfiguration().getTemplate(templateName);
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		} catch (Exception ex) {
			FormLogger.logFlow(String.format("解析模板失败{%s} {%s}", templateName,ex.getMessage()), FormLogger.LOG_TYPE_ERROR);
		}
		return null;
	}
	
	public String getContent(Template t, Map<String, Object> model){
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		} catch (Exception ex) {
			FormLogger.logFlow(String.format("解析模板失败{%s} {%s}", t,ex.getMessage()), FormLogger.LOG_TYPE_ERROR);
		}
		return null;
	}
}
