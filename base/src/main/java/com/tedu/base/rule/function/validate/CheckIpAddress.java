package com.tedu.base.rule.function.validate;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.tedu.base.engine.util.ValidatorUtil;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/8/31
 */
public class CheckIpAddress extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String ipAddress = args[0].getValue(env).toString();

        if (ValidatorUtil.isIpAddress(ipAddress)) {
            return AviatorBoolean.TRUE;
        }
        return AviatorBoolean.FALSE;
    }

    @Override
    public String getName() {
        return "CheckIpAddress";
    }
}
