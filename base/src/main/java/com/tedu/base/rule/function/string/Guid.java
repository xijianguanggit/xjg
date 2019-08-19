package com.tedu.base.rule.function.string;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.TokenUtils;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Guid extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String guid = TokenUtils.genUUID().toUpperCase();
        return new AviatorString(guid);
    }

    @Override
    public String getName() {
        return "Guid";
    }
}
