package com.tedu.base.initial.model.xml.logic;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
@XStreamAlias("logic") 
public class LogicModel implements Serializable{
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private boolean multi;		
	
	@XStreamAsAttribute
	private ParamsModel params;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMulti() {
		return multi;
	}

	public void setMulti(boolean multi) {
		this.multi = multi;
	}

	public ParamsModel getParams() {
		return params;
	}

	public void setParams(ParamsModel params) {
		this.params = params;
	}

	public String toSummaryString(){
		StringBuilder summary = new StringBuilder(String.format("{%s}%s分支,", name,multi?"有":"无"));
		if(params==null || params.getParamList().isEmpty()){
			summary.append("无参数。");
		}else{
			summary.append(String.format("共有%s个参数，", params.getParamList().size()));
			for(LogicParam param:params.getParamList()){
				summary.append(String.format("{%s}参数%s必填;", param.getName(),param.isRequired()?"":"非"));
			}
		}
		return summary.toString();
	}
	
}
