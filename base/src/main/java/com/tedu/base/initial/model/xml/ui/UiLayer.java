package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("ui_layer")  
public class UiLayer {
	public UiLayer(List<UI> uiList){
		this.uiList = uiList;
	}
	 @XStreamImplicit(itemFieldName="ui")
	private List<UI> uiList;

	public List<UI> getUiList() {
		return uiList;
	}

	public void setUiList(List<UI> uiList) {
		this.uiList = uiList;
	}


}
