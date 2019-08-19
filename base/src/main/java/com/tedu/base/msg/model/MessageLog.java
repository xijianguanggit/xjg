package com.tedu.base.msg.model;

import java.io.Serializable;
import java.util.Date;

import com.tedu.base.engine.model.TableModel;

public class MessageLog implements Serializable,TableModel{
	private Long id;
	private Date createTime;
	private Date endTime;
	private Long cost;
	private String messageClass;
	private String message;
	private String topic;
	private String type;
	private Integer status;
	
	public MessageLog(){
		createTime = new Date();
	}
	@Override
	public String getTableName() {
		return "t_message_log";
	}

	@Override
	public String[] getFields() {
		return new String[]{
				"id",
				"create_time",
				"end_time",
				"cost",
				"message_class",
				"message",
				"topic",
				"type",
				"status"
		};			
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
		if(createTime!=null){
			cost = endTime.getTime()-createTime.getTime();
		}
	}

	public Long getCost() {
		return cost;
	}

	public void setCost(Long cost) {
		this.cost = cost;
	}

	public String getMessageClass() {
		return messageClass;
	}

	public void setMessageClass(String messageClass) {
		this.messageClass = messageClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}

}
