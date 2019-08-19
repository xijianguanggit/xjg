package com.tedu.base.task.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.tedu.base.msg.mail.Email;
import com.tedu.base.task.model.ScheduleJobModel;

public interface ScheduleJobMapperDao {
//	int deleteByPrimaryKey(Long jobId);
//
//	int insert(ScheduleJobModel record);
//
//	int insertSelective(ScheduleJobModel record);
//
//	ScheduleJobModel selectByPrimaryKey(Long jobId);
//
//	int updateByPrimaryKeySelective(ScheduleJobModel record);
//
//	int updateByPrimaryKey(ScheduleJobModel record);

	List<ScheduleJobModel> getAll(String id);
	void insertLog(Map<String, String> map);
	void insertEmail(List<Email> listMail);
	List<Email> getFailEmail();
	void setEmailSendResult(@Param("ids")List<Long> ids);
	
}