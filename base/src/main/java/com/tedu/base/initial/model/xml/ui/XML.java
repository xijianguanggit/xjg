package com.tedu.base.initial.model.xml.ui;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("tsoftware")  
public class XML {
	private UiLayer ui_layer;
	private ModelLayer model_layer;
	public UI getUiByName(String uiName){
		if(uiName == null) return null;
		List<UI> uiList = this.ui_layer.getUiList();
		for(UI ui:uiList){
			if(uiName.equals(ui.getName())){
				return ui;
			}
		}
		return null;
	}
	//init时预先构造panelMap
	public Panel getPanel(String uiName,String panelName){
		UI ui = getUiByName(uiName);
		if(ui != null ){
			return ui.getUiPanel(panelName);
		}
		return null;
	}
	
	/**
	 * 获取UI的flow对象
	 * @param uiName
	 * @return
	 */
	public List<Flow> getFlow(String uiName){
		UI ui = getUiByName(uiName);
		if(ui == null) return null;
		return ui.getFlowList();
	}
	
	public UI getUI(String uiName){
		UI ui = getUiByName(uiName);
		if(ui == null) return null;
		return ui;
	}
	
	public UiLayer getUi_layer() {
		return ui_layer;
	}
	public void setUi_layer(UiLayer ui_layer) {
		this.ui_layer = ui_layer;
	}
	public ModelLayer getModel_layer() {
		return model_layer;
	}
	public void setModel_layer(ModelLayer model_layer) {
		this.model_layer = model_layer;
	}
}
