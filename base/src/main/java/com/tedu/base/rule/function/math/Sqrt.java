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
public class Sqrt extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        Object d = args[0].getValue(env);
        env.put("d",d);
        return AviatorNumber.valueOf(AviatorEvaluator.execute("math.sqrt(d)", env));
    }

    @Override
    public String getName() {
        return "Sqrt";
    }
}
