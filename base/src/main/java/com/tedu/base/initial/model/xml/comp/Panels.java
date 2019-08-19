package com.tedu.base.initial.model.xml.comp;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("panels")  
public class Panels {
	@XStreamImplicit(itemFieldName="panel")
	private List<Panel> panelList;

	public List<Panel> getPanelList() {
		return panelList;
	}

	public void setPanelList(List<Panel> panelList) {
		this.panelList = panelList;
	}


}
