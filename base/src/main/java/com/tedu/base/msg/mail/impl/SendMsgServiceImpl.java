package com.tedu.base.msg.mail.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tedu.base.common.page.QueryPage;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.LogUtil;
import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.msg.mail.Email;
import com.tedu.base.msg.mail.SendMsgService;
import com.tedu.base.task.dao.ScheduleJobMapperDao;

/**
 * 
 * @Description: 计划任务管理
 */
@Service("sendMsgService")
public class SendMsgServiceImpl implements SendMsgService{
	@Value("${msg.mail.account}")
	private String emailAccount;
	@Value("${msg.mail.password}")
	private String emailPassword;
	@Value("${msg.mail.SMTPHost}")
	private String emailSMTPHost;
	
	@Value("${msg.mail.smtp.auth:true}")
	private String auth = "false";

	
	@Value("${msg.alisms.accessKeyId:''}")
	private String accessKeyId;
	@Value("${msg.alisms.accessKeySecret:''}")
	private String accessKeySecret;
	@Value("${msg.alisms.signName:''}")
	private String signName;
	
	@Value("${mail.smtp.starttls.enable:false}")
	private String tlsEnable = "";
	@Value("${mail.smtp.socketFactory.port:25}")
	private String socketFactoryPort = "";	
	@Value("${mail.debuggable:false}")
	private boolean debuggable = false;
	
	@Autowired
	private ScheduleJobMapperDao scheduleJobMapper;
	
	@Resource
	private FormMapper formMapper;
	//三个参数1：temp_code 模板id,例如：SMS_134311231 2，mobile 3.data 模板参数（json格式非必须）
	@Override
	public SendSmsResponse sentSMS(Map<String, Object> map) throws Exception {
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
		//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
		accessKeySecret);
		DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		IAcsClient acsClient = new DefaultAcsClient(profile);
		 //组装请求对象
		 SendSmsRequest request = new SendSmsRequest();
		 //使用post提交
		 request.setMethod(MethodType.POST);
		 //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		 request.setPhoneNumbers(map.get("mobile").toString());
		 if(map.get("signName")==null){
			 //必填:短信签名-可在短信控制台中找到
			 request.setSignName(signName);
		 } else {
			 request.setSignName(map.get("signName").toString());
		 }
		 //必填:短信模板-可在短信控制台中找到
		 request.setTemplateCode(map.get("temp_code").toString());
		 //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		 request.setTemplateParam(map.get("data").toString());
		 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		 //request.setSmsUpExtendCode("90997");
		 //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//		 request.setOutId("yourOutId");
		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
		if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
		//请求成功
		}
		LogUtil.info(sendSmsResponse.getCode());
		return sendSmsResponse;
	}
	@Override
	public void sendMail(List<Email> listMail, String invokeType) throws Exception {
		if(listMail==null || listMail.isEmpty())
			return;
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", emailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", auth);            // 需要请求认证

        props.setProperty("mail.smtp.starttls.enable", tlsEnable);
        props.setProperty("mail.smtp.socketFactory.port", socketFactoryPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  
        
        Session session = Session.getInstance(props);
        session.setDebug(debuggable);                                 // 设置为debug模式, 可以查看详细的发送 log
        Transport transport = session.getTransport();
        transport.connect(emailAccount, emailPassword);
        for(Email email: listMail){
        	if(ObjectUtils.toString(email.getAddress()).isEmpty()) continue;
        	email.setUpdateBy(1l);
        	email.setCreateBy(1l);
    		email.setResult(Email.SUCCESS);
    		try {
    			MimeMessage message = new MimeMessage(session);
    			String charSet = StringUtils.isEmpty(email.getCharSet())?"UTF-8":email.getCharSet();
    			message.setFrom(new InternetAddress(emailAccount, email.getSendName(), charSet));
    			message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email.getAddress(), email.getReceiveName(), charSet));

//    			message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(emailAccount, email.getReceiveName(), charSet));
    			
    			message.setSubject(email.getSubject(), charSet);
    			message.setContent(email.getContent(), "text/html;charset=UTF-8");
    			message.setSentDate(email.getSentDate());
    			message.saveChanges();
    			transport.sendMessage(message, message.getAllRecipients());
			} catch (Exception e) {
				email.setResult(Email.FAIL);
			}
    		if(email.getResult().equals(Email.SUCCESS)){
    			FormLogger.logMSG(String.format("发送至[%s]的邮件发送成功，这是第[%s]次发送", email.getAddress(), email.getSendCount()+1), FormLogger.LOG_TYPE_INFO);
    		} else {
    			FormLogger.logMSG(String.format("发送至[%s]的邮件发送失败，这是第[%s]次发送，稍后我们可能会重新发送，一共重新发送三次", email.getAddress(), email.getSendCount()+1), FormLogger.LOG_TYPE_WARN);
    		}
        }
        transport.close();
        if(Email.INVOKETYPE1.equals(invokeType)){
        	scheduleJobMapper.insertEmail(listMail);
        } 
    }

	@Override
	public void updateEmail() throws Exception {
		List<Email> list = scheduleJobMapper.getFailEmail();
		sendMail(list, Email.INVOKETYPE0);

    	List<Long> ids = new ArrayList<Long>();
    	for(int i=0;i<list.size();i++){
    		if(Email.SUCCESS.equals(list.get(i).getResult()))
    			ids.add(list.get(i).getId());
    	}
    	if(ids.size()>0)
    	scheduleJobMapper.setEmailSendResult(ids);
    
	}

	@Override
	public void sendMail(Email mail, String invokeType) throws Exception {
		if(mail!=null){
			List<Email> list = new ArrayList<>();
			list.add(mail);
			sendMail(list,invokeType);
		}
		
	}
	@Override
	public boolean checkCode(String mobile, String code, String type) {
        QueryPage queryPage1 = new QueryPage();
        queryPage1.setQueryParam("verification/QryVerification");
        queryPage1.addCondition("mobile", mobile);
        queryPage1.addCondition("type", type);//发送验证码,完成注册时校验
        queryPage1.addCondition("code", code);
        queryPage1.setSort("id desc");
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		ca.add(Calendar.MINUTE, -105);
		queryPage1.addCondition("dg_creatTime",
				DateUtils.getDateToStr(DateUtils.YYMMDD_HHMMSS_24, ca.getTime()));
        List<Map<String, Object>> list1 = formMapper.query(queryPage1);
        if(list1==null || list1.size()==0){
        	LogUtil.info(String.format("{%s}:{%s},验证码错误或者失效", mobile,code));
        	return false;
        } else {
        	LogUtil.info(String.format("{%s}:{%s},验证码验证成功", mobile,code));
        	return true;
        }
        
	}

}
