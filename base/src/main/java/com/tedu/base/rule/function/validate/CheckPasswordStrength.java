package com.tedu.base.rule.function.validate;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.engine.util.ValidatorUtil;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/7
 */
public class CheckPasswordStrength extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String value = args[0].getValue(env).toString();
        if (ValidatorUtil.checkPasswordStrength(value)) {
            return AviatorBoolean.TRUE;
        }
        return AviatorBoolean.FALSE;
    }

    @Override
    public String getName() {
        return "CheckPasswordStrength";
    }
}
