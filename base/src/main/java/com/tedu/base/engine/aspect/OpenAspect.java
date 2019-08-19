package com.tedu.base.engine.aspect;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.tedu.base.common.error.ErrorCode;
import com.tedu.base.common.error.ServiceException;
import com.tedu.base.common.utils.DateUtils;
import com.tedu.base.common.utils.SignUtil;
import com.tedu.base.common.utils.UCRequest;
import com.tedu.base.engine.util.FormLogger;
/**
 * form engine logic controller切片
 */
@Component 
@Aspect
public class OpenAspect {
	public static final Logger log = Logger.getLogger(OpenAspect.class);
	
	@Before("execution(* com.tedu.base.auth.login.controller.RestAction..*(..)))" 
			)  
    public void checkSign(JoinPoint joinPoint){
    	//do nothing
		Object[] args = joinPoint.getArgs();
		if(args!=null){
			for(Object obj:args){
				if(obj instanceof UCRequest){
					UCRequest param = (UCRequest)obj;
					checkSign(param);
					checkTime(param);
					break;
				}
			}
		}
    }
	
	private void checkSign(UCRequest param){
		if(!SignUtil.getSign(param).equals(param.getSign())){
			FormLogger.logToken(String.format("验签失败,请求被篡改", param.toString()));
			throw new ServiceException(ErrorCode.ACCESS_DENIED,"验签失败[" + param.toString() + "]");
		}
	}
	private static final Long THREE_HOUR = 1000*60*60*3L;
    private void checkTime(UCRequest param){
		if(param.getTime()!=null){
			try{
				long paramTime = DateUtils.getStrToDate(DateUtils.YYMMDD_HHMMSS_24,param.getTime()).getTime();
				long current = new Date().getTime();
				if(Math.abs(paramTime-current)>THREE_HOUR){//最多允许3小时差异
					throw new ServiceException(ErrorCode.INVALIDATE_FORM_DATA,"请求串中的时间不在允许的时间周期内[" + param.getTime() + "]");
				}
			}catch(ParseException e){
				throw new ServiceException(ErrorCode.INVALIDATE_FORM_DATA,"时间格式不正确");
			}
		}
	}
}
