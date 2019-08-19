package com.tedu.base.log.model;

import java.io.Serializable;
/**
 * 添加日志model
 * @author xijianguang
 */
public final class LogModel implements Serializable {
	private static final long serialVersionUID = -6932402953488259449L;
	private long sysLogId;
	// 用户id
	private long userId;
	// 员工id
	private long empId;
	// 资源地址
	private String url;
	//客户端ip
	private String clientIp;
	//agent
	private String userAgent;
	// 记录结果
	private String result;
	//服务器ip
	private String serverIp;
	//服务器端口
	private String serverPort;
	// 访问时长
	private Long cost;
	// 访问时长
	private String request;
	// 访问时长
	private String respones;
	
	public long getSysLogId() {
		return sysLogId;
	}
	public void setSysLogId(long sysLogId) {
		this.sysLogId = sysLogId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerPort() {
		return serverPort;
	}
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}
	public Long getCost() {
		return cost;
	}
	public void setCost(Long cost) {
		this.cost = cost;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getRespones() {
		return respones;
	}
	public void setRespones(String respones) {
		this.respones = respones;
	}
	
}
