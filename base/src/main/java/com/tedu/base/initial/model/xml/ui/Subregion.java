package com.tedu.base.initial.model.xml.ui;

import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

//最内层panel
@XStreamAlias("subregion") 
public class Subregion {
	//xml 中name关联的panel对象
	@XStreamAsAttribute
	private String panel;
	//子区域Panel排列方向	vertical|horizontal|tab
	@XStreamAsAttribute
	private String location;
	@XStreamAsAttribute
	private String scale;
	private Panel panelObj;
	private float width;
	private float height;
	private List<Subregion> listSubregion;
	public String getPanel() {
		return panel;
	}
	public void setPanel(String panel) {
		this.panel = panel;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	// 通过subregion panelName获取panel对象
	public Panel getPanelObj() {
		return this.panelObj;
	}
	public void setPanelObj(UI ui) {
		List<Panel> panelList = ui.getPanelList();
		for(Panel panel:panelList){
			if(this.panel.equals(panel.getName())){
				this.panelObj = panel;
				break;
			}
		}
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
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}

	public List<Subregion> getListSubregion() {
		return listSubregion;
	}
	public void setListSubregion(List<Subregion> listSubregion) {
		this.listSubregion = listSubregion;
	}
	
}
