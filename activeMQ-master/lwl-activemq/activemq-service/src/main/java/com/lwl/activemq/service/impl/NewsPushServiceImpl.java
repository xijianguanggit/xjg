package com.lwl.activemq.service.impl;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.lwl.activemq.domain.News;
import com.lwl.activemq.service.PushService;

@Service("newsPushService")
public class NewsPushServiceImpl implements PushService {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	@Qualifier("newsServiceQueue")
	private Destination destination;

	@Override
	public void push(final Object info) {
		pushExecutor.execute(new Runnable() {
			@Override
			public void run() {
				jmsTemplate.send(destination, new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						MapMessage msg1 = session.createMapMessage();
						msg1.setStringProperty("name", "张三");
						msg1.setStringProperty("age", "23");
						msg1.setStringProperty("color", "blue");
						msg1.setIntProperty("sal", 2200);
						int id = 1;
						msg1.setIntProperty("id", id);
						String receiver = id % 2 == 0 ? "A" : "B";
						msg1.setStringProperty("receiver", receiver);
						return msg1;
					}
				});
				jmsTemplate.send(destination, new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
						MapMessage msg2 = session.createMapMessage();
						msg2.setStringProperty("name", "李四");
						msg2.setStringProperty("age", "26");
						msg2.setStringProperty("color", "red");
						msg2.setIntProperty("sal", 1300);
						int id = 2;
						msg2.setIntProperty("id", id);
						String receiver = id % 2 == 0 ? "A" : "B";
						msg2.setStringProperty("receiver", receiver);
						return msg2;
					}
				});
			}
		});
	}

}
