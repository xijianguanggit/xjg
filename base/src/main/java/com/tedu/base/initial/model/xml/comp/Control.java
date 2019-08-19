package com.tedu.base.initial.model.xml.comp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("control")
public class Control {
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private Methods methods;
	@XStreamAsAttribute
	private Events events;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Methods getMethods() {
		return methods;
	}
	public void setMethods(Methods methods) {
		this.methods = methods;
	}
	public Events getEvents() {
		return events;
	}
	public void setEvents(Events events) {
		this.events = events;
	}
	
	

}
