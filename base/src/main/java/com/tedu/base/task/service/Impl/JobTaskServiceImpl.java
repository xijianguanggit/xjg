package com.tedu.base.task.service.Impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.tedu.base.common.utils.ContextUtils;
import com.tedu.base.common.utils.JsonUtil;
import com.tedu.base.engine.util.FormLogger;
import com.tedu.base.task.QuartzJobFactory;
import com.tedu.base.task.QuartzJobFactoryDisallowConcurrentExecution;
import com.tedu.base.task.dao.ScheduleJobMapperDao;
import com.tedu.base.task.model.ScheduleJobModel;
import com.tedu.base.task.service.JobTaskService;

/**
 * 
 * @Description: 计划任务管理
 */
@Service("jobTaskService")
public class JobTaskServiceImpl implements JobTaskService{
	@Value("${task.startup}")
	private String startup;
	@Value("${task.cron}")
	private String cronExpression;
	@Autowired(required=false)
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ScheduleJobMapperDao scheduleJobMapper;
	@Autowired
	private ServletContext servletContext;
//	/**
//	 * 从数据库中取 区别于getAllJob
//	 * 
//	 * @return
//	 */
//	public List<ScheduleJobModel> getAllTask() {
//
//		return scheduleJobMapper.getAll();
//	}
//
//	/**
//	 * 添加到数据库中 区别于addJob
//	 */
//	public void addTask(ScheduleJobModel job) {
//		job.setCreateTime(new Date());
//		scheduleJobMapper.insertSelective(job);
//	}
//
//	/**
//	 * 从数据库中查询job
//	 */
//	public ScheduleJobModel getTaskById(Long jobId) {
//		return scheduleJobMapper.selectByPrimaryKey(jobId);
//	}
//
//	/**
//	 * 更改任务状态
//	 * 
//	 * @throws SchedulerException
//	 */
//	public void changeStatus(Long jobId, String cmd) throws SchedulerException {
//		ScheduleJobModel job = getTaskById(jobId);
//		if (job == null) {
//			return;
//		}
//		if ("stop".equals(cmd)) {
//			deleteJob(job);
//			job.setStatus(ScheduleJobModel.STATUS_NOT_RUNNING);
//		} else if ("start".equals(cmd)) {
//			job.setStatus(ScheduleJobModel.STATUS_RUNNING);
//			addJob(job);
//		}
//		scheduleJobMapper.updateByPrimaryKeySelective(job);
//	}
//
//	/**
//	 * 更改任务 cron表达式
//	 * 
//	 * @throws SchedulerException
//	 */
//	public void updateCron(Long jobId, String cron) throws SchedulerException {
//		ScheduleJobModel job = getTaskById(jobId);
//		if (job == null) {
//			return;
//		}
//		job.setExpression(cron);
//		if (ScheduleJobModel.STATUS_RUNNING.equals(job.getStatus())) {
//			updateJobCron(job);
//		}
//		scheduleJobMapper.updateByPrimaryKeySelective(job);
//
//	}

	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void addJob(ScheduleJobModel job) throws SchedulerException {
		if (job == null || !ScheduleJobModel.STATUS_RUNNING.equals(job.getStatus())) {
			return;
		}

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		FormLogger.logSchedule(String.format("启动添加[%s]定时任务", job.getName()), FormLogger.LOG_TYPE_INFO);
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getId().toString(), Scheduler.DEFAULT_GROUP);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (null == trigger) {
			Class clazz = ScheduleJobModel.CONCURRENT_IS.equals(job.getConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

			JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getId().toString(), Scheduler.DEFAULT_GROUP).build();

			jobDetail.getJobDataMap().put("scheduleJob", job);

			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getExpression());
			trigger = TriggerBuilder.newTrigger().withIdentity(job.getId().toString(), Scheduler.DEFAULT_GROUP).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	@PostConstruct
	public void init() throws Exception {
		initAllJob();
	}

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
//	public List<ScheduleJobModel> getAllJob() throws SchedulerException {
//
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
//		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
//		List<ScheduleJobModel> jobList = new ArrayList<ScheduleJobModel>();
//		for (JobKey jobKey : jobKeys) {
//			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
//			for (Trigger trigger : triggers) {
//				ScheduleJobModel job = new ScheduleJobModel();
//				job.setName(jobKey.getName());
//				job.setDescription("触发器:" + trigger.getKey());
//				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//				job.setStatus(triggerState.name());
//				if (trigger instanceof CronTrigger) {
//					CronTrigger cronTrigger = (CronTrigger) trigger;
//					String cronExpression = cronTrigger.getCronExpression();
//					job.setExpression(cronExpression);
//				}
//
//				jobList.add(job);
//			}
//		}
//		return jobList;
//	}
//
//	/**
//	 * 所有正在运行的job
//	 * 
//	 * @return
//	 * @throws SchedulerException
//	 */
//	public List<ScheduleJobModel> getRunningJob() throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
//		List<ScheduleJobModel> jobList = new ArrayList<ScheduleJobModel>(executingJobs.size());
//		for (JobExecutionContext executingJob : executingJobs) {
//			ScheduleJobModel job = new ScheduleJobModel();
//			JobDetail jobDetail = executingJob.getJobDetail();
//			JobKey jobKey = jobDetail.getKey();
//			Trigger trigger = executingJob.getTrigger();
//			job.setName(jobKey.getName());
//			job.setDescription("触发器:" + trigger.getKey());
//			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
//			job.setStatus(triggerState.name());
//			if (trigger instanceof CronTrigger) {
//				CronTrigger cronTrigger = (CronTrigger) trigger;
//				String cronExpression = cronTrigger.getCronExpression();
//				job.setExpression(cronExpression);
//			}
//			jobList.add(job);
//		}
//		return jobList;
//	}
//
//	/**
//	 * 暂停一个job
//	 * 
//	 * @param scheduleJob
//	 * @throws SchedulerException
//	 */
//	public void pauseJob(ScheduleJobModel scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), Scheduler.DEFAULT_GROUP);
//		scheduler.pauseJob(jobKey);
//	}
//
//	/**
//	 * 恢复一个job
//	 * 
//	 * @param scheduleJob
//	 * @throws SchedulerException
//	 */
//	public void resumeJob(ScheduleJobModel scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//		JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), Scheduler.DEFAULT_GROUP);
//		scheduler.resumeJob(jobKey);
//	}

	/**
	 * 删除一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void deleteJob(ScheduleJobModel scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getId().toString(), Scheduler.DEFAULT_GROUP);
		scheduler.deleteJob(jobKey);

	}

//	
	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runAJobNow(String id) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		TriggerKey triggerKey = TriggerKey.triggerKey(id, Scheduler.DEFAULT_GROUP);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(id, Scheduler.DEFAULT_GROUP).build();
			List<ScheduleJobModel> list = scheduleJobMapper.getAll(id);
			Calendar now = Calendar.getInstance();  
			StringBuffer express = new StringBuffer();
			express.append(now.get(Calendar.SECOND)).append(" ");
			express.append(now.get(Calendar.MINUTE)).append(" ");
			express.append(now.get(Calendar.HOUR_OF_DAY)).append(" ");
			express.append((now.get(Calendar.DAY_OF_MONTH))).append(" ");
			express.append((now.get(Calendar.MONTH) + 1)).append(" ? ");
			express.append(now.get(Calendar.YEAR));
			list.get(0).setExpression(express.toString());
			jobDetail.getJobDataMap().put("scheduleJob", list.get(0));
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(express.toString());
			trigger = TriggerBuilder.newTrigger().withIdentity(id, Scheduler.DEFAULT_GROUP).withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			JobKey jobKey = JobKey.jobKey(id, Scheduler.DEFAULT_GROUP);
			scheduler.triggerJob(jobKey);
		}
	}
//
//	/**
//	 * 更新job时间表达式
//	 * 
//	 * @param scheduleJob
//	 * @throws SchedulerException
//	 */
//	public void updateJobCron(ScheduleJobModel scheduleJob) throws SchedulerException {
//		Scheduler scheduler = schedulerFactoryBean.getScheduler();
//
//		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getName(), Scheduler.DEFAULT_GROUP);
//
//		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//
//		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getExpression());
//
//		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
//
//		scheduler.rescheduleJob(triggerKey, trigger);
//	}


	@Override
	public void initAllJob() throws SchedulerException {
		if(ScheduleJobModel.BASE_JOB_OPEN.equals(startup)){
//			FormLogger.logSchedule("定时任务开关开启，定时刷新数据库执行cron："+cronExpression, FormLogger.LOG_TYPE_INFO);
			Map<Long, ScheduleJobModel> jobListDb = scheduleJobMapper.getAll("").stream().collect(Collectors.toMap(ScheduleJobModel::getId, (p) -> p));
			ScheduleJobModel s = new ScheduleJobModel();
			s.setExpression(cronExpression);
			s.setName("轮训数据库");
			s.setId(0l);
			s.setConcurrent("single");
			s.setStatus("enable");
			s.setType("springbean");
			s.setParam("methodname,springid,beanclass|initAllJob,jobTaskService,com.tedu.base.task.service.JobTaskService");
			jobListDb.put(0l, s);
			if(servletContext.getAttribute("jobList")!=null){
				Map<Long, ScheduleJobModel> jobListContext = (Map<Long, ScheduleJobModel>)ContextUtils.getAttrbute("jobList");
				for(Map.Entry<Long, ScheduleJobModel> entry : jobListContext.entrySet()){
					if(!jobListDb.containsKey(entry.getKey())){
//						FormLogger.logSchedule(String.format("数据库发生变化删除[%s]任务", entry.getValue().getName()), FormLogger.LOG_TYPE_INFO);
						deleteJob(entry.getValue());
					} else if(jobListDb.get(entry.getKey())!=null && !jobListDb.get(entry.getKey()).equals(entry.getValue())){
//						FormLogger.logSchedule(String.format("数据库发生变化刷新[%s]任务", jobListDb.get(entry.getKey()).getName()), FormLogger.LOG_TYPE_INFO);
						deleteJob(jobListDb.get(entry.getKey()));
						addJob(jobListDb.get(entry.getKey()));
					}
				}
				for(Map.Entry<Long, ScheduleJobModel> entry : jobListDb.entrySet()){
					if(!jobListContext.containsKey(entry.getKey())){
//						FormLogger.logSchedule(String.format("数据库发生变化新增[%s]任务", jobListDb.get(entry.getKey()).getName()), FormLogger.LOG_TYPE_INFO);
						addJob(jobListDb.get(entry.getKey()));
					} 
				}
			} else {
				for (Map.Entry<Long, ScheduleJobModel> entry : jobListDb.entrySet()){
//					FormLogger.logSchedule(String.format("系统启动时发现[%s]任务", entry.getValue().getName()), FormLogger.LOG_TYPE_INFO);
					addJob(entry.getValue());
				}
			}
			servletContext.setAttribute("jobList", jobListDb);
		}
	
	}

	@Override
	public void insertLog(ScheduleJobModel scheduleJob, String result, long cost) {
		
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getId().toString(), Scheduler.DEFAULT_GROUP);
		try {
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//	        FormLogger.logSchedule("执行间隔_"+(trigger.getNextFireTime().getTime()-(new Date()).getTime()), FormLogger.LOG_TYPE_INFO);
			if(trigger.getNextFireTime()!=null&&trigger.getNextFireTime().getTime()-(new Date()).getTime()>59*1000){
	    		Map<String, String> map = new HashMap<String, String>();
	    		map.put("result", result);
	    		map.put("cost", String.valueOf(cost));
	    		map.put("jobId", String.valueOf(scheduleJob.getId()));
	    		map.put("type", scheduleJob.getType());
	    		map.put("concurrent", scheduleJob.getConcurrent());
	    		map.put("param", JsonUtil.mapToJson(scheduleJob.getParamMap()));
	    		scheduleJobMapper.insertLog(map);
	        } else {
//	        	FormLogger.logSchedule("执行间隔小于1分钟，不需要计入日志表", FormLogger.LOG_TYPE_DEBUG);
	        }
		} catch (SchedulerException e) {
			FormLogger.logSchedule("插入日志异常", FormLogger.LOG_TYPE_ERROR);
		}
		
	}
	
}
