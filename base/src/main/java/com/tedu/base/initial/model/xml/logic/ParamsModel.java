package com.tedu.base.initial.model.xml.logic;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("params") 
public class ParamsModel  implements Serializable {
	@XStreamImplicit(itemFieldName="param")
	private List<LogicParam> paramList;

	public List<LogicParam> getParamList() {
		return paramList;
	}

	public void setParamList(List<LogicParam> paramList) {
		this.paramList = paramList;
	}
	
	
}
