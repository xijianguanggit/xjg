package com.tedu.base.initial.model.xml.ui;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

@XStreamAlias("property") 
public class Property implements Serializable{
	public static final String TYPE_LONG = "long";
	public static final String TYPE_DOUBLE = "double";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_KEYWORD = "keyword";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_DATETIME = "datetime";
	public static final String TYPE_TIME = "time";
	public Property(String name){
		this.name = name;
	}
	public Property(String name,String type,String length,String validate,String initial,String field){
		this.name = name;
		this.type = type;
		this.length = length;
		this.validate = validate;
		this.initial = initial;
		this.field = field;
	}
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String type;
	@XStreamAsAttribute
	private String length;
	@XStreamAsAttribute
	private String validate;
	@XStreamAsAttribute
	private String initial;
	@XStreamAsAttribute
	private String field;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getInitial() {
		return initial;
	}
	public void setInitial(String initial) {
		this.initial = initial;
	}
	public String getField() {
		return field==null?"":field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
}
