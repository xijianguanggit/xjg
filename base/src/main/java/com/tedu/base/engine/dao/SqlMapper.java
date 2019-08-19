package com.tedu.base.engine.dao;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.tedu.base.common.page.QueryPage;
@Component("sqlMapper")  
public interface SqlMapper {
	Integer mapUpdate(ModelMap modelMap);
	Integer mapInsert(ModelMap modelMap);
	Integer mapDelete(ModelMap modelMap);

	//单条数据
	Map<Object,Object> mapSelect(ModelMap modelMap);
	//通用查询list，常用于表格表格数据
//	List<Map<Object,Object>> query(QueryPage page);
	//通用查询单条数据
	Map<Object,Object> selectOne(QueryPage page);//语句必须指定0-1条返回结果
}
