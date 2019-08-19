package com.tedu.base.workflow.controller;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.SessionUtils;
import com.tedu.base.engine.model.*;
import com.tedu.base.initial.model.xml.ui.FormConstants;
import com.tedu.base.workflow.service.WorkflowUserService;
import com.tedu.base.workflow.util.WorkflowConstant;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/11/13
 */
@Controller
@RequestMapping("/api")
public class WorkFlowLogicController {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private IdentityService identityService;
    @Resource
    private WorkflowUserService workflowUserService;
    @Resource
    private RepositoryService repositoryService;

    /**
     * 启动工作流
     *
     * @param request
     * @param requestObj
     *
     * @return
     */
    @RequestMapping("startworkflow")
    @ResponseBody
    public FormEngineResponse startWorkflow(HttpServletRequest request, @RequestBody FormEngineRequest requestObj, CustomFormModel formModel, ServerTokenData serverTokenData) {
        FormEngineResponse responseObj = new FormEngineResponse("");
        // 流程定义的key
        try {
            String processDefinitionKey = requestObj.getData().get("processDefinitionKey").toString();
            String businessKey = serverTokenData.getUiName() + "_" + requestObj.getData().get("modelId");
            Map<String, Object> variables = new HashMap<String, Object>();
            //公用的业务单据的流程变量
            variables.put("sqlMap", formModel);
            variables.put("model", formModel.getData());
            variables.put("starter", SessionUtils.getUserInfo().getEmpId()+"");
            variables.put("viewUrl",requestObj.getData().get("viewUrl").toString());

            //设置业务相关流程变量
            //设置流程启动人
            identityService.setAuthenticatedUserId(SessionUtils.getUserInfo().getUserId() + "");
            // 使用流程定义的key启动流程实例，key对应HelloWorld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
            ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

        } catch (Exception e) {
            throw new ServiceException(ErrorCode.ILLEGAL_FORM_CONFIGURATION, "WorkOut参数或者In没获取到");

        }

        return responseObj;
    }


    /**
     * 手工执行工作流跳转窗体
     *
     * @param requestObj
     * @param request
     * @param formModel
     *
     * @return
     */
    @RequestMapping("execactivity")
    public ModelAndView execActivity(FormEngineRequest requestObj, HttpServletRequest request, FormModel formModel) {
        String businessKey = "";
        String processDefinitionKey = request.getParameter("processDefinitionKey").toString();

        try {
            String uiName = formModel.getData().get("uiName").toString();
            String modelId = request.getParameter("modelId").toString();
            businessKey = uiName + "_" + modelId;

        } catch (Exception e) {

        }
        String auditGuideline = formModel.getData().get("auditGuideline").toString();
        ProcessInstanceQuery instanceQuery = runtimeService.createProcessInstanceQuery();
        instanceQuery.processInstanceBusinessKey(businessKey);
        ProcessInstance processInstance = instanceQuery.singleResult();
        String pId = processInstance.getId();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(SessionUtils.getUserInfo().getUserId() + "").list();
        String taskId = "";
        for (Task task : tasks) {
            if (task.getProcessInstanceId().equals(pId)) {
                taskId = task.getId();
                break;
            }
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        boolean isShowRadio = true;

        if(task.getCategory()!=null&&task.getCategory().equals(WorkflowConstant.USER_TASK_NOTIFY)){
            isShowRadio=false;
        }

        ModelAndView mv = new ModelAndView(FormConstants.VIEW_EXEC_ACTIVITY);
        mv.addObject("pId", pId);
        mv.addObject("isShowRadio", isShowRadio);
        mv.addObject("token", formModel.getServerTokenData().getToken());
        mv.addObject("taskId", taskId);
        mv.addObject("auditGuideline", auditGuideline);
        //设置流程变量
        taskService.setVariable(taskId, "auditResult", "");
        taskService.setVariableLocal(taskId, "model", formModel);
        return mv;
    }


    /**
     * 查看工作流进度
     *
     * @param requestObj
     * @param request
     * @param formModel
     *
     * @return
     */
    @RequestMapping("listactivity")
    public ModelAndView listActivity(FormEngineRequest requestObj, HttpServletRequest request, FormModel formModel) {
        String businessKey = "";
        String uiName ="";
        String processDefinitionKey = request.getParameter("processDefinitionKey").toString();

        try {
            uiName = formModel.getData().get("uiName").toString();
            String modelId = request.getParameter("modelId").toString();
            businessKey = uiName + "_" + modelId;

        } catch (Exception e) {

        }
        ModelAndView mv = new ModelAndView(FormConstants.VIEW_LIST_ACTIVITY);
        List<Map<String, String>> hisList = new ArrayList<Map<String, String>>();
        String processId ="";
        String processDefinitionId="";
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
            if(historicProcessInstance!=null){
            processId = historicProcessInstance.getId();
             processDefinitionId = historicProcessInstance.getProcessDefinitionId();
             List<HistoricActivityInstance> list = historyService
                    .createHistoricActivityInstanceQuery()
                    .processInstanceId(processId).activityType("userTask")
                    .list();


            List<String> userIds = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                HistoricActivityInstance his = list.get(i);
                if (his.getAssignee() != null) {
                    if(!his.getAssignee().equals(WorkflowConstant.ASSIGNEE)) {
                        userIds.add(his.getAssignee());
                    }
                }
            }
            Map<String, String> userMap = null;
            if(userIds.size()>0) {
                userMap= workflowUserService.getNamesByIds(userIds);
            }
            for (int i = 0; i < list.size(); i++) {
                HistoricActivityInstance his = list.get(i);
                Map<String, String> stringStringMap = new HashMap<String, String>();
                if (his.getActivityName() != null && !his.getActivityName().equals("")) {
                    stringStringMap.put("activityName", his.getActivityName());
                    String assignName = "";
                    if (his.getAssignee() != null && his.getAssignee().equals(WorkflowConstant.ASSIGNEE)) {
                        assignName = WorkflowConstant.ASSIGNEE_NAME;
                    } else {
                        assignName = userMap.get(his.getAssignee());
                    }
                    stringStringMap.put("assignee", assignName);

                    ZoneId zoneId = ZoneId.systemDefault();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                    String startTimeStr = df.format(his.getStartTime().toInstant().atZone(zoneId).toLocalDateTime()).toString();
                    stringStringMap.put("startTime", startTimeStr);
                    String endTimeStr = "";
                    if (his.getEndTime() != null) {
                        endTimeStr = df.format(his.getEndTime().toInstant().atZone(zoneId).toLocalDateTime()).toString();
                    }
                    stringStringMap.put("endTime", endTimeStr);

                    String taskId = his.getTaskId();
                    List<HistoricVariableInstance> varHisList = historyService
                            .createHistoricVariableInstanceQuery().taskId(taskId).list();
                    for (HistoricVariableInstance historicVariableInstance : varHisList) {
                        if (historicVariableInstance.getVariableName().equals("auditResultText")) {
                            stringStringMap.put("auditResult", historicVariableInstance.getValue().toString());
                        } else if (historicVariableInstance.getVariableName().equals("auditOption")) {

                            stringStringMap.put("auditOption", historicVariableInstance.getValue().toString());
                        }
                    }
                    hisList.add(stringStringMap);
                }
            }
            }else{
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey).singleResult();
                processDefinitionId = processDefinition.getId();
            }
        }catch (Exception e){

        }
        mv.addObject("hisList", hisList);
        mv.addObject("token", formModel.getServerTokenData().getToken());
        mv.addObject("processId", processId);
        mv.addObject("processDefinitionId", processDefinitionId);
        mv.addObject("resourceName", processDefinitionId.split(":")[0]);
        return mv;
    }

}
