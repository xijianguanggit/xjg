package com.tedu.base.rule.function.typeChange;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/6
 */
public class Double extends AbstractVariadicFunction {
    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        env.put("v",args[0].getValue(env));
        return AviatorDouble.valueOf(AviatorEvaluator.execute("double(v)", env));
    }

    @Override
    public String getName() {
        return "Double";
    }
}
