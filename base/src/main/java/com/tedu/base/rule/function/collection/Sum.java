package com.tedu.base.rule.function.collection;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/8/31
 */
public class Sum extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        env.put("seq",args[0].getValue(env));
        return AviatorDouble.valueOf(AviatorEvaluator.execute("reduce(seq,+,0)", env));
    }

    @Override
    public String getName() {
        return "Sum";
    }
}
