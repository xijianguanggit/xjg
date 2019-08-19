package com.tedu.base.task.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
/**
 * 
* @Description: 计划任务信息
 */
public class ScheduleJobModel {

	public static final String STATUS_RUNNING = "enable";
	public static final String STATUS_NOT_RUNNING = "disable";
	public static final String CONCURRENT_IS = "multi";
	public static final String CONCURRENT_NOT = "single";
	public static final String PARA_TYPE_SPRINGID = "springid";
	public static final String PARA_TYPE_SQL = "sql";
	public static final String PARA_TYPE_PARAM = "paramVal";
	public static final String PARA_TYPE_METHODNAME = "methodname";
	public static final String PARA_TYPE_BEANCLASS = "beanclass";
	public static final String BASE_JOB_OPEN = "1";
	public static final String BASE_JOB_CLOSE = "0";
	public static final String Excute_SUCCESS = "success";
	public static final String Excute_FAIL = "fail";
	private Long id;

	private Date createTime;

	private Date updateTime;
	/**
	 * 任务名称
	 */
	private String name;
	/**
	 * 任务状态 是否启动任务
	 */
	private String status;
	private String type;
	/**
	 * cron表达式
	 */
	private String expression;
	/**
	 * 描述
	 */
	private String description;

	/**
	 * 任务是否有状态
	 */
	private String concurrent;
	private String param;
	private Map<String, String> paramMap;
	private List<String> paramVal;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getConcurrent() {
		return concurrent;
	}
	public void setConcurrent(String concurrent) {
		this.concurrent = concurrent;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		if(!StringUtils.isEmpty(param)){
			Map<String, String> map = new HashMap<String, String>();
			String[] key = param.split("\\|")[0].split(",");
			String[] val = param.split("\\|")[1].split(",");
			for(int i=0;i<key.length;i++){
				if(!PARA_TYPE_PARAM.equals(key[i])){
					map.put(key[i], val[i]);
				}
			}
			setParamMap(map);
			List<String> paramVal = new ArrayList<String>();
			for(int i=0;i<key.length;i++){
				if(PARA_TYPE_PARAM.equals(key[i])){
					paramVal.add(val[i]);
				}
			}
			setParamVal(paramVal);
		}
	}
	public Map<String, String> getParamMap() {
		return paramMap;
	}
	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	public boolean equals(ScheduleJobModel obj){
		  if(obj.getId()==0l||this.getUpdateTime().equals(obj.getUpdateTime()))  
		        return true;  
		    return false;  
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getParamVal() {
		return paramVal;
	}
	public void setParamVal(List<String> paramVal) {
		this.paramVal = paramVal;
	}

}