package com.tedu.base.initial.model.xml.comp;

//@XStreamAlias("item")  
public class Item {
//	@XStreamAsAttribute
	private String name;
//	@XStreamAsAttribute
	private String value;
//	@XStreamAsAttribute
	private String path;
	private String item;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
}
