package com.tedu.base.commitLog.dao;

import java.util.List;

import com.tedu.base.commitLog.model.CommitLog;

public interface CommitLogMapper {
	
	/**
	 * 
	 * @Description: 新增git提交日志 
	 * @author: gaolu
	 * @date: 2017年11月7日 下午5:36:24  
	 * @param:      
	 * @return: void
	 */
	public void insertLog(List<CommitLog> logList);
	
	/**
	 * 
	 * @Description: 获取git日志表中最后一条记录的提交时间
	 * @author: gaolu
	 * @date: 2017年11月7日 下午5:36:24  
	 * @param:      
	 * @return: void
	 */
	public CommitLog getLastTime();

}
