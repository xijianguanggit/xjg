package com.tedu.base.common.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tedu.base.common.dao.CommonMapperDao;
import com.tedu.base.common.excel.ExcelUtil;
import com.tedu.base.common.model.ExcelTemp;
import com.tedu.base.common.service.CommonService;

/**
 * 公共service
 * 
 * @author xijianguang
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {

	@Resource
	public CommonMapperDao commonMapperDao;

	/**
	 * 查询数据库系统时间
	 * 
	 * @author xijianguang
	 */
	@Override
	public Date getSysTime() {
		return commonMapperDao.getSysTime();
	}

	/**
	 * 查询excel导入配置
	 * 
	 * @author xijianguang
	 */
	@Override
	public Map<String, Map<String, Object>> getExcelConfig(String tableName) {
		return commonMapperDao.getExcelConfig(tableName);
	}

	/**
	 * 插入临时表
	 * 
	 * @author xijianguang
	 */
	@Override
	public void saveExcelTemp(Map<String, Object> map) {
		String tableCol = map.get("tableCol").toString();
		String sqlCol = map.get("sqlCol").toString();
		String sqlVal = map.get("sqlVal").toString();
		String batId = map.get("batId").toString();
		List<Map<String, Object>> foreign = (List<Map<String, Object>>) map.get("foreign");
		// 临时表
		commonMapperDao.saveExcelTemp(sqlVal, tableCol);
		// 临时附表
		StringBuffer sql = new StringBuffer();
		for (int i = 0; i < sqlCol.split(",").length; i++) {
			sql.append("('");
			sql.append(batId).append("',").append("'col" + (i + 1) + "',").append("'" + sqlCol.split(",")[i] + "'");
			sql.append("),");
		}
		commonMapperDao.saveExcelTempReference(sql.substring(0, sql.lastIndexOf(",")));
		// 查询外键去重
		for (int i = 0; i < foreign.size(); i++) {
			Map<String, Object> foreignMap = foreign.get(i);
			List<String> foreignVal = (List) foreignMap.get("foreignVal");
			Set<String> foreignSet = commonMapperDao.selectForeign(foreignMap.get("foreign_col").toString(), foreignMap.get("foreign_table").toString());
			String[] foreignArr = foreignVal.get(i).split(",");
			StringBuffer msg = new StringBuffer();
			for(int j=0;j<foreignArr.length;j++){
				if(!foreignSet.contains(foreignArr[j].replace("'", ""))){
					msg.append("('").append(batId).append("',");
					msg.append(j).append(",");
					msg.append("'第"+j+"条外键不存在'),");
				}
			}
			//插入外键不存在日志
			if(msg.length()>0)
				commonMapperDao.saveMsg(msg.substring(0, msg.lastIndexOf(",")));
			//保存外键关联
			commonMapperDao.saveforeign(batId, 
					foreignMap.get("foreign_key").toString(),
					foreignMap.get("foreign_table").toString(),
					foreignMap.get("foreign_col").toString(),
					foreignMap.get("foreignOption").toString());
		}

	}

	// 插入最终表
	public void saveFinalTable(String batId, String tableName) throws IllegalArgumentException, IllegalAccessException {
		List<ExcelTemp> temp = commonMapperDao.getTemp(batId);
		Map<String, Map<String, Object>> reference = commonMapperDao.getReference(batId);
		List<String> colList = new ArrayList<String>();
		StringBuffer colsb = new StringBuffer();
		for (Entry<String, Map<String, Object>> entry : reference.entrySet()) {
			colList.add(entry.getKey());
			colsb.append(entry.getValue().get("col_num")).append(",");
		}
		Map<String, Map<String, Object>> foreign = commonMapperDao.getforeign(batId);
		StringBuffer sql = new StringBuffer();
		for (ExcelTemp excelTemp : temp) {
			sql.append("(");
			for (String str : colList) {
				if(foreign.get(str)==null){
					Field field = ExcelUtil.getFieldByName(str, excelTemp.getClass());
					field.setAccessible(true);
					if (field.get(excelTemp) == null) {
						sql.append(field.get(excelTemp));
						sql.append(",");
					} else {
						sql.append("'");
						sql.append(field.get(excelTemp).toString());
						sql.append("',");
					}
				} else {
					sql.append("'");
					sql.append(foreign.get(str).get("val"));
					sql.append("',");
				}
			}
			sql = new StringBuffer(sql.subSequence(0, sql.lastIndexOf(",")));
			sql.append("),");
		}
		commonMapperDao.saveFinalTable(tableName, colsb.substring(0, colsb.lastIndexOf(",")),
				sql.substring(0, sql.lastIndexOf(",")));
		// for(int i=0;i<foreign.size();i++){TODO 多表情况
		// commonMapperDao.saveFinalTable(foreign.get(i).get("foreign_table").toString(),
		// "", "");
		// }
	}

	@Override
	public List<String> getCheckResult(String batId) {
		return commonMapperDao.getCheckResult(batId);
	}
}
