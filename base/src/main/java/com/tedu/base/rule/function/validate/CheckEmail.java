package com.tedu.base.rule.function.validate;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.engine.util.ValidatorUtil;

import java.util.Map;

public class CheckEmail extends AbstractVariadicFunction {



    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String email = args[0].getValue(env).toString();
        if (ValidatorUtil.isEmail(email)) {
            return AviatorBoolean.TRUE;
        }
        return AviatorBoolean.FALSE;
    }


    @Override
    public String getName() {
        return "CheckEmail";
    }
}
