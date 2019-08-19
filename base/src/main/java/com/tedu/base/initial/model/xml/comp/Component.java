package com.tedu.base.initial.model.xml.comp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("tsoftware")  
public class Component {
	private Panels panels;
	private Controls controls;
	public Controls getControls() {
		return controls;
	}
	public void setControls(Controls controls) {
		this.controls = controls;
	}
	public Panels getPanels() {
		return panels;
	}
	public void setPanels(Panels panels) {
		this.panels = panels;
	}
	
}
