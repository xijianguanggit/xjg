package com.tedu.base.initial.model.xml.comp;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("controls")
public class Controls {
	@XStreamImplicit(itemFieldName="control")
	private List<Control> ControlList;

	public List<Control> getControlList() {
		return ControlList;
	}

	public void setControlList(List<Control> controlList) {
		ControlList = controlList;
	}


}
