package com.tedu.base.rule.function.math;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorNumber;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Pow extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Object d1 = args[0].getValue(env);
        Object d2 = args[0].getValue(env);
        env.put("d1",d1);
        env.put("d2",d2);
        return AviatorNumber.valueOf(AviatorEvaluator.execute("math.pow(d1,d2)", env));
    }

    @Override
    public String getName() {
        return "Pow";
    }
}
