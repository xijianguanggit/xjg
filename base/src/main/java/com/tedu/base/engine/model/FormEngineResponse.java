package com.tedu.base.engine.model;

import java.io.Serializable;

import com.tedu.base.common.utils.MD5Util;
import com.tedu.base.engine.exception.FormEngineException;

public class FormEngineResponse implements Serializable{
	String code = "0";//errorCode
	String msg = "";//errorMessage
	Object data;
	String method;
	ClientContext token;//初始值不要new,客户端会判断这个key来决定是否更新本地token
	String hmac = "";

	public FormEngineResponse(Object data){
		this.data = data;
	}
	
	/**
	 * 构造异常类的响应
	 * @param ex
	 */
	public FormEngineResponse(FormEngineException ex){
		setCode(ex.getErrorCode().getCode());
		setMsg(ex.getErrorCode().getMsg());
		this.data = "";
	}
	
	public ClientContext getToken() {
		return token;
	}
	public void setToken(ClientContext token) {
		this.token = token;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	//response的hmac值源于对整个对象的加密结果，用于客户端判断数据是否被篡改
	public String getHmac() {
		return MD5Util.MD5Encode(String.format("%s%s%s", data,code,msg));
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	
}


