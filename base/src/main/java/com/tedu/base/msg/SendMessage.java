package com.tedu.base.msg;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.msg.model.MessageLog;
/**
 * 消息发送类
 * 在具体业务处理类中Autowired,将消息数据对象发送到指定topic
 * 每个topic配不同listener(MessageListenerAdapter)
 * MessageListenerAdapter将消息对象序列化并转交至指定类的指定方法处理。
 * 
 * @author wangdanfeng
 *
 */
@Component
public class SendMessage {
	@Autowired(required=false)
    private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private FormLogService formLogService;	
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void sendMessage(String channel, Serializable message) {
        MessageLog log = new MessageLog();
        redisTemplate.convertAndSend(channel, message);
        FormLogger.logFlow(String.format("已将邮件加入发送队列{%s}"  , channel), FormLogger.LOG_TYPE_INFO);
        saveLog(channel, message,log);
    }
	
	private void saveLog(String channel,Serializable message,MessageLog log){
		  log.setTopic(channel);
		  log.setMessageClass(message.getClass().getSimpleName());
		  log.setType("send");
		  log.setMessage(message.toString());
		  log.setEndTime(new Date());
		  log.setCost(log.getEndTime().getTime()-log.getCreateTime().getTime());
		  //执行
		  formLogService.save(log);
	}
}
