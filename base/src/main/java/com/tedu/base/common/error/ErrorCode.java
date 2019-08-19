package com.tedu.base.common.error;
/**
 * 错误码
 * 1开头的是系统级错位
 * 第一位：分类码,2-3位错误码
 * 终止逻辑的执行
 * 
 * @author wangdanfeng
 *
 */
public enum ErrorCode {
//	拦截器的通用错误代码的提示信息：系统错误(10YY)，请联系管理员 
//	- 未知错误
//	- 请求次数过于频繁
//	- 请求内容长度超过限制
//	- HTTP Header内容非法
//	- 请求参数错误
//	- JSON解析失败
//	- Header内容非法
//	- HMAC验证失败
//	- Token非法
//	- 重复请求
//	- 访问权限验证不通过
//	- 隐藏域必填项验证异常
//	- 隐藏域输入格式验证异常
//	- 服务超时
//	- 服务不存在
	//系统级
	UNKNOWN("101", "未知错误"),
	TOO_MANY_REQUESTS("102", "请求次数过于频繁"),
	LENGTH_LIMIT_EXCEEDED("103", "请求内容长度超过限制"),
	ILLEGAL_HTTP_HEADER("104", "HTTP Header内容非法"),
	ILLEGAL_REQUEST_PARAMETER("105", "请求参数错误"),
	JSON_PARSING_FAILED("106", "JSON解析失败"),
	ILLEGAL_REQUEST_HEADER("107", "Header内容非法"),
	ILLEGAL_TOKEN("108", "Token非法"),
	RESUBMIT_ERROR("109", "重复请求"),
	ACCESS_DENIED("110", "访问权限验证不通过"),
	REQUIRED_HIDDEN_FIELD_MISSING("1011", "隐藏域必填项验证异常"),
	SERVICE_TIMEOUT("112", "服务超时"),
	SERVICE_NOT_EXISTS("113", "服务不存在"),
	ACL_LOAD_FAILED("143", "用户权限列表装载失败"),
	SESSION_TIMEOUT("199", "Session超时"),
	REDIS_ERROR("115", "redis操作异常"),

	
	INVALIDATE_FORM_DATA("201", "数据校验异常"),
	DATA_NOT_FOUND("202", "数据不存在"),
	UNSUPPORTED_DATA_TYPE("203", "不支持的数据类型"),
	TEMPLATE_PARSING_FAILED("204", "配置文件模板未找到或解析不正确"),
	TEMPLATE_NOT_EXISTS("205", "配置文件模板不存在"),
	REQUIRED_FIELD_MISSING("206", "必填项为空"),
	CLASS_MISSING("207", "无法加载处理类,可能不存在"),
	PLUGIN_EXCEPTION("208", "插件处理类执行异常"),
	FILE_EXCEPTION("209", "文件类操作异常"),
	MAIL_EXCEPTION("210", "邮件发送异常"),
	ALREADY_ORDER("211", "不能重复报名"),
	ILLEGAL_FORM_CONFIGURATION("301", "配置不正确的表单"),
	
	SAVE_MASTER_DATA_FAILED("401", "主表数据更新失败"),
	SAVE_DETAIL_DATA_FAILED("402", "明细表数据更新失败"),
	SAVE_TRANSACTION_FAILED("403", "执行保存事物异常")	,
	SAVE_DIRTY_DATA("404", "数据已改变，请重新装载"),
	
	SQL_PREPARE_FAILED("901", "预编译SQL语句失败"),
	SQL_CONSTRAINTS_VIOLATION("902","数据约束异常"),
	SQL_UNIQUE_KEY_VIOLATION("903","唯一性约束异常"),
	SQL_DUPLICATE_KEY("904","主键重复"),


	LOGIN_USER_INVALID("501","用户名或口令错误"),
	LOGIN_CODE_ERROR("502","验证码错误"),
	LOGIN_CODE_OPENID("503","openid不存在");

	private String code;
	private String msg;
	private ErrorCode(String code,String msg){
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	 @Override
	  public String toString() {
	    return code + ": " + msg;
	  }
}
