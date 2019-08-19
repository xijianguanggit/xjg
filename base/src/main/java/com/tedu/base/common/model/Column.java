package com.tedu.base.common.model;

import com.tedu.base.initial.model.xml.ui.Control;

public class Column implements java.io.Serializable {
	String field;
	String title;
	String controlName;
	int width;
	static final int DEFAULT_WIDTH = 180;
	public Column(){}
	public Column(String field,String title){
		this.field = field;
		this.title = title;
		this.width = DEFAULT_WIDTH;
	}
	/**
	 * 
	 * @param fieldAndTitleAndWidth
	 */
	public Column(String fieldAndTitleAndWidth){
		String[] arr = fieldAndTitleAndWidth.split(",");
		field = arr[0];
		title = arr.length>1?arr[1]:"";
		controlName = arr.length>2?arr[2]:"";//组件名
		
		String strWidth = arr.length>3?arr[3]:DEFAULT_WIDTH+"";
		if(strWidth.isEmpty()) {
			strWidth = DEFAULT_WIDTH+"";
		}
		this.width = Integer.parseInt(strWidth);
	}
	public Column(Control control){
		field = control.getName();
		title = control.getTitle();
		
		String strWidth = "";
		if(control.getWidth().isEmpty()) {
			strWidth = DEFAULT_WIDTH+"";
		} else {
			strWidth = String.valueOf((Long.parseLong(control.getWidth())*120));
		}
		this.width = Integer.parseInt(strWidth);
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public String getControlName() {
		return controlName;
	}
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}
	public String toString(){
		if(width==0){
			return String.format("{field:'%s',title:'%s',hidden:%s}", field,title,"true");
		}else{
			return String.format("{field:'%s',title:'%s',width:%s,halign:%s}", field,title,width,"'center'");
		}
	}
}
