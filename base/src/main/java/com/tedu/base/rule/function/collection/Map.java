package com.tedu.base.rule.function.collection;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Map extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(java.util.Map<String, Object> env, AviatorObject... args) {
        env.put("seq",args[0].getValue(env));
        env.put("func",AviatorEvaluator.getFunction(args[1].getValue(env).toString()));

        return new AviatorRuntimeJavaType(AviatorEvaluator.execute("map(seq,func)", env));
    }

    @Override
    public String getName() {
        return "Map";
    }
}
