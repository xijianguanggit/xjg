package com.tedu.base.rule.function.string;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/5
 */
public class Join extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        String s = args[0].getValue(env).toString();
        String separator = args[1].getValue(env).toString();
        env.put("s",s);
        env.put("separator",separator);
        return new AviatorRuntimeJavaType(AviatorEvaluator.execute("string.join(s,separator)", env));


    }

    @Override
    public String getName() {
        return "Join";
    }
}
