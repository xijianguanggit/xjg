package com.tedu.base.commitLog.dao;

import java.util.List;
import com.tedu.base.commitLog.model.CommitFile;

public interface CommitFileMapper {
	
	/**
	 * 
	 * @Description: 新增git提交日志 
	 * @author: gaolu
	 * @date: 2017年11月7日 下午5:36:24  
	 * @param:      
	 * @return: void
	 */
	public void insertReport(List<CommitFile> reportList);

}
