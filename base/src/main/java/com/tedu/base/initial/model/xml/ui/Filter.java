package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("filter")  
public class Filter {
	@XStreamAsAttribute
	private String name;
	@XStreamImplicit(itemFieldName="field")
	private List<FilterField> fieldList;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<FilterField> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<FilterField> fieldList) {
		this.fieldList = fieldList;
	}
	
	
}
