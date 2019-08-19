package com.tedu.base.rule.function.string;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.tedu.base.common.utils.RMBUtils;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Chinese2Double extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {

        String chinese = FunctionUtils.getStringValue(args[0], env);
        return new AviatorString(RMBUtils.covertToDigital(chinese));

    }

    @Override
    public String getName() {
        return "Chinese2Double";
    }
}
