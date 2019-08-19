package com.tedu.base.initial.model.xml.comp;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("events")  
public class Events {

	@XStreamImplicit(itemFieldName="event")
	private List<Event> eventList;

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
	
}
