package com.tedu.base.engine.model;

import java.io.Serializable;

import com.tedu.base.initial.model.xml.ui.Procedure;

/**
 * 每个服务端请求的token 对应的数据
 * @author wangdanfeng
 *
 */
public class ServerTokenData implements Serializable{
	private String uiName;
	private String trigger;
	private String event;
	private String functionId;//具体logic在运行时刻的唯一id
	private Procedure procedure;
	private String logic;
	private String url;
	private String token;
	private String controlName;
	private Long createTime;
	private String flowId;//每个procedure所属的flow唯一标识 uuid
	private String flowName;//for log
	private boolean beginLogic = false;
	private boolean endLogic = false;
	private String uiId;
	
	public ServerTokenData(String uiName,String trigger,String event,Procedure procedure,String functionId,String logic){
		this.uiName = uiName;
		this.trigger = trigger;
		this.event = event;
		this.functionId = functionId;
		this.procedure = procedure;
		this.logic = logic;
	}
	
	public ServerTokenData(){}
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public Procedure getProcedure() {
		return procedure;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public String getLogic() {
		return logic;
	}
	public void setLogic(String logic) {
		this.logic = logic;
	}

	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}


	public String getFlowId() {
		return flowId;
	}


	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getFlowName() {
		return flowName;
	}


	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}


	public boolean isBeginLogic() {
		return beginLogic;
	}


	public void setBeginLogic(boolean beginLogic) {
		this.beginLogic = beginLogic;
	}


	public boolean isEndLogic() {
		return endLogic;
	}


	public void setEndLogic(boolean endLogic) {
		this.endLogic = endLogic;
	}

	public String getUiId() {
		return uiId;
	}

	public void setUiId(String uiId) {
		this.uiId = uiId;
	}
	
	
}
