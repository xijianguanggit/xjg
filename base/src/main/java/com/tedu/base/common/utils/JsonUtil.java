package com.tedu.base.common.utils;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;


public class JsonUtil {

	/**
	 * 
	 * @Description: 根据key查询json中对应的value
	 * @author: gaolu
	 * @param: @param key
	 * @param: @return      
	 * @return: String
	 */
	public static String getValueByKey(String jsonString,String key){
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		return jsonObject.getString(key);
	}
	
	/**
	 * 
	 * @Description: 返回json size
	 * @author: gaolu
	 * @param: @param jsonString
	 * @param: @return      
	 * @return: Integer
	 */
	public static Integer getJsonSize(String jsonString){
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.size();
	}
	
	/**
	 * 
	 * @Description: objcet对象转换成json字符串
	 * @author: gaolu
	 * @param: @param javaObj
	 * @param: @return      
	 * @return: String
	 */
    public static String objectToJson(Object javaObj) {
    	if(javaObj!=null){
    		JSONObject json;
        	JsonConfig jsonConfig = new JsonConfig();
        	jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            json = JSONObject.fromObject(javaObj, jsonConfig);
            return json.toString();
    	}else{
    		return "";
    	}
    }
    
    /**
     * 
     * @Description: list转json
     * @author: gaolu
     * @param: @param list
     * @param: @return      
     * @return: String
     */
    public static String listToJson(List list) {
        if(list!=null&&list.size()>0){
        	JSONArray json;
            json = JSONArray.fromObject(list);
            return json.toString();
        }else{
        	return "";
        }
    }
    
    /**
     * 
     * @Description: map转json
     * @author: gaolu
     * @param: @param map
     * @param: @return      
     * @return: String
     */
    public static <T> String mapToJson(Map<String, T> map){
    	if(map!=null&&!map.isEmpty()){
    		JSONObject json= JSONObject.fromObject(map);
        	return json.toString();
    	}else{
    		return "";
    	}
    	
    }
}
