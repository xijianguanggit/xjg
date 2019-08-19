package com.tedu.base.rule.function;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.CodeUtil;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2018/1/3
 */
public class GetOrderCode extends AbstractVariadicFunction {

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        return new AviatorString(CodeUtil.GenerateCode());
    }

    @Override
    public String getName() {
        return "GetOrderCode";
    }
}
