package com.tedu.base.workflow.util;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/12/13
 */

public class WorkflowExecTaskListener implements ActivitiEventListener {


    @Override
    public void onEvent(ActivitiEvent event) {
        switch (event.getType()) {
            case ACTIVITY_COMPLETED:
                System.out.println("活动结束");
                break;
            case TASK_CREATED:
                System.out.println("任务创建");
                TaskEntityImpl taskEntity = (TaskEntityImpl) ((ActivitiEntityEventImpl) event).getEntity();


                break;

        default:
        System.out.println("Event received: " + event.getType());
    }

}

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
