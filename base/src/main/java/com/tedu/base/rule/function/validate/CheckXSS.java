package com.tedu.base.rule.function.validate;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.engine.util.ValidatorUtil;

import java.util.Map;

public class CheckXSS extends AbstractVariadicFunction {
    public String getName() {
        return "CheckXSS";
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String value = args[0].getValue(env).toString();
        if (ValidatorUtil.isXSS(value)) {
            return AviatorBoolean.TRUE;
        }
        return AviatorBoolean.FALSE;
    }
}
