package com.tedu.base.initial.model.xml.ui;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

@XStreamAlias("flow") 
public class Flow implements Serializable{
	// 触发条件 与panel或者control名字匹配 例如：panel.control或者control
	@XStreamAsAttribute
	private String trigger;
	// 对应事件
	@XStreamAsAttribute
	private String event;
	//表达式
	@XStreamAsAttribute
	private String filter;
	
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean defaultEnter;
	
	//对应Procedure
	@XStreamImplicit(itemFieldName="procedure")
	private List<Procedure> procedureList;
	private String uiName;//冗余,方便存取
	public String getTrigger() {
		return trigger;
	}
	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public List<Procedure> getProcedureList() {
		return procedureList==null?Collections.emptyList():procedureList;
	}
	public void setProcedureList(List<Procedure> procedureList) {
		this.procedureList = procedureList;
	}
	
	/**
	 * 保持命名逻辑统一性
	 * @param flow
	 * @return
	 */
	public String getFlowFunctionName(String uiName){
		return event.concat("_")
				.concat(trigger.replace(".", "_"))
				.concat("_").concat(uiName);
	}
	
	
	/**
	 * log所在function唯一识别
	 * @param uiName
	 * @return
	 */
	public String getProcedureFunctionId(String uiName,String procedureName){
		return getFlowFunctionName(uiName) + "_" + procedureName;
	}
	
	public boolean isOnLoadFlow(){
		return trigger==null || trigger.isEmpty();
	}
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	
	public void setBeginEndProcedure(){
		if(procedureList==null) return;
		Map<String,String> map = new HashMap<String,String>();
		//按惯例首个是flow开始
		int size = procedureList.size(); 
		if(size>0){
			procedureList.get(0).setBegin(true);
			procedureList.get(0).setEnd(size==1);
		}
		for(Procedure p:procedureList){
			map.put(p.getIfyes(), "");
			map.put(p.getIfno(), "");
		}
		//没有下一步骤，且被别的procedure的ifyes或ifno引用的是最后一步
		for(Procedure p:procedureList){
			if(ObjectUtils.toString(p.getIfyes()).isEmpty() 
					&& ObjectUtils.toString(p.getIfno()).isEmpty() 
					&& map.get(p.getName())!=null){
				p.setEnd(true);
			}
		}
	}
	public boolean isDefaultEnter() {
		return defaultEnter;
	}
	public void setDefaultEnter(boolean defaultEnter) {
		this.defaultEnter = defaultEnter;
	}
	
}