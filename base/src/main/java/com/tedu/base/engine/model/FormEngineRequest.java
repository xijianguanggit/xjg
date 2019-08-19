package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.Map;

import com.tedu.base.common.page.QueryPage;
import com.tedu.base.engine.util.FormUtil;

public class FormEngineRequest implements Serializable{
	String app;//app name
	String ver;//version
	String cid;//client uuid
	String uid;//user id
	String wid;//window id
	String pid;//window parent id
	String time;//now
	String token;//目前没值
	String clientIp;
	Map<String,Object> data;
	String hmac;
	QueryPage query;
	
	public String toString(){
		return FormUtil.toJsonString(this);
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Map<String,Object> getData() {
		return data;
	}
	public void setData(Map<String,Object> data) {
		this.data = data;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public QueryPage getQuery() {
		return query;
	}
	public void setQuery(QueryPage query) {
		this.query = query;
	}

	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	
}
