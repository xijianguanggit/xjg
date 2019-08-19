package com.tedu.base.auth.login.service.impl;

import com.tedu.base.common.utils.UCRequest;

public interface RestService {
	//userCode 用于唯一编码
	public String setUserToken(String userId);
	public String getUserToken(String userId);
	public boolean checkToken(String userToken);
	
	public String getUser(String userName);//认证成功的userName对应的userCode
	public String setUser(String userId);//认证成功的userName对应的userCode，生成
    public String getUserIdByToken(String userToken);//根据token获取userCode
    public void initResource();
    public boolean checkSign(UCRequest request);
}
