package com.tedu.base.msg.mail;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable{
	// 主动调用
	public static final String INVOKETYPE1 = "1";
	// 定时调用
	public static final String INVOKETYPE0 = "0";
	// 发送结果
	public static final String SUCCESS = "success";
	// 发送结果
	public static final String FAIL = "fail";
	private long id;
	// 收信人地址
	private String address;
	//希望显示的发送人昵称
	private String sendName;
	//邮件编码
	private String charSet;
	// 希望现实的收件人昵称
	private String receiveName;
	//邮件正文
	private String content;
	//邮件主题
	private String subject;
	// 发送时间默认like发送
	private Date sentDate;
	private String result;
	private Date createTime;
	private Date updateTime;
	private long createBy;
	private long updateBy;
	private long sendCount;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getSentDate() {
		return sentDate;
	}
	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}
	public long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String toString(){
		return String.format("发送给{%s}:%s", address,subject);
	}
}
