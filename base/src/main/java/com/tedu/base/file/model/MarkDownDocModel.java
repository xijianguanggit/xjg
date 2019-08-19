package com.tedu.base.file.model;

import java.io.Serializable;
/**
 * 流程图表.
 * @author hejiakuo
 * @date 2017-12-21
 *
 */
public class MarkDownDocModel implements Serializable{
	private static final long serialVersionUID = -6932402953488259149L;
	
	//主键id
	private Integer id;
	//文档名称
	private String title;
	//文件表id
	private Integer fileId;
	//ui标识
	private String property;
	//文档类型
	private String type;
	//文档名称
	private String name;
	//创建时间
	private String createTime;
	//创建人
	private Integer createBy;
	//文档路径
	private String path;
	
	//xml文件位置
	private String xmlpath;
	
	public Integer getId() {
		return id;
	}
	public String getXmlpath() {
		return xmlpath;
	}
	public void setXmlpath(String xmlpath) {
		this.xmlpath = xmlpath;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
