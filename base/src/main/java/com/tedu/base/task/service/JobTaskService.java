package com.tedu.base.task.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.SchedulerException;

import com.tedu.base.task.model.ScheduleJobModel;

/**
 * 
 * @Description: 计划任务管理
 */
public interface JobTaskService {
//	public List<ScheduleJobModel> getAllTask();

	/**
	 * 添加到数据库中 区别于addJob
	 */
//	public void addTask(ScheduleJobModel job);

	/**
	 * 从数据库中查询job
	 */
//	public ScheduleJobModel getTaskById(Long jobId) ;

	/**
	 * 更改任务状态
	 * 
	 * @throws SchedulerException
	 */
//	public void changeStatus(Long jobId, String cmd) throws SchedulerException ;

	/**
	 * 更改任务 cron表达式
	 * 
	 * @throws SchedulerException
	 */
//	public void updateCron(Long jobId, String cron) throws SchedulerException;

	/**
	 * 添加任务
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void addJob(ScheduleJobModel job) throws SchedulerException ;

	@PostConstruct
	public void init() throws Exception ;

	/**
	 * 获取所有计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
//	public List<ScheduleJobModel> getAllJob() throws SchedulerException;

	/**
	 * 所有正在运行的job
	 * 
	 * @return
	 * @throws SchedulerException
	 */
//	public List<ScheduleJobModel> getRunningJob() throws SchedulerException ;

	/**
	 * 暂停一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
//	public void pauseJob(ScheduleJobModel scheduleJob) throws SchedulerException ;

	/**
	 * 恢复一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
//	public void resumeJob(ScheduleJobModel scheduleJob) throws SchedulerException;

	/**
	 * 删除一个job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void deleteJob(ScheduleJobModel scheduleJob) throws SchedulerException ;
	public void initAllJob() throws SchedulerException ;

	/**
	 * 立即执行job
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void runAJobNow(String id) throws SchedulerException ;

	/**
	 * 更新job时间表达式
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
//	public void updateJobCron(ScheduleJobModel scheduleJob) throws SchedulerException ;
	public void insertLog(ScheduleJobModel scheduleJob, String result, long cost);
}
