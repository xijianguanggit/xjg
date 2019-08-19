package com.tedu.base.task;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tedu.base.engine.model.CustomFormModel;
import com.tedu.base.engine.service.FormService;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.task.model.ScheduleJobModel;
import com.tedu.base.task.service.JobTaskService;
@Component
public class TaskUtils {
	private static final Logger log = Logger.getLogger(TaskUtils.class);
	@Autowired
	private JobTaskService jobTaskService;
	@Autowired
	private FormService formService;
	public JobTaskService getJobTaskService() {
		return jobTaskService;
	}
	public void setJobTaskService(JobTaskService jobTaskService) {
		this.jobTaskService = jobTaskService;
	}
	
	
	public FormService getFormService() {
		return formService;
	}
	public void setFormService(FormService formService) {
		this.formService = formService;
	}

	private static TaskUtils taskUtils;
	
	
	@PostConstruct
	public void init() {
		taskUtils = this;
		taskUtils.jobTaskService = this.jobTaskService;
		taskUtils.formService = this.formService;
	}
	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	public static void invokMethod(ScheduleJobModel scheduleJob) {
		Object object = null;
		Class clazz = null;
		try {
			if (StringUtils.isNotBlank(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_SPRINGID))) {
//				FormLogger.logSchedule(String.format("任务[%s]开始编译添加", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
				object = SpringUtils.getBean(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_SPRINGID));
				excutejob(object, clazz, scheduleJob);
			} else if (StringUtils.isNotBlank(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_BEANCLASS))) {
//				FormLogger.logSchedule(String.format("任务[%s]开始编译添加", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
				clazz = Class.forName(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_BEANCLASS));
				object = clazz.newInstance();
				excutejob(object, clazz, scheduleJob);
			} else if (StringUtils.isNotBlank(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_SQL))) {
//				FormLogger.logSchedule(String.format("任务[%s]sql准备执行", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
//				FormLogger.logSchedule(String.format("任务[%s]sql开始执行", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
				String result = new String(ScheduleJobModel.Excute_SUCCESS);
				long begin = System.currentTimeMillis();
				try {
					Map map = new HashMap();
					CustomFormModel formModel = new CustomFormModel("","",map);
					formModel.setSqlId(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_SQL));
					taskUtils.formService.saveCustom(formModel);
				} catch (Exception e) {
					log.error(e.getMessage());
					result = ScheduleJobModel.Excute_FAIL;
				}
//				FormLogger.logSchedule(String.format("任务[%s]sql执行结束具体可以参看任务执行日志", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
				taskUtils.jobTaskService.insertLog(scheduleJob, result, System.currentTimeMillis()-begin);
			}
		} catch (Exception e) {
			FormLogger.logSchedule(String.format("任务[%s]未编译添加成功，请检查是否配置正确！！！", scheduleJob.getName()),
					FormLogger.LOG_TYPE_ERROR);
			taskUtils.jobTaskService.insertLog(scheduleJob, "fail", 0);
		}
		
	}
	
	private static void excutejob(Object object, Class clazz, ScheduleJobModel scheduleJob){
		if (object == null) {
			FormLogger.logSchedule(String.format("任务[%s]未编译添加成功，请检查是否配置正确！！！", scheduleJob.getName()),
					FormLogger.LOG_TYPE_ERROR);
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {			
			List<String> paramVal = scheduleJob.getParamVal();
			if(paramVal!=null && paramVal.size()>0){
				Class[] paramclazz=new Class[paramVal.size()];
				for(int i=0;i<paramVal.size();i++){
					paramclazz[i]=String.class;
				}
				method = clazz.getDeclaredMethod(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_METHODNAME), paramclazz);
			} else {
				method = clazz.getDeclaredMethod(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_METHODNAME));
			}
		} catch (NoSuchMethodException e) {
			FormLogger.logSchedule(String.format("任务[%s]未编译添加成功，方法名设置错误！！！", scheduleJob.getName()),
					FormLogger.LOG_TYPE_ERROR,e);
		} catch (SecurityException e) {
			FormLogger.logSchedule(String.format("任务[%s]未编译添加成功，请检查是否配置正确！！！", scheduleJob.getName()),
					FormLogger.LOG_TYPE_ERROR,e);
		}
		if (method != null) {
			String result = new String(ScheduleJobModel.Excute_SUCCESS);
			long begin = System.currentTimeMillis();
			try { 
//				FormLogger.logSchedule(String.format("任务[%s]编译添加成功", scheduleJob.getName()),
//						FormLogger.LOG_TYPE_INFO);
//				FormLogger.logSchedule(String.format("任务[%s]开始执行", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
				List<String> paramVal = scheduleJob.getParamVal();
				if(paramVal!=null && paramVal.size()>0){
					String[] param=new String[paramVal.size()];
					for(int i=0;i<paramVal.size();i++){
						param[i]=paramVal.get(i);
					}
					method.invoke(object, param);
				} else {
					method.invoke(object);
				}
			} catch (IllegalAccessException e) {
				result = ScheduleJobModel.Excute_FAIL;
				FormLogger.logSchedule(String.format("任务[%s]执行失败，IllegalAccessException", scheduleJob.getName()),
						FormLogger.LOG_TYPE_ERROR);
			} catch (IllegalArgumentException e) {
				result = ScheduleJobModel.Excute_FAIL;
				FormLogger.logSchedule(String.format("任务[%s]执行失败，IllegalArgumentException", scheduleJob.getName()),
						FormLogger.LOG_TYPE_ERROR);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				result = ScheduleJobModel.Excute_FAIL;
				FormLogger.logSchedule(String.format("任务[%s]执行失败，InvocationTargetException", scheduleJob.getName()),
						FormLogger.LOG_TYPE_ERROR);
			}
//			FormLogger.logSchedule(String.format("任务[%s]执行结束具体可以参看任务执行日志", scheduleJob.getName()), FormLogger.LOG_TYPE_INFO);
			taskUtils.jobTaskService.insertLog(scheduleJob, result, System.currentTimeMillis()-begin);
		}
	}
}
