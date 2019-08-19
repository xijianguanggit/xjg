package com.tedu.base.common.error;
//package com.tedu.base.common.error;
//
//import java.util.Date;
//
//import javax.annotation.Resource;
//
//import org.apache.commons.beanutils.BeanUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import com.tedu.base.common.service.CommonService;
//import com.tedu.base.common.utils.SessionUtils;
//import com.tedu.base.auth.login.model.UserModel;
//
///**
// * 错误日志切片
// * 
// * @author xijianguang
// */
//@Aspect
//@Component
//public class UpdateAspect {
//
//	@Resource
//	private CommonService commonService;
//
//	@Pointcut("execution(* *..service*..update*(..))")  
//    public void controllerAspect() {
//    }
//
//	@Before("controllerAspect()")
//	public void doBefore(JoinPoint joinPoint) throws Exception {
//		Date date = commonService.getSysTime();
//		UserModel userModel = SessionUtils.getUserInfo();
//		BeanUtils.copyProperty(joinPoint.getArgs()[0], "updateTime", date);
//		BeanUtils.copyProperty(joinPoint.getArgs()[0], "updateBy", userModel.getUserId());
//	}
//
//	@AfterReturning(pointcut = "controllerAspect()")
//	public void doAfter(JoinPoint joinPoint) throws Exception {
//	}
//
//	@AfterThrowing(value = "controllerAspect()", throwing = "e")
//	public void doAfter(JoinPoint joinPoint, Exception e) throws Exception {
//	}
//
//}
