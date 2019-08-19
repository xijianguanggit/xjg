package com.tedu.base.common.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tedu.base.engine.exception.FormEngineException;

/**
 * 业务异常
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="ServiceException") 
public class ServiceException extends RuntimeException implements FormEngineException{
	private String detail="";
	private ErrorCode errorCode;
	private Map<String,Object> data = new HashMap<>();//携带业务数据返回
	public ServiceException(ErrorCode errorCode,String detail){
		super(errorCode.getMsg());
		this.detail = detail;//错误明细
		this.errorCode = errorCode;
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}
	
	 @Override
	 public String toString() {
	    return String.format("%s %s [%s]", errorCode.getCode(),errorCode.getMsg(),detail);
	 }

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
}
