package com.tedu.base.msg.mail;

import java.io.Serializable;

import javax.annotation.Resource;

import com.tedu.base.engine.service.FormLogService;
import com.tedu.base.engine.util.FormLogger;

public class MailConsumer {
	@Resource
	private SendMsgService sendMsgService;
	@Resource
	private FormLogService formLogService;
	
	public void handleMessage(Serializable message){
		if(message instanceof Email){
			FormLogger.logFlow("处理邮件消息", FormLogger.LOG_TYPE_INFO);
			Email mail = (Email)message;
			try {
				sendMsgService.sendMail(mail, Email.INVOKETYPE0);
			} catch (Exception e) {
				FormLogger.logMSG("邮件发送异常," + e.getMessage(), FormLogger.LOG_TYPE_ERROR);
			}
		}
    }
}
