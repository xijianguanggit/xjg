package com.tedu.base.msg.mail;

import java.util.Date;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.connection.DefaultMessage;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.msg.model.MessageLog;

@Component
@Aspect 
public class MessageAspect {
	@Resource
	private FormLogService formLogService;
	

//	@Pointcut("execution(* onMessage(..))")
	@Pointcut("execution(* com.tedu..*..onMessage(..))")  
	public void processMessage(){
    	//do nothing
    }
	org.springframework.data.redis.listener.adapter.MessageListenerAdapter messageListenerAdapter;
    @Around("processMessage()")
	public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//    	if(!(joinPoint.getArgs()[1] instanceof byte[])) {
//    		joinPoint.proceed(joinPoint.getArgs()); 
//    	}
    	MessageListenerAdapter adapter = ((MessageListenerAdapter)(joinPoint.getTarget()));
    	Object delegate = adapter.getDelegate();
    	if(delegate.getClass().getName().startsWith("com.tedu")){
        	String topic = new String((byte[])joinPoint.getArgs()[1],"utf-8");
			  MessageLog log = new MessageLog();
			  log.setTopic(topic);//和topic无关
			  
			  log.setMessageClass(delegate.getClass().getSimpleName());
			  log.setType("consume");
			  log.setMessage("");
			  //执行
			  joinPoint.proceed(joinPoint.getArgs()); 
			  log.setEndTime(new Date());
			  formLogService.save(log);
    	}else{
    		System.out.println("not redis");
//    		joinPoint.proceed(joinPoint.getArgs()); 
    	}
	}
}
