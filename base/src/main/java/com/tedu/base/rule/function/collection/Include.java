package com.tedu.base.rule.function.collection;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Include extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        env.put("seq",args[0].getValue(env));
        env.put("element",args[1].getValue(env));
        return AviatorBoolean.valueOf((Boolean) AviatorEvaluator.execute("include(seq,element)", env));
    }

    @Override
    public String getName() {
        return "Include";
    }
}
