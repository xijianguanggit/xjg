package com.tedu.base.auth.login.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.engine.model.FormEngineRequest;
import com.tedu.base.engine.model.FormEngineResponse;
/**
 * 登录service
 * @author xijianguang
 */
public interface LoginService {
	/**
	 * 查询用户密码
	 * @author xijianguang
	 */
	List<UserModel> getUserInfoByName(String userName);
	/**
	 * 查询用户密码（客户）
	 * @author xijianguang
	 */
	List<UserModel> getCustomUserInfoByName(String userName);
	/**
	 * 查询用户密码（客户）
	 * @author xijianguang
	 */
	List<UserModel> getCustomUserInfoByOpenId(String openId);
	/**
	 * 查询用户密码（会员）
	 * @author hejk
	 */
	List<UserModel> getAssociatorInfoByName(String userName);
	
	/**
	 * 查询用户菜单权限
	 * @author xijianguang
	 */
	List<Map<String, String>> getAuthorization(String userName, String menu);

	/**
	 * 查询所有的菜单
	 * @return
	 */
	List<Map<String, String>> getAllAuthorization();
	/**
	 * 更新密码错误次数
	 * @author xijianguang
	 */
	void updateWrongCount(UserModel userModel);
	
	public void updatePwd(UserModel user);
	
	public FormEngineResponse login(FormEngineRequest requestObj, HttpServletRequest request);
}
