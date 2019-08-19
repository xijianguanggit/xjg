package com.tedu.base.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.impl.RestService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.JsonUtil;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.task.SpringUtils;

/**
 * 
 * 
 * 登录拦截器
 * 
 * @author xijianguang
 */
public class PlatformLoginFilter implements Filter {

	private String[] includeUrls;

	private String[] excludeUrls;
	
	private String[] redirectUrls; 

	private String defaultLoginPage;

	// 日志记录器
	public final Logger log = Logger.getLogger(this.getClass());

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if (hasInIncludes(request)) {
			if (!hasInExcludes(request)) {
			    ContextUtils.saveShareRequest(request);
			    ContextUtils.saveRedirectRequest(request,hasInRedirect(request));
			    if(ContextUtils.isPublicRequest(request)){//使用token鉴权的无态用户
			    	if(checkResource(request,resp)){
			    		chain.doFilter(req, resp);
			    	}
			    }else{ //需要走登录页面
			    	rememberLastUrl(request);
					if (SessionUtils.getAttrbute(ConstantUtil.USER_INFO) != null) {
						chain.doFilter(req, resp);
					} else {
						forwardDefaultPage(request, response);//登录拦截
					}
			    }
			} else {
				chain.doFilter(req, resp);
			}
		} else {
			chain.doFilter(req, resp);
		}
	}

	/**
	 * 允许携带ID参数.
	 * 用于UI表单定位数据
	 * @param request
	 */
	private void rememberLastUrl(HttpServletRequest request) {
		String url = request.getRequestURI().substring(request.getContextPath().length());
		if(FormConfiguration.isUIUrl(url) && FormConfiguration.isOuterAccess(request)) {
			SessionUtils.setAttrbute(FormConstants.SESSION_LAST_URL, url);
			SessionUtils.setAttrbute(FormConstants.SESSION_LAST_URL_PARAM_ID, request.getParameter("id"));
			FormLogger.logFlow("记录登录前访问的url", FormLogger.LOG_TYPE_DEBUG);
		}
	}
    /**
     * token拦截器需要将所有可访问资源初始在内存中
     * open/login和gettoken接口走白名单
     * 在session拦截器中遇到/open资源时，将携带的参数token取出，在redis中检验是否存在，若存在
     * @throws IOException 
     */
    private boolean checkResource(ServletRequest req,ServletResponse resp) throws IOException {
    	String url = ContextUtils.getSourceUrl((HttpServletRequest)req);
    	String userToken = ObjectUtils.toString(req.getParameter("token"));

    	if(url.indexOf("/open/")>=0){
    		FormLogger.logToken("checkResource:skip  open interface " + url);
    		UserModel userModel = new UserModel();
    		String userId = "-1";
    		if(userId!=null){
	    		userModel.setUserId(Long.parseLong(userId));
	    		userModel.setRoleName(FormConstants.ROLE_OPEN);
	    		SessionUtils.getSession().setAttribute(ConstantUtil.USER_INFO, userModel);
	    		FormLogger.logToken("create session for token [" + userToken + "]" );
    		}
    		return true;
    	}
    	
    	RestService restService = SpringUtils.getBean(RestService.class);


    	//根据token获取缓存中对应用户的可访问权限，以确定是否可访问
    	///ui/frmRoleList
    	if(url.endsWith("/login") || url.endsWith("/gettoken")){
    		return true;
    	}
    	if(userToken.isEmpty()) {
    		FormLogger.logToken("没有携带token的外部请求 " + url);
    		return false;
    	}
    	//如果有token,在redis中检测是否存在(类似session过期)，如果存在，则构造session
    	boolean isAuthed = restService.checkToken(userToken);
    	if(!isAuthed){//session已过期
			FormLogger.logToken("PlatformLoginFilter:认证失败 " + url + "::" + userToken);
			((HttpServletResponse)resp).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return false;
    	}
    	if(SessionUtils.getSession().getAttribute(ConstantUtil.USER_INFO)==null){
    		FormLogger.logToken("session user is null  [" + userToken + "]" );  
//    		restService.getUserIdByToken(userToken);
    		UserModel userModel = new UserModel();
    		String userId = restService.getUserIdByToken(userToken);
    		if(userId!=null){
	    		userModel.setUserId(Long.parseLong(userId));
	    		userModel.setRoleName(FormConstants.ROLE_ADMIN);
	    		SessionUtils.getSession().setAttribute(ConstantUtil.USER_INFO, userModel);
	    		FormLogger.logToken("create session for token [" + userToken + "]" );
    		}else{
    			FormLogger.logToken("external login expired for token [" + userToken + "]" );
    		}
    	}else{
    		FormLogger.logToken("session exists [" + userToken + "]");  
    	}
    	return true;
    }
    
	public boolean hasLogin(HttpServletRequest request) {
		return false;
	}

	public void init(FilterConfig filterConfig) throws ServletException {      
		String includeUrls = filterConfig.getInitParameter("includeUrls");
		this.includeUrls = toArray(includeUrls);
		String excludeUrls = filterConfig.getInitParameter("excludeUrls");
		this.excludeUrls = toArray(excludeUrls);
		
		
		String strRedirectUrls = ObjectUtils.toString(filterConfig.getInitParameter("redirectUrls"));
		this.redirectUrls = toArray(strRedirectUrls);
		
		this.defaultLoginPage = filterConfig.getInitParameter("defaultLoginPage");
	}

	private boolean hasInIncludes(HttpServletRequest request) {
		String url = getRequestUrl(request);
		if (includeUrls != null) {
			for (String str : includeUrls) {
				if (url.matches(str)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasInExcludes(HttpServletRequest request) {
		String url = getRequestUrl(request);
		if (excludeUrls != null) {
			for (String str : excludeUrls) {
				if (url.matches(str)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasInRedirect(HttpServletRequest request) {
		String url = getRequestUrl(request);
		if (redirectUrls != null) {
			for (String str : redirectUrls) {
				if (url.matches(str)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private String getRequestUrl(HttpServletRequest request) {
		String contextPath = request.getContextPath(), 
				requestUri = request.getRequestURI(),
				requestUrl = StringUtils.replace(requestUri, contextPath, "", 1);
		return requestUrl;
	}

	/**
	 * 来自app或ajax请求，session失效时返回199错误
	 * 否则回到登录页
	 * @param request
	 * @param response
	 */
	private void forwardDefaultPage(HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath();
		try {
			if(!ObjectUtils.toString(request.getParameter("app")).isEmpty() || 
					request.getHeader("x-requested-with")!=null){
				java.io.PrintWriter out = response.getWriter();
				FormEngineResponse res = new FormEngineResponse("过期");
				res.setCode(ErrorCode.SESSION_TIMEOUT.getCode());
				out.print(JsonUtil.objectToJson(res));
				out.flush();//加上这个就会在数据前自动加上大小及最后一个0
				out.close();
			} else{
				response.sendRedirect(contextPath + defaultLoginPage);
			} 
		} catch (Exception e) {
			log.error("context", e);
		}
	}

	private String[] toArray(String str) {
		return str.replaceAll("\\s", "").split(",");
	}

	public void destroy() {
	}

}
