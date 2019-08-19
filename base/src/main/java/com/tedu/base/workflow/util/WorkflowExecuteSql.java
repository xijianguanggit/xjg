package com.tedu.base.workflow.util;

import com.tedu.base.engine.dao.FormMapper;
import com.tedu.base.engine.model.CustomFormModel;
import org.activiti.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author yangjixin
 * @Description: 工作流执行SQL
 * @date 2017/11/23
 */
@Component
public class WorkflowExecuteSql {
    @Resource
    FormMapper formMapper;


    public void executeSql(DelegateExecution execution, String sqlName, CustomFormModel sqlMap) {
        //工作流SQL都放在workflow下
        sqlMap.setSqlId("workflow/"+sqlName);
        formMapper.saveCustom(sqlMap);
    }
}
