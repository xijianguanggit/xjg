package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class ModelLayer {
	 @XStreamImplicit(itemFieldName="object")
	private List<ModuleObject> moduleObjectList;
	public ModelLayer(List<ModuleObject> moduleObjectList){
		this.moduleObjectList = moduleObjectList;
	}
	 /**
	  * 根据名字找到model描述对象
	  * @param modelName
	  * @return
	  */
	 public ModuleObject getModel(String objectName){
		 for(ModuleObject m:moduleObjectList){
			 if(m.getName().equals(objectName)){
				 return m;
			 }
		 }
		 return null;
	 }
	 
	public List<ModuleObject> getModuleObjectList() {
		return moduleObjectList;
	}

	public void setModuleObjectList(List<ModuleObject> moduleObjectList) {
		this.moduleObjectList = moduleObjectList;
	}
	 
}
