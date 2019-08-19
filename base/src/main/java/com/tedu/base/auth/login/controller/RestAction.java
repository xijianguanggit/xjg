package com.tedu.base.auth.login.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tedu.base.auth.login.model.ShareModel;
import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.auth.login.service.LoginService;
import com.tedu.base.auth.login.service.impl.RestService;
import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.utils.ConstantUtil;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.common.utils.TokenUtils;
import com.tedu.base.common.utils.UCRequest;
import com.tedu.base.common.utils.UCResponse;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
import com.tedu.base.engine.util.FormConfiguration;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.engine.util.FormUtil;
import com.tedu.base.initial.model.xml.ui.UI;

/**
 * 基于UC提供的用户认证协议，将表单引擎功能提供外部访问入口
 * 使用方式
 * 1、登录 /open/login，返回成功继续调用open/gettoken，token在配置的有效期内有效
 * 2、http://我方服务地址/open/view?ui=frmStatement&token=token值&参数1=xxx&参数2=yyy
 *   参数将存储在session中，用户可以<b>session.parameter.UI名.参数名<b>方式在xml配置和sql中引用其值
 * 3、 
 * 
 * @author wangdanfeng
 *
 */
@Controller
public class RestAction {
	private static final String PARAM_USERID = "userId";

    @Resource
    private LoginService loginService;	
    
	@Autowired
	private RestService service;
    
    /**
     * 后台登录接口
     * 按照UC协议请求登录并返回 
     * 如果redis中存在此token对应的登录记录，则返回，否则模拟后台用户登录后返回
     * @deprecated
     * redis key:userName
     * redis val:userId
     * @param request
     * @param requestObj
     * @return
     */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/open/login")
	@ResponseBody
	public UCResponse login(HttpServletRequest request,@RequestBody UCRequest requestObj){
		UCResponse ucResponse = new UCResponse();
		//验签
		if(!service.checkSign(requestObj)){
			ucResponse.setCode(ErrorCode.INVALIDATE_FORM_DATA.getCode());
			ucResponse.setMsg(ErrorCode.INVALIDATE_FORM_DATA.getMsg());
			return ucResponse;
		}
		
		Map<String, Object> paramMap = requestObj.getBody();
		String loginName = ObjectUtils.toString(paramMap.get("loginName"));
		String userCode = service.getUser(loginName);
		String userId = "";
		if(userCode == null){
			//验证签名
			FormEngineRequest engineRequest = new FormEngineRequest();
			Map<String,Object> data = new HashMap();
			data.put("password", paramMap.get("password"));
			data.put("userName", loginName);
			data.put("validateCode", "");
			engineRequest.setData(data);
			//这里不一定和前端login一致的逻辑
			FormEngineResponse engineResponse = loginService.login(engineRequest, request);
			if(engineResponse.getCode().equals("0")){
				data = (Map<String, Object>)engineResponse.getData();
				ucResponse.setUserCode(ObjectUtils.toString(data.get(PARAM_USERID)));
				userId = ObjectUtils.toString(data.get(PARAM_USERID));
				
				userCode = service.setUser(userId);

				UserModel userModel = new UserModel();
				userModel.setUserId(Long.parseLong(userId));
				SessionUtils.setAttrbute(ConstantUtil.USER_INFO,userModel);
			}
			ucResponse.setCode(engineResponse.getCode());
			ucResponse.setMsg(engineResponse.getMsg());
		}
		ucResponse.setUserCode(userCode);
		ucResponse.setUserLastLoginTime(DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24,new Date()));
		ucResponse.setUserLastLoginIp(FormUtil.getClientIP(request));
		return ucResponse ;
	}
	
	
	/**
	 * 客户嵌入/rest/open?ui=xxx
	 * 我方验证token后重定向
	 * @param request
	 * @param requestObj
	 * @return
	 */
	@RequestMapping(value = "/open/view")
	public String open(HttpServletRequest request,Model model){
		String token = request.getParameter("token");
		String appid = request.getParameter("appid");
		FormLogger.logFlow("open view token =" + token + "[" + SessionUtils.getSessionId() + "]  " , FormLogger.LOG_TYPE_INFO);
		String ui = request.getParameter("ui");
		String type = ObjectUtils.toString(request.getParameter("type"));
		model.addAttribute("uiName", "/ui/"+ui);
		UI uiObj = FormConfiguration.getUI(ui);
		String title = "";
		if(uiObj!=null){
			title = uiObj.getTitle();
		}
		//取请求参数,可能每次都不同，存入参数
		Map<String,Object> uiParamMap = new HashMap<>();
		
		Enumeration e =request.getParameterNames();  
		while (e.hasMoreElements()) {  
			String paramName = (String) e.nextElement();  
			String paramValue = request.getParameter(paramName);  
			//形成键值对应的map  
			uiParamMap.put(paramName, paramValue);  
		 }  
		
		SessionUtils.setParameter(ui, uiParamMap);
		//这里的token认证
		ShareModel redirectModel = new ShareModel(title,ui,"");
		redirectModel.setUiType(type);//默认ui，也可以入参中指定为ue
		model.addAttribute("token",request.getParameter("token"));
		model.addAttribute("redirect",FormUtil.toJsonString(redirectModel));
		return "openFrame";
	}
	

	/**
	 * 根据userCode获取token
	 * 验证userCode为登录用户-->获取对应token-->若不存在则新建
	 * 若为空，则返回session失效错误码
	 * @param request
	 * @param ucRequest
	 * @return
	 */
	@RequestMapping(value = "/open/gettoken")
	@ResponseBody
	public UCResponse getToken(HttpServletRequest request,HttpServletResponse response,@RequestBody UCRequest requestObj){
		//check sign
		UCResponse ucResponse = new UCResponse();

		Map<String, Object> paramMap = requestObj.getBody();
		String userCode = ObjectUtils.toString(paramMap.get("userCode"));
		String token = service.getUserToken(userCode);
		Map<String, Object> retMap = new HashMap<>();//response body
		if(token == null || token.isEmpty()){
			token = service.setUserToken(userCode);
			FormLogger.logFlow("generate a token for sesssion [" + SessionUtils.getSessionId() + "]  " , FormLogger.LOG_TYPE_INFO);
		}
		retMap.put("token", token);
		ucResponse.setBody(retMap);
		return ucResponse;
	}

	@RequestMapping(value = "/m/view")
	public String mview(HttpServletRequest request,Model model){
		String token = request.getParameter("token");
		String appid = request.getParameter("appid");
		FormLogger.logFlow("open view token =" + token + "[" + SessionUtils.getSessionId() + "]  " , FormLogger.LOG_TYPE_INFO);
		String ui = request.getParameter("ui");
		String type = ObjectUtils.toString(request.getParameter("type"));
		model.addAttribute("uiName", "/ui/"+ui);
		UI uiObj = FormConfiguration.getUI(ui);
		String title = "";
		if(uiObj!=null){
			title = uiObj.getTitle();
		}
		//取请求参数,可能每次都不同，存入参数
		Map<String,Object> uiParamMap = new HashMap<>();
		
		Enumeration e =request.getParameterNames();  
		while (e.hasMoreElements()) {  
			String paramName = (String) e.nextElement();  
			String paramValue = request.getParameter(paramName);  
			//形成键值对应的map  
			uiParamMap.put(paramName, paramValue);  
		 }  
		SessionUtils.setParameter(ui, uiParamMap);
		String template = ObjectUtils.toString(uiObj.getTemplate());
		if(template.isEmpty()){
			return "openFrame";	
		}else{
			return template;
		}
		
	}
	/**
	 * 登录请求参数示例
	 * @param args
	 */
	public static void main(String[] args){
		System.out.println("token"+TokenUtils.generateToken());
	}
	
}
