//package com.tedu.base.task.controller;
//
//import java.lang.reflect.Method;
//import java.util.List;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.quartz.CronScheduleBuilder;
//import org.quartz.SchedulerException;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.tedu.base.task.RetObj;
//import com.tedu.base.task.SpringUtils;
//import com.tedu.base.task.model.ScheduleJobModel;
//import com.tedu.base.task.service.JobTaskService;
//
//@Controller
//@RequestMapping("/task")
//public class JobTaskController {
//	// 日志记录器
//	public final Logger log = Logger.getLogger(this.getClass());
//    @Resource
//	private JobTaskService taskService;
//
//	@RequestMapping("/taskList")
//	public String taskList(HttpServletRequest request) {
//		List<ScheduleJobModel> taskList = taskService.getAllTask();
//		request.setAttribute("taskList", taskList);
//		return "task/taskList";
//	}
//
//	@RequestMapping("add")
//	@ResponseBody
//	public RetObj taskList(HttpServletRequest request, ScheduleJobModel scheduleJob) {
//		RetObj retObj = new RetObj();
//		retObj.setFlag(false);
//		try {
//			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getExpression());
//		} catch (Exception e) {
//			retObj.setMsg("cron表达式有误，不能被解析！");
//			return retObj;
//		}
//		Object obj = null;
//		try {
//			if (StringUtils.isNotBlank(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_SPRINGID))) {
//				obj = SpringUtils.getBean(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_SPRINGID));
//			} else {
//				Class clazz = Class.forName(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_BEANCLASS));
//				obj = clazz.newInstance();
//			}
//		} catch (Exception e) {
//			// do nothing.........
//		}
//		if (obj == null) {
//			retObj.setMsg("未找到目标类！");
//			return retObj;
//		} else {
//			Class clazz = obj.getClass();
//			Method method = null;
//			try {
//				method = clazz.getMethod(scheduleJob.getParamMap().get(ScheduleJobModel.PARA_TYPE_METHODNAME), null);
//			} catch (Exception e) {
//				// do nothing.....
//			}
//			if (method == null) {
//				retObj.setMsg("未找到目标方法！");
//				return retObj;
//			}
//		}
//		try {
//			taskService.addTask(scheduleJob);
//		} catch (Exception e) {
//			log.error("context", e);
//			retObj.setFlag(false);
//			retObj.setMsg("保存失败，检查 name group 组合是否有重复！");
//			return retObj;
//		}
//
//		retObj.setFlag(true);
//		return retObj;
//	}
//
//	@RequestMapping("changestatus")
//	@ResponseBody
//	public RetObj changeJobStatus(HttpServletRequest request, Long jobId, String cmd) {
//		RetObj retObj = new RetObj();
//		retObj.setFlag(false);
//		try {
//			taskService.changeStatus(jobId, cmd);
//		} catch (SchedulerException e) {
//			log.error(e.getMessage(), e);
//			retObj.setMsg("任务状态改变失败！");
//			return retObj;
//		}
//		retObj.setFlag(true);
//		return retObj;
//	}
//
//	@RequestMapping("updateCron")
//	@ResponseBody
//	public RetObj updateCron(HttpServletRequest request, Long jobId, String cron) {
//		RetObj retObj = new RetObj();
//		retObj.setFlag(false);
//		try {
//			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
//		} catch (Exception e) {
//			retObj.setMsg("cron表达式有误，不能被解析！");
//			return retObj;
//		}
//		try {
//			taskService.updateCron(jobId, cron);
//		} catch (SchedulerException e) {
//			retObj.setMsg("cron更新失败！");
//			return retObj;
//		}
//		retObj.setFlag(true);
//		return retObj;
//	}
//}
