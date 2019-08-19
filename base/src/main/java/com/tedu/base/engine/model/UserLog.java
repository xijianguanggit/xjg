package com.tedu.base.engine.model;

import java.io.Serializable;
import java.util.Date;
/**
 * 给用户看的记录行为的
 * 若里面涉及logic是save，要记录changeLog
 * @author wangdanfeng
 *
 */
public class UserLog implements Serializable,TableModel{
	private Long id;
	private Long userId;
	private Long empId;
	private String action;
	private String uiName;
	private String uiTitle;
	private String panelName;
	private String panelTitle;	
	private String controlName;
	private String controlTitle;
	private String flowId;
	private String sessionId;
	private Date createTime;
	private Long createBy;
	private String execResult;
	private String errorReason;
	private String clientIp;
	
	private String flow;
	private static final String TABLE = "t_user_log";
	private static String[] FIELDS = new String[]{
			"user_id",
			"emp_id",
			"action",
			"ui_name",
			"ui_title",
			"panel_name",
			"panel_title",
			"control_name",
			"control_title",
			"flow_id",
			"session_id",
			"create_time",
			"create_by",
			"exec_result",
			"error_reason",
			"client_ip"
	};
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	public String getUiTitle() {
		return uiTitle;
	}
	public void setUiTitle(String uiTitle) {
		this.uiTitle = uiTitle;
	}
	public String getControlTitle() {
		return controlTitle;
	}
	public void setControlTitle(String controlTitle) {
		this.controlTitle = controlTitle;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	@Override
	public String getTableName() {
		return TABLE;
	}
	@Override
	public String[] getFields() {
		return FIELDS;
	}
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String getPanelName() {
		return panelName;
	}
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}
	public String getPanelTitle() {
		return panelTitle;
	}
	public void setPanelTitle(String panelTitle) {
		this.panelTitle = panelTitle;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getExecResult() {
		return execResult;
	}

	public void setExecResult(String execResult) {
		this.execResult = execResult;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
}
