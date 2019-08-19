package com.tedu.base.rule.function.workflow;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.task.SpringUtils;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/12/7
 */
public class HasTask extends AbstractVariadicFunction {

    TaskService taskService=  SpringUtils.getBean("taskService");

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        //任务判断
        String businessKey = args[0].getValue(env).toString();
        String userId = args[1].getValue(env).toString();
        Task task =taskService.createTaskQuery().processInstanceBusinessKey(businessKey).taskCandidateOrAssigned(userId).singleResult();
        if(task!=null){
            return AviatorBoolean.TRUE;
        }else{
            return AviatorBoolean.FALSE;
        }
    }

    @Override
    public String getName() {
        return "HasTask";
    }
}
