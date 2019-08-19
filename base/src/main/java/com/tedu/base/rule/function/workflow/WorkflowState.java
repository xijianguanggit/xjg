package com.tedu.base.rule.function.workflow;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.task.SpringUtils;
import com.tedu.base.workflow.util.WorkflowConstant;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/12/12
 */
public class WorkflowState extends AbstractVariadicFunction {

    RuntimeService runtimeService = SpringUtils.getBean("runtimeService");

    HistoryService historyService = SpringUtils.getBean("historyService");


    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {

        String businessKey = args[0].getValue(env).toString();
        String state = args[1].getValue(env).toString();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        boolean hasAudit = false;
        if (state.equals(WorkflowConstant.NOT_STARTED)) {
            HistoricProcessInstance his = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            if (his != null) {
                return AviatorBoolean.FALSE;
            } else {
                return AviatorBoolean.TRUE;

            }
        } else if (state.equals(WorkflowConstant.START)) {
            if (processInstance == null) {
                return AviatorBoolean.FALSE;
            } else {
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId()).orderByHistoricActivityInstanceId().asc().list();
                //processInstance.
                for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                    if (historicActivityInstance.getActivityType().equals("userTask")) {
                        //已经有人审批了
                        hasAudit = true;
                        break;
                    }
                }

                if (!hasAudit) {
                    return AviatorBoolean.TRUE;
                } else {
                    return AviatorBoolean.FALSE;
                }
            }
        } else if (state.equals(WorkflowConstant.PROCESSING)) {
            if (processInstance == null) {
                return AviatorBoolean.FALSE;
            } else {
                List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId()).orderByHistoricActivityInstanceId().asc().list();
                //processInstance.
                for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
                    if (historicActivityInstance.getActivityType().equals("userTask")) {
                        //已经有人审批了
                        hasAudit = true;
                        break;
                    }
                }

                if (hasAudit) {
                    return AviatorBoolean.TRUE;
                } else {
                    return AviatorBoolean.FALSE;
                }
            }
        } else if (state.equals(WorkflowConstant.END)) {
            if (processInstance == null) {
                return AviatorBoolean.TRUE;
            } else {
                return AviatorBoolean.FALSE;
            }
        }
        return AviatorBoolean.FALSE;
    }

    @Override
    public String getName() {
        return "WorkflowState";
    }
}
