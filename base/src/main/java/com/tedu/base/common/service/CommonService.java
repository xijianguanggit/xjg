package com.tedu.base.common.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 公共service
 * @author xijianguang
 */
public interface CommonService {
	/**
	 * 查询数据库系统时间
	 * @author xijianguang
	 */
	public Date getSysTime();
	/**
	 * 查询excel导入配置
	 * @author xijianguang
	 */
	public Map<String, Map<String,Object>> getExcelConfig(String tableName);
	/**
	 * 插入临时表
	 * @author xijianguang
	 */
	public void saveExcelTemp(Map<String, Object> map);
	/**
	 * 插入最终表
	 * @author xijianguang
	 */
	public void saveFinalTable(String batId, String tableName)throws IllegalArgumentException, IllegalAccessException;
	/**
	 * 查询excel错误日志
	 * @author xijianguang
	 */
	public List<String> getCheckResult(String batId);
	
}
