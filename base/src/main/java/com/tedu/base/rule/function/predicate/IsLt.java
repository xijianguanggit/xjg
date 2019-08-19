package com.tedu.base.rule.function.predicate;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class IsLt extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Object value = args[0].getValue(env);
        env.put("value",value);
        return new AviatorRuntimeJavaType(AviatorEvaluator.execute("seq.lt(value)", env));
    }

    @Override
    public String getName() {
        return "IsLt";
    }
}
