package com.tedu.base.file.model;

import java.io.Serializable;
/**
 * 文档表
 * @author hejiakuo
 * @date 2017-11-30
 *
 */
public class DocumentModel implements Serializable{
	private static final long serialVersionUID = -6932402953488259149L;
	
	//主键id
	private Integer id;
	//文件表id
	private Integer fileId;
	//目录表id
	private Integer cataId;
	//文档名称
	private String title;
	//关键词
	private String keyword;
	//文档状态
	private String status;
	//创建时间
	private String createTime;
	//创建人
	private Integer createBy;
	//更新时间
	private String updateTime;
	//更新人
	private Integer updateBy;
	//版本号未映射
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFileId() {
		return fileId;
	}
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	public Integer getCataId() {
		return cataId;
	}
	public void setCataId(Integer cataId) {
		this.cataId = cataId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	
	
	
}
