package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
// 最外层panel
@XStreamAlias("region") 
public class Region {
	// 位置
	@XStreamAsAttribute
	private String location;
	// 尺寸
	@XStreamAsAttribute
	private String scale;
	private float width;
	private float height;
	private String westOreastHeight;
	// 子panel
	@XStreamImplicit(itemFieldName="subregion")
	private List<Subregion> subregionList;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public List<Subregion> getSubregionList() {
		return subregionList;
	}
	public void setSubregionList(List<Subregion> subregionList) {
		this.subregionList = subregionList;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public String getWestOreastHeight() {
		return westOreastHeight;
	}
	public void setWestOreastHeight(String westOreastHeight) {
		this.westOreastHeight = westOreastHeight;
	}
	
}
