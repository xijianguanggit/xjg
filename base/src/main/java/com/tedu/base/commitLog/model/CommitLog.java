package com.tedu.base.commitLog.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 提交日志
 * @author gaolu
 *
 */
public class CommitLog implements Serializable{
	private static final long serialVersionUID = -6932402953488259449L;
	
	//版本号
	private String sha1;
	//内容
	private String message;
	//提交者
	private String author;
	//提交时间
	private Date date;
	//所在分支
	private String branch;
	//工作项编号
	private String issueId;
	//定时任务编号
	private Integer jobId;
	//创建时间
	private String insertTime;
	
	/**
	 * 
	 * @Description: set,get方法
	 * @author: gaolu
	 * @date: 2017年8月16日 下午1:20:55  
	 * @param: @return      
	 * @return: Integer
	 */
	public String getSha1() {
		return sha1;
	}
	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public Integer getJobId() {
		return jobId;
	}
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	public String getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	
}
