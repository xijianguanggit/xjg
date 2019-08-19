package com.tedu.base.rule.function.typeChange;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Str extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        env.put("v",args[0].getValue(env));
        return new AviatorString(AviatorEvaluator.execute("str(v)", env).toString());
    }

    @Override
    public String getName() {
        return "Str";
    }
}
