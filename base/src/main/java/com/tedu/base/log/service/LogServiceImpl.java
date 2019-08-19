package com.tedu.base.log.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.log.dao.LogMysqlManagerDao;
import com.tedu.base.log.model.LogModel;

@Service("logService")
public class LogServiceImpl implements LogService {

	@Resource
	public LogMysqlManagerDao logMysqlManagerDao;
	
	public void addLog(LogModel logModel) {
//		try {
//			Thread.sleep(2000);
//			for(int i=0;i<5;i++){
//				Thread.sleep(2 * 1000);
//				System.out.println("等待中。。。。。。。。。。。。。。。。");
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		logMysqlManagerDao.addLog(logModel);
	}
}
