package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("layout")
public class LayOut {

	@XStreamImplicit(itemFieldName = "region")
	private List<Region> regionList;

	public List<Region> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<Region> regionList) {
		this.regionList = regionList;
	}

}
