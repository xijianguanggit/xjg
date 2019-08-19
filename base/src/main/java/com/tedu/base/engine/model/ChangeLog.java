package com.tedu.base.engine.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.engine.util.FormUtil;

public class ChangeLog implements Serializable,TableModel{
	private static final String[] FIELDS = new String[]{
			"flow_id",
			"url",
			"proc",
			"type",
			"entity_id",
			"entity_name",
			"old_content",
			"new_content",
			"create_time",
			"create_by"
	};
	private Long id;
	private String flowId;
	private String url;
	private String proc;
	private String type;
	private String entityId;
	private String entityName;
	private Map<String,Object> oldData;
	private Map<String,Object> newData;	
	private Date createTime;
	private Long createBy;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOldContent() {
		return toContentString(oldData);
	}

	public String getNewContent() {
		return toContentString(newData);
	}

	private String toContentString(Map<String,Object> content){
		  ObjectMapper mapper = new ObjectMapper();  
		  mapper.setSerializationInclusion(Inclusion.NON_NULL); 
		  mapper.setSerializationInclusion(Inclusion.NON_EMPTY); 
		  mapper.setDateFormat(new SimpleDateFormat(DateUtils.YYMMDD_HHMMSS_24));
	      try{
	    	  return mapper.writeValueAsString(content);  
	      }catch(Exception e){
	    	  return "{error:\"" + e.getMessage() + "\"}";
	      }	
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
	
	public String[] getFields(){
		return FIELDS;
	}
	public Map<String, Object> getOldData() {
		return oldData;
	}
	public void setOldData(Map<String, Object> oldData) {
		this.oldData = oldData;
	}
	public Map<String, Object> getNewData() {
		return newData;
	}
	public void setNewData(Map<String, Object> newData) {
		this.newData = newData;
	}
	
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	/**
	 * 以新内容为准，新旧内容相同的删除，只保留差异
	 * update时使用
	 */
	public void shrink(){
		if(newData==null || oldData==null) return;
		FormUtil.shrink(oldData,newData);
	}
	
	@Override
	public String getTableName() {
		return "t_change_log";
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getProc() {
		return proc;
	}
	public void setProc(String proc) {
		this.proc = proc;
	}
	
	
}
