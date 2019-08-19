package com.tedu.base.commitLog.model;

import java.io.Serializable;

/**
 * 提交文件记录
 * @author gaolu
 *
 */
public class CommitFile implements Serializable{
	private static final long serialVersionUID = -6932402953488259449L;
	
	//编号
	private Integer id;
	//对应提交编号
	private String commitSha1;
	//提交文件路径
	private String path;
	//提交文件后缀
	private String extension;
	//提交状态
	private String status;
	//增加行数
	private Integer addLines;
	//移除行数
	private Integer removeLines;
	
	/**
	 * 
	 * @Description: set、get方法
	 * @author: gaolu
	 * @date: 2017年11月6日 下午2:10:45  
	 * @param:      
	 * @return: Integer
	 */
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCommitSha1() {
		return commitSha1;
	}
	public void setCommitSha1(String commitSha1) {
		this.commitSha1 = commitSha1;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getAddLines() {
		return addLines;
	}
	public void setAddLines(Integer addLines) {
		this.addLines = addLines;
	}
	public Integer getRemoveLines() {
		return removeLines;
	}
	public void setRemoveLines(Integer removeLines) {
		this.removeLines = removeLines;
	}
	


	
}
