package com.tedu.base.auth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tedu.base.auth.login.service.impl.RestService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.ServerTokenData;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.service.FormTokenService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.initial.model.xml.ui.FormConstants;

/**
 * view类型的请求时不验证token，只生成token
 * Token 拦截器
 * 生成token的部分涉及对response对象的改变，已用LogicAspect实现
 * <p/>
 */
public class FormTokenInterceptor extends HandlerInterceptorAdapter {
	@Resource
	private FormTokenService formTokenService;
	@Resource
	private FormService formService;
	@Resource
	private RestService restService;
	
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
    	String sourceUrl = getSourceUrl(request);
    	if(!sourceUrl.startsWith("/")) sourceUrl = "/" + sourceUrl;
    	String userToken = request.getParameter("token");
    	if(ContextUtils.isPublicRequest(request)){
    		FormLogger.logToken(String.format("url {%s} 是open资源",sourceUrl),FormLogger.LOG_TYPE_DEBUG);
    	}else if(SessionUtils.isAccessibleUrl(sourceUrl)){
    		FormLogger.logToken(String.format("url {%s} 在可访问资源表中",sourceUrl),FormLogger.LOG_TYPE_DEBUG);
    	}else{
        	validateRequestToken(request);
    	}
        return super.preHandle(request, response, handler);
    }
    
    private String getSourceUrl(HttpServletRequest request){
    	String url = request.getRequestURI();
    	String pureUrl = url.substring(request.getContextPath().length()+1);
    	if(pureUrl.lastIndexOf('?')>=0){
    		return pureUrl.substring(0,pureUrl.lastIndexOf('?'));
    	}
    	return pureUrl;
    }
    
	private void validateRequestToken(HttpServletRequest request){
		if (SessionUtils.getUserInfo() == null) {
			throw new ServiceException(ErrorCode.ACCESS_DENIED,"session用户信息为空");//session过期的异常提供一个
		}
		String token = ObjectUtils.toString(request.getHeader(FormConstants.REQUEST_TOKEN));
		if(token.isEmpty()){
			token = ObjectUtils.toString(request.getParameter(FormConstants.REQUEST_TOKEN));
		}
		String sourceUrl = getSourceUrl(request);
		if(token.isEmpty()){
			//transition第二层
			FormLogger.logFlow(String.format("请求 %s 时未携带token", sourceUrl), FormLogger.LOG_TYPE_ERROR);
			throw new ServiceException(ErrorCode.ILLEGAL_TOKEN,sourceUrl);
		}
		ServerTokenData tokenData = formTokenService.geToken(token);
		
		if(tokenData == null){
			FormLogger.logFlow(String.format("请求 %s 时携带的token %s 未找到", sourceUrl,token), FormLogger.LOG_TYPE_ERROR);
			throw new ServiceException(ErrorCode.ILLEGAL_TOKEN,sourceUrl);
		}
//		if(formTokenService.expiredToken(tokenData)){
//			FormLogger.logFlow("token已过期", token);
//			Map<String,Object> env = new HashMap<>();
//			env.putAll(request.getParameterMap());
//			Boolean ret = FormTokenUtil.isEnabledFlow(tokenData, env);
//			if(ret==null || !ret){
//				throw new ServiceException(ErrorCode.ILLEGAL_TOKEN,token+"已过期");
//			}
//			FormLogger.logFlow("过期token "+token, "已验证有效,继续使用");
//		}
	}
}
