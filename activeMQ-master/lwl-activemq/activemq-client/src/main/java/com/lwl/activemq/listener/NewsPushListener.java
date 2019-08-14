package com.lwl.activemq.listener;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

@Component("newsPushListener")
public class NewsPushListener implements SessionAwareMessageListener<ActiveMQMapMessage>{
    @Override
    public void onMessage(ActiveMQMapMessage message, Session session) throws JMSException {
        try {
            System.out.println("QueueReceiver1接收到消息：" + message.getStringProperty("name"));
        } catch (Exception e) {
            session.rollback();
            e.printStackTrace();
        }
        session.commit();
    }
}

