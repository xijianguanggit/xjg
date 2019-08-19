package com.tedu.base.common.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

public class TreeItem implements Serializable{
	private String id;
	private String text;
	private String state = "close";
	private TreeItem[] children;
	private String iconCls;
	private boolean isDirectory = false;
	public TreeItem(String id,String text,TreeItem[] children){
		this.id = id;
		this.text = text;
		this.children = children;
//		  map.put("id", file.getPath());
//	        map.put("text", file.getName());
//	        map.put("children",catalogs);
//	        map.put("iconCls","icon-add");
	}
	
	public TreeItem(String id,String text){
		this.id = id;
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		if(getChildren()!=null){
			return "closed";	
		}
		return "";
	}
	public void setState(String state) {
		this.state = state;
	}
	public TreeItem[] getChildren() {
		return children;
	}
	public void setChildren(TreeItem[] children) {
		this.children = children;
	}
	
	public void setChildren(List<TreeItem> childrenList) {
		if(childrenList!=null && !childrenList.isEmpty()){
			this.children = new TreeItem[childrenList.size()];
			childrenList.toArray(children);
		}
	}
	
	public String getIconCls() {
		if(iconCls!=null) return iconCls;
		
		String fileName = ObjectUtils.toString(text).toLowerCase();
		if(getChildren()!=null || isDirectory()){
			return "eclipse-folder-close";
		}else if( (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif"))){
			return "eclipse-img";//iconCls;	
		}else if(fileName.endsWith(".js")){
			return "eclipse-js";
		}else if(fileName.endsWith(".html") || fileName.endsWith(".htm")){
			return "eclipse-html";
		}else if(fileName.endsWith(".css")){
			return "eclipse-css";
		}
		return "eclipse-file";//iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	public void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
		this.setIconCls("eclipse-folder-close");
	}
	
	
}
