package com.tedu.base.common.model;

import java.io.Serializable;
/**
 * 用于服务easyUI下拉类组件的数据源
 * @author tedu
 *
 */
public class DataItem implements Serializable{
	private String cataCode;//对应字典表分类码
	private String value; //值
	private String text; //名称
	private boolean selected;
	public DataItem(){
		
	}
	public String getCataCode() {
		return cataCode;
	}

	public void setCataCode(String cataCode) {
		this.cataCode = cataCode;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public DataItem(String text,String value){
		this.text = text;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
