package com.tedu.base.rule.function.predicate;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/7
 */
public class IsAnd extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        for(int i = 1 ; i <= args.length; i++){
            env.put("p"+i,args[i-1]);
        }
        return new AviatorRuntimeJavaType(AviatorEvaluator.execute("seq.and(p1, p2, p3, ...)", env));
    }

    @Override
    public String getName() {
        return "IsAnd";
    }
}
