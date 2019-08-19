package com.tedu.base.initial.model.xml.ui;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
@XStreamAlias("ui")  
public class UI {
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private String title;
	@XStreamImplicit(itemFieldName="panel")
	private List<Panel> panelList;
	@XStreamImplicit(itemFieldName="flow")
	private List<Flow> flowList;
	@XStreamAsAttribute
	private LayOut layout;
	private Set<String> panelSet;
	private Set<String> controlSet;
	// 此ui对应的flow
	public Flow getUiFlow(){
		return getFlow("",FormConstants.EVENT_ONLOAD);
	}
	@XStreamAsAttribute
	private String template;//ui template
	public Flow getFlow(String trigger,String event){
		for(Flow flow:this.flowList){
			// 规则如果为空则返回uiName上的flow 如果为pnlUp.ctlQuery这种格式则是返回pnlUp
			// panel内的ctlQuerycontrol 的flow
			if(trigger.equals(flow.getTrigger()) && event.equals(flow.getEvent())){
				return flow;
			}
		}
		return null;
	}
	/**
	 * 常用于根据	ServerTokenData找到对应procedure对象,获取参数
	 * @param trigger
	 * @param event
	 * @param procedureName
	 * @return
	 */
	public Procedure getProcedure(String trigger,String event,String procedureName){
		Flow flow = getFlow(trigger,event);
		if(flow != null){
			for(Procedure p:flow.getProcedureList()){
				if(procedureName.equalsIgnoreCase(p.getName())){
					return p;
				}
			}
		}
		return null;
	}
	
	/**
	 * 从当前ui下找出指定名字的panel描述
	 * @param panelName
	 * @return
	 */
	public Panel getUiPanel(String panelName){
		for(Panel panel:this.panelList){
			// 规则如果为空则返回uiName上的flow 如果为pnlUp.ctlQuery这种格式则是返回pnlUp
			// panel内的ctlQuerycontrol 的flow
			if(panelName.equals(panel.getName()))
				return panel;
		}
		return null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Panel> getPanelList() {
		return panelList;
	}
	public void setPanelList(List<Panel> panelList) {
		this.panelList = panelList;
	}
	public List<Flow> getFlowList() {
		return flowList==null?Collections.emptyList():flowList;
	}
	public void setFlowList(List<Flow> flowList) {
		this.flowList = flowList;
	}
	public LayOut getLayout() {
		return layout;
	}
	public void setLayout(LayOut layout) {
		this.layout = layout;
	}

	public Set<String> getPanelSet() {
		panelSet = new HashSet<String>();
		controlSet = new HashSet<String>();
		for(Panel p: this.panelList){
			panelSet.add(p.getType());
			for(Control c: p.getControlList()){
				controlSet.add(c.getType());
			}
		}
		return panelSet;
	}

	public void setPanelSet(Set<String> panelSet) {
		this.panelSet = panelSet;
	}

	public Set<String> getControlSet() {
		return controlSet;
	}

	public void setControlSet(Set<String> controlSet) {
		this.controlSet = controlSet;
	}

	public String toString(){
		return name;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	
}
