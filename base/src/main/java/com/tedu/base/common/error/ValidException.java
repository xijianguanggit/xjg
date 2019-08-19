package com.tedu.base.common.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.tedu.base.engine.exception.FormEngineException;
import com.tedu.base.engine.model.UniqueModel;

/**
 * 校验错误
 */
public class ValidException extends RuntimeException implements FormEngineException{
	BindingResult errors;
	ErrorCode errorCode;
	String detail;
	Map<String,Object> data = new HashMap<>();//携带业务数据返回
	public ValidException(ErrorCode errorCode,BindingResult errors){
		this.errorCode = errorCode;
		this.errors = errors;
	}

	public ValidException(ErrorCode errorCode,UniqueModel uniqueModel){
		this.errorCode = errorCode;
		BindingResult result = new MapBindingResult(new HashMap(), "");
		result.rejectValue(uniqueModel.getControlName(),"",String.format("[%s]已存在", uniqueModel.getControlValue()));
		this.errors = result;
	}
	
	public ValidException(ErrorCode errorCode,String controlName,String detailMsg){
		this.errorCode = errorCode;
		BindingResult result = new MapBindingResult(new HashMap(), "");
		result.rejectValue(controlName,"",detailMsg);
		this.errors = result;
	}
	
	public ValidException(ErrorCode errorCode,String controlName,String detailMsg,Map<String,Object> data){
		this.errorCode = errorCode;
		BindingResult result = new MapBindingResult(new HashMap(), "");
		result.rejectValue(controlName,"",detailMsg);
		this.errors = result;
		this.data = data;
	}
	
	public BindingResult getErrors() {
		return errors;
	}

	@Override
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}
	
	
}
