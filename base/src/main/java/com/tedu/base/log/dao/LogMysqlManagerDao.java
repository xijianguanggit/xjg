package com.tedu.base.log.dao;

import org.apache.ibatis.annotations.Param;

import com.tedu.base.log.model.LogModel;

/**
 * 添加日志
 * @author xijianguang
 */
public interface LogMysqlManagerDao {
	public void addLog(@Param("logModel")LogModel logModel);
}
