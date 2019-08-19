package com.tedu.base.workflow.util;

import com.tedu.base.msg.mail.Email;
import com.tedu.base.msg.mail.SendMsgService;
import com.tedu.base.task.SpringUtils;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yangjixin
 * @Description: 工作流集成邮件接口
 * @date 2017/11/23
 */
public class WorkflowMailService implements JavaDelegate {


    SendMsgService sendMsgService= SpringUtils.getBean("sendMsgService");

    private Expression address;

    private Expression subject;

    private Expression content;

    @Override
    public void execute(DelegateExecution execution) {
        String address1=((String)address.getValue(execution));
        String subject1=((String)subject.getValue(execution));
        String content1=((String)content.getValue(execution));
        Email mail = new Email();
        mail.setAddress(address1);
        mail.setContent(content1);
        mail.setSentDate(new Date());
        mail.setSubject(subject1);
        List<Email> list = new ArrayList<Email>();
        list.add(mail);
        try {
            sendMsgService.sendMail(list, Email.INVOKETYPE0);
        }catch (Exception e){

        }
    }


}
