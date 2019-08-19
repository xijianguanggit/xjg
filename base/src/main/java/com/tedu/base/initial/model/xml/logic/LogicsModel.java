package com.tedu.base.initial.model.xml.logic;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("logics") 
public class LogicsModel implements Serializable{
	@XStreamImplicit(itemFieldName="logic")
	private List<LogicModel> logicList;

	public List<LogicModel> getLogicList() {
		return logicList;
	}

	public void setLogicList(List<LogicModel> logicList) {
		this.logicList = logicList;
	}
	
	public String toSummaryString(){
		return logicList.stream().map(LogicModel::toSummaryString).collect(Collectors.joining("\n"));
	}
	
	public LogicModel getLogic(String logicName){
		if(logicList==null) return null;
		for(LogicModel logic:logicList){
			if(logic.getName().equals(logicName)){
				return logic;
			}
		}
		return null;
	}
}
