package com.tedu.base.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.lang.StringUtils;

import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.engine.util.FormLogger;
public class FieldCondition {
	String fieldName;
	String fieldValue;
	String fieldOp;//条件操作符
	public static final String NULL_FIELD_VALUE = "$NULL";
	public static final String NOT_NULL_FIELD_VALUE = "$NOTNULL";
	public static final String ALL_FIELD_VALUE = "$ALL";
	    
	public static final String OP_EQUAL = "eq";//=
	public static final String OP_LIKE = "lk";//like '%%'
	public static final String OP_GREAT_EQUAL = "ge";//>=d
	public static final String OP_LITTLE_EQUAL = "le";//<=
	
	public static final String OP_DATE_LITTLE_EQUAL = "dl";//日期<= 含当日全天
	public static final String OP_DATE_GREAT_EQUAL = "dg";//日期>= 含当日全天
	public static final String OP_DATE_EQUAL = "de";//日期= 含当日全天全天
	
	public static final String OP_GREAT_THAN = "gt";//>
	public static final String OP_LITTLE_THAN = "lt";//<
	public static final String OP_IN = "in";//>
	public static final String OP_SQL = "ss";//自由SQL条件,不拼fieldName
	private static final String REG_FIELD = "^[eq|ge|le|gt|lt|lk|in|dl|de|dg|ss]{2}[_].*";
	String[] ARR_OP = new String[]{"eq","ge","le","gt","lt","lk"};
	String[] ARR_OP_SIGN = new String[]{"=",">=","<=",">","<","like"};
	private static final String REG_COMPARE_FIELD = "^[eq|ge|le|gt|lt|in|dg]{2}.*";
	private static Map<String,String> OP_MAP = new HashMap<String,String>(){
		{
			put("eq","=");
			put("ge",">=");
			put("le","<=");
			put("gt",">");
			put("lt","<");
			put("lk","like");
			put("in","in");
			//日期
			put("dg",">=");
		}
	} ;
	
	public static final String PREFIX = "";//自由SQL条件,不拼fieldName,去掉q_
	private static final int PREFIX_LENGTH = PREFIX.length()+2+1;//前缀长度
	//$eq_role_id="123" 
	public FieldCondition(){
		
	}
	public FieldCondition(String fName,String fValue){
		fieldName = fName;
		fieldValue = fValue;
	}
	
	/**
	 * 前缀可能需要调换，不能写死
	 * @param fieldName
	 * @return
	 */
	public String getFieldOp(String fieldName){
		return fieldName.substring(PREFIX.length(),PREFIX.length()+2);
	}
    /**
     * 根据字段名前缀获取SQL匹配符
     * 将当前对象构造成sql子句
     * @param fieldName
     * @return
     */
    public String toString(){
    	if(fieldName == null || fieldName.length() <= PREFIX_LENGTH || fieldValue==null || fieldValue.isEmpty()) return "";
    	if(fieldValue.startsWith("[")&&fieldValue.endsWith("]")){
    		fieldValue = fieldValue.replace(" ", "");
    		fieldValue = fieldValue.substring(1,fieldValue.length()-1);
    	}
    	String op = getFieldOp(fieldName);
    	String realFieldName = fieldName.substring(PREFIX_LENGTH);
    	String[] arrFieldValue = fieldValue.split(",");
    	if(arrFieldValue == null) return "";
    	
    	StringJoiner sj = new StringJoiner(" or ");
    	for(String val:arrFieldValue){
    		if(realFieldName.indexOf(",")>0){
    			sj.add(genMultiFieldsSql(realFieldName,op,val));
    		}else{
    			sj.add(genOneFieldSql(op,realFieldName,val));
    		}
    	}
    	return " (" +sj.toString() + ") ";
    }
    
    
    private String genMultiFieldsSql(String realFieldName,String op,String val){
    	StringJoiner sj = new StringJoiner(" or ");
		String[] arr = realFieldName.split(",");
		String operator = op;
		String field ;
		for(String f:arr){//第一个不用处理
			if(FieldCondition.isCondition(f)){
				operator = getFieldOp(f);
				field = f.substring(PREFIX_LENGTH);
			}else{
				field = f;
			}
	    	sj.add(genOneFieldSql(operator,field,val));
		}
		return " (" +sj.toString() + ") ";
    }
    
    /**
     * 目前只支持等于和like，更多条件类型可在此处扩展
     * @param op
     * @param fieldName
     * @param fieldValue
     * @return
     */
    private String genOneFieldSql(String op,String fieldName,String fieldValue){
    	if(op.equalsIgnoreCase(OP_EQUAL)){
    		if(fieldValue.equals(NULL_FIELD_VALUE)){
    			return fieldName + " is null ";
    		}else if(fieldValue.equals(NOT_NULL_FIELD_VALUE)){
    			return fieldName + " is not null ";
    		}else if(fieldValue.equals(ALL_FIELD_VALUE)){
    			return " (1=1) ";//含义为不参与条件拼接
    		}else{
    			return fieldName + "='" + fieldValue+"'";
    		}
    	}else if(op.equalsIgnoreCase(OP_LIKE)){
    		return fieldName + " like '%" + fieldValue + "%'";
    	}else if(op.equalsIgnoreCase(OP_SQL)){
    		return " (" + fieldValue + ") ";
    	}else if(op.equals(OP_DATE_LITTLE_EQUAL) || op.equals(OP_DATE_EQUAL) || op.equals(OP_DATE_GREAT_EQUAL)){
    		try{
    			String nextDay = DateUtils.getNextDay(fieldValue);
    			if(nextDay == null){
    				FormLogger.logConf("配置了无法格式化的日期查询字段,转为字符匹配方式筛选" , op + fieldValue);
    				return fieldName + "='" + fieldValue + "'";
    			}else if(op.equals(OP_DATE_LITTLE_EQUAL)){
    				return String.format(" ( %s < '%s') ", fieldName,nextDay); 
    			}else{
    				return String.format(" (%s >= '%s' and %s < '%s') ", fieldName,fieldValue,fieldName,nextDay);
    			}
    		}catch(Exception e){
				FormLogger.logConf("配置了无法格式化的日期查询字段,转为字符匹配方式筛选" , e.getMessage());
    		}
    	}else if(op.equals(OP_IN)){
    		String[] arr = fieldValue.split(",");
    		return fieldName + " " + OP_MAP.get(op) + " ('"+StringUtils.join(arr,"','")+"')";
    	}else{
    		if(fieldValue.indexOf('-')>=0 || fieldValue.indexOf(':')>=0){//date.如果是le类型的日期条件，应把10位时间变为当日最后一秒的时间较好，否则使用时需截止第二天
    			return fieldName + " " + OP_MAP.get(op) + "'" + fieldValue + "'";
    		}else{
    			return fieldName + " " + OP_MAP.get(op) + fieldValue;
    		}
    	}
    	return null;
    }
  
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public String getFieldOp() {
		return fieldOp;
	}
	public void setFieldOp(String fieldOp) {
		this.fieldOp = fieldOp;
	}
    
    /**
     * 判断是否两位条件字符加下划线开头
     * @param key
     * @return
     */
    public static boolean isCondition(String key){
    	return key!=null && key.matches(REG_FIELD);
    }

}
