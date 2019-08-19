package com.tedu.base.commitLog.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.commitLog.dao.CommitLogMapper;
import com.tedu.base.commitLog.dao.CommitFileMapper;
import com.tedu.base.commitLog.model.CommitLog;
import com.tedu.base.commitLog.model.CommitFile;
import com.tedu.base.commitLog.service.CommitLogService;
import com.tedu.base.common.utils.JGitUtils;

/**
 * git提交记录service接口实现类
 * @author gaolu
 *
 */
@Service("CommitLogService")
public class CommitLogServiceImpl implements CommitLogService {
	@Resource
	public CommitLogMapper commitLogMapper;
	@Resource
	public CommitFileMapper commitFileMapper;

	@Override
	public void saveCommitLog() {
		Map<String, List> resultMap = null; 
		List<CommitLog> logList = null;
		List<CommitFile> reportList = null;

		//获取git日志表中最后一条数据的提交时间
		//CommitLog cLog = commitLogMapper.getLastTime();
		Date tableLastTime = new Date();
		
		//获取git提交日志
		JGitUtils jgit = new JGitUtils();
		resultMap = jgit.getLog(tableLastTime);
		
		//插入数据库中
		if (resultMap!=null) {
			logList = resultMap.get("commitlog");
			reportList = resultMap.get("report");
			//插入数据库中
			if (reportList != null) {
				commitFileMapper.insertReport(reportList);
			}
			if (logList != null) {
				commitLogMapper.insertLog(logList);
			}
		}
		
		
	}
	

}
