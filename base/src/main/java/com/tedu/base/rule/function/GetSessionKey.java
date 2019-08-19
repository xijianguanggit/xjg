package com.tedu.base.rule.function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.tedu.base.common.utils.SessionUtils;

import java.util.Map;

public class GetSessionKey  extends AbstractVariadicFunction{
      public String getName() {
          return "GetSessionKey";
      }

	@Override
	public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
		if(args.length==0) return null;
		String sessionKey = args[0].getValue(env).toString();
		return new AviatorRuntimeJavaType(SessionUtils.getSession().getAttribute(sessionKey));
	}

}
