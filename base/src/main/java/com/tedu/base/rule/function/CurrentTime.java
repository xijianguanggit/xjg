package com.tedu.base.rule.function;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.tedu.base.common.service.CommonService;
import com.tedu.base.task.SpringUtils;
@Component
public class CurrentTime  extends AbstractFunction{
	@Resource
	private CommonService commonService;	
	
	public String getName() {
		return "CurrentTime";
	}

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
//        Object s = FunctionUtils.getJavaObject(arg1, env);
        Date d = ((CommonService)SpringUtils.getBean("commonService")).getSysTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        if(arg1 instanceof AviatorLong){
        	cal.add(Calendar.DATE,((AviatorLong) arg1).toBigInt().intValue());	
        }
        return new AviatorRuntimeJavaType(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()));
    }
    
    //取数据库时间
    @Override
    public AviatorObject call(Map<String, Object> env) {
        Date d = ((CommonService)SpringUtils.getBean("commonService")).getSysTime();
        return new AviatorRuntimeJavaType(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));    
    }
}
