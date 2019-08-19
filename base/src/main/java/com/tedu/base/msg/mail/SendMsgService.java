package com.tedu.base.msg.mail;

import java.util.List;
import java.util.Map;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;

/**
 * 
 * @Description: 计划任务管理
 */
public interface SendMsgService {
	public SendSmsResponse sentSMS(Map<String, Object> map) throws Exception;
	public void sendMail(List<Email> listMail, String invokeType) throws Exception;
	public void sendMail(Email mail, String invokeType) throws Exception;
	public void updateEmail() throws Exception;
	public boolean checkCode(String mobile, String code, String type) ;
}
