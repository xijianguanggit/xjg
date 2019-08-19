package com.tedu.base.initial.model.xml.comp;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("children")  
public class Children {
	@XStreamImplicit(itemFieldName="item")
	private List<Item> itemList;

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	
}
