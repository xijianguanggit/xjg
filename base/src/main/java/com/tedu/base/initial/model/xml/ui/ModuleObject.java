package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("object")  
public class ModuleObject {

	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String table;
	@XStreamAsAttribute
	private String primary;
	@XStreamAsAttribute
	private String unique;
	@XStreamAsAttribute
	private String order;
	@XStreamAsAttribute
	private String father;
	//是否自增
	@XStreamAsAttribute
//	@XStreamConverter(value=BooleanConverter.class, booleans={false}, strings={"Y", "N"})
	private String  autoinc ;//auto increment
	
	@XStreamImplicit(itemFieldName="property")
	private List<Property> propertyList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<Property> getPropertyList() {
		return propertyList;
	}

	public void setPropertyList(List<Property> propertyList) {
		this.propertyList = propertyList;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getAutoinc() {
		return autoinc;
	}

	public void setAutoinc(String autoinc) {
		this.autoinc = autoinc;
	}
	
	public boolean isAutoinc() {
		return autoinc==null?true:autoinc.equals("Y");
	}

}
