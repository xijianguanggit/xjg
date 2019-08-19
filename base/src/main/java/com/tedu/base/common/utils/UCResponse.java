package com.tedu.base.common.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * UC用户中心接口
 * @author wangdanfeng
 *
 */
public class UCResponse implements Serializable {
	public String responseid;
	public String code = "0";
	public String msg = "";	
	private String userCode;
	private String userLastLoginIp;
	private String userLastLoginTime;
	private Map<String,Object> body = new HashMap();
	public UCResponse(){
		responseid = TokenUtils.genUUID();
	}

	public String getResponseid() {
		return responseid;
	}

	public void setResponseid(String responseid) {
		this.responseid = responseid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getBody() {
		return body;
	}
	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserLastLoginIp() {
		return userLastLoginIp;
	}

	public void setUserLastLoginIp(String userLastLoginIp) {
		this.userLastLoginIp = userLastLoginIp;
	}

	public String getUserLastLoginTime() {
		return userLastLoginTime;
	}

	public void setUserLastLoginTime(String userLastLoginTime) {
		this.userLastLoginTime = userLastLoginTime;
	}
	
}
