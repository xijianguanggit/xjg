package com.tedu.base.auth.login.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tedu.base.initial.model.xml.ui.FormConstants;

import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 用户model
 *
 * @author xijianguang
 */
public final class UserModel implements Serializable {
	public static final String ANONYMOUS = "anonymous";
	private static final long serialVersionUID = -6932402953488259449L;

	private long userId;
	
	private long orgId;
	// 用户名
	private String userName;
	// 员工id
	private long empId;
	//密码加盐
	private String salt;
	//密码
	private String password;
	// 授权类型
	private String authType;
	//密码输错次数
	private int wrongCount;
	private int validate;
	private Date updateTime;
	private Date createTime;
	private Long updateBy;
	private Long createBy;
	private String validateCode;
	private String cancelBy;
	private String isCancel;
	private String version;
	private String checkResult;
	private Date test;
	private String empId_foreign="t_employee.id";
	private Long roleId;//废弃
	private String roleName;//系统支持多角色,逗号隔开
	private String empName;
	private String status;
	private String grade;
	private String gender;

	private String mobile;
	private String name;
	private String nickName;
	
	private String orgName; //所属部门
	
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String email;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmpId_foreign() {
		return empId_foreign;
	}
	public void setEmpId_foreign(String empId_foreign) {
		this.empId_foreign = empId_foreign;
	}
	public String getCancelBy() {
		return cancelBy;
	}
	public void setCancelBy(String cancelBy) {
		this.cancelBy = cancelBy;
	}
	public String getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(String isCancel) {
		this.isCancel = isCancel;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(long empId) {
		this.empId = empId;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthType() {
		return authType;
	}
	public void setAuthType(String authType) {
		this.authType = authType;
	}
	public int getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}
	public int getValidate() {
		return validate;
	}
	public void setValidate(int validate) {
		this.validate = validate;
	}
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
//	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getTest() {
		return test;
	}
	public void setTest(Date test) {
		this.test = test;
	}
	public Date getUpdateime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public boolean isAdminRole(){
		String[] arrRole = ObjectUtils.toString(roleName).split(",");
		Arrays.sort(arrRole);
		return Arrays.binarySearch(arrRole, FormConstants.ROLE_ADMIN)>=0;
	}
	public String getRoleName() {
		return roleName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}







}
