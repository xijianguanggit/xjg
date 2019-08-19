package com.tedu.base.auth.login.dao;

import com.tedu.base.auth.login.model.UserModel;
import com.tedu.base.common.page.QueryPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户登录用DAO
 * @author xijianguang
 */
public interface LoginMysqlDao {
	/**
	 * 查询用户密码
	 * @author xijianguang
	 */
	public List<UserModel> getUserInfoByName(String userName);
	/**
	 * 查询用户密码（客户）
	 * @author xijianguang
	 */
	public List<UserModel> getCustomUserInfoByName(String userName);
	/**
	 * 查询用户密码（客户）
	 * @author xijianguang
	 */
	public List<UserModel> getCustomUserInfoByOpenId(String openId);
	/**
	 * 查询用户菜单权限
	 * @author xijianguang
	 */
	public List<Map<String, String>> getAuthorization(@Param("userName")String userName, @Param("menu")String menu);

	/**
	 * administrator
	 * @return
	 */
	public List<Map<String, String>> getAllAuthorization();
	/**
	 * 更新密码错误次数
	 * @author xijianguang
	 */
	public void updateWrongCount(UserModel userModel);
	/**
	 * 查询资源列表
	 * @author xijianguang
	 */
	public List<Map<String, String>> getResList(@Param("userName")String userName);
	
	//通用查询list，常用于表格表格数据
	List<Map<Object,Object>> query(QueryPage page);

	public void updatePwd(UserModel user);
	public List<UserModel> getAssociatorInfoByName(String userName);

}
