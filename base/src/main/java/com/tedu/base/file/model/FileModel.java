package com.tedu.base.file.model;

import java.io.Serializable;

public class FileModel implements Serializable{
	private static final long serialVersionUID = -6932402953488259449L;
	
	//主键id
	private Integer id;
	//存储方式
	private String mediaType;
	//公有public、私有private
	private String accessType;
	//存储类型
	private String source;
	//文件uuid名称
	private String fileUUID;
	//文件大小
	private String length;
	//文件原始名称
	private String title;
	//文件后缀
	private String fileType;
	//创建时间
	private String createTime;
	//创建人
	private Integer createBy;
	//备注
	private String remark;
	//备注
	private String path;
	//public文件url
	private String url;
	
	private String newName;
	/**
	 * 
	 * @Description: set,get方法
	 * @author: gaolu
	 * @date: 2017年8月16日 下午1:20:55  
	 * @param: @return      
	 * @return: Integer
	 */
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getAccessType() {
		return accessType;
	}
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	public String getFileUUID() {
		return fileUUID;
	}
	public void setFileUUID(String fileUUID) {
		this.fileUUID = fileUUID;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
}
