package com.tedu.base.common.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tedu.base.engine.util.FormUtil;

/**
 * UC用户中心接口
 * @author wangdanfeng
 *
 */
public class UCRequest implements Serializable{
	public String requestid;
	public String clientid;
	public String clientver;
	public String version;
	public String terminal;
	public String time;
	public String appcode;
	private String sign ;
	private Map<String,Object> body = new HashMap();
	public UCRequest(){
		requestid = TokenUtils.genUUID();
		clientid = "1.0";
		clientver = "12.1";
		version = "1.2";
		terminal = "pc";
		time = DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24,new Date());
		appcode = "crm";
	}
	public String getRequestid() {
		return requestid;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public String getClientver() {
		return clientver;
	}
	public void setClientver(String clientver) {
		this.clientver = clientver;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public Map<String, Object> getBody() {
		return body;
	}
	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String toString(){
		return FormUtil.toJsonString(this);
	}
}
