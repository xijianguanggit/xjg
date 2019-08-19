package com.tedu.base.rule.function.collection;

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
public class Reduce extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        env.put("seq",args[0].getValue(env));
        env.put("func",AviatorEvaluator.getFunction(args[1].getValue(env).toString()));
        env.put("init",args[2].getValue(env));
        return new AviatorRuntimeJavaType(AviatorEvaluator.execute("reduce(seq,func,init)", env));
    }

    @Override
    public String getName() {
        return "Reduce";
    }
}
