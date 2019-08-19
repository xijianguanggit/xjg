package com.tedu.base.initial.model.xml.comp;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Methods {

	@XStreamImplicit(itemFieldName="item")
	private List<Item> itemList;

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	
}
