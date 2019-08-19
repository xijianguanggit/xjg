package com.tedu.base.common.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.tedu.base.common.model.ExcelTemp;

/**
 * 公共查询dao
 * 
 * @author xijianguang
 */
public interface CommonMapperDao {
	/**
	 * 获取系统时间
	 */
	public Date getSysTime();

	/**
	 * 查询excel导入配置
	 */
	@MapKey("display_name")
	public Map<String, Map<String, Object>> getExcelConfig(String tableName);

	/**
	 * 插入临时表
	 * 
	 * @author xijianguang
	 */
	@Insert("INSERT INTO t_excel_temp_reference(bat_id,col_name,col_num) VALUES ${sql}")
	public void saveExcelTempReference(@Param("sql") String sql);

	@Insert("INSERT INTO t_excel_temp(${sqlCol}) VALUES ${tableCol}")
	public void saveExcelTemp(@Param("sqlCol") String sqlCol, @Param("tableCol") String tableCol);

	@Select("select ${foreignCol} from ${foreignTable}")
	public Set<String> selectForeign(@Param("foreignCol") String foreignCol,
			@Param("foreignTable") String foreignTable);

	@Insert("INSERT INTO t_excel_foreign (bat_id, col, val) "
			+"SELECT "
			+"reference.bat_id,"
			+"reference.col_num,"
			+"a.${foreignCol} "
			+"FROM "
				+"t_excel_temp temp "
			+"LEFT JOIN t_excel_temp_reference reference ON temp.bat_id = reference.bat_id "
			+"LEFT JOIN ${tableName} a ON a.${foreignVal}=temp.${option} "
			+"WHERE "
			+"	reference.col_name = '${option}' "
			+"AND ISNULL(temp.msg) "
			+"AND temp.bat_id=#{batId}")
	public void saveforeign(@Param("batId")String batId, @Param("foreignCol")String foreignCol, @Param("tableName")String tableName,
			@Param("foreignVal")String foreignVal, @Param("option")String option);

	
	@Insert("INSERT INTO t_excel_msg(bat_id,excel_id,msg) VALUES ${val}")
	public void saveMsg(@Param("val") String val);
	
	@Select("SELECT col,val,col_name from t_excel_foreign f "
			+ "LEFT JOIN t_excel_temp_reference r ON r.col_num=f.col where f.bat_id=#{batId}")
	@MapKey("col_name")
	public Map<String, Map<String, Object>> getforeign(@Param("batId") String batId);
	
	@Select("SELECT col_name,col_num, f.* from t_excel_temp_reference temp "
			+ "LEFT JOIN t_excel_foreign f ON f.bat_id=temp.bat_id"
			+ " where temp.bat_id=#{batId} GROUP BY col_name")
	@MapKey("col_name")
	public Map<String, Map<String, Object>> getReference(@Param("batId") String batId);

	@Select("SELECT * from t_excel_temp temp where temp.bat_id=#{batId} and ISNULL(temp.msg)")
	public List<ExcelTemp> getTemp(@Param("batId") String batId);

	@Insert("INSERT INTO ${tableName}(${col}) VALUES ${val}")
	public void saveFinalTable(@Param("tableName") String tableName, @Param("col") String col,
			@Param("val") String val);

	@Select("select msg from t_excel_temp where bat_id=#{batId}")
	public List<String> getCheckResult(@Param("batId") String batId);

}
