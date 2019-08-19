package com.tedu.base.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.engine.model.TreeNode;
import com.tedu.base.initial.model.xml.ui.FormConstants;
/**
 * Session工具类
 * @author xijianguang
 *
 */
@Component
public class SessionUtils {
//	
//	public RedisService getRedisService() {
//		return redisService;
//	}
//	public void setRedisService(RedisService redisService) {
//		this.redisService = redisService;
//	}
	
	public static SessionUtils getInstance(){
		return sessionUtils;
	}
	private static SessionUtils sessionUtils;
	@PostConstruct
	public void init() {
		sessionUtils = this;
//		sessionUtils.redisService = this.redisService;
	}
	
	/**
	 * 输出日志用
	 * @return
	 */
	public  static String getSessionId(){
		Session session = getSession();
		if(session==null || session.getId()==null){
			return "System";
		}else{
			return ObjectUtils.toString(session.getId());
		}
	}

	/**
	 * 获取当前用户Session
	 * 
	 */
	public static Session getSession() {
        Subject currentUser = ThreadContext.getSubject();
        if(currentUser==null)
        	return null;
		return currentUser.getSession();
	}
	/**
	 * 获取Session中key属性Session
	 *  @author xijianguang
	 */
	public static Object getAttrbute(String key) {
		if(StringUtils.isEmpty(key))
			return null;
		if(getSession()==null)
			return null;
		return getSession().getAttribute(key);
	}
	/**
	 * 获取Session中key属性Session返回string
	 *  @author xijianguang
	 */
	public static String getAttrbuteStr(String key) {
		if(getAttrbute(key)==null)
			return null;
		return getAttrbute(key).toString();
	}
	/**
	 * 添加Session
	 * @author xijianguang
	 */
	public static void setAttrbute(String key, Object val) {
		if(StringUtils.isEmpty(key))
			return ;
		getSession().setAttribute(key, val);
	}
	/**
	 * 移除key属性Session
	 * @author xijianguang
	 */
	public static void removeAttrbute(String key) {
		if(StringUtils.isEmpty(key))
			return ;
		getSession().removeAttribute(key);
	}
	/**
	 * 移除所有Session
	 * 
	 */
	public static void removeAll() {
		SecurityUtils.getSubject().logout();
	}
	/**
	 * 取得用户登录信息
	 * @author xijianguang
	 */
	public static UserModel getUserInfo() {
		if(getSession()!=null && getSession().getAttribute(ConstantUtil.USER_INFO)!=null)
			return (UserModel)getSession().getAttribute(ConstantUtil.USER_INFO);
		else
			return null;
	}
	/**
	 * 取得用户登录信息
	 * @author xijianguang
	 */
	public static Map<String, Object> getExtInfo() {
		if(getSession()!=null && getSession().getAttribute(ConstantUtil.USER_INFO)!=null)
			return (Map<String, Object>)getSession().getAttribute(ConstantUtil.EXT_INFO);
		else
			return null;
	}
	
	public static String getServerletContext(HttpServletRequest request, String path){
		return request.getSession().getServletContext().getRealPath(path);
	}

	
	/**
	 * UI构造时的editMode需要记录在session中用于其他逻辑解析表达式时使用
	 * @param ui
	 * @param editMode
	 */
	public static void setWindowMode(String uiId,String editMode) {
		if(getSession()==null){
			//throw
			return;
		}
		if(getSession().getAttribute(FormConstants.AVIATOR_ENV_MODE)==null){
			getSession().setAttribute(FormConstants.AVIATOR_ENV_MODE, new HashMap<String,String>());
		}
		((Map<String,String>)getSession().getAttribute(FormConstants.AVIATOR_ENV_MODE)).put(uiId, editMode);
	}
	
	public static String getWindowMode(String uiId) {
		if(getSession()!=null && getSession().getAttribute(FormConstants.AVIATOR_ENV_MODE)!=null)
			return ((Map<String,String>)getSession().getAttribute(FormConstants.AVIATOR_ENV_MODE)).get(uiId);
		else
			return null;
	}
	
	public static void setACL(Map<String,String> acl){
		if(getSession()==null){
			//throw
			return;
		}
		getSession().setAttribute(FormConstants.SESSION_AUTH_CONTROL,acl);
	}
	
	public static boolean isAccessibleControl(String authKey){
		if(getSession()==null){
			//throw
			return false;
		}
		Map<String,String> map = (Map<String,String>)(getSession().getAttribute(FormConstants.SESSION_AUTH_CONTROL));
		String flag = map==null?null:map.get(authKey) ;
		return (flag==null || !flag.equals("-1"));//null表示非授权组件,任何人都可以访问;非空表示授权组件且当前用户有权限
	}

	/**
	 * 测试open接口阶段先return true
	 * @param url
	 * @return
	 */
	public static boolean isAccessibleUrl(String url){
		Map<String,String> viewAction = (Map<String,String>)(getSession().getAttribute(FormConstants.SESSION_AUTH_URL));
 		return (SessionUtils.getUserInfo()!=null && SessionUtils.getUserInfo().isAdminRole()) || (viewAction!=null && viewAction.get(url)!=null) ;
	}
	
	public static String getUrlDataAuth(String url){
		Map<String,String> viewAction = (Map<String,String>)(getSession().getAttribute(FormConstants.SESSION_AUTH_URL));
 		return (SessionUtils.getUserInfo()!=null && SessionUtils.getUserInfo().isAdminRole())?"":
 			viewAction==null?"":viewAction.get(url) ;
	}
	
	public static void setAccessibleUrl(List<Map<String, Object>> list) {
		if(list == null) return;
		
		Map<String,String> viewAction = new HashMap<>();
		list.forEach(e->viewAction.put(ObjectUtils.toString(e.get("url")),ObjectUtils.toString(e.get("dataAuth"))));
		getSession().setAttribute(FormConstants.SESSION_AUTH_URL,viewAction);
	}
	
	public static void setUINode(TreeNode node){
		Map<String,TreeNode> mapPath;
		if(getSession().getAttribute(FormConstants.SESSION_UINODE)==null){
			mapPath = new HashMap<String,TreeNode>();
		}else{
			mapPath = (Map<String,TreeNode>)getSession().getAttribute(FormConstants.SESSION_UINODE);
		}
		mapPath.put(node.getId(), node);
		getSession().setAttribute(FormConstants.SESSION_UINODE,mapPath);
	}
	
	public static TreeNode getUINode(String uiId){
		if(getSession().getAttribute(FormConstants.SESSION_UINODE)==null){
			return null;
		}
		return ((Map<String,TreeNode>)(getSession().getAttribute(FormConstants.SESSION_UINODE))).get(uiId);
	}
	
	/**
	 * 用于存放基于UI的参数，通常来自外部开放接口的请求
	 * @param key
	 * @param val
	 */
	public static void setParameter(String key,Object val){
		Map<String,Object> mapParameter = (Map<String,Object>)(getSession().getAttribute(FormConstants.SESSION_PARAMETER));
		if(mapParameter==null){
			mapParameter = new HashMap<String,Object>();
		}
		mapParameter.put(key, val);
		getSession().setAttribute(FormConstants.SESSION_PARAMETER,mapParameter);
	}
	
	public static Map<String,Object> getParameter(){
		Map<String,Object> mapParameter = (Map<String,Object>)(getSession().getAttribute(FormConstants.SESSION_PARAMETER));
		if(mapParameter==null){
			mapParameter = new HashMap<String,Object>();
		}
		getSession().setAttribute(FormConstants.SESSION_PARAMETER,mapParameter);
		return mapParameter;
	}
}
