package com.tedu.base.engine.exception;

import com.tedu.base.common.error.ErrorCode;

/**
 * 表单引擎异常
 * @author wangdanfeng
 *
 */
public interface FormEngineException {
	public ErrorCode getErrorCode();
}
