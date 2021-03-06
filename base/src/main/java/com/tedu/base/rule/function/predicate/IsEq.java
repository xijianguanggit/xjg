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
public class IsEq extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {

        if (env.get("value") == null) {
            String value = args[0].toString();
            env.put("value",value);
            return new AviatorRuntimeJavaType(AviatorEvaluator.execute("seq.eq(value)", env));

        } else {
            return new AviatorRuntimeJavaType(AviatorEvaluator.execute("seq.eq(value)", env));
        }
    }

    @Override
    public String getName() {
        return "IsEq";
    }
}
